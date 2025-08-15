package org.example.aipoweredstudyresourcegenerator.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuestionsWrapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String Question;
    private String OptionA;
    private String OptionB;
    private String OptionC;
    private String OptionD;
    private String Answer;


    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
}
