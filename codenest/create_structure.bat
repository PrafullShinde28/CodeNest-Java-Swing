@echo off
set ROOT=%cd%\src

:: Create directories
mkdir %ROOT%\main\java\com\codenest\model
mkdir %ROOT%\main\java\com\codenest\dao
mkdir %ROOT%\main\java\com\codenest\service
mkdir %ROOT%\main\java\com\codenest\controller
mkdir %ROOT%\main\java\com\codenest\ui
mkdir %ROOT%\main\java\com\codenest\util
mkdir %ROOT%\test\java
mkdir %ROOT%\main\resources

:: Main class
echo package com.codenest; public class Main { public static void main(String[] args) { System.out.println("CodeNest Running..."); } } > %ROOT%\main\java\com\codenest\Main.java

:: Model classes
echo package com.codenest.model; public class User {} > %ROOT%\main\java\com\codenest\model\User.java
echo package com.codenest.model; public class Problem {} > %ROOT%\main\java\com\codenest\model\Problem.java
echo package com.codenest.model; public class Quiz {} > %ROOT%\main\java\com\codenest\model\Quiz.java
echo package com.codenest.model; public class Question {} > %ROOT%\main\java\com\codenest\model\Question.java
echo package com.codenest.model; public class Community {} > %ROOT%\main\java\com\codenest\model\Community.java

:: DAO classes
echo package com.codenest.dao; public interface UserDAO {} > %ROOT%\main\java\com\codenest\dao\UserDAO.java
echo package com.codenest.dao; public interface ProblemDAO {} > %ROOT%\main\java\com\codenest\dao\ProblemDAO.java
echo package com.codenest.dao; public interface QuizDAO {} > %ROOT%\main\java\com\codenest\dao\QuizDAO.java

:: Service classes
echo package com.codenest.service; public class UserService {} > %ROOT%\main\java\com\codenest\service\UserService.java
echo package com.codenest.service; public class ProblemService {} > %ROOT%\main\java\com\codenest\service\ProblemService.java
echo package com.codenest.service; public class QuizService {} > %ROOT%\main\java\com\codenest\service\QuizService.java

:: Controller classes
echo package com.codenest.controller; public class AuthController {} > %ROOT%\main\java\com\codenest\controller\AuthController.java
echo package com.codenest.controller; public class ProblemController {} > %ROOT%\main\java\com\codenest\controller\ProblemController.java
echo package com.codenest.controller; public class QuizController {} > %ROOT%\main\java\com\codenest\controller\QuizController.java

:: UI classes
echo package com.codenest.ui; public class LoginWindow {} > %ROOT%\main\java\com\codenest\ui\LoginWindow.java
echo package com.codenest.ui; public class RegistrationWindow {} > %ROOT%\main\java\com\codenest\ui\RegistrationWindow.java
echo package com.codenest.ui; public class StudentDashboard {} > %ROOT%\main\java\com\codenest\ui\StudentDashboard.java
echo package com.codenest.ui; public class AdminDashboard {} > %ROOT%\main\java\com\codenest\ui\AdminDashboard.java
echo package com.codenest.ui; public class QuizWindow {} > %ROOT%\main\java\com\codenest\ui\QuizWindow.java

:: Utility class
echo package com.codenest.util; public class DatabaseUtil {} > %ROOT%\main\java\com\codenest\util\DatabaseUtil.java

:: Resources
echo # Application configuration > %ROOT%\main\resources\application.properties

echo.
echo ✅ CodeNest structure created inside Maven project!
pause
