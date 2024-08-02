# GitHub User Repositories Inspector


The **GitHub User Repositories Inspector** allows users to list all GitHub repositories belonging to a specified user that are not forks. For each repository, the application provides the following information:

-   **Repository Name**: The name of the repository.
-   **Owner Login**: The GitHub username of the repository owner.
-   **Branches Details**: For each branch, the application lists:
-
    -   Branch Name
    -   Last Commit SHA


This application was developed as part of a recruitment task, showcasing the ability to interact with the GitHub API and handle various response scenarios, including error handling for non-existent users.

## Technologies Used

-   **Java 21**
-   **Spring Boot 3**
-   **GitHub API version 2022-11-28**
-   **Maven**
-   **JUnit 5**

## Getting Started
These instructions will get you a copy of the project up and running on your local machine.

### Instalation
1. Clone the repository:

   ```bash
   git clone https://github.com/BlackRaven18/github-user-repositories-inspector.git
   ```

2. Navigate to the Project Directory:

   ```bash
   cd github-user-repositories-inspector
   ```

3. Run the application

   Open the project in your preferred IDE, such as **IntelliJ IDEA**.

   If you are using IntelliJ IDEA, you can run the application by clicking the green run button or using the shortcut (usually `Shift + F10`).


### Usage
To get a list of user repositories, follow these steps:
1. Send a GET Request:
    - Open your preferred tool for making HTTP requests, such as Postman or curl
    - Construct the request URL by replacing `{username}` with the desired GitHub username:

   ```
   http://localhost:8080/api/repos/{username}
   ```

   **Example**

   ```
   http://localhost:8080/api/repos/blackraven18
   ```

2. Set the Request Headers

   Add the following header to your request:

   ```
   Accept: application/json
   ```

3. Send the Request

   Send the GET request to the constructed URL.


**Expected Response:**

-   If the user exists and has repositories that are not forks, the response will include a JSON object containing:

    -   The repository name
    -   The owner login
    -   Details of each branch, including branch name and last commit SHA.

    **Example**

    ```json
        [
            {
              "name" : "task-manager",
              "owner" : {
                "login" : "BlackRaven18"
              },
              "branches" : [ {
                "name" : "main",
                "commit" : {
                  "sha" : "affd48752295f325523d63f202189dc74a14b712"
                }
              } ]
            }
        ]
    ```

-   If the user does not exist, the application will return a 404 response in the following format:

    ```json
    { 
        "status":  "responseCode", 
        "message":  "whyHasItHappened" 
    }
    ```
    **Example**
    ```json
    { 
        "status":  404, 
        "message":  "Not Found"
    }
    ```

## Tests

To run the tests for the application, follow these steps:

1. Open the Project in Your IDE:
2. Locate the Test Classes:
   Tests are located under following path:

   ```bash
   src/test/java
   ```

3. Run the Tests
-   In **IntelliJ IDEA**:

    -   Right-click on the test class or the test method you want to run.
    -   Select “Run 'TestClassName'” or “Run 'TestMethodName'”.

-   In **Eclipse**:
    -   Right-click on the test class or method.
    -   Choose “Run As” > “JUnit Test”.