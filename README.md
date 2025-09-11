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
spring.datasource.driver-class-name=${DATASOURCE_DRIVER_CLASS_NAME}
spring.datasource.username=${DATASOURCE_USERNAME}
spring.datasource.password=${DATASOURCE_PASSWORD}
spring.datasource.url=${DATASOURCE_URL}
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

```

### 3️⃣ Add OpenAI API Key
```properties
# AI
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.model=${OPENAI_CHAT_MODEL}

```

### 4️⃣ Configure Email Settings
```properties
# Mail
spring.mail.host=${MAIL_HOST}
spring.mail.port=${MAIL_PORT}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
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


## 📌 API Endpoints

| Method | Endpoint     | Parameters                                                                 | Description |
|--------|--------------|-----------------------------------------------------------------------------|-------------|
| GET    | `/note`      | `topic` (query param, String)                                               | Generates and returns study notes for the given topic. |
| GET    | `/create`    | `topic` (in request body, String)                                           | Generates test questions for the given topic. |
| POST   | `/testStart` | Request Body: <br>• `topic` (String) <br>• `date` (LocalDate) <br>• `time` (LocalTime) | Schedules a test generation at the given date and time. Sends questions via email. |
| DELETE | `/testStop`  | None                                                                        | Stops the currently scheduled test. |
| GET    | `dppCreate`  | `topic` (in request body, String)                                           | Generates <br/>DPP (Daily Practice Paper) questions for the given topic. |
| POST   | `/dppStart`  | Request Body: <br>• `topic` (String) <br>• `time` (LocalTime)               | Schedules DPP generation at the given time. Sends questions via email. |
| DELETE | `/dppStop`   | None                                                                        | Stops the currently scheduled DPP. |

---

---

## 📂 Project Structure
```
src/main/java/org/example/aipoweredstudyresourcegenerator
│── config/ # Configuration classes 
│── controller/ # REST API endpoints
│── DAO/ # Data Access Objects / Repositories
│── Model/ # Entities 
│── service/ # Business logic & services
│── AiPoweredStudyResourceGeneratorApplication.java # Main Spring Boot application
```

---



## 👨‍💻 Author
**Tornov Dutta**  
📧 your-email@example.com  
🌐 [LinkedIn](https://linkedin.com/in/yourprofile) | [GitHub](https://github.com/yourusername)
