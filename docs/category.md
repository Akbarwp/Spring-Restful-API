# Category Product API Spec

## Create Category Product

Endpoint : POST /api/categories

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
    "name" : "Computer & Laptop"
}
```

Response Body (Success) :

```json
{
    "messages" : "Create category success",
    "data" : {
        "id" : "random-string",
        "name" : "Computer & Laptop"
    }
}
```

Response Body (Failed) :

```json
{
    "messages" : "Create category failed",
    "errors" : "Name must be filled, ..."
}
```

## Update Category Product

Endpoint : PUT /api/categories/{category_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
    "name" : "Gaming"
}
```

Response Body (Success) :

```json
{
    "messages" : "Update category success",
    "data" : {
        "id" : "random-string",
        "name" : "Gaming"
    }
}
```

Response Body (Failed) :

```json
{
    "messages" : "Update category failed",
    "errors" : "Name must be filled, ..."
}
```

## Get Category Product

Endpoint : GET /api/categories/{category_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Get category success",
    "data" : {
        "id" : "random-string",
        "name" : "Computer & Laptop"
    }
}
```

Response Body (Failed, 404) :

```json
{
    "messages" : "Get category failed",
    "errors" : "Category is not found"
}
```

## Search Category Product

Endpoint : Get /api/categories

Query Param :

- name : String, category name, using like query, optional
- page : Integer, start from 0, default 0
- size : Integer, default 10

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Search category success",
    "data" : [
        {
            "id" : "random-string",
            "name" : "Computer & Laptop"
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

## Delete Category Product

Endpoint : DELETE /api/categories/{category_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Delete category success",
}
```

Response Body (Failed, 404) :

```json
{
    "messages" : "Delete category failed",
    "errors" : "Category is not found"
}
```
