## Endpoints
### Returns the information about the movie theatre.
```
GET /seats
Accept: application/json
Content-Type: application/json
```
**200**
```
{
   "total_rows":5,
   "total_columns":6,
   "available_seats":[
      {
         "row":1,
         "column":1
      },

      ........

      {
         "row":5,
         "column":5
      },
      {
         "row":5,
         "column":6
      }
   ]
}

```

### Marks a booked ticket as purchased.
```
POST /purchase
Accept: application/json
Content-Type: application/json
```
**200**
```
{
    "token": "00ae15f2-1ab6-4a02-a01f-07810b42c0ee",
    "ticket": {
        "row": 1,
        "column": 1,
        "price": 10
    }
}
```
**400**
```
{
    "error": "The ticket has been already purchased!"
},

{
    "error": "The number of a row or a column is out of bounds!"
}

```

### Requests and allow customers to refund their tickets.
```
POST /return
Accept: application/json
Content-Type: application/json
```
**200**
```

{
    "returned_ticket": {
        "row": 1,
        "column": 1,
        "price": 10
    }
}
```
**400**
```
{
    "error": "Wrong token!"
}

```

### Returns the movie theatre statistics
Request parm:
password = super_secret
```
POST /stats
Accept: application/json
Content-Type: application/json
```
**200**
```
{
    "current_income": 0,
    "number_of_available_seats": 81,
    "number_of_purchased_tickets": 0
}
```
**401**
```
{
    "error": "The password is wrong!"
}
```
