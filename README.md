javafx-database-manager
=======================

It's example usage of javaFX in a desktop app. 
It operate on User list stored in database and do stuff like adding, editing or updating.
Repository contains also basic Database Abstraction Layer to convert objects into SQL commands.

## Configuration
If you want run a demo, you should make some changes in project:

* Download JDBC driver to your sql engine
* Fill database.properties file with your details to look like this:
    
    ```
    jdbc.drivers=org.postgresql.Driver
    jdbc.url=jdbc:postgresql://localhost/db_users
    jdbc.username=tester
    jdbc.password=test
    ```
    
    where `db_users` is a table name

* Init database with SQL _(PostgreSQL example)_:
    ``` 
    CREATE TABLE users(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name CHAR(20), registered DATE);
    ``` 
**I know** I should generate this automatically. I have it in my todo list.


