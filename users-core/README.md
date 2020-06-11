# users-core

This module covers user account storage, provide user login, register and 
retrieval for user, cards and addresses.

[domain model](./src/main/java/io/helidon/examples/sockshop/users/User.java), 
[REST API](./src/main/java/io/helidon/examples/sockshop/users/UserResource.java), as well as the
[data repository abstraction](./src/main/java/io/helidon/examples/sockshop/users/UserRepository.java) 
and its [in-memory implementation](./src/main/java/io/helidon/examples/sockshop/users/DefaultUserRepository.java).

## Building the Service

See [main documentation page](../README.md#building-the-service) for instructions.

## Running the Service

Because this implementation uses in-memory data store, it is trivial to run.

Once you've built the Docker image per instructions above, you can simply run it by executing:

```bash
$ docker run -p 7001:7001 helidon/sockshop/users-core
``` 

Once the container is up and running, you should be able to access [service API](../README.md#api) 
by navigating to http://localhost:7001/.

As a basic test, you should be able to perform an HTTP GET against `/cards` endpoint:

```bash
$ curl http://localhost:7001/cards
``` 
which should return 200 response code.

## License

The Universal Permissive License (UPL), Version 1.0
