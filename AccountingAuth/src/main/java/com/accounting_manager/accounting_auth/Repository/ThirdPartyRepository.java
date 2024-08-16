package com.accounting_manager.accounting_auth.Repository;

import com.accounting_manager.accounting_auth.Entity.ThirdParty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {

    Optional<ThirdParty> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<ThirdParty> findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT * FROM third_party tp " +
            "JOIN type t on tp.fk_third_party_role = t.id_type " +
            "WHERE " +
            "CASE WHEN TRIM(?1) <> '' THEN " +
            "CONCAT(firstname, ' ', lastname) LIKE CONCAT('%', TRIM(?1), '%') " +
            "OR legal_name LIKE CONCAT('%', TRIM(?1), '%') " +
            "OR email LIKE CONCAT('%', TRIM(?1), '%') " +
            "OR nic LIKE CONCAT('%', TRIM(?1), '%') " +
            "OR siret_number LIKE CONCAT('%', TRIM(?1), '%') " +
            "ELSE TRUE END " +
            "AND CASE WHEN ?2 <> '' THEN  discriminator = ?2 ELSE TRUE END " +
            "AND t.code_type <> 'ADMIN'" // Exclude records with role 'admin'
            , nativeQuery = true)
    Page<ThirdParty> findAllThirdParties(String query, String discriminator, Pageable pageable);
}
