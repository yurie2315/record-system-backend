package com.medicalrecord.springboot.controller;

import com.medicalrecord.springboot.enums.Status;
import com.medicalrecord.springboot.exception.ResourceNotFoundException;
import com.medicalrecord.springboot.model.ChurchMember;
import com.medicalrecord.springboot.repository.ChurchMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ChurchMemberController {

    @Autowired
    private ChurchMemberRepository churchMemberRepository;

    private final Path uploadDir = Paths.get("C:/MedicalUploads/");

    public ChurchMemberController() throws IOException {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    // ✅ GET all members
    @GetMapping("/churchmembers")
    public List<ChurchMember> getAllChurchMembers() {
        return churchMemberRepository.findAll();
    }

    // ✅ Get member by ID
    @GetMapping("/churchmembers/{id}")
    public ResponseEntity<ChurchMember> getChurchMemberById(@PathVariable Long id) {
        ChurchMember member = churchMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Church member not exist with id:" + id));
        return ResponseEntity.ok(member);
    }

    // ✅ Update member with optional image
    @PutMapping(value = "/churchmembers/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateChurchMember(
            @PathVariable Long id,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String contactNumber,
            @RequestParam String gender,
            @RequestParam String address,
            @RequestParam(required = false) String birthday,
            @RequestParam(required = false) String baptismDate,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String status
    ) {
        // Manual required validation
        if (firstName == null || firstName.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First Name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last Name is required");
        }

        try {
            ChurchMember churchMember = churchMemberRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Church member not exist with id:" + id));

            churchMember.setFirstName(firstName);
            churchMember.setLastName(lastName);
            churchMember.setEmail(email);
            churchMember.setContactNumber(contactNumber);
            churchMember.setGender(gender);
            churchMember.setAddress(address);

            // Dates
            if (birthday != null && !birthday.isEmpty()) {
                churchMember.setBirthday(LocalDate.parse(birthday));
            }
            if (baptismDate != null && !baptismDate.isEmpty()) {
                churchMember.setBaptismDate(LocalDate.parse(baptismDate));
            }

            // Status with default ACTIVE
            try {
                if (status != null && !status.isEmpty()) {
                    churchMember.setStatus(Status.valueOf(status.toUpperCase()));
                } else {
                    churchMember.setStatus(Status.ACTIVE);
                }
            } catch (Exception e) {
                churchMember.setStatus(Status.ACTIVE);
            }

            // Image upload
            if (image != null && !image.isEmpty()) {
                String fileName = image.getOriginalFilename();
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                churchMember.setImageUrl(fileName);
            }

            ChurchMember updatedMember = churchMemberRepository.save(churchMember);
            return ResponseEntity.ok(updatedMember);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving member: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating member: " + e.getMessage());
        }
    }

    // ✅ Serve uploaded images
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws MalformedURLException {
        Path file = uploadDir.resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // or detect type dynamically
                .body(resource);
    }

    // ✅ Create member (similar to update)
    @PostMapping(value = "/churchmembers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createChurchMember(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String contactNumber,
            @RequestParam(required = false) String birthday,
            @RequestParam(required = false) String baptismDate,
            @RequestParam String gender,
            @RequestParam String address,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String status
    ) {
        if (firstName == null || firstName.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First Name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last Name is required");
        }

        try {
            String fileName = "default.jpg";
            if (image != null && !image.isEmpty()) {
                fileName = image.getOriginalFilename();
                Files.copy(image.getInputStream(), uploadDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }

            Status memberStatus;
            try {
                memberStatus = (status != null && !status.isEmpty())
                        ? Status.valueOf(status.toUpperCase())
                        : Status.ACTIVE;
            } catch (Exception e) {
                memberStatus = Status.ACTIVE;
            }

            ChurchMember member = new ChurchMember();
            member.setFirstName(firstName);
            member.setLastName(lastName);
            member.setEmail(email);
            member.setContactNumber(contactNumber);
            member.setGender(gender);
            member.setAddress(address);
            member.setImageUrl(fileName);
            member.setStatus(memberStatus);

            if (birthday != null && !birthday.isEmpty()) {
                member.setBirthday(LocalDate.parse(birthday));
            }
            if (baptismDate != null && !baptismDate.isEmpty()) {
                member.setBaptismDate(LocalDate.parse(baptismDate));
            }

            ChurchMember savedMember = churchMemberRepository.save(member);
            return ResponseEntity.ok(savedMember);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving member: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating member: " + e.getMessage());
        }
    }

    // delete member rest api
    @DeleteMapping("/churchmembers/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteChurchMember(@PathVariable Long id) {
        ChurchMember churchMember = churchMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not exist with id : " + id));

        churchMemberRepository.delete(churchMember);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }


}