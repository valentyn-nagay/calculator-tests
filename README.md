# Java Calculator with Allure Reports

![Java](https://img.shields.io/badge/Java-17-blue)
![Gradle](https://img.shields.io/badge/Gradle-8.10-green)
![Allure](https://img.shields.io/badge/Allure-2.23.0-ff69b4)

Calculator implementation with rich test reporting using Allure.

## Features

- Basic arithmetic operations
- Comprehensive unit tests
- Beautiful Allure test reports
- Parameterized tests
- Fluent assertions with AssertJ

## Setup

1. **Install Allure**:
   ```bash
   # For Mac/Linux
   brew install allure
   
   # For Windows (via Scoop)
   scoop install allure

2. **Run tests and generate report**:
    ```bash
    gradle test
    allure serve build/allure-results