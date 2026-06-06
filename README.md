# LoanWatch 🔍

A backend system that helps microfinance companies find out which borrowers are likely to stop paying their loans — before it's too late.

## Why I Built This

Microfinance companies give small loans to farmers, shop owners, daily workers. The big problem is they have no way to know who is about to default until the money is already lost. I built LoanWatch to solve this — it automatically scores every borrower every night and flags the risky ones early.

## What It Does

- Runs a risk scoring engine every night at 2 AM automatically
- Gives every borrower a risk score from 0 to 100
- Flags HIGH risk borrowers so the company can follow up before default happens
- Different roles see different data — Admin manages users, Manager sees all borrowers, Field Agent sees only their assigned borrowers
- Redis caching so risk scores load fast without hitting the database every time

## Tech Stack

- Java 17 + Spring Boot 3.2
- MySQL — stores borrowers, loans, payments, risk scores
- Redis — caches risk scores for 12 hours
- Spring Security + JWT — login and role based access
- Spring Scheduler — runs risk engine every night automatically
- Swagger UI — to test all APIs
- Deployed on Railway

## Risk Scoring Logic

The risk score is calculated using 5 factors — same logic used in real credit scoring:

- Payment history (35%) — how many payments were missed
- Days overdue (25%) — how long the loan has been overdue
- Missed payment count (20%) — total number of missed payments
- Loan utilization (10%) — loan amount vs monthly income
- Number of active loans (10%) — more loans = more risk

Score above 70 = HIGH risk → send field agent
Score 40-70 = MEDIUM risk → keep an eye
Score below 40 = LOW risk → all good

## API Endpoints

| Method | Endpoint | Who can access |
|--------|----------|----------------|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public |
| GET | /api/manager/borrowers | Manager, Admin |
| POST | /api/manager/borrowers | Manager, Admin |
| GET | /api/manager/borrowers/{id} | Manager, Admin |
| POST | /api/manager/run-risk-engine | Manager, Admin |
| GET | /api/agent/my-borrowers/{agentId} | Field Agent |
| GET | /api/agent/borrowers/{id}/risk | Field Agent |
| GET | /api/admin/users | Admin |
| POST | /api/admin/users | Admin |

## Live Demo

Base URL: https://loanwatch-production.up.railway.app

Swagger UI: https://loanwatch-production.up.railway.app/swagger-ui/index.html

## How to Run Locally

1. Clone the repo
2. Install Java 21, MySQL, Redis
3. Create a database called `loanwatch` in MySQL
4. Update `application.properties` with your MySQL password
5. Run `mvn spring-boot:run`
6. Open http://localhost:8080/swagger-ui/index.html

## What I Would Add Next

- DTOs to separate API response from database models
- Global exception handling with proper error messages
- Input validation on all fields
- ML model using logistic regression trained on Kaggle loan default dataset to replace the rule based scoring
