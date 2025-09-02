# 📚 AI-Generated Study Material Platform

An AI-powered educational platform built with **Spring Boot** that generates **Notes**, **Daily Practice Papers (DPPs)**, and **Tests** using the OpenAI API.  
The system automatically schedules DPPs and tests and sends email reminders to students.

---

## 🚀 Features

- 🤖 **AI-Generated Notes** — Automatically create subject/topic-specific notes.
- 📄 **Daily Practice Papers (DPPs)** — Generated and scheduled daily.
- 📝 **Test Generation** — Automatic test creation for specific topics.
- 📅 **Automatic Scheduling** — DPPs and tests are scheduled without manual intervention.
- 📧 **Email Reminders** — Sends reminders for upcoming DPPs/tests.
- 🌐 **Deployed on Render** — Accessible online via a live demo.

---

## 🛠 Tech Stack

**Backend:**
- Java 17+
- Spring Boot (Web, Data JPA, Scheduling, Mail)
- Hibernate / JPA
- PostgreSQL (Database)

**AI Integration:**
- OpenAI API (ChatGPT/GPT models)

**Others:**
- Spring Scheduler (for DPP/Test scheduling)
- JavaMailSender (for email reminders)
- Lombok (reduce boilerplate code)
- Spring Dotenv (for environment variables)

---

## ⚙️ Installation & Setup

### 1️⃣ Clone the repository
```bash

git clone https://github.com/TornovDutta/AI-Powered-Study-Resource-Generator.git
cd AI-Powered-Study-Resource-Generator

```

### 2️⃣ Configure Database
Update `application.properties`  with environment variables:

```properties
# Database
spring.datasource.driver-class-name=${SPRING_DATASOURCE_DRIVER_CLASS_NAME}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

```

### 3️⃣ Add OpenAI API Key
```properties
# AI
spring.ai.openai.api-key=${SPRING_AI_OPENAI_API_KEY}
spring.ai.openai.chat.model=${SPRING_AI_OPENAI_CHAT_MODEL}

```

### 4️⃣ Configure Email Settings
```properties
# Mail
spring.mail.host=${SPRING_MAIL_HOST}
spring.mail.port=${SPRING_MAIL_PORT}
spring.mail.username=${SPRING_MAIL_USERNAME}
spring.mail.password=${SPRING_MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
email.from=${EMAIL_FROM}

```

### 4️⃣ Configure OAuth
```properties
# OAuth
spring.security.oauth2.client.registration.github.client-id=${SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_SECRET}


```

### 5️⃣ Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

---

## 📡 REST API Endpoints

| Method | Endpoint               | Description |
|--------|------------------------|-------------|
| **POST**   | `/notes/generate`       | Generate AI-powered study notes for a given topic. |
| **GET**    | `/notes/{id}`           | Get a specific note by its ID. |
| **GET**    | `/notes`                | Get all generated notes. |
| **POST**   | `/dpp/generate`         | Generate a Daily Practice Paper (DPP) for a given topic. |
| **GET**    | `/dpp/{id}`             | Get a specific DPP by its ID. |
| **GET**    | `/dpp`                  | Get all generated DPPs. |
| **POST**   | `/tests/generate`       | Generate a test for a given topic. |
| **GET**    | `/tests/{id}`           | Get a specific test by its ID. |
| **GET**    | `/tests`                | Get all generated tests. |
| **POST**   | `/schedule/dpp`         | Schedule a DPP at a specific date/time. |
| **POST**   | `/schedule/test`        | Schedule a test at a specific date/time. |
| **POST**   | `/email/reminder`       | Send a manual email reminder. |

---

## 📂 Project Structure
```
src/main/java/org/example/aipoweredstudyresourcegenerator
│── config/ # Configuration classes (security, AI, etc.)
│── controller/ # REST API endpoints
│── DAO/ # Data Access Objects / Repositories
│── Model/ # Entities (User, Questions, Topic, etc.)
│── service/ # Business logic & services
│── AiPoweredStudyResourceGeneratorApplication.java # Main Spring Boot application
```

---

## 📜 License
This project is licensed under the MIT License.

---

## 👨‍💻 Author
**Tornov Dutta**  
📧 your-email@example.com  
🌐 [LinkedIn](https://linkedin.com/in/yourprofile) | [GitHub](https://github.com/yourusername)
