package org.example.aipoweredstudyresourcegenerator.service;

import org.example.aipoweredstudyresourcegenerator.Repo.QuestionRepo;
import org.example.aipoweredstudyresourcegenerator.Repo.TopicRepo;
import org.example.aipoweredstudyresourcegenerator.Model.QuestionsWrapper;
import org.example.aipoweredstudyresourcegenerator.Model.Topic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuestionGenerater {

    private final OpenAIService service;
    private final TopicRepo topicRepository;
    private final QuestionRepo questionsWrapperRepository;
    private final EmbeddingService embeddingService;
    private final PineconeService pineconeService;

    static final int number = 10;

    public QuestionGenerater(OpenAIService service, TopicRepo topicRepository,
                             QuestionRepo questionsWrapperRepository,
                             EmbeddingService embeddingService, PineconeService pineconeService) {
        this.service = service;
        this.topicRepository = topicRepository;
        this.questionsWrapperRepository = questionsWrapperRepository;
        this.embeddingService = embeddingService;
        this.pineconeService = pineconeService;
    }

    public ResponseEntity<String> generated(String topicName) {
        String prompt = "Generate " + number + " multiple choice questions (MCQs) on the topic: "
                + topicName + ".\n"
                + "Strictly use the following format for each question:\n\n"
                + "Question: <question text>\n"
                + "A) <option A>\n"
                + "B) <option B>\n"
                + "C) <option C>\n"
                + "D) <option D>\n"
                + "Answer: <correct option letter>\n\n"
                + "Do not include explanations, only follow this exact format for each question.";

        String response = service.getResponse(prompt);

        Topic topic = new Topic();
        topic.setName(topicName);
        topicRepository.save(topic);

        String[] questionsArray = response.split("Question:");
        for (String qBlock : questionsArray) {
            if (qBlock.trim().isEmpty()) continue;

            String[] lines = qBlock.trim().split("\n");
            QuestionsWrapper q = new QuestionsWrapper();
            q.setQuestion(lines[0].trim());

            for (String line : lines) {
                if (line.startsWith("A)")) q.setOptionA(line.substring(2).trim());
                else if (line.startsWith("B)")) q.setOptionB(line.substring(2).trim());
                else if (line.startsWith("C)")) q.setOptionC(line.substring(2).trim());
                else if (line.startsWith("D)")) q.setOptionD(line.substring(2).trim());
                else if (line.startsWith("Answer:")) q.setAnswer(line.substring(7).trim());
            }

            q.setTopic(topic);
            QuestionsWrapper saved = questionsWrapperRepository.save(q);

            // Index question in Pinecone to build a searchable question bank
            List<Float> embedding = embeddingService.embed(topicName + " " + saved.getQuestion());
            pineconeService.upsert(
                "question-" + saved.getId(),
                embedding,
                Map.of("type", "question", "topic", topicName, "questionId", String.valueOf(saved.getId()))
            );
        }

        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    public List<PineconeService.QueryMatch> findSimilarQuestions(String query, int topK) {
        List<Float> embedding = embeddingService.embed(query);
        return pineconeService.query(embedding, topK).stream()
            .filter(m -> "question".equals(m.metadata().get("type")))
            .toList();
    }
}
