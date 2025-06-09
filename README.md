# Corporate-Banking-Loan-Origination-System
# Alliances Fintech - Loan Management System (CBLOS)

## Project Description

The Alliances Fintech Loan Management System (CBLOS - Corporate Business Loan Origination System) is a comprehensive web-based application designed to streamline and automate the entire lifecycle of corporate business loans, from application submission to final disbursement. This system aims to enhance efficiency, reduce manual errors, and provide a clear, trackable process for both loan applicants and internal bank administrators.

### Core Functionality Breakdown:

* **Loan Origination:** Allows potential borrowers to apply for various types of corporate business loans online, providing all necessary financial and business details.
* **Document Management:** Facilitates the upload of crucial supporting documents by applicants, which are then managed and validated by dedicated document administrators.
* **Credit Evaluation Integration:** Incorporates a mechanism for credit evaluation, providing insights into the applicant's creditworthiness and risk profile.
* **Approval Workflow:** Implements a structured approval process where loan applications progress through different stages (e.g., applied, documents pending, documents approved, loan approved/rejected) involving multiple administrative roles.
* **Disbursement Management:** Enables the scheduling and tracking of loan disbursements for approved applications.
* **Role-Based Access Control:** Ensures that different users within the system (e.g., Loan Applicant, Document Administrator, Super Administrator) have access only to functionalities relevant to their roles, maintaining security and operational integrity.
* **Reporting:** Provides various reports, such as a comprehensive disbursement report, to aid in monitoring and auditing.

This system centralizes loan data, improves collaboration among departments, and offers transparency to applicants regarding their application status, thereby enhancing the overall loan processing experience.

## Features

* **Secure User Authentication & Authorization:** Implemented using Spring Security, ensuring robust role-based access control for different user types (User/Applicant, DOC_ADMIN, ADMIN).
* **Intuitive User Interface:** Built with Thymeleaf and Bootstrap 5, providing a responsive and user-friendly experience across devices.
* **Loan Application Submission:** Users can easily apply for loans by filling out forms and providing required details.
* **Document Upload & Management:** Secure upload and storage of diverse document types. Admins can view and download uploaded files for review.
* **Document Validation Workflow:** Dedicated interface for `DOC_ADMIN` to approve or reject individual supporting documents.
* **Credit Evaluation Display:** Integrates and displays credit score and risk level for loan applications, aiding in decision-making.
* **Loan Application Status Tracking:** Real-time updates on the status of loan applications for both applicants and administrators.
* **Admin Dashboard:** A centralized control panel for `ADMIN` users to oversee all loan applications, manage approvals/rejections, and view key metrics like total and pending applications.
* **Loan Approval/Rejection:** `ADMIN` users can approve or reject loan applications, triggering appropriate status changes and notifications.
* **Disbursement Scheduling:** `ADMIN` can schedule and track loan disbursements.
* **Disbursement Reporting:** Generate reports on scheduled and completed disbursements.
* **Database Persistence:** Data is securely stored and managed using Spring Data JPA with a MySQL database.

## Technologies Used

* **Backend:**
    * **Java:** JDK 17 (or your specific version, e.g., 21)
    * **Spring Boot:** 3.x (Framework for rapid application development)
    * **Spring Data JPA:** For seamless database interaction and ORM (Object-Relational Mapping)
    * **Hibernate:** JPA implementation
    * **Spring Security:** For authentication and authorization
    * **Lombok:** Reduces boilerplate code (optional, but highly recommended)
* **Frontend:**
    * **Thymeleaf:** Server-side Java template engine for rendering dynamic HTML.
    * **HTML5, CSS3:** Standard web technologies.
    * **Bootstrap 5.x:** Frontend framework for responsive and modern UI components.
    * **Bootstrap Icons:** Icon library for visual enhancements.
* **Database:**
    * **MySQL Server:** Version 8.0 or higher (Relational database management system).
* **Build Tool:**
    * **Maven:** (or Gradle, depending on your project setup) for dependency management and build automation.
* **Version Control:**
    * **Git:** Distributed version control system.
    * **GitHub:** Cloud-based Git repository hosting service.

## Prerequisites

Before running this project, ensure you have the following software installed on your development machine:

* **Java Development Kit (JDK):** Version 17 or higher. You can download it from [Oracle](https://www.oracle.com/java/technologies/downloads/) or use OpenJDK.
* **Maven:** Version 3.6.0 or higher. Download from [Apache Maven](https://maven.apache.org/download.cgi).
* **Git:** Latest version. Download from [Git-SCM](https://git-scm.com/downloads).
* **MySQL Server:** Version 8.0 or higher. Download from [MySQL Community Downloads](https://dev.mysql.com/downloads/mysql/).
    * (Optional but Recommended): A MySQL client like **MySQL Workbench** or a universal database tool like **DBeaver** for easier database management.
* **Integrated Development Environment (IDE):**
    * **IntelliJ IDEA** (Community or Ultimate Edition) - Highly recommended for Spring Boot development.
    * **Eclipse IDE for Enterprise Java and Web Developers.**
    * **VS Code** with relevant Java extensions.

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### 1. Clone the Repository

Open your terminal or command prompt and navigate to the directory where you want to store  project.

```bash
git clone https://github.com/pinak2161/Corporate-Banking-Loan-Origination-System

