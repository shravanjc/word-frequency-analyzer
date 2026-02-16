# Word Frequency Analyzer

This is a lightweight Domain-Driven Design (DDD) Spring Boot application that analyzes the frequency
of words in a given text which contains words separated by non-characters.
Following assumptions are considered:

- **Word**: A word represents a sequence of 1 or more characters (a-z A-Z). For
  example: kjsHKDieh or Insurance;
- **Input Text**: The input text contains words which are separated by different types of
  separation characters. Added assumption that any non-char (not a-z A-Z) is considered a
  separation.
- **Available memory**: There is sufficient memory available to store the entire
  entered text;
- **Capitalization**: For this case, there is no distinction made between uppercase and
  lowercase letters. For example: the sentence "The car is the color purple." will
  indicate that "the" occurs 2 times.

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
