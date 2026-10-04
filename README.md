# AI-Powered Study Resource Generator

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/spring%20boot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/postgresql-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

A highly scalable, production-ready backend system that leverages Generative AI to dynamically generate study notes, MCQ tests, and daily practice papers. Designed with performance and cost-optimization in mind, this system uses advanced Retrieval-Augmented Generation (RAG) concepts and semantic caching to ensure it never processes the same query twice.

---

## Table of Contents

- [Features](#features)
- [Architecture & Design](#architecture--design)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [API Reference](#api-reference)
- [Author](#author)

---

## Features

- **🧠 Dynamic Notes Generation**: On-demand, detailed study materials powered by state-of-the-art Gen AI models (Qwen / LLaMA).
- **📝 Automated Assessments**: Generates 10-question MCQs with answers. Supports Daily Practice Papers (DPPs) for spaced repetition and streak building.
- **⚡ Semantic Caching**: Uses vector similarity search to retrieve previously generated answers, bypassing the LLM layer for duplicate or highly similar queries, saving time and API costs.
- **📅 Asynchronous Task Scheduling**: Users can schedule tests and DPPs to arrive in their inbox at specific times via email integrations.
- **🔍 Semantic Search Engine**: Ask natural language questions and retrieve the most relevant notes from the global database, ranked by semantic meaning.
- **🔒 Secure Authentication**: Robust session management using GitHub OAuth2 integration.

---

## Architecture & Design

### Key System Design Principles

This project was built with enterprise-level system design patterns to handle high traffic and provide a seamless user experience:

- **Scalability**: The stateless Spring Boot backend can be replicated across multiple instances behind a load balancer, while PostgreSQL with pgvector handles both relational data and vector scaling effortlessly.
- **High Availability**: Decoupling the heavy AI generation tasks from core retrieval logic ensures high uptime. Cached semantic hits are served instantly even if the LLM provider experiences latency.
- **Data Consistency**: Relies on strict ACID properties of PostgreSQL for user data and scheduling logic, maintaining seamless consistency with the pgvector extension.
- **Cost-Optimization**: Instead of hitting the LLM for every request, the system embeds user queries and checks the vector database for a semantic match (cosine similarity ≥ 0.85).

### System Workflow

```text
                        ┌─────────────────────────┐
                        │      Spring Boot API    │
                        │  (Java 17, Spring AI)   │
                        └────────────┬────────────┘
                                     │
               ┌─────────────────────┼─────────────────────┐
               │                                           │
               ▼                                           ▼
    ┌──────────────────┐                  ┌────────────────────────────────┐
    │   Hugging Face   │                  │           PostgreSQL           │
    │  (Gen AI Layer)  │                  │          (Primary DB)          │
    │                  │                  │                                │
    │  Qwen / LLaMA    │                  │  Notes, Topics, Questions      │
    │  (generation)    │                  │                                │
    │                  │                  │       [ pgvector ext ]         │
    │  all-MiniLM-L6-v2│                  │  Vector index (semantic search)│
    │  (embeddings)    │                  │  384-dim cosine distance       │
    └──────────────────┘                  └────────────────────────────────┘
```

### RAG & Semantic Caching Flow

```text
User Request
     │
     ▼
[ PostgreSQL ] Exact match? ──── YES ──► Return it (O(1) fast path)
     │
     NO
     │
     ▼
[ Hugging Face (all-MiniLM-L6-v2) ] Embed the query into a 384-dimensional vector
     │
     ▼
[ pgvector ] Query Vector DB (Cosine Similarity)
     │
     ├── Score ≥ 0.75 ──► Cache Hit: Return existing note from DB
     │
     └── No match ────► Cache Miss: Generate via Hugging Face LLM
                            │
                            ├── Save payload to PostgreSQL
                            └── Index vector in pgvector
```

---

## Tech Stack

- **Backend Framework**: Spring Boot 3.5, Java 17, Spring Data JPA, Spring Security, Spring Scheduling
- **Generative AI Integration**: Spring AI 1.0, Hugging Face API
- **Models Used**: Qwen / LLaMA (Text Generation), all-MiniLM-L6-v2 (Embeddings)
- **Databases**: PostgreSQL (Relational) with `pgvector` extension for Semantic Search
- **Authentication**: OAuth 2.0 (GitHub)
- **Infrastructure / DevOps**: Docker, Docker Compose, Maven

---

## Getting Started

### Prerequisites
- Docker and Docker Compose
- Java 17+ (If running without Docker for the backend)
- Maven (If building locally)

### 1. Environment Configuration

Create a `.env` file at the root directory of the project:

```env
# Gen AI (Hugging Face)
HF_MODEL=Qwen/Qwen2.5-72B-Instruct
HF_TOKEN=your_hugging_face_token_here

# Primary Database
DB_URL=jdbc:postgresql://db:5432/studydb
DB_USERNAME=postgres
DB_PASSWORD=postgres

# SMTP Mail Server
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# OAuth2 Authentication
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret

# Security
ALLOWED_ORIGINS=http://localhost:3000
```

### 2. Run the Application

The easiest way to start the entire system (including PostgreSQL with pgvector and the Spring Boot API) is via Docker Compose:

```bash
git clone https://github.com/TornovDutta/AI-Powered-Study-Resource-Generator.git
cd AI-Powered-Study-Resource-Generator

# Package the application (skip tests for faster build)
./mvnw clean package -DskipTests

# Run the complete stack
docker-compose up --build -d
```

### 3. Alternative: Run Locally via IDE

If you prefer to run the Spring Boot application locally while keeping the database in Docker:

1. Update `DB_URL` in your `.env` to `jdbc:postgresql://localhost:5432/studydb`
2. Start the database container:
   ```bash
   docker-compose up db -d
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

**Accessing the Application:**
- API Base URL: `http://localhost:8080/api/v1`
- Swagger UI Documentation: `http://localhost:8080/api/v1/swagger-ui.html`

---

## API Reference

*Note: All endpoints are prefixed with `/api/v1` and require GitHub OAuth2 authentication (except health check endpoints).*

### Notes API

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/note?topic={topic}` | Fetch or generate study notes for a specific topic (Semantic Cache first). |
| `GET`  | `/note/search?query={query}` | Perform a semantic search across all previously generated notes. |

### Tests & DPPs API

| Method | Endpoint | Request Body | Description |
|--------|----------|--------------|-------------|
| `POST` | `/tests/` | `{ "topic": "..." }` | Generate an MCQ test immediately. |
| `POST` | `/tests/schedule` | `{ "topic": "...", "date": "...", "time": "..." }` | Schedule a one-time test via email. |
| `GET`  | `/dpp?topic={topic}` | — | Generate a Daily Practice Paper (DPP) immediately. |
| `POST` | `/dpp/schedule` | `{ "topic": "...", "time": "HH:MM" }` | Schedule a recurring daily DPP. |
| `DELETE`| `/tests/schedule` | — | Cancel a scheduled test or DPP. |

---

## Author

Built by [Tornov Dutta](https://github.com/TornovDutta) - Passionate about building scalable Backend Systems and Generative AI applications.
