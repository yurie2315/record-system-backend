package com.medicalrecord.springboot.repository;

import com.medicalrecord.springboot.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findByChurchMemberId(Long memberId);
}