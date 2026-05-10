ai-study-platform
│
├── src/main/java/com/example/aistudy
│
│   ├── config
│   │     ├── OpenAIConfig.java
│   │     ├── SecurityConfig.java
│   │     └── SchedulerConfig.java
│   │
│   ├── common
│   │     ├── exception
│   │     │      ├── GlobalExceptionHandler.java
│   │     │      └── CustomException.java
│   │     │
│   │     ├── utils
│   │     │      ├── PromptBuilder.java
│   │     │      └── DateUtils.java
│   │     │
│   │     └── constants
│   │            └── AppConstants.java
│
│   ├── user
│   │     ├── controller
│   │     │      └── UserController.java
│   │     │
│   │     ├── service
│   │     │      └── UserService.java
│   │     │
│   │     ├── repository
│   │     │      └── UserRepository.java
│   │     │
│   │     ├── entity
│   │     │      └── User.java
│   │     │
│   │     └── dto
│   │            └── UserDTO.java
│
│   ├── notes
│   │     ├── controller
│   │     │      └── NotesController.java
│   │     │
│   │     ├── service
│   │     │      ├── NotesService.java
│   │     │      └── NotesGeneratorService.java
│   │     │
│   │     ├── repository
│   │     │      └── NotesRepository.java
│   │     │
│   │     ├── entity
│   │     │      └── Notes.java
│   │     │
│   │     └── dto
│   │            └── NotesRequestDTO.java
│
│   ├── dpp
│   │     ├── controller
│   │     │      └── DppController.java
│   │     │
│   │     ├── service
│   │     │      ├── DppService.java
│   │     │      └── DppGeneratorService.java
│   │     │
│   │     ├── repository
│   │     │      └── DppRepository.java
│   │     │
│   │     ├── entity
│   │     │      └── Dpp.java
│   │     │
│   │     └── dto
│   │            └── DppRequestDTO.java
│
│   ├── test
│   │     ├── controller
│   │     │      └── TestController.java
│   │     │
│   │     ├── service
│   │     │      ├── TestService.java
│   │     │      └── TestGeneratorService.java
│   │     │
│   │     ├── repository
│   │     │      └── TestRepository.java
│   │     │
│   │     ├── entity
│   │     │      └── Test.java
│   │     │
│   │     └── dto
│   │            └── TestRequestDTO.java
│
│   ├── scheduler
│   │     ├── service
│   │     │      └── StudySchedulerService.java
│   │     │
│   │     └── job
│   │            ├── DppSchedulerJob.java
│   │            └── TestSchedulerJob.java
│
│   ├── email
│   │     ├── service
│   │     │      └── EmailService.java
│   │     │
│   │     └── template
│   │            └── ReminderEmailTemplate.java
│
│   ├── ai
│   │     ├── service
│   │     │      └── OpenAIService.java
│   │     │
│   │     ├── client
│   │     │      └── OpenAIClient.java
│   │     │
│   │     └── dto
│   │            ├── AIRequestDTO.java
│   │            └── AIResponseDTO.java
│
│   └── AiStudyPlatformApplication.java
│
└── src/main/resources
├── application.yml
├── templates
│      └── email-template.html
└── static