## Endpoints

```GET api/quizzes``` returns only 10 quizzes at once. Reqest parameter ```page```, type: integer.\
```GET api/quizzes/comleted``` returns only 10 comleted quizzes for user at once.\
```GET api/quizzes/{id}``` returns quiz by id.\
```POST api/quizzes``` create quiz.\
```POST api/quizzes/{id}/solve``` solve quiz by id. Request parameter ```answer```, type: integer array.\
```DELETE api/quizzes/{id}``` delete quiz by id. Delete by id. This can only be done by the author of the quiz.\
```POST /api/register``` register user.

## Examples

Example 1: ```GET api/quizzes```
Response:
```
{
  "totalPages":1,
  "totalElements":3,
  "content":[
    {"id":102,"title":"Test 1","text":"Text 1","options":["a","b","c"]},
    {"id":103,"title":"Test 2","text":"Text 2","options":["a", "b", "c", "d"]},
    {"id":202,"title":"The Java Logo","text":"What is depicted on the Java logo?",
     "options":["Robot","Tea leaf","Cup of coffee","Bug"]}
  ]
}
```
Example 2: ```GET api/quizzes/comleted```
Response:
```
{
  "totalPages":1,
  "totalElements":5,
  "content":[
    {"id":103,"completedAt":"2019-10-29T21:13:53.779542"},
    {"id":102,"completedAt":"2019-10-29T21:13:52.324993"},
    {"id":101,"completedAt":"2019-10-29T18:59:58.387267"},
    {"id":101,"completedAt":"2019-10-29T18:59:55.303268"},
    {"id":202,"completedAt":"2019-10-29T18:59:54.033801"}
  ]
}
```

Example 3: ```GET api/quizzes/{id}```\
Response:
```
{
  "id":102,
  "title":"Test 1",
  "text":"Text 1",
  "options":["a","b","c"]
}
```

Example 4: ```POST api/quizzes``` request with the following body:
```
{
  "title": "Coffee drinks",
  "text": "Select only coffee drinks.",
  "options": ["Americano","Tea","Cappuccino","Sprite"],
  "answer": [0,2]
}
```
Response:
```
{
  "id": 1,
  "title": "Coffee drinks",
  "text": "Select only coffee drinks.",
  "options": ["Americano","Tea","Cappuccino","Sprite"]
}
```

Example 5: ```POST api/quizzes/{id}/solve```\
Response:
If the passed answer is correct:
```
{"success":true,"feedback":"Congratulations, you're right!"}
```
If the answer is incorrect:
```
{"success":false,"feedback":"Wrong answer! Please, try again."}
```
If the specified quiz does not exist, the server returns the 404 (Not found) status code.

Example 6: ```DELETE api/quizzes/{id}```\
If the operation was successful, the service returns the 204 (No content) status code without any content.\

If the specified quiz does not exist, the server returns 404 (Not found). If the specified user is not the author of this quiz, the response is the 403 (Forbidden) status code.

Example 7: ```/api/register``` request with the following body:\
```
{
  "email": "test@gmail.com",
  "password": "secret"
}
```
The service returns 200 (OK) status code if the registration has been completed successfully.\
If the email is already taken by another user, the service will return the 400 (Bad request) status code.\
Here are some additional restrictions to the format of user credentials:\
the email must have a valid format (with @ and .);\
the password must have at least five characters.\
If any of them is not satisfied, the service will also return the 400 (Bad request) status code.\
All the following operations need a registered user to be successfully completed.


