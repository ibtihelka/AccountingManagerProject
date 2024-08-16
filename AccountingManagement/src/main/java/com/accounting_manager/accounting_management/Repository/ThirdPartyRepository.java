package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.ThirdParty;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
@Profile("sql")
public interface ThirdPartyRepository extends JpaRepository<ThirdParty,Long> {

    Optional<ThirdParty> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<ThirdParty> findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT tp FROM ThirdParty tp " +
            "WHERE " +
            "CASE WHEN TRIM(?1) <> '' THEN " +
            "CONCAT(tp.firstname, ' ', tp.lastname) LIKE CONCAT('%', TRIM(?1), '%') " +
            "OR tp.legalName LIKE CONCAT('%', TRIM(?1), '%') " +
            "OR tp.email LIKE CONCAT('%', TRIM(?1), '%') " +
            "OR tp.nic LIKE CONCAT('%', TRIM(?1), '%') " +
            "OR tp.siretNumber LIKE CONCAT('%', TRIM(?1), '%') " +
            "ELSE TRUE END " +
            "AND CASE WHEN ?2 <> '' THEN  tp.discriminator = ?2 ELSE TRUE END ")
    Page<ThirdParty> findThirdParties(String query, String discriminator, Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM third_party AS tp " +
            "JOIN accountant_client AS ac " +
            "ON tp.id_third_party = ac.client_id " +
            "WHERE ac.accountant_id=?1 " +
            "AND CASE WHEN TRIM(?2) <> '' THEN " +
            "CONCAT(tp.firstname, ' ', tp.lastname) LIKE CONCAT('%', TRIM(?2), '%') " +
            "OR tp.legal_name LIKE CONCAT('%', TRIM(?2), '%') " +
            "OR tp.email LIKE CONCAT('%', TRIM(?2), '%') " +
            "OR tp.nic LIKE CONCAT('%', TRIM(?2), '%') " +
            "OR tp.siret_number LIKE CONCAT('%', TRIM(?2), '%') ELSE TRUE END",nativeQuery = true)
    Page<ThirdParty> findAccountantClients(Long id, String query, Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM third_party AS tp " +
            "JOIN accountant_client AS ac " +
            "ON tp.id_third_party = ac.accountant_id " +
            "WHERE ac.client_id=?1 " +
            "AND CASE WHEN TRIM(?2) <> '' THEN " +
            "CONCAT(tp.firstname, ' ', tp.lastname) LIKE CONCAT('%', TRIM(?2), '%') " +
            "OR tp.legal_name LIKE CONCAT('%', TRIM(?2), '%') " +
            "OR tp.email LIKE CONCAT('%', TRIM(?2), '%') " +
            "OR tp.nic LIKE CONCAT('%', TRIM(?2), '%') " +
            "OR tp.siret_number LIKE CONCAT('%', TRIM(?2), '%') ELSE TRUE END",nativeQuery = true)
    Page<ThirdParty> findClientAccountants(Long id, String query, Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM ThirdParty as tp " +
            "JOIN Type t on tp.thirdPartyRole.idType=t.idType " +
            "WHERE t.codeType = 'CLIENT' AND t.parentType.codeType= 'ROLE' ")
    Long getClientsCount();

    @Query(value = "SELECT COUNT(*) " +
            "FROM ThirdParty as tp " +
            "JOIN Type t on tp.thirdPartyRole.idType=t.idType " +
            "WHERE t.codeType = 'ACCOUNTANT' AND t.parentType.codeType= 'ROLE' ")
    Long getAccountantsCount();

    @Query(value = "SELECT COUNT(*) FROM accountant_client WHERE accountant_id=?1",nativeQuery = true)
    Long getClientsCountByThirdPartyId(Long id);

    @Query(value = "SELECT COUNT(*) FROM accountant_client WHERE client_id=?1",nativeQuery = true)
    Long getAccountantsCountByThirdPartyId(Long id);
}