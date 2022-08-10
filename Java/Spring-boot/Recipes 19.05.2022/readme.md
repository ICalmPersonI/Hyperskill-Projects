## Endpoints

```POST /api/recipe/new``` receives a recipe as a JSON object and returns a JSON object with one id field;\
```GET /api/recipe/{id}``` returns a recipe with a specified id as a JSON object (where {id} is the id of a recipe). If a recipe with a specified id does not exist, the server respond with 404 (Not found);\
```DELETE /api/recipe/{id}``` endpoint. It deletes a recipe with a specified {id}. The server respond with the 204 (No Content) status code. If a recipe with a specified id does not exist, the server return 404 (Not found);\
```PUT /api/recipe/{id}``` receives a recipe as a JSON object and updates a recipe with a specified id;\
```GET /api/recipe/search``` takes one of the two mutually exclusive query parameters:
category – if this parameter is specified, it returns a JSON array of all recipes of the specified category.
name – if this parameter is specified, it returns a JSON array of all recipes with the names that contain the specified parameter;\
```POST /api/register``` receives a JSON object with two fields: email (string), and password (string). If a user with a specified email does not exist, the program saves (registers) the user in a database and responds with 200 (Ok). If a user is already in the database, server respond with the 400 (Bad Request) status code. Both fields are required.

Only an author of a recipe can delete or update a recipe. If a user is not the author of a recipe, but they try to carry out the actions mentioned above, the service respond with the 403 (Forbidden) status code.

## Examples
Example 1: POST /api/recipe/new request with the following body:
```
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```
Response:
```
{
   "id": 1
}
```
Example 2: GET /api/recipe/1 request

Response:
```
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2021-04-06T14:10:54.009725",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```
Example 3: GET /api/recipe/999 request
```
Status code: 404 (Not found)
```
Example 4: DELETE /api/recipe/1 request
```
Status code: 204 (No Content)
```

DELETE /api/recipe/1 request
```
Status code: 404 (Not found)
```

Example 5: PUT /api/recipe/1 request
```
{
   "name": "Warming Ginger Tea",
   "category": "beverage",
   "description": "Ginger tea is a warming drink for cool weather, ...",
   "ingredients": ["1 inch ginger root, minced", "1/2 lemon, juiced", "1/2 teaspoon manuka honey"],
   "directions": ["Place all ingredients in a mug and fill with warm water (not too hot so you keep the beneficial honey compounds in tact)", "Steep for 5-10 minutes", "Drink and enjoy"]
}
```
Further response for the GET /api/recipe/1 request:
```
{
   "name": "Warming Ginger Tea",
   "category": "beverage",
   "date": "2021-04-06T14:10:54.009725",
   "description": "Ginger tea is a warming drink for cool weather, ...",
   "ingredients": ["1 inch ginger root, minced", "1/2 lemon, juiced", "1/2 teaspoon manuka honey"],
   "directions": ["Place all ingredients in a mug and fill with warm water (not too hot so you keep the beneficial honey compounds in tact)", "Steep for 5-10 minutes", "Drink and enjoy"]
}
```
Example 6: A database with several recipes
```
{
   "name": "Iced Tea Without Sugar",
   "category": "beverage",
   "date": "2019-07-06T17:12:32.546987",
   ....
},
{
   "name": "vegan avocado ice cream",
   "category": "DESSERT",
   "date": "2020-01-06T13:10:53.011342",
   ....
},
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2021-09-06T14:11:51.006787",
   ....
},
{
   "name": "Vegan Chocolate Ice Cream",
   "category": "dessert",
   "date": "2021-04-06T14:10:54.009345",
   ....
},
{
   "name": "warming ginger tea",
   "category": "beverage",
   "date": "2020-08-06T14:11:42.456321",
   ....
}
```
Response for the GET /api/recipe/search/?category=dessert request:
```
[
   {
      "name": "Vegan Chocolate Ice Cream",
      "category": "dessert",
      "date": "2021-04-06T14:10:54.009345",
      ....
   },
   {
      "name": "vegan avocado ice cream",
      "category": "DESSERT",
      "date": "2020-01-06T13:10:53.011342",
      ....
   },
]
```
Response for the GET /api/recipe/search/?name=tea request:
```
[
   {
      "name": "Fresh Mint Tea",
      "category": "beverage",
      "date": "2021-09-06T14:11:51.006787",
      ....
   },
   {
      "name": "warming ginger tea",
      "category": "beverage",
      "date": "2020-08-06T14:11:42.456321",
      ....
   },
   {
      "name": "Iced Tea Without Sugar",
      "category": "beverage",
      "date": "2019-07-06T17:12:32.546987",
      ....
   },
]
```
Example 7: POST /api/register request without authentication
```
{
   "email": "Cook_Programmer@somewhere.com",
   "password": "RecipeInBinary"
}
```
Status code: 200 (Ok)
