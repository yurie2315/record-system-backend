package com.medicalrecord.springboot.dto;

import com.medicalrecord.springboot.model.MedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDTO {

    private Long id;
    private Long memberId;
    private String memberName;    // <-- include member name
    private String imageUrl;      // <-- optional member image

    private String bloodPressure;
    private String bloodSugar;
    private Double height;
    private Double weight;
    private Double uricAcid;
    private LocalDateTime createdAt;

    public MedicalRecordDTO(MedicalRecord record) {
        this.id = record.getId();
        this.memberId = record.getChurchMember().getId();
        this.memberName = record.getChurchMember().getFirstName() + " " +
                record.getChurchMember().getLastName();
        this.bloodPressure = record.getBloodPressure();
        this.bloodSugar = record.getBloodSugar();
        this.height = record.getHeight();
        this.weight = record.getWeight();
        this.uricAcid = record.getUricAcid();
        this.createdAt = record.getCreatedAt();
    }

}