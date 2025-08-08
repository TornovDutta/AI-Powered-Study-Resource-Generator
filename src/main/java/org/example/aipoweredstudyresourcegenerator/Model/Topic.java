package org.example.aipoweredstudyresourcegenerator.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy = "topic" ,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<QuestionsWrapper> questions;
}
