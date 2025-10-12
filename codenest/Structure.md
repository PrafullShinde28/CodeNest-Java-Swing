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









mvn clean package
java -jar target/student-service-platform-1.0.0-jar-with-dependencies.jar
