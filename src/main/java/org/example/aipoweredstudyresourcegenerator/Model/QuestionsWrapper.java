package org.example.aipoweredstudyresourcegenerator.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuestionsWrapper {
    @Id
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
