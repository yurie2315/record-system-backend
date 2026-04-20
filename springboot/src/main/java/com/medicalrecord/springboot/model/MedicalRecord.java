package com.medicalrecord.springboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bloodPressure;
    private String bloodSugar;
    private Double height;
    private Double weight;
    private Double uricAcid;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "member_id") // foreign key column
    private ChurchMember churchMember;

}