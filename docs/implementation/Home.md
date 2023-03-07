# Implementation

## Quick Start

Clone this Repository and import the gradle project.
The Main class is located in `/src/man/kotlin/de/matthiasklenz/Application.kt`, execute it and a local web server will
be started under `localhost:8080`. You also need to start a database, in the root directory of the project is a
`docker-compose.yml` file that can start the necessary services. Modify the config file with the resource directory so
the API can connect to them. For help see the [Deploy Doc](Deploy.md).

Use [Postman](https://www.postman.com/) or other Apps to interact with the API. See the [API Docs](../api/Home.md) on
what you can do.

## Technologies

### Ktor

### SQL
