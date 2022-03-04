This repo reproduces an issue occuring when adding a query result from postgresql to a session in a Kit web application. 

To reproduce the problem, first create the postgresql database:

```
psql postgres
CREATE USER kbd WITH PASSWORD 'password';
CREATE DATABASE kbd WITH OWNER kbd;
```

Then start the app and navigate to localhost:3000. The homepage will try to lookup the user and add them to the session in [pages.clj](https://github.com/jpe90/kit-bug-demo/blob/master/src/clj/jpe/kit_bug_demo/web/routes/pages.clj), which currently throws an exception.
