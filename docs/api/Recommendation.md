# Recommendation Routes

## User based Filtering

### Query Similarities

To request the similarities of a user to other users.

**Route:** `/userBased/similarities/{userid}` with an optional query `SimilarityMeasure`

Example:`/userBased/similarities/214?SimilarityMeasure=euklid`

For the query parameter `SimilarityMeasure` are the following values are valid:

- `euklid`
- `cosine`
- `pearson`

Default is `pearson`.

**Answer Sample:**

````json
[
  {
    "userid": 1,
    "ratings": {
      "9823": 3,
      //the key is the item id, the value is the rating of this item
      "212": 1,
      "90823": 2
    },
    "similarity": 0.2
  },
  {
    "userid": 2,
    "ratings": {},
    "similarity": 0.2
  }
]
````

### Query Recommendations

**Route:** `/userBased/recommendations/{userid}` with an optional query `SimilarityMeasure`, `knn` and `weightedMean`

Example:`/userBased/recommendations/214?SimilarityMeasure=euklid&knn=3&weightedMean=false`

For the query parameter `SimilarityMeasure` are the following values are valid:

- `euklid`
- `cosine`
- `pearson`

Default is `pearson`.

For the query parameter `knn` only positive numbers are valid, the value also shouldn't exceed the number of users.
Recommended is a value between 2 and 5. Default is 2.

For the query parameter `weightedMean` only a bool is valid, default is true.

**Answer Sample:**

````json
{
  "9823": 3.2,
  "123": 2.1,
  "122": 4.2,
  "192": 2.2
}
````

The keys are the ids of all items that are not rated by the user, while the values are the estimated rating for the
user.