# BpmRecommender

[![GitHub license](https://badgen.net/github/license/12rcu/BpmRecommender)](https://github.com/12rcu/BpmRecommender/blob/master/LICENSE)
[![Build](https://github.com/12rcu/BpmRecommender/actions/workflows/build.yml/badge.svg)](https://github.com/12rcu/BpmRecommender/actions/workflows/build.yml)

(Entwicklung eines Recommender Systems für die Prozessevaluation/-modellierung)

A Recommender System for Business Process Modeling Notations. Matches Notations characteristics against user preferences
or matches user against each other to make a recommendation on what Notation a user should use or what the user might
want to try.

## Recommender Structure

Items is the term for the BPM Notations. Users don't have to be actual users but can instead be profiles an actual user
can be matched against.

Tables:

| User Item Assessment | BPMN 2.0 | Declarative | eGantt | EPC | ... |
|----------------------|----------|-------------|--------|-----|-----|
| User A               | 1        | 5           | -      | 2   | ... |
| User B               | -        | 2           | 1      | 4   | ... |
| User C               | -        | -           | 3      | -   | ... |
| ...                  | ...      | ...         | ...    | ... | ... |

| Item Characteristics | A   | B   | ... |
|----------------------|-----|-----|-----|
| BPMN 2.0             | 1   | 0   | ... |
| Declarative          | 0   | 1   | ... |
| Declarative          | 0   | 1   | ... |
| ...                  | ... | ... | ... |

To maintain Scalability, Columns will probably need separate Tables. This will be solved with the
issue [#8](https://github.com/12rcu/BpmRecommender/issues/8).

## API

Build as a REST API with JSON as Content Negotiation.
More infos on how to use the API, go to [API Docs](./docs/api/Home.md).

## Implementation

For contributions, or forks visit [Impl Docs](./docs/api/Home.md)

## Deploy your own Recommender API

Visit https://hub.docker.com/r/12build/bpmn_recommender/tags to see the latest docker image builds to use.
Map a config.yaml volume and start the api. If you want to add a frontend to the deployed api, you have to map/mount a
frontend directory with the frontend files to `/run/dist/`.