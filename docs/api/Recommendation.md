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
      "9823": 3,  //the key is the item id, the value is the rating of this item
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

**Route:** `/userBased/recommendations/{userid}` with an optional query `SimilarityMeasure` and `knn`

Example:`/userBased/recommendations/214?SimilarityMeasure=euklid&knn=2`

For the query parameter `SimilarityMeasure` are the following values are valid:
- `euklid`
- `cosine`
- `pearson`

Default is `pearson`.

For the query parameter `knn` only positive numbers are valid, the value also shouldn't exceed the number of users.
Recommended is a value between 2 and 5. Default is 2.