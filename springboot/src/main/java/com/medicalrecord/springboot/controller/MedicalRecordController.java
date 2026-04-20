package com.medicalrecord.springboot.controller;

import com.medicalrecord.springboot.dto.MedicalRecordDTO;
import com.medicalrecord.springboot.repository.MedicalRecordRepository;
import com.medicalrecord.springboot.service.MedicalRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    // Create Medical Record
    @PostMapping("/medical-records")
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@RequestBody MedicalRecordDTO dto) {
        MedicalRecordDTO createdRecord = medicalRecordService.createMedicalRecord(dto);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }

    // Get all medical records
    @GetMapping("/medical-records")
    public ResponseEntity<List<MedicalRecordDTO>> getAllMedicalRecords() {
        List<MedicalRecordDTO> records = medicalRecordService.getAllRecords();
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/medical-records/member/{memberId}")
    public List<MedicalRecordDTO> getByMemberId(@PathVariable Long memberId) {
        return medicalRecordRepository.findByChurchMemberId(memberId)
                .stream()
                .map(MedicalRecordDTO::new)
                .toList();
    }

}