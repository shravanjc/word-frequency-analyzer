# Word Frequency Analyzer

This is a lightweight Domain-Driven Design (DDD) Spring Boot application that analyzes the frequency
of words in a given text.

## Running the application

To run the application, you can use the following command:

```bash
./gradlew bootRun
```

The application will be available at `http://localhost:8080`.

## Running the tests

To run the tests, you can use the following command:

```bash
./gradlew test
```

## Project Structure

The project is structured as follows:

- `src/main/java`: Contains the main source code of the application.
    - `api`: Contains the REST controllers.
    - `application`: Contains the application services.
    - `domain`: Contains the domain objects.
- `src/test/java`: Contains the test source code.
