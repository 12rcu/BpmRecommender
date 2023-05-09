# User Routes

## Get User Info

- Route: `/user/{id}`
- Request Type: `GET`

Sample Answer:

````json
{
  "userid": 12,
  "name": "Peter",
  "info": "Admin"
  //optional -> might be missing
}
````

## Create a new User

- Route: `/user`
- Request Type: `PUT`

Sample Request Body:

````json
{
  "name": "Peter",
  "info": "Admin"
  //optional
}
````

## Get all User Ratings

- Route: `/user/ratings`
- Request Type: `GET`

Sample Answer:

````json
[
  {
    "userid": 3,
    "ratings": {
      "1": 2,
      "2": 3,
      "3": 1,
      "5": 2
    }
  },
  {
    "userid": 2,
    "ratings": {}
  }
]
````

Ratings Key: Within the `ratings` key are all items listed with the rating of the user. If the user hasn't rated the
item it will not show up in the map. The key is the item id and the value the rating.

## Add Ratings

- Route: `/user/rating`
- Request Type: `POST`

Sample Request Body:

````json
{
  "userid": 3,
  "ratings": {
    "1": 2,
    "2": 3,
    "3": 1,
    "5": 2
  }
}
````

If a rating already exist it will be overwritten, so this route can also be used to updated ratings!