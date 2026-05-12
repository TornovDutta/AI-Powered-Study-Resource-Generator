# AI-Powered Study Resource Generator

A backend that generates study notes, MCQ tests, and daily practice papers on demand — then remembers everything it creates so it never does the same work twice.

---

## The core idea

Most AI study tools hit the LLM every time you ask a question. This one doesn't.

Every note and every question generated gets embedded and stored in Pinecone. The next time someone asks for something semantically close — even with completely different words — the system finds it without touching the AI. Only genuinely new content triggers a generation call.

```
User request
     │
     ▼
Exact match in PostgreSQL? ──── YES ──► Return it (fast path)
     │
     NO
     │
     ▼
Embed the query via NVIDIA nv-embed-v1
     │
     ▼
Query Pinecone (cosine similarity)
     │
     ├── Score ≥ 0.85 ──► Return existing note from PostgreSQL
     │
     └── No match ──► Generate via NVIDIA LLaMA 3.1
                           │
                           ├── Save to PostgreSQL
                           └── Index in Pinecone (for next time)
```

---

## What it generates

**Notes** — detailed study notes for any topic. Returned instantly if a semantically similar topic was ever requested before.

**Tests** — 10 MCQs with four options and an answer key. Can be scheduled to arrive in your inbox at a specific date and time.

**Daily Practice Papers (DPPs)** — same format as tests, but scheduled to repeat daily at a fixed time. Good for building streaks.

**Semantic search** — ask a natural language question and get back the most relevant notes from everything ever generated, ranked by meaning not keywords.

---

## Architecture

```
                        ┌─────────────────────────┐
                        │      Spring Boot API      │
                        │  (Java 17, Spring AI 1.0) │
                        └────────────┬────────────┘
                                     │
               ┌─────────────────────┼─────────────────────┐
               │                     │                     │
               ▼                     ▼                     ▼
    ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
    │   NVIDIA NIM      │  │    PostgreSQL     │  │    Pinecone      │
    │                  │  │                  │  │                  │
    │  LLaMA 3.1 8B    │  │  Notes, Topics,  │  │  Vector index    │
    │  (generation)    │  │  Questions,      │  │  (semantic       │
    │                  │  │  Users           │  │   search)        │
    │  nv-embed-v1     │  │                  │  │                  │
    │  (embeddings)    │  │                  │  │  4096-dim cosine │
    └──────────────────┘  └──────────────────┘  └──────────────────┘
```

Auth is handled through GitHub OAuth2. All endpoints except health check and Swagger require a valid session.

---

## API

All routes are prefixed with `/api/v1`.

### Notes

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/note?topic=` | Get or generate a note for a topic |
| `GET` | `/note/search?query=` | Semantic search across all generated notes |

The `GET /note` endpoint checks PostgreSQL first (exact match), then Pinecone (semantic match above 0.85 cosine similarity), and only calls the LLM if nothing close exists.

### Tests

| Method | Path | Body | Description |
|--------|------|------|-------------|
| `POST` | `/tests/` | `{ "topic": "..." }` | Generate a test immediately |
| `POST` | `/tests/schedule` | `{ "topic": "...", "date": "YYYY-MM-DD", "time": "HH:MM" }` | Schedule a one-time test via email |
| `DELETE` | `/tests/schedule` | — | Cancel the scheduled test |

### Daily Practice Papers

| Method | Path | Body | Description |
|--------|------|------|-------------|
| `GET` | `/dpp?topic=` | — | Generate a DPP immediately |
| `POST` | `/dpp/schedule` | `{ "topic": "...", "time": "HH:MM" }` | Schedule a recurring daily DPP |
| `DELETE` | `/dpp/schedule` | — | Cancel the scheduled DPP |

### Other

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/auth/me` | Current authenticated user |
| `GET` | `/` | Health check |

Swagger UI is available at `/api/v1/swagger-ui.html` without authentication.

---

## Environment variables

Copy these into a `.env` file at the project root.

```env
# AI (NVIDIA NIM)
NVIDIA_API_KEY=

# Database
DB_URL=jdbc:postgresql://localhost:5432/studydb
DB_USERNAME=
DB_PASSWORD=

# Pinecone
PINECONE_API_KEY=
PINECONE_INDEX_HOST=https://your-index.svc.us-east-1.pinecone.io

# Mail (Gmail SMTP)
MAIL_USERNAME=
MAIL_PASSWORD=

# GitHub OAuth2
GITHUB_CLIENT_ID=
GITHUB_CLIENT_SECRET=

# CORS
ALLOWED_ORIGINS=http://localhost:3000
```

---

## Pinecone setup

1. Create an account at [pinecone.io](https://pinecone.io)
2. Create a new serverless index with these settings:
   - **Dimensions:** `4096`
   - **Metric:** `cosine`
   - **Cloud / Region:** any (e.g. AWS us-east-1)
3. Copy the **API key** from the sidebar and the **Host URL** from the index detail page into your `.env`

The index starts empty. It fills up automatically as notes and questions are generated.

---

## Running locally

```bash
git clone https://github.com/TornovDutta/AI-Powered-Study-Resource-Generator.git
cd AI-Powered-Study-Resource-Generator
mvn spring-boot:run
```

The app starts on `http://localhost:8080/api/v1`.

---

## Tech used

- **Spring Boot 3.5** — web, JPA, scheduling, mail, security
- **Spring AI 1.0** — OpenAI-compatible client (pointed at NVIDIA NIM)
- **NVIDIA NIM** — LLaMA 3.1 8B for generation, nv-embed-v1 for embeddings
- **Pinecone** — serverless vector database for semantic search and deduplication
- **PostgreSQL** — primary data store for all generated content and users
- **GitHub OAuth2** — authentication

---

Built by [Tornov Dutta](https://github.com/TornovDutta)
