# Address API Spec

## Create Address

Endpoint : POST /api/contacts/{contact_id}/addresses

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
    "street" : "Street XYZ",
    "city" : "Sidoarjo",
    "province" : "East Java",
    "country" : "Indonesia",
    "postalCode" : "12345"
}
```

Response Body (Success) :

```json
{
    "messages" : "Create address success",
    "data" : {
        "id" : "random-string",
        "street" : "Street XYZ",
        "city" : "Sidoarjo",
        "province" : "East Java",
        "country" : "Indonesia",
        "postalCode" : "12345",
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed) :

```json
{
    "messages" : "Create address failed",
    "errors" : "Contact is not found, ..."
}
```

## Update Address

Endpoint : PUT /api/contacts/{contact_id}/addresses/{address_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
    "street" : "Street ABC",
    "city" : "Surabaya",
    "province" : "East Java",
    "country" : "Indonesia",
    "postalCode" : "54321"
}
```

Response Body (Success) :

```json
{
    "messages" : "Update address success",
    "data" : {
        "id" : "random-string",
        "street" : "Street ABC",
        "city" : "Surabaya",
        "province" : "East Java",
        "country" : "Indonesia",
        "postalCode" : "54321",
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed) :

```json
{
    "messages" : "Update address failed",
    "errors" : "Contact is not found, ..."
}
```

## Get Address

Endpoint : GET /api/contacts/{contact_id}/addresses/{address_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Get address success",
    "data" : {
        "id" : "random-string",
        "street" : "Street XYZ",
        "city" : "Sidoarjo",
        "province" : "East Java",
        "country" : "Indonesia",
        "postalCode" : "12345",
        "created_at" : "current-timestamp",
        "updated_at" : "current-timestamp"
    }
}
```

Response Body (Failed, 404) :

```json
{
    "messages" : "Get address failed",
    "errors" : "Address is not found"
}
```

## Delete Address

Endpoint : DELETE /api/contacts/{contact_id}/addresses/{address_id}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Delete address success",
}
```

Response Body (Failed, 404) :

```json
{
    "messages" : "Delete address failed",
    "errors" : "Contact is not found"
}
```

## List Address

Endpoint : GET /api/contacts/{contact_id}/addresses

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "messages" : "Get list address success",
    "data" : [
        {
            "id" : "random-string",
            "street" : "Street XYZ",
            "city" : "Sidoarjo",
            "province" : "East Java",
            "country" : "Indonesia",
            "postalCode" : "12345",
            "created_at" : "current-timestamp",
            "updated_at" : "current-timestamp"
        }
    ]
}
```

Response Body (Failed, 404) :

```json
{
    "messages" : "Get list address failed",
    "errors" : "Contact is not found"
}
```
