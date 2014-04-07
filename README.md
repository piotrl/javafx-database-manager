javafx-database-manager
=======================

It's example usage of javaFX in a desktop app.
It operate on User list stored in database and do stuff like adding, editing or updating.

## Configuration
If you want run a demo, you should make some changes in project:

* Download JDBC driver to your sql engine
* Fill database.properties file with your details to look like this:
    jdbc.drivers=org.postgresql.Driver
    jdbc.url=jdbc:postgresql://localhost/db_users
    jdbc.username=tester
    jdbc.password=test
where `db_users` are table name
* Init database with SQL:
    CREATE TABLE users(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name CHAR(20), registered DATE);

**I know I** should generate this automatically. I have it in my todo list.

## Licence
MIT Licence: http://rem.mit-license.org/


