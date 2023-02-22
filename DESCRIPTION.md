# Woven Challenge

## About the app

The app is built with Scala 3 with Cats Effect.

Files metadata such as ids, size, content types are stored in a PostgreSQL table. Table migrations are run automatically when the app starts.

The files are stored on the app container’s local file system.

## Running the app

### With Docker Compose

To spin up and/or stop the app with the database, use the following Docker Compose commands:

```shell
$ docker-compose up -d
```

```shell
$ docker-compose down
```

### Natively

To run the app natively, use the following command from your shell:

```
$ sbt run
```

Or, from the SBT shell:

```
run
```

For interactive development with SBT, consider:

```
reStart
```

### Testing

To run unit tests, use the following shell command:

```shell
$ sbt test
```

Or the following SBT command:

```
test
```

### Future work

Things I’d love to improve on:
- Increase test coverage and write proper integration tests.
- Improve file storage by using S3 or other remote service.
- Reduce Docker image size!
