# BpmRecommender

(Entwicklung eines Recommender Systems für die Prozessevaluation/-modellierung)

A Recommender System for Business Process Modeling Notations. Matches Notations characteristics against user preferences
or matches user against each other to make a recommendation on what Notation a user should use or what the user might
want to try.

## Recommender Structure

Items is the term for the BPN Notations. Users don't have to be actual users but can instead be profiles an actual user
can be matched against.

Tables:

| User Item Assessment | Item A | Item B | Item C | Item D |
|----------------------|--------|--------|--------|--------|
| User A               | 1      | 5      | -      | 2      |
| User B               | -      | 2      | 1      | 4      |
| User C               | -      | -      | 3      | -      |

| Item Characteristics | A   | B   |
|----------------------|-----|-----|
| Item A               | 1   | 0   |
| Item B               | 0   | 1   |

## API

Build as a REST API with JSON as Content Negotiation.
More infos on how to use the API, go to [API Docs](./docs/api/Home.md).

## Implementation

For contributions, or forks visit [Impl Docs](./docs/api/Home.md)

## Deploy your own Recommender API

Download the latest build from the [Releases](https://github.com/12rcu/BpmRecommender/releases)?
Or use the docker image that I will provide?

=> TODO