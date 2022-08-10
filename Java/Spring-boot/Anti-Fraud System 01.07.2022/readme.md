# About
This project demonstrates (in a simplified form) the principles of anti-fraud systems in the financial sector. For this project, we will work on a system with an expanded role model, a set of REST endpoints responsible for interacting with users, and an internal transaction validation logic based on a set of heuristic rules.

# Endpoints

## Transaction
```POST /api/antifraud/transaction```\
**Request body:**
```
{
  "amount": <Long>,
  "ip": "<String value, not empty>",
  "number": "<String value, not empty>",
  "region": "<String value, not empty>",
  "date": "yyyy-MM-ddTHH:mm:ss"
}
````
**Response:**
- HTTP OK (200)
```
{
  "result": <String>,
  "info": <String>
}
```
- HTTP Bad Request (400)\
If a request contains wrong data.

```PUT /api/antifraud/transaction``` adds feedback for a transaction.\
**Request body:**
```
{
   "transactionId": <Long>,
   "feedback": "<String>"
}
```
**Response:**
- HTTP OK (200)
```
{
  "transactionId": <Long>,
  "amount": <Long>,
  "ip": "<String value, not empty>",
  "number": "<String value, not empty>",
  "region": "<String value, not empty>",
  "date": "yyyy-MM-ddTHH:mm:ss",
  "result": "<String>",
  "feedback": "<String>"
}
```
- HTTP Conflict (409)\
If the feedback for a specified transaction is already in the database.
- HTTP Bad Request (400)\
If the feedback has the wrong format (other than ALLOWED, MANUAL_PROCESSING, PROHIBITED).
- HTTP Unprocessable Entity (422)\
If the feedback throws an Exception following the table.
- HTTP Not Found (404).\
If the transaction is not found in history.

```GET /api/antifraud/history``` shows the transaction history.\
**Response:**
- HTTP OK (200)
```
[
    {
      "transactionId": <Long>,
      "amount": <Long>,
      "ip": "<String value, not empty>",
      "number": "<String value, not empty>",
      "region": "<String value, not empty>",
      "date": "yyyy-MM-ddTHH:mm:ss",
      "result": "<String>",
      "feedback": "<String>"
    },
     ...
    {
      "transactionId": <Long>,
      "amount": <Long>,
      "ip": "<String value, not empty>",
      "number": "<String value, not empty>",
      "region": "<String value, not empty>",
      "date": "yyyy-MM-ddTHH:mm:ss",
      "result": "<String>",
      "feedback": "<String>"
    }
]
```

```GET /api/antifraud/history/{(String) number}``` ransaction history for a specified card number.

## Blacklist controller
```POST /api/antifraud/suspicious-ip``` saves suspicious IP addresses to the database.\
**Request body:**
```
{
   "ip": "<String value, not empty>"
}
```
**Responses:**
- HTTP OK (200)
```
{
   "id": "<Long value, not empty>",
   "ip": "<String value, not empty>"
}
```
- HTTP Conflict (409)\
If an IP is already in the database.
- HTTP Bad Request (400)\
If an IP address has the wrong format.

```GET /api/antifraud/suspicious-ip``` shows IP addresses in the database.\
**Response:**
- HTTP OK (200)
```
[
    {
        "id": 1,
        "ip": "192.168.1.1"
    },
     ...
    {
        "id": 100,
        "ip": "192.168.1.254"
    }
]
```

```DELETE /api/antifraud/suspicious-ip/{(String) ip}``` deletes IP addresses from the database.\
**Response:**
- HTTP OK (200)
```
{
   "status": "IP <ip address> successfully removed!"
}
```
- HTTP Not Found (404)\
If an IP is not found in the database.
- HTTP Bad Request (400)\
If an IP address has the wrong format.

```POST /api/antifraud/stolencard``` saves stolen card data in the database.\
**Request body:**
```
{
   "number": "<String value, not empty>"
}
```
**Response:**
- HTTP OK (200)
```
{
   "id": "<Long value, not empty>",
   "number": "<String value, not empty>"
}
```
- HTTP Conflict (409).\
If the card number is already in the database.
- HTTP Bad Request (400).\
If a card number has the wrong format.

```GET /api/antifraud/stolencard``` shows card numbers stored in the database.\
**Response:**
- HTTP OK (200)
```
[
    {
        "id": 1,
        "number": "4000008449433403"
    },
     ...
    {
        "id": 100,
        "number": "4000009455296122"
    }
]
```

```DELETE /api/antifraud/stolencard/{(String) number}``` deletes a card number from the database.\
**Response:**
- HTTP OK (200)
```
{
   "status": "Card <number> successfully removed!"
}
```
- HTTP Not Found (404)\
If a card number is not found in the database.
- HTTP Bad Request (400)\
If a card number follows the wrong format.

## User controller
```POST /api/auth/user``` registration.\
**Request body:**
```
{
   "id": <Long value, not empty>,
   "name": "<String value, not empty>",
   "username": "<String value, not empty>",
   "role": "<String value, not empty>"
}
```
**Response:**
- HTTP CREATED (201)
```
{
   "id": <Long value, not empty>,
   "name": "<String value, not empty>",
   "username": "<String value, not empty>"
}
```
- HTTP CONFLICT (409)\
If an attempt to register an existing user was a failure.
- BAD REQUEST (400)\
If a request contains wrong data.

```GET /api/auth/list```\
**Response:**
- HTTP OK (200)
```
[
    {
        "id": <user1 id>,
        "name": "<user1 name>",
        "username": "<user1 username>",
        "role": "<user1 role>"
    },
     ...
    {
        "id": <userN id>,
        "name": "<userN name>",
        "username": "<userN username>",
        "role": "<userN role>"
    }
]
```

```DELETE /api/auth/user/{(String) username}``` delete user by username.\
**Response:**
- HTTP OK (200)
```
{
   "username": "<username>",
   "status": "Deleted successfully!"
}
```
- HTTP Not Found (404)\
If a user is not found.

```PUT /api/auth/role``` changes user roles.\
**Request body:**
```
{
   "username": "<String value, not empty>",
   "role": "<String value, not empty>"
}
```
**Response:**
- HTTP OK (200)
```
{
   "id": <Long value, not empty>,
   "name": "<String value, not empty>",
   "username": "<String value, not empty>",
   "role": "<String value, not empty>"
}
````
- HTTP Not Found (404)\
If a user is not found.
- HTTP Bad Request (400)\
If a role is not SUPPORT or MERCHANT.
- HTTP Conflict (409)\
If you want to assign a role that has been already provided to a user.

```PUT /api/auth/access``` locks/unlocks users\
**Request body:**
```
{
   "username": "<String value, not empty>",
   "operation": "<[LOCK, UNLOCK]>"  // determines whether the user will be activated or deactivated
}
```
**Response:**
- HTTP OK (200)
```
{
    "status": "User <username> <[locked, unlocked]>!"
}
```
- HTTP Bad Request (400)\
For safety reasons, ADMINISTRATOR cannot be blocked.
- HTTP Not Found (404)\
If a user is not found.

## Role model
|                                  | Anonymous | MERCHANT | ADMINISTRATOR | SUPPORT |
|----------------------------------|-----------|----------|---------------|---------|
| POST /api/auth/user/             | +         | +        | +             | +       |
| PUT /api/auth/access/            |           |          | +             |         |
| PUT /api/auth/role/              |           |          | +             |         |
| GET /api/auth/list/              |           |          | +             | +       |
| DELETE /api/auth/user/           |           |          | +             |         |
| POST /api/antifraud/transaction/ |           | +        |               |         |
| POST /api/antifraud/access/      |           |          | +             |         |
| /api/antifraud/suspicious-ip/    |           |          |               | +       |
| /api/antifraud/stolencard/       |           |          |               | +       |
| PUT /api/antifraud/transaction/  |           |          |               | +       |
| GET /api/antifraud/history/      |           |          |               | +       |
