# LoanWatch 🔍

A loan risk monitoring system that helps microfinance companies identify which borrowers are likely to default — before it's too late. Built with Spring Boot + a rule-based risk engine that runs automatically every night.

---

## Why I Built This

Microfinance companies give small loans to farmers, shop owners, and daily workers. The big problem is they have no way to know who is about to default until the money is already lost. They manage everything in Excel sheets and only find out someone has defaulted after missing 3-4 payments.

I built LoanWatch to solve this — it automatically scores every borrower every night and flags the risky ones early, so a field agent can be sent before the loan goes bad.

---

## Live Demo

> **Frontend:** loanwatch-production.up.railway.app
> **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

**Demo credentials (no registration needed):**
```
Email:    demo@loanwatch.com
Password: demo123
Role:     Manager (full access)
```

---

## What It Does

- Runs a **risk scoring engine every night at 2 AM** automatically using Spring Scheduler
- Gives every borrower a **risk score from 0 to 100**
- Flags **HIGH risk** borrowers so the company can follow up before default happens
- **Role-based access** — Admin manages users, Manager sees all borrowers and scores, Field Agent sees only their assigned borrowers
- **Redis caching** so risk scores load fast without hitting the database every time
- **Frontend UI** — Login, Dashboard with risk overview, Borrower detail page with loan history
- Loaded with **100 real borrowers** from a Kaggle loan default dataset

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21 + Spring Boot 3.2 |
| Database | MySQL |
| Cache | Redis |
| Auth | Spring Security + JWT |
| Scheduler | Spring Scheduler |
| API Docs | Swagger UI |
| Frontend | HTML, CSS, Vanilla JS (served by Spring Boot) |

---

## Risk Scoring Logic

The risk score is calculated using 5 factors — same logic used in real credit scoring:

| Factor | Weight | What it checks |
|---|---|---|
| Payment history | 35% | How many payments were missed |
| Days overdue | 25% | How long the loan has been overdue |
| Missed payment count | 20% | Total number of missed payments |
| Loan utilization | 10% | Loan amount vs monthly income |
| Number of active loans | 10% | More loans = more risk |

```
Score above 70  → HIGH RISK   🔴  send field agent immediately
Score 40 to 70  → MEDIUM RISK 🟡  keep monitoring
Score below 40  → LOW RISK    🟢  all good
```

---

## API Endpoints

| Method | Endpoint | Who can access |
|---|---|---|
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |
| GET | `/api/manager/borrowers` | Manager, Admin |
| POST | `/api/manager/borrowers` | Manager, Admin |
| GET | `/api/manager/borrowers/{id}` | Manager, Admin |
| PUT | `/api/manager/borrowers/{id}/assign-agent` | Manager, Admin |
| POST | `/api/manager/run-risk-engine` | Manager, Admin |
| GET | `/api/manager/risk-scores` | Manager, Admin |
| GET | `/api/manager/loans/borrower/{id}` | Manager, Admin |
| GET | `/api/agent/borrowers/{id}/risk` | Field Agent, Manager, Admin |
| GET | `/api/admin/users` | Admin |
| DELETE | `/api/admin/users/{id}` | Admin |

---

## How to Run Locally

**Requirements:** Java 21, MySQL, Redis

```bash
# 1. Clone the repo
git clone https://github.com/irajvaibhav/LoanWatch.git
cd LoanWatch

# 2. Create MySQL database
CREATE DATABASE loanwatch;

# 3. Update src/main/resources/application.properties
spring.datasource.password=your_mysql_password

# 4. Start Redis (Windows)
redis-server

# 5. Run the app
mvn spring-boot:run

# 6. Open in browser
http://localhost:8080
```

On first startup, the app:
- Creates a demo account automatically (`demo@loanwatch.com` / `demo123`)
- Loads 100 borrowers from the Kaggle loan default dataset

---

## Project Structure

```
src/main/java/com/loanwatch/
├── controller/     # REST API endpoints
├── service/        # Business logic + risk engine
├── model/          # Borrower, Loan, Payment, RiskScore, User
├── repository/     # JPA repositories
├── security/       # JWT filter + Spring Security config
├── config/         # DataLoader, scheduler
└── LoanwatchApplication.java

src/main/resources/
├── static/         # Frontend (index.html, dashboard.html, borrower.html)
├── application.properties
└── Loan_Default.csv   # Kaggle dataset
```

---

## What I Would Add Next

- **DTOs** to separate API response from database models
- **Global exception handling** with proper error messages
- **Input validation** on all fields using `@Valid`
- **ML model** using logistic regression trained on the Kaggle dataset to replace the rule-based scoring
- **Deploy to Railway or Render** for a live public URL

---

## Dataset

Used the [Loan Default Dataset](https://www.kaggle.com/datasets/nikhil1e9/loan-default) from Kaggle — 255,000 real loan records. The app loads the first 100 as seed data.
