# Product API Spec

## Create Product

Endpoint : POST /api/products

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
    "name" : "Rexus Daxa Air IV",
    "price_buy" : 800000.00,
    "price_sell" : 900000.00,
    "stock" : 100,
    "description" : "Gaming mouse high end",
    "category_id" : "random-string"
}
```

Response Body (Success) :

```json
{
    "messages" : "Create product success",
    "data" : {
        "id" : "random-string",
        "name" : "Rexus Daxa Air IV",
        "price_buy" : 800000.00,
        "price_sell" : 900000.00,
        "stock" : 100,
        "description" : "Gaming mouse high end",
        "category_product" : {
            "id" : "random-string",
            "name" : "Computer & Laptop"
        },
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed) :

```json
{
    "messages" : "Create product failed",
    "errors" : "Name must be filled, ..."
}
```

## Update Product

Endpoint : PUT /api/products/{product_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
    "name" : "Rexus Daxa Air Mini",
    "price_buy" : 850000.00,
    "price_sell" : 950000.00,
    "stock" : 50,
    "description" : "Gaming mouse high end",
    "category_id" : "random-string"
}
```

Response Body (Success) :

```json
{
    "messages" : "Update product success",
    "data" : {
        "id" : "random-string",
        "name" : "Rexus Daxa Air Mini",
        "price_buy" : 850000.00,
        "price_sell" : 950000.00,
        "stock" : 50,
        "description" : "Gaming mouse high end",
        "category_product" : {
            "id" : "random-string",
            "name" : "Computer & Laptop"
        },
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed) :

```json
{
    "messages" : "Update product failed",
    "errors" : "Name must be filled, ..."
}
```

## Get Product

Endpoint : GET /api/products/{product_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Get product success",
    "data" : {
        "id" : "random-string",
        "name" : "Rexus Daxa Air Mini",
        "price_buy" : 850000,
        "price_sell" : 950000,
        "stock" : 50,
        "description" : "Gaming mouse high end",
        "category_product" : {
            "id" : "random-string",
            "name" : "Computer & Laptop"
        },
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed, 404) :

```json
{
    "messages" : "Get product failed",
    "errors" : "Product is not found"
}
```

## Search Product

Endpoint : Get /api/products

Query Param :

- name : String, product name, using like query, optional
- price_buy : Double, product price_buy, using >= query, optional
- price_sell : Double, product price_sell, using >= query, optional
- stock : Integer, product stock, using >= query, optional
- page : Integer, start from 0, default 0
- size : Integer, default 10

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Search product success",
    "data" : [
        {
            "id" : "random-string",
            "name" : "Rexus Daxa Air Mini",
            "price_buy" : 850000,
            "price_sell" : 950000,
            "stock" : 50,
            "description" : "Gaming mouse high end",
            "category_product" : {
                "id" : "random-string",
                "name" : "Computer & Laptop"
            },
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

## Delete Product

Endpoint : DELETE /api/products/{product_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Delete product success",
}
```

Response Body (Failed, 404) :

```json
{
    "messages" : "Delete product failed",
    "errors" : "Product is not found"
}
```

## List Product by Category

Endpoint : GET /api/category/{category_id}/products

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Get list product success",
    "data" : [
        {
            "id" : "random-string",
            "name" : "Rexus Daxa Air Mini",
            "price_buy" : 850000.00,
            "price_sell" : 950000.00,
            "stock" : 50,
            "description" : "Gaming mouse high end",
            "category_product" : {
                "id" : "random-string",
                "name" : "Computer & Laptop"
            },
            "created_at" : "current-timestamp",
            "updated_at" : "current-timestamp"
        }
    ]
}
```

Response Body (Failed, 404) :

```json
{
    "messages" : "Get list product failed",
    "errors" : "Category product is not found"
}
```
