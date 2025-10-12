# CodeNest - Student Service Platform

A comprehensive desktop Java application designed for college students to practice programming problems, take quizzes, and engage with programming communities.

## Features

### For Students:
- **Programming Practice**: Access 400+ coding problems categorized by difficulty and pattern
- **Interactive Quizzes**: Subject-wise MCQs with timer and immediate feedback
- **Progress Tracking**: Monitor solved problems, quiz scores, and streaks
- **Leaderboard**: Compete with other students
- **Community Features**: Join programming communities and discussions

### For Administrators:
- **Content Management**: Add/edit/delete coding problems and quizzes
- **User Management**: Monitor and manage student accounts
- **Analytics**: View system statistics and user activity
- **Community Moderation**: Moderate community posts and discussions

## Technology Stack

- **Frontend**: Java Swing
- **Backend**: Java with MVC architecture
- **Database**: MySQL (PostgreSQL supported)
- **Build Tool**: Maven
- **Testing**: JUnit, Mockito

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- MySQL Server 8.0+ or PostgreSQL 13+
- Maven 3.6+

## Installation & Setup

### 1. Database Setup

#### MySQL Setup:
```bash
# Create database
mysql -u root -p
CREATE DATABASE codenest_db;

# Run schema and seed scripts
mysql -u root -p codenest_db < database/schema.sql
mysql -u root -p codenest_db < database/seed_data.sql
```

#### PostgreSQL Setup:
```bash
# Create database
psql -U postgres
CREATE DATABASE codenest_db;

# Run schema and seed scripts (modify syntax for PostgreSQL)
psql -U postgres -d codenest_db -f database/schema.sql
psql -U postgres -d codenest_db -f database/seed_data.sql
```

### 2. Configuration

Update database connection settings in `src/main/java/com/codenest/util/DatabaseUtil.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/codenest_db";
private static final String DB_USER = "your_username";
private static final String DB_PASSWORD = "your_password";
```

### 3. Build and Run

```bash
# Clone the repository
git clone <repository-url>
cd codenest

# Build the project
mvn clean compile

# Run tests
mvn test

# Package into executable JAR
mvn clean package

# Run the application
java -jar target/student-service-platform-1.0.0-jar-with-dependencies.jar
```

## Default Login Credentials

### Admin Account:
- Email: `admin@codenest.com`
- Password: `admin123`
- Role: Admin

### Student Account:
- Email: `john@example.com`
- Password: `password123`
- Role: Student

## Project Structure

```
src/
├── main/java/com/codenest/
│   ├── Main.java                 # Application entry point
│   ├── model/                    # Data models
│   │   ├── User.java
│   │   ├── Problem.java
│   │   ├── Quiz.java
│   │   ├── Question.java
│   │   └── Community.java
│   ├── dao/                      # Data Access Objects
│   │   ├── UserDAO.java
│   │   ├── ProblemDAO.java
│   │   └── QuizDAO.java
│   ├── service/                  # Business Logic Layer
│   │   ├── UserService.java
│   │   ├── ProblemService.java
│   │   └── QuizService.java
│   ├── controller/               # Controllers
│   │   ├── AuthController.java
│   │   ├── ProblemController.java
│   │   └── QuizController.java
│   ├── ui/                       # User Interface
│   │   ├── LoginWindow.java
│   │   ├── RegistrationWindow.java
│   │   ├── StudentDashboard.java
│   │   ├── AdminDashboard.java
│   │   └── QuizWindow.java
│   └── util/                     # Utility Classes
│       └── DatabaseUtil.java
├── test/java/                    # Test cases
└── resources/                    # Resources
```

## Database Schema

### Core Tables:
- `users`: User accounts and authentication
- `problems`: Programming problems database
- `quizzes`: Quiz metadata
- `questions`: Quiz questions and options
- `communities`: Programming communities
- `posts`: Community discussions
- `quiz_attempts`: Student quiz results
- `problem_progress`: Problem solving progress

## Features in Detail

### Programming Section:
- Browse 400+ problems by difficulty (Easy/Medium/Hard)
- Filter by patterns (Arrays, Strings, Trees, etc.)
- Mark problems as solved
- Track progress and statistics

### Quiz Section:
- Subject-wise multiple choice questions
- Timed quizzes with countdown
- Immediate feedback and scoring
- Progress tracking and history

### Admin Panel:
- Add new programming problems
- Manage quiz questions
- User account management
- System analytics and monitoring

## Testing

Run the test suite:
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Generate test report
mvn surefire-report:report
```

## Building Distribution

### Create Executable JAR:
```bash
mvn clean package
```

### Create Distribution Package:
```bash
# The JAR file will be created in target/ directory
target/student-service-platform-1.0.0-jar-with-dependencies.jar
```

## Deployment

### Single Machine Deployment:
1. Install Java 11+ on target machine
2. Install MySQL/PostgreSQL server
3. Create database and run setup scripts
4. Copy JAR file and configuration
5. Run application

### Multi-User Deployment:
1. Set up central database server
2. Install application on each client machine
3. Configure database connection to central server
4. Ensure network connectivity

## Troubleshooting

### Common Issues:

1. **Database Connection Error**:
   - Verify database server is running
   - Check connection credentials
   - Ensure database exists and is accessible

2. **Application Won't Start**:
   - Check Java version (requires Java 11+)
   - Verify all dependencies are included
   - Check system resources

3. **UI Display Issues**:
   - Update Java to latest version
   - Check system display settings
   - Try different Look and Feel

### Debug Mode:
```bash
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar CodeNest.jar
```

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -am 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Create Pull Request

## Future Enhancements

- Web-based interface
- Real-time collaboration
- Code execution engine
- Advanced analytics
- Mobile application
- Integration with version control systems

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the repository
- Contact: support@codenest.com
- Documentation: https://docs.codenest.com

## Version History

- **v1.0.0** - Initial release with core features
  - User authentication and registration
  - Programming problems database
  - Quiz system
  - Basic admin panel
  - Student dashboard

---

**Note**: This is an educational project designed for learning purposes. It demonstrates full-stack Java desktop application development with proper MVC architecture, database integration, and comprehensive testing.
*/