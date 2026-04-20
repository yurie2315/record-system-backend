package com.medicalrecord.springboot.repository;

import com.medicalrecord.springboot.model.ChurchMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChurchMemberRepository extends JpaRepository<ChurchMember, Long> {

    // 🔍 Find by Church ID
    ChurchMember findByChurchId(String churchId);

    // ✅ Check if exists
    boolean existsByChurchId(String churchId);

    // 📧 Find by Email
    ChurchMember findByEmail(String email);

    // 🔎 Search by name
    List<ChurchMember> findByFirstNameContainingIgnoreCase(String firstName);

    List<ChurchMember> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName
    );
}