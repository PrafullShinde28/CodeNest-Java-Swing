// ===== DATABASE UTILITY AND CONNECTION =====
package com.codenest.util;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/codenest_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "manager";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
          System.out.println("Connecting to database as: " + DB_USER);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            createTables(conn);
            seedData(conn);
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }
    
    private static void createTables(Connection conn) throws SQLException {
        String[] createTableSQL = {
            // Users table
            """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(255) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                role ENUM('STUDENT', 'ADMIN') DEFAULT 'STUDENT',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            
            // Problems table
            """
            CREATE TABLE IF NOT EXISTS problems (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(200) NOT NULL,
                description TEXT NOT NULL,
                difficulty ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL,
                pattern ENUM('ARRAYS', 'STRINGS', 'TREES', 'GRAPHS', 'DYNAMIC_PROGRAMMING', 'SORTING', 'SEARCHING') NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            
            // Quizzes table
            """
            CREATE TABLE IF NOT EXISTS quizzes (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                subject VARCHAR(100) NOT NULL,
                title VARCHAR(200) NOT NULL,
                time_limit INT DEFAULT 30,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            
            // Questions table
            """
            CREATE TABLE IF NOT EXISTS questions (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                quiz_id BIGINT NOT NULL,
                question_text TEXT NOT NULL,
                option1 VARCHAR(500) NOT NULL,
                option2 VARCHAR(500) NOT NULL,
                option3 VARCHAR(500) NOT NULL,
                option4 VARCHAR(500) NOT NULL,
                correct_answer INT NOT NULL,
                FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
            )
            """,
            
            // Communities table
            """
            CREATE TABLE IF NOT EXISTS communities (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                description TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            
            // Posts table
            """
            CREATE TABLE IF NOT EXISTS posts (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                community_id BIGINT NOT NULL,
                user_id BIGINT NOT NULL,
                content TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (community_id) REFERENCES communities(id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
            """,
            
            // Quiz attempts table
            """
            CREATE TABLE IF NOT EXISTS quiz_attempts (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                student_id BIGINT NOT NULL,
                quiz_id BIGINT NOT NULL,
                score INT NOT NULL,
                attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
            )
            """,
            
            // Problem progress table
            """
            CREATE TABLE IF NOT EXISTS problem_progress (
                student_id BIGINT NOT NULL,
                problem_id BIGINT NOT NULL,
                status ENUM('NOT_STARTED', 'IN_PROGRESS', 'SOLVED') DEFAULT 'NOT_STARTED',
                solved_at TIMESTAMP NULL,
                PRIMARY KEY (student_id, problem_id),
                FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY (problem_id) REFERENCES problems(id) ON DELETE CASCADE
            )
            """,
            //
            """
            CREATE TABLE IF NOT EXISTS community_members (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    community_id BIGINT NOT NULL,
                    user_id BIGINT NOT NULL,
                    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (community_id) REFERENCES communities(id) ON DELETE CASCADE,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                    UNIQUE (community_id, user_id)
                )
            """,
        };
        
        for (String sql : createTableSQL) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        }
    }
    
    private static void seedData(Connection conn) throws SQLException {
        // Check if data already exists
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next() && rs.getInt(1) > 0) {
                return; // Data already exists
            }
        }
        
        // Insert sample users
        String insertUsers = """
            INSERT INTO users (name, email, password, role) VALUES
            ('Admin User', 'admin@codenest.com', 'admin123', 'ADMIN'),
            ('John Doe', 'john@example.com', 'password123', 'STUDENT'),
            ('Jane Smith', 'jane@example.com', 'password123', 'STUDENT'),
            ('Mike Johnson', 'mike@example.com', 'password123', 'STUDENT')
        """;
        
        // Insert sample problems (30 problems)
       String insertProblems = """
    INSERT INTO problems (title, description, difficulty, pattern) VALUES
    ('Two Sum', 'Given an array of integers, return indices of two numbers that add up to target. LeetCode: https://leetcode.com/problems/two-sum/', 'EASY', 'ARRAYS'),
    ('Reverse String', 'Write a function that reverses a string. LeetCode: https://leetcode.com/problems/reverse-string/', 'EASY', 'STRINGS'),
    ('Maximum Depth of Binary Tree', 'Find the maximum depth of a binary tree. LeetCode: https://leetcode.com/problems/maximum-depth-of-binary-tree/', 'EASY', 'TREES'),
    ('Valid Parentheses', 'Determine if input string has valid parentheses. LeetCode: https://leetcode.com/problems/valid-parentheses/', 'EASY', 'STRINGS'),
    ('Merge Two Sorted Lists', 'Merge two sorted linked lists. LeetCode: https://leetcode.com/problems/merge-two-sorted-lists/', 'EASY', 'ARRAYS'),
    ('Best Time to Buy and Sell Stock', 'Find maximum profit from stock prices. LeetCode: https://leetcode.com/problems/best-time-to-buy-and-sell-stock/', 'EASY', 'ARRAYS'),
    ('Single Number', 'Find the single number in array where others appear twice. LeetCode: https://leetcode.com/problems/single-number/', 'EASY', 'ARRAYS'),
    ('Climbing Stairs', 'Count ways to reach the top of stairs. LeetCode: https://leetcode.com/problems/climbing-stairs/', 'EASY', 'DYNAMIC_PROGRAMMING'),
    ('Binary Tree Inorder Traversal', 'Return inorder traversal of binary tree. LeetCode: https://leetcode.com/problems/binary-tree-inorder-traversal/', 'MEDIUM', 'TREES'),
    ('3Sum', 'Find all unique triplets that sum to zero. LeetCode: https://leetcode.com/problems/3sum/', 'MEDIUM', 'ARRAYS'),
    ('Longest Substring Without Repeating Characters', 'Find length of longest substring without repeating characters. LeetCode: https://leetcode.com/problems/longest-substring-without-repeating-characters/', 'MEDIUM', 'STRINGS'),
    ('Container With Most Water', 'Find container that can hold most water. LeetCode: https://leetcode.com/problems/container-with-most-water/', 'MEDIUM', 'ARRAYS'),
    ('Group Anagrams', 'Group strings that are anagrams of each other. LeetCode: https://leetcode.com/problems/group-anagrams/', 'MEDIUM', 'STRINGS'),
    ('Valid Sudoku', 'Determine if Sudoku board is valid. LeetCode: https://leetcode.com/problems/valid-sudoku/', 'MEDIUM', 'ARRAYS'),
    ('Search in Rotated Sorted Array', 'Search for target in rotated sorted array. LeetCode: https://leetcode.com/problems/search-in-rotated-sorted-array/', 'MEDIUM', 'SEARCHING'),
    ('Merge Intervals', 'Merge overlapping intervals. LeetCode: https://leetcode.com/problems/merge-intervals/', 'MEDIUM', 'ARRAYS'),
    ('Unique Paths', 'Count unique paths in grid. LeetCode: https://leetcode.com/problems/unique-paths/', 'MEDIUM', 'DYNAMIC_PROGRAMMING'),
    ('Word Break', 'Determine if string can be segmented using dictionary. LeetCode: https://leetcode.com/problems/word-break/', 'MEDIUM', 'DYNAMIC_PROGRAMMING'),
    ('Course Schedule', 'Determine if you can finish all courses. LeetCode: https://leetcode.com/problems/course-schedule/', 'MEDIUM', 'GRAPHS'),
    ('House Robber', 'Maximum money you can rob without robbing adjacent houses. LeetCode: https://leetcode.com/problems/house-robber/', 'MEDIUM', 'DYNAMIC_PROGRAMMING'),
    ('N-Queens', 'Place N queens on N×N chessboard. LeetCode: https://leetcode.com/problems/n-queens/', 'HARD', 'ARRAYS'),
    ('Word Ladder', 'Length of shortest transformation sequence. LeetCode: https://leetcode.com/problems/word-ladder/', 'HARD', 'GRAPHS'),
    ('Merge k Sorted Lists', 'Merge k sorted linked lists. LeetCode: https://leetcode.com/problems/merge-k-sorted-lists/', 'HARD', 'ARRAYS'),
    ('Trapping Rain Water', 'Calculate how much rain water can be trapped. LeetCode: https://leetcode.com/problems/trapping-rain-water/', 'HARD', 'ARRAYS'),
    ('Regular Expression Matching', 'Implement regular expression matching. LeetCode: https://leetcode.com/problems/regular-expression-matching/', 'HARD', 'STRINGS'),
    ('Edit Distance', 'Minimum operations to convert one string to another. LeetCode: https://leetcode.com/problems/edit-distance/', 'HARD', 'DYNAMIC_PROGRAMMING'),
    ('Sliding Window Maximum', 'Find maximum in each sliding window. LeetCode: https://leetcode.com/problems/sliding-window-maximum/', 'HARD', 'ARRAYS'),
    ('Serialize and Deserialize Binary Tree', 'Design algorithm to serialize/deserialize binary tree. LeetCode: https://leetcode.com/problems/serialize-and-deserialize-binary-tree/', 'HARD', 'TREES'),
    ('Largest Rectangle in Histogram', 'Find area of largest rectangle in histogram. LeetCode: https://leetcode.com/problems/largest-rectangle-in-histogram/', 'HARD', 'ARRAYS'),
    ('Max Points on a Line', 'Find maximum number of points on a line. LeetCode: https://leetcode.com/problems/max-points-on-a-line/', 'HARD', 'ARRAYS')
""";

        
        // Insert sample quizzes
        String insertQuizzes = """
            INSERT INTO quizzes (subject, title, time_limit) VALUES
            ('Data Structures', 'Arrays and Linked Lists Quiz', 20),
            ('Algorithms', 'Sorting and Searching Quiz', 25),
            ('Java Programming', 'OOP Concepts Quiz', 30),
            ('Database Systems', 'SQL Fundamentals Quiz', 15),
            ('Computer Networks', 'Network Protocols Quiz', 20)
        """;
        
        // Insert sample communities
        String insertCommunities = """
            INSERT INTO communities (name, description) VALUES
            ('Competitive Programming', 'Discuss competitive programming problems and strategies'),
            ('Web Development', 'Share web development tips and resources'),
            ('Data Science', 'Explore data science concepts and tools')
        """;
        
        // Execute inserts
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertUsers);
            stmt.execute(insertProblems);
            stmt.execute(insertQuizzes);
            stmt.execute(insertCommunities);
        }
        
        // Insert sample questions for quizzes
        insertSampleQuestions(conn);
    }
    
    private static void insertSampleQuestions(Connection conn) throws SQLException {
        String[] questions = {
             // Quiz 1: Data Structures (ID 1)
        "INSERT INTO questions (quiz_id, question_text, option1, option2, option3, option4, correct_answer) VALUES " +
        "(1, 'What is the time complexity of accessing an element in an array by index?', 'O(1)', 'O(n)', 'O(log n)', 'O(n²)', 1)",
        "(1, 'Which data structure follows LIFO principle?', 'Queue', 'Stack', 'Array', 'Tree', 2)",
        "(1, 'What is the worst-case time complexity for insertion in a linked list?', 'O(1)', 'O(n)', 'O(log n)', 'O(n²)', 2)",
        "(1, 'Which data structure uses FIFO principle?', 'Stack', 'Queue', 'Tree', 'Graph', 2)",
        "(1, 'What is the height of a perfectly balanced binary tree with n nodes?', 'log(n)', 'n', 'sqrt(n)', 'n log n', 1)",

        // Quiz 2: Algorithms (ID 2)
        "(2, 'Which sorting algorithm has the best average case time complexity?', 'Bubble Sort', 'Selection Sort', 'Merge Sort', 'Insertion Sort', 3)",
        "(2, 'Binary search works on which type of array?', 'Unsorted', 'Sorted', 'Partially sorted', 'Any array', 2)",
        "(2, 'Which algorithm is used to find the shortest path in a weighted graph?', 'DFS', 'BFS', 'Dijkstra', 'Kruskal', 3)",
        "(2, 'Which algorithm technique uses divide and conquer?', 'Merge Sort', 'Bubble Sort', 'Selection Sort', 'Insertion Sort', 1)",
        "(2, 'Which of the following is a greedy algorithm?', 'Knapsack 0/1', 'Prim''s MST', 'DFS', 'BFS', 2)",

        // Quiz 3: Java Programming (ID 3)
        "(3, 'Which keyword is used to inherit a class in Java?', 'this', 'extends', 'implements', 'super', 2)",
        "(3, 'What is the default value of a boolean variable in Java?', 'true', 'false', '0', 'null', 2)",
        "(3, 'Which method is the entry point of a Java program?', 'start()', 'main()', 'init()', 'run()', 2)",
        "(3, 'What is the access modifier for members visible only within the same package?', 'private', 'protected', 'public', 'default', 4)",
        "(3, 'Which interface is used for sorting in Java collections?', 'Comparable', 'Serializable', 'Cloneable', 'Iterable', 1)",

        // Quiz 4: Database Systems (ID 4)
        "(4, 'Which SQL command is used to remove all records from a table?', 'DROP', 'DELETE', 'TRUNCATE', 'REMOVE', 3)",
        "(4, 'Which clause is used to filter records in SQL?', 'WHERE', 'HAVING', 'ORDER BY', 'GROUP BY', 1)",
        "(4, 'What type of join returns all records from left table and matched records from right table?', 'INNER JOIN', 'LEFT JOIN', 'RIGHT JOIN', 'FULL JOIN', 2)",
        "(4, 'Which command is used to add a new column to an existing table?', 'ALTER TABLE ADD', 'CREATE TABLE', 'INSERT COLUMN', 'UPDATE TABLE', 1)",
        "(4, 'Which keyword is used to ensure uniqueness in a column?', 'PRIMARY KEY', 'FOREIGN KEY', 'UNIQUE', 'CHECK', 3)",

        // Quiz 5: Computer Networks (ID 5)
        "(5, 'Which layer of the OSI model is responsible for routing?', 'Physical', 'Data Link', 'Network', 'Transport', 3)",
        "(5, 'Which protocol is used to send emails?', 'HTTP', 'SMTP', 'FTP', 'TCP', 2)",
        "(5, 'Which IP version provides a larger address space?', 'IPv4', 'IPv6', 'Both', 'None', 2)",
        "(5, 'What does DNS stand for?', 'Domain Name System', 'Data Network Service', 'Dynamic Name Server', 'Direct Network Setup', 1)",
        "(5, 'Which protocol ensures reliable transmission of data?', 'UDP', 'TCP', 'ICMP', 'IP', 2)"
    };
        
        for (String sql : questions) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        }
    }
}