package com.medicalrecord.springboot.service;

import com.medicalrecord.springboot.dto.MedicalRecordDTO;
import com.medicalrecord.springboot.model.ChurchMember;
import com.medicalrecord.springboot.model.MedicalRecord;
import com.medicalrecord.springboot.repository.ChurchMemberRepository;
import com.medicalrecord.springboot.repository.MedicalRecordRepository;
import com.medicalrecord.springboot.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private ChurchMemberRepository memberRepository;

    // Create Medical Record
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO dto) {

        ChurchMember member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id " + dto.getMemberId()));

        MedicalRecord record = new MedicalRecord();
        record.setChurchMember(member);
        record.setBloodPressure(dto.getBloodPressure());
        record.setBloodSugar(dto.getBloodSugar());
        record.setHeight(dto.getHeight());
        record.setWeight(dto.getWeight());
        record.setUricAcid(dto.getUricAcid());
        record.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());

        MedicalRecord savedRecord = medicalRecordRepository.save(record);

        return mapToDTO(savedRecord);
    }

    // List all records
    public List<MedicalRecordDTO> getAllRecords() {
        return medicalRecordRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Map entity to DTO
    private MedicalRecordDTO mapToDTO(MedicalRecord record) {
        return new MedicalRecordDTO(
                record.getId(),
                record.getChurchMember().getId(),
                record.getChurchMember().getFirstName() + " " + record.getChurchMember().getLastName(), // memberName
                record.getChurchMember().getImageUrl(), // imageUrl
                record.getBloodPressure(),
                record.getBloodSugar(),
                record.getHeight(),
                record.getWeight(),
                record.getUricAcid(),
                record.getCreatedAt()
        );
    }

}