This repo reproduces a bug when adding a query result from postgresql to a session in a Kit web application. A successful login will add a result of a user query to the session state. When a login is attempted, the session state is logged. 

To reproduce the problem, first create the postgresql database:

```
psql postgres
CREATE USER kbd WITH PASSWORD 'password';
CREATE DATABASE kbd WITH OWNER kbd;
```

Migrations should automatically create a user database and insert a user:
Email:    `a@b.com`
Password: `pass`

Logging in as the above user, and then making another login attempt, should show that the user was added to the session state. However here it will throw an exception.
