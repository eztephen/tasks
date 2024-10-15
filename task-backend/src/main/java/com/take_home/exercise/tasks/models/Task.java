package com.take_home.exercise.tasks.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.take_home.exercise.tasks.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@DynamicUpdate //added so that only the modified parameters/columns are updated. not the whole row
@EntityListeners(AuditingEntityListener.class)
public record Task(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id,
        String title,
        String description,
        @Enumerated(EnumType.STRING) Status status,
        LocalDate dueDate
) {}
