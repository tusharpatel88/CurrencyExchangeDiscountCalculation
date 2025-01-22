CurrencyExchangeDiscountCalculation - Spring Boot Application
Overview

The CurrencyExchangeDiscountCalculation is a Spring Boot application designed to integrate with third-party currency exchange APIs and calculate the payable amount of a bill after applying various discounts. The application exposes an API endpoint that allows users to submit a bill in one currency and get the payable amount in another currency, accounting for applicable discounts.
Key Features

    Third-Party API Integration: The application integrates with a currency exchange API (such as ExchangeRate-API or Open Exchange Rates) to retrieve real-time currency exchange rates.
    Discount Calculation: Discounts are applied based on user status (employee, affiliate, customer tenure) and other rules (e.g., per $100 discount).
    Currency Conversion: The bill amount is converted from the original currency to the target currency using the fetched exchange rates.
    Authentication: The application secures the API with user authentication.
    API Endpoint: An exposed endpoint (/api/calculate) that accepts bill details and returns the final payable amount in the target currency.

Requirements
1. Third-Party API Integration:

   Integrates with a currency exchange API (e.g., ExchangeRate-API or Open Exchange Rates).
   API Key is used to authenticate and retrieve exchange rates (replace your-api-key in the endpoint URL).

Example endpoint:

https://open.er-api.com/v6/latest/{base_currency}?apikey=your-api-key

2. Discount and Conversion Logic:

   Apply various discounts based on the following rules:
   Employee: 30% discount.
   Affiliate: 10% discount.
   Customer for over 2 years: 5% discount.
   Every $100 on the bill: $5 discount.
   Discount rules do not apply to groceries.
   Only one percentage discount can apply to a bill.

   Currency Conversion:
   Convert the total amount to the target currency using the retrieved exchange rates.
   Calculate the final payable amount in the target currency after applying discounts.

3. Authentication:

   API endpoints are secured using authentication mechanisms.

4. API Endpoint:

   The main API endpoint for calculation: /api/calculate
   Accepts parameters like:
   Bill details (items, categories, total amount).
   User type (employee, affiliate, customer tenure).
   Original currency and target currency.
   Returns the net payable amount in the target currency.

5. Design and Testing:

   Use object-oriented principles for application design.
   Provide a high-level UML class diagram for the solution.
   Write unit tests with appropriate mocking where applicable.
   Maintain simplicity and adhere to modern software development practices.

6. Documentation:

   This README file provides information on running the code, testing, and generating coverage reports.

Project Structure

src/
├── main/
│   ├── java/
│   │   ├── com/
│   │   │   ├── exchangerate/
│   │   │   │   ├── discountcalculator/
│   │   │   │   │   ├── controllers/
│   │   │   │   │   ├── models/
│   │   │   │   │   ├── services/
│   │   │   │   │   └── utils/
│   │   └── resources/
│   │       └── application.properties
└── test/
├── java/
│   └── com/
│       ├── exchangerate/
│       │   ├── discountcalculator/
│       │   │   ├── controllers/
│       │   │   ├── services/
│       │   │   └── utils/
└── resources/

How to Run
Prerequisites

    Java 21 or higher
    Maven 3.8 or higher
    API key for the currency exchange service (Open Exchange Rates or similar)

Steps to Run the Application

    Clone the repository:

git clone https://github.com/your-username/CurrencyExchangeDiscountCalculation.git

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

{
"items": [
{"name": "Laptop", "category": "electronics", "price": 1000},
{"name": "Apple", "category": "groceries", "price": 5}
],
"userType": "employee",
"customerTenure": 3,
"originalCurrency": "USD",
"targetCurrency": "EUR"
}

Sample Response

{
"netPayableAmount": 850.30,
"currency": "EUR"
}

Unit Tests and Code Coverage
Running Unit Tests

You can run the tests with:

mvn test

Generating Code Coverage Report

Generate a Jacoco code coverage report:

mvn verify

The code coverage report will be available in the target/site/jacoco/index.html file.
Build Scripts and Static Analysis

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

UML Class Diagram

A high-level UML class diagram for the solution is available in the docs/uml-class-diagram.png file.
License

This project is licensed under the MIT License - see the LICENSE file for details.
