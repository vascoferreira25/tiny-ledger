# Tiny Ledger REST API

## Requirements

Implement a set of apis to power a simple ledger.

### Functional Requirements

- Ability to record money movements (ie: deposits and withdrawals)
- View current balance
- View transaction history

### Technical Requirements

- Functional web application (no UI, just the apis) that can be run locally.
- You can use any programming language and framework of your choice.
- For the sake of simplicity, use in-memory data structures to store the data
(for example: a map or an array), and it should not be necessary to install any
optional software to run it (besides any libraries that you choose to use).

You are free to make assumptions whenever you feel it is necessary, but please 
document them.

Please try to keep it simple. The objective is to understand your approach to 
problems and your thought process rather than a test of your technical knowledge, 
even if it means having to make trade-offs.

This means that you are not expected to deliver any of the below:

- authentication/authorisation
- logging / monitoring
- transactions/atomic operations
- ...

(Feel free to cut down other parts as much as you need to fit the solution into the
time you have available)
The solution should be submitted as a link to a public git repository (GitHub, GitLab,
Bitbucket, etc.) with a README file containing instructions on how to run the
application as well as a few examples of how to execute the implemented features.

## Assumptions

- Authentication and authorisation won't be implemented
- It will exist some logged information for debugging purposes but monitoring won't be
implemented
- Transactions/atomic operations won't be implemented, therefore concurrent operations
to the same account might cause issues when updating the balance.
- As there will be multiple transactions, for the sake of coherence, there will also be
multiple accounts. This way, the implementation of both repositories (Account and 
Transaction) will be very similar, thus making the code more easily maintainable.
- As the functional requirements about transactions don't specify how many types of 
transactions should exist (e.g. between multiple accounts), only two types will be
implemented: Deposits and Withdrawals.
- The transaction history will only show transactions (deposits and withdrawals) of one
account.
- The balance will be an attribute of an account and updated with every transaction.
- As it was not specified, results won't be paginated
- As specified in the technical requirements, a REST API will be provided and the endpoints
will be described here - [Endpoints](#Endpoints). To use the endpoints use an HTTP client
of your choice - e.g. Bruno or HTTPie for open source options or Postman. 
  - If you use VS Code REST Client extension or IntelliJ Ultimate you can use the 
`.http` files in the `/docs/http` directory to execute requests.
- This project will use Java and Spring Boot.
- As specified in the technical requirements, a Map will be used to store data.
- As it was not specified, balance cannot be negative and when creating an account
the balance must be non-negative.

## Running the application

Use the following command to build the application:

```bash
./mvnw package -DskipTests
```

**Note**: This command will skip test to make it faster to build the jar file, if you
wish to check the tests remove the `-DskipTests` argument.

Then, to run the application, change the current working directory to the target directory
and run the Java jar file:

```bash
cd ./target
java -jar tinyledger-0.0.1-SNAPSHOT.jar
```

### Endpoints

The application has the following endpoints:

- 