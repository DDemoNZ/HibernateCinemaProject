# Cinema project

# Table of Contents
* [Project purpose](#purpose)
* [Project structure](#structure)
* [For developer](#developer-start)
* [Author](#author)

# <a name="purpose"></a>Project purpose
This is a template for creating an e-cinema (ticket office). Application is built on REST principles.
You can use Postman for testing it.
<hr>

Every user can register with email and password, then user can see available functions:
* get all movies
* get available movie-sessions
* add ticket to shopping cart
* get all tickets from shopping cart
* complete order
* get orders history

There are specific tools for admins only such as:
* add movies, cinema-halls, movie-sessions
<hr>

# <a name="structure"></a>Project Structure
* Java 11
* Maven 4.0.0
* Spring 5.2.2.RELEASE (Context, ORM, Web-MVC, Security-core)
* Hibernate 6.1.2.Final
* Jackson-databind 2.10.2
* Log4j 2.13.0
* MySQL
<hr>

# <a name="developer-start"></a>For developer
Open the project in your IDE as Maven project.

Configure Tomcat:
* add artifact
* add SKD 11.*
* Add SDK 11.* in project structure.
* Change a db.url, db.username and db.password in src/main/resources/db.properties on your own
 properties.
* Change a logPath in src/main/resources/log4j2.xml if you want logging info.
* Run the project.

* By default there are two users already registered with ADMIN role (username - "admin", password - "admin
") and with USER role (username - "user" and password - "user").

You can use Postman for testing it.

# <a name="author"></a>Author
[Dmitrii Zinchuk](https://github.com/DDemoNZ)
