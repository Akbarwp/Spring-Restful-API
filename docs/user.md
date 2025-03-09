# User API Spec

## Register User

Endpoint : POST /api/auth/register

Request Body :

```json
{
    "email" : "admin@gmail.com",
    "password" : "admin123",
    "name" : "Admin Admin",
}
```

Response Body (Success) :

```json
{
    "messages" : "Register success",
    "data" : {
        "id" : "random-string",
        "email" : "admin@gmail.com",
        "name" : "Admin Admin",
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed) :

```json
{
    "messages" : "Register failed",
    "errors" : "Email must not blank, ..."
}
```

## Login User

Endpoint : POST /api/auth/login

Request Body :

```json
{
    "email" : "admin@gmail.com",
    "password" : "admin123",
}
```

Response Body (Success) :

```json
{
    "messages" : "Login success",
    "data" : {
        "email" : "admin@gmail.com",
        "name" : "Admin Admin",
        "token" : "TOKEN",
        "expired_at" : 1234567 // miliseconds
    }
}
```

Response Body (Failed, 401) :

```json
{
    "messages" : "Login failed",
    "errors" : "Email or Password wrong"
}
```

## Get User

Endpoint : GET /api/users/current

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "data" : {
        "id" : "random-string",
        "email" : "admin@gmail.com",
        "name" : "Admin Admin",
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed, 401) :

```json
{
    "messages" : "Login first",
    "errors" : "Unauthorized"
}
```

## Update User

Endpoint : PATCH /api/users/current

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
    "password" : "new password", // if only want to update password
    "name" : "new name" // if only want to update name
}
```

Response Body (Success) :

```json
{
    "messages" : "Update user success",
    "data" : {
        "id" : "random-string",
        "email" : "admin@gmail.com",
        "name" : "Admin Admin",
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed, 401) :

```json
{
    "messages" : "Update user failed",
    "errors" : "Unauthorized"
}
```

## Logout User

Endpoint : DELETE /api/auth/logout

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Logout success",
}
```