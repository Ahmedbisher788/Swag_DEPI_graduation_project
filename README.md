# Swag_DEPI_graduation_project

## 🧪 Swag Labs UI Test Automation Project

This project is an automated test suite for the [SauceDemo](https://www.saucedemo.com/) web application using **Java**, **Selenium WebDriver**, **JUnit/TestNG**, and the **Page Object Model (POM)** design pattern.

---

### 🚀 Project Objective

To verify the functionality of the SauceDemo e-commerce website by writing reliable, scalable, and maintainable automated UI tests.

---

### 🔧 Technologies Used

- Java
- Selenium WebDriver
- TestNG / JUnit (choose what was used)
- Maven
- Page Object Model (POM)
- Git & GitHub

---

### 📋 Test Case Management (Jira)

All detailed test cases are documented using **AIO Test Management** in **Jira**.

🔗 **Note:** [Click here to view test cases in Jira](https://bisher676.atlassian.net/projects/GPD?selectedItem=com.atlassian.plugins.atlassian-connect-plugin:com.kaanha.jira.tcms__aio-tcms-project-overview) *(Access may be restricted to internal users)*

---

### 📄 Tested Pages and Features

| Page                     | Tested Features                                 |
|--------------------------|-------------------------------------------------|
| **Login Page**           | Valid/Invalid login, empty fields               |
| **Home Page**            | Product listing, sorting options                |
| **Product Details Page** | Viewing product details, back navigation        |
| **Cart Page**            | Adding/removing items, verifying cart total     |
| **Checkout Page**        | Entering shipping info, form validation         |
| **Checkout Overview**    | Price summary, tax calculation, order review    |
| **Sidebar**              | Navigation links (About, Logout, etc.)          |
| **Footer**               | Footer visibility and social links              |
| **E2E Scenarios**        | Full purchase flow from login to order success  |

---

### 📁 Project Structure

<pre>
src
└── test
    └── java
        └── FunctionalityTesting
            ├── CartPageTests.java
            ├── CheckoutPageTests.java
            ├── CheckoutOverviewPageTests.java
            ├── E2ETests.java
            ├── FooterTests.java
            ├── HomePageTests.java
            ├── LoginPageTests.java
            ├── ProductDetailsPageTests.java
            └── SidebarTests.java
</pre>

---
