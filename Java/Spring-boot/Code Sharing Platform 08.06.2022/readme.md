## Endpoints

```GET /api/code/N``` returns JSON with the N-th uploaded code snippet.\
```POST /api/code/new``` takes a JSON object, and return JSON with a single field id.\
```GET /api/code/latest``` returns a JSON array with 10 most recently uploaded code snippets sorted from the newest to the oldest.\
```GET /code/N``` returns HTML that contains the N-th uploaded code snippet.\
```GET /code/new``` return HTML.\
```GET /code/latest``` returns HTML that contains 10 most recently uploaded code snippets.

## Examples

Example 1: ```POST /api/code/new``` with the following body:
```
{
    "code": "class Code { ...",
    "time": 0,
    "views": 0
}
```
Response: ```{ "id" : "7dc53df5-703e-49b3-8670-b1c468f47f1f" }```.

Example 2: ```GET /api/code/2187c46e-03ba-4b3a-828b-963466ea348c``` request:

Response:
```
{
    "code": "Secret code",
    "date": "2020/05/05 12:01:45",
    "time": 4995,
    "views": 4
}
```

Example 3: ```GET /code/2187c46e-03ba-4b3a-828b-963466ea348c``` request:

Response:
![ScreenShot](https://github.com/ICalmPersonI/Spring-Boot/tree/main/Code%20Sharing%20Platform/Example%203.jpg?raw=true)


Example 4: ```GET /api/code/latest``` request:

Response:
```
[
    {
        "code": "public static void ...",
        "date": "2020/05/05 12:00:43",
        "time": 0,
        "views": 0
    },
    {
        "code": "class Code { ...",
        "date": "2020/05/05 11:59:12",
        "time": 0,
        "views": 0
    }
]
```

Example 5 : ```GET /code/latest``` request:

Response:
![ScreenShot](https://github.com/ICalmPersonI/Spring-Boot/tree/main/Code%20Sharing%20Platform/Example%205.jpg?raw=true)

Example 6: ```GET /code/new``` request:

Response:
![ScreenShot](https://github.com/ICalmPersonI/Spring-Boot/tree/main/Code%20Sharing%20Platform/Example%206.jpg?raw=true)


