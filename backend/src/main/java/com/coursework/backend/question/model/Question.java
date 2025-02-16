package com.coursework.backend.question.model;

import com.coursework.backend.test.model.Test;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questions")

public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", referencedColumnName = "uuid_training", nullable = false)
    @JsonIgnore
    private Test test;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "points", nullable = true)
    private Integer points;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    public static enum Type{
        TEXT,
        RADIOBUTTON,
        CHECKBOX
    }
}
