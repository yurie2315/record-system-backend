package com.medicalrecord.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medicalrecord.springboot.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "church_member")
public class ChurchMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "church_id", unique = true)
    private String churchId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String email;

    private String contactNumber;

    private String imageUrl; // ✅ single image field

    private LocalDate birthday;

    private String gender;

    private String address;

    private String age;

    @Column(name = "baptism_date")
    private LocalDate baptismDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE; // ✅ default

    @OneToMany(mappedBy = "churchMember", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MedicalRecord> medicalRecords;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}