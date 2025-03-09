# Contact API Spec

## Create Contact

Endpoint : POST /api/contacts

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
    "firstname" : "Ucup",
    "lastname" : "bin Otong",
    "email" : "ucup@gmail.com",
    "phone" : "081234567890"
}
```

Response Body (Success) :

```json
{
    "messages" : "Create contact success",
    "data" : {
        "id" : "random-string",
        "firstname" : "Ucup",
        "lastname" : "bin Otong",
        "email" : "ucup@gmail.com",
        "phone" : "081234567890",
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed) :

```json
{
    "messages" : "Create contact failed",
    "errors" : "Email format invalid, ..."
}
```

## Update Contact

Endpoint : PUT /api/contacts/{contact_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
    "firstname" : "Otong",
    "lastname" : "Surotong",
    "email" : "otong@gmail.com",
    "phone" : "081234567891"
}
```

Response Body (Success) :

```json
{
    "messages" : "Update contact success",
    "data" : {
        "id" : "random-string",
        "firstname" : "Otong",
        "lastname" : "Surotong",
        "email" : "otong@gmail.com",
        "phone" : "081234567891",
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed) :

```json
{
    "messages" : "Update contact failed",
    "errors" : "Email format invalid, ..."
}
```

## Get Contact

Endpoint : GET /api/contacts/{contact_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Get contact success",
    "data" : {
        "id" : "random-string",
        "firstname" : "Ucup",
        "lastname" : "bin Otong",
        "email" : "ucup@gmail.com",
        "phone" : "081234567890",
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed, 404) :

```json
{
    "messages" : "Get contact failed",
    "errors" : "Contact is not found"
}
```

## Search Contact

Endpoint : Get /api/contacts

Query Param :

- name : String, contact firstname or lastname, using like query, optional
- phone : String, contact phone, using like query, optional
- email : String, contact email, using like query, optional
- page : Integer, start from 0, default 0
- size : Integer, default 10

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Search contact success",
    "data" : [
        {
            "id" : "random-string",
            "firstname" : "Ucup",
            "lastname" : "bin Otong",
            "email" : "ucup@gmail.com",
            "phone" : "081234567890",
            "created_at" : "current-timestamp",
            "updated_at" : "current-timestamp"
        }
    ],
    "paging" : {
        "current_page" : 0,
        "total_page" : 5,
        "size" : 10,
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

## Delete Contact

Endpoint : DELETE /api/contacts/{contact_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Delete contact success",
}
```

Response Body (Failed, 404) :

```json
{
    "messages" : "Delete contact failed",
    "errors" : "Contact is not found"
}
```
