**CurrencyExchangeDiscountCalculation - Spring Boot Application**
**Overview**

The CurrencyExchangeDiscountCalculation is a Spring Boot application designed to integrate with third-party currency exchange APIs and calculate the payable amount of a bill after applying various discounts. The application exposes an API endpoint that allows users to submit a bill in one currency and get the payable amount in another currency, accounting for applicable discounts.
**Key Features**

**Third-Party API Integration:**

The application integrates with a currency exchange API (e.g., ExchangeRate-API, Open Exchange Rates) to fetch real-time exchange rates.
Uses an API key for authentication to retrieve exchange rate data for currency conversion.
**Discount Calculation:**

Discounts are applied based on user status and other rules:
Employee: 30% discount.
Affiliate: 10% discount.
Customer with over 2 years of tenure: 5% discount 
5$ discount on 100$ bill. if 105$ they get 5$ if 200$ bill then 10$ discount
Exclusions: Percentage discounts do not apply to groceries.
Limitations: Only one percentage discount can apply to a bill; the $5 discount per $100 applies to all items in the bill.
**Currency Conversion:**

The bill amount is converted from the original currency to the target currency using the real-time exchange rates fetched from the third-party API.
The final payable amount is calculated in the target currency after applying the appropriate discounts.
Authentication:

The application secures the API endpoints using user authentication.
Ensures only authorized users can access the discount and currency calculation features.
API Endpoint:

The application exposes a REST API endpoint at /api/calculate.
This endpoint accepts bill details (items, categories, total amount), user type (employee, affiliate, customer tenure), original currency, and target currency.
It returns the final payable amount in the target currency after all discounts and currency conversion are applied.



**Requirements**
1. Third-Party API Integration:

   Integrates with a currency exchange API (e.g., ExchangeRate-API or Open Exchange Rates).
   API Key is used to authenticate and retrieve exchange rates (replace your-api-key in the endpoint URL).

**Example endpoint:**

https://open.er-api.com/v6/latest/{base_currency}?apikey=your-api-key

**2. Discount and Conversion Logic:**

  - Apply various discounts based on the following rules:
  - Employee: 30% discount.
  - Affiliate: 10% discount.
  - Customer for over 2 years: 5% discount.
  - Every $100 on the bill: $5 discount.If 105$ bill get 5$
  - Percentage Discount rules do not apply to groceries.
  - Only one percentage discount can apply to a bill.
  - Every $100 on the bill: $5 discount will apply to all the bill.

   Currency Conversion:
   - Convert the total amount to the target currency using the retrieved exchange rates.
   - Calculate the final payable amount in the target currency after applying discounts.

**4. Authentication:
**
   API endpoints are secured using authentication mechanisms.

**5. API Endpoint:**

   - The main API endpoint for calculation: /api/calculate
   - Accepts parameters like:
   - Bill details (items, categories, total amount).
   - User type (employee, affiliate, customer tenure).
   - Original currency and target currency.
   - Returns the net payable amount in the target currency.

**6. Design and Testing:**

   - Use object-oriented principles for application design.
   - Provide a high-level UML class diagram for the solution.
   - Write unit tests with appropriate mocking where applicable.
   - Maintain simplicity and adhere to modern software development practices.

**7. Documentation:
**
   This README file provides information on running the code, testing, and generating coverage reports.

   **UML Diagram**
   ![image](https://github.com/user-attachments/assets/e58bfc9e-18f1-471a-ba0d-e4fcf895be22)


Project Structure


![image](https://github.com/user-attachments/assets/a9b0bf6d-6f86-4876-b5f8-c5109928b252)

How to Run
Prerequisites

    - Java 21 or higher
    - Maven 3.8 or higher
     - API key for the currency exchange service (Open Exchange Rates or similar)

Steps to Run the Application

    Clone the repository:

git clone https://github.com/tusharpatel88/CurrencyExchangeDiscountCalculation.git

Navigate to the project directory:

cd CurrencyExchangeDiscountCalculation

Build the project using Maven:

mvn clean install

Run the application:

    mvn spring-boot:run

    Access the API at http://localhost:8080/api/calculate.

Sample Request

POST /api/calculate
Content-Type: application/json
```json
{
  "items": [
    {
      "itemName": "Laptop",
      "category": "Electronics",
      "amount": 1200.0
    },
    {
      "itemName": "Headphones",
      "category": "Electronics",
      "amount": 150.0
    },
    {
      "itemName": "Apple",
      "category": "Groceries",
      "amount": 5.0
    },
    {
      "itemName": "Banana",
      "category": "Groceries",
      "amount": 3.0
    }
  ],
  "userType": "affiliate",
  "customerTenure": 3,  // Number of years the customer has been with the store
  "originalCurrency": "USD",
  "targetCurrency": "EUR"
}
```


Sample Response
```json
{
    "data": 1111.82475,
    "currency": "EUR"
}
```


This project uses Maven for building and testing. The following plugins are configured:

    PMD: For static code analysis.
    JaCoCo: For code coverage reports.
    SonarQube: For generating a quality summary.

To run static analysis and code coverage, use the following Maven commands:

mvn pmd:pmd
mvn jacoco:report
mvn sonar:sonar

Bonus Activities

    Caching for Exchange Rates: The application caches exchange rates using Caffeine to reduce unnecessary API calls.
    SonarQube Report: A SonarQube integration has been added to provide a quality summary.

Technologies Used

    Spring Boot: Framework for building the application.
    Spring Security: To secure the API endpoints.
    Spring Cache: For caching exchange rates.
    JUnit: For unit testing.
    Maven: For build automation and dependency management.
    Caffeine: For caching exchange rates.
    SonarQube: For static code analysis and quality reporting.

Commands to run for various case
**Build the Project**
mvn clean install



**Run the Spring Boot Application**
mvn spring-boot:run

Starts the Spring Boot application.The API will be available at http://localhost:8080/api/calculate.

**Run Unit Tests**
mvn test
Runs all unit tests in the project. Ensures that the functionality of the application works as expected.

**Run Static Code Analysis (PMD)**
mvn pmd:pmd
Runs static code analysis using the PMD plugin. It checks your code for potential issues and generates an HTML report.

**Generate Code Coverage Report (Jacoco)**
mvn jacoco:report
Generates the Jacoco code coverage report. This step should be run after tests to get detailed coverage stats.

**Run SonarQube Analysis**
Run Sonarcube Server First prior to running this
mvn sonar:sonar
Runs the SonarQube scanner and sends the results to a SonarQube server. This helps to analyze and monitor code quality and maintainability.

**Compile the Project (Without Running Tests)**
mvn compile
Compiles the project's source code without running any tests, making it a faster option for just compiling.
Clean the Project

**mvn clean**
Removes the target directory where the compiled artifacts are stored. Ensures a fresh build with no leftover files.
Install the Project into the Local Repository

**mvn install**
Installs the built artifacts (like the JAR file) into the local Maven repository (~/.m2/repository). This is useful when other projects depend on this artifact.
Validate the Project

**mvn clean install -DskipTests**
Builds the project and installs it without running tests. Useful for quick builds when you are confident that tests are already passing.

