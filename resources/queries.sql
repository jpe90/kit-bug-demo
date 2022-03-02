-- place your sql queries here
-- see https://www.hugsql.org/ for documentation

-- :name get-user-for-auth* :? :1
-- :doc selects a user for authentication
SELECT * from users
WHERE email = :email
