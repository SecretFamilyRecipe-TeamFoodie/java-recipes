# java-recipes

Introduction
Java Backend for Secret Family Recipes 

Apis
Users
POST /login

Requires username, password, and headers.

Returns an OAuth2 authorization token. This token is required for all endpoints except /login and /createnewuser.

GET /logout

Logs out the user. The user's existing token will be invalid and should be deleted from the frontend. Nothing is returned from this endpoint.

POST /createnewuser

Requires:

{
    "username": "new username",
    "password": "new user's password",
    "primaryemail": "primary email address"
}
Creates a new user with the specified information and the role "user". Authorizes the new user and returns their authorization token.

Recipes
GET /recipes/all

Returns all recipes where the logged-in user is the owner or guest.

GET /recipes/{id}

Returns the recipe with id {id}. This does not require the user to be the owner or guest.

POST /recipes/new

Creates a new recipe.

Requires:

{
    "title": "new recipe title",
    "source": "recipe source",
    "instructions": "instructions",
    "category": "category",
    "ingredients":
    [
        "ingredient 1"
        "ingredient 2"
    ],
    "guests":
    [
        "guest name 1"
        "guest name 2"
    ]
}
Guests (if provided) must be existing users. Ingredients do not have to be existing ingredients.

Returns the recipe that was just created. Owner will automatically be set to the logged-in user.

PUT /recipes/{id}

Updates the recipe with id {id}.

Requires all of:

{
    "title": "new recipe title",
    "source": "recipe source",
    "instructions": "instructions",
    "category": "category",
    "ingredients":
    [
        "ingredient 1"
        "ingredient 2"
    ],
    "guests":
    [
        "guest name 1"
        "guest name 2"
    ]
}
The recipe must belong to the currently logged-in user.

Returns the updated recipe.

PATCH /recipes/{id}

Updates the recipe with id {id}.

Requires any of:

{
    "title": "new recipe title",
    "source": "recipe source",
    "instructions": "instructions",
    "category": "category",
    "ingredients":
    [
        "ingredient 1"
        "ingredient 2"
    ],
    "guests":
    [
        "guest name 1"
        "guest name 2"
    ]
}
The recipe must belong to the currently logged-in user.

Returns the updated recipe.

DELETE /recipes/{id}

Deletes the recipe with id {id}.

Returns nothing.

Database models
User

userid (long)
username (String, unique, non-null)
password (String, encrypted)
primaryemail (String)
Role

roleid (long)
name (String)
Userrole

userid (long)
roleid (long)
Useremail

useremailid (long)
userid (long)
useremail (String)
Userrecipe

userid (long)
recipeid (long)
Recipe

recipeid (long, unique)
owner (long (userid))
title (String, unique, non-null)
source (String)
instructions (String)
category (String)
Ingredient

ingredientid (long)
name (String, non-null)
Recipeingredient

recipeid (long)
ingredientid (long)
Userrecipe

userid (long)
recipeid (long)
Relationships
Many users : many roles
Many emails : one user
One user : many recipes (as owner)
Many users : many recipes (as guests)
Many recipes : many ingredients
