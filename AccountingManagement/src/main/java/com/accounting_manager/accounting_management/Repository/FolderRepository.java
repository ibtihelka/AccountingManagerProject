package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.Folder;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Profile("sql")
public interface FolderRepository extends JpaRepository<Folder, Long> {

    /*PLEASE IGNORE THE QUERY ERROR, IT'S A FALSE ERROR*/
    @Query("SELECT f FROM Folder f " +
            "JOIN ThirdParty tp on tp.idThirdParty = f.client.idThirdParty " +
            "WHERE " +
            "f.deleted = false " +
            "AND CASE WHEN ?3 = 'archive' THEN f.archived = true AND f.creator.idThirdParty = ?1 " +
            "WHEN ?3 = 'myfolders' THEN (f.creator.idThirdParty = ?1 AND f.client.idThirdParty = ?1) " +
            "ELSE ( " +
            "    f.archived = false AND (f.creator.idThirdParty = ?1 OR f.client.idThirdParty = ?1) " +
            ") " +
            "OR ( " +
            "    f.archived = true AND f.creator.idThirdParty != ?1 AND f.client.idThirdParty = ?1) END " +
            "AND CASE " +
            "WHEN ?3 = 'open' THEN f.status=true " +
            "WHEN ?3 = 'closed' THEN f.status=false " +
            "WHEN ?3 = 'favorite' THEN f.favorite=true " +
            "ELSE TRUE END " +
            "AND " +
            "(?2 IS NULL OR f.name LIKE CONCAT('%', TRIM(?2), '%') OR " +
            "(tp.discriminator = 'legal' AND EXISTS (SELECT 1 FROM Legal l WHERE l.idThirdParty = tp.idThirdParty AND l.legalName LIKE %?2%)) OR " +
            "(tp.discriminator = 'physical' AND EXISTS (SELECT 1 FROM Physical p WHERE p.idThirdParty = tp.idThirdParty AND (p.firstname LIKE %?2% OR p.lastname LIKE %?2% OR CONCAT(p.firstname,' ',p.lastname) LIKE %?2%) ))) " +
            "ORDER BY f.favorite DESC, f.creationDate DESC ")
    Page<Folder> findMyFolders(Long idThirdParty, String query, String type, Pageable pageable);



    @Query("SELECT f FROM Folder f " +
            "JOIN ThirdParty tp on tp.idThirdParty = f.client.idThirdParty " +
            "WHERE " +
            "((?1 = ?2 AND f.client.idThirdParty = ?1) " +
            "OR (f.creator.idThirdParty = ?1 AND f.client.idThirdParty = ?2))  " +
            "AND f.deleted = false " +
            "AND (CASE WHEN f.creator.idThirdParty = ?1 THEN f.archived = false ELSE true END) "+
            "AND (?3 IS NULL OR f.name LIKE CONCAT('%', TRIM(?3), '%')) " +
            "ORDER BY f.favorite DESC, f.creationDate DESC")
    Page<Folder> findMyClientFolders(Long idThirdParty, Long idClient, String query, Pageable pageable);


    @Query("SELECT f FROM Folder f " +
            "JOIN ThirdParty tp on tp.idThirdParty = f.client.idThirdParty " +
            "WHERE " +
            "f.creator.idThirdParty = ?2 " +
            "AND f.client.idThirdParty = ?1 " +
            "AND f.deleted = false " +
            "AND (?3 IS NULL OR f.name LIKE %?3% ) " +
            "ORDER BY f.favorite DESC, f.creationDate DESC ")
    Page<Folder> findMyAccountantFolders(Long idThirdParty,Long idAccountant, String query, Pageable pageable);

    @Query("SELECT f FROM Folder f " +
            "JOIN ThirdParty tp on tp.idThirdParty = f.client.idThirdParty " +
            "WHERE " +
            "CASE WHEN ?3 = 'archive' THEN f.archived = true ELSE TRUE END " +
            "AND CASE " +
            "WHEN ?3 = 'open' THEN f.status=true " +
            "WHEN ?3 = 'closed' THEN f.status=false " +
            "WHEN ?3 = 'favorite' THEN f.favorite=true " +
            "ELSE TRUE END " +
            "AND (?1 IS NULL OR f.creator.idThirdParty = ?1 OR f.client.idThirdParty = ?1) " +
            "AND " +
            "(?2 IS NULL OR f.name LIKE CONCAT('%', TRIM(?2), '%') OR " +
            "(tp.discriminator = 'legal' AND EXISTS (SELECT 1 FROM Legal l WHERE l.idThirdParty = tp.idThirdParty AND l.legalName LIKE %?2%)) OR " +
            "(tp.discriminator = 'physical' AND EXISTS (SELECT 1 FROM Physical p WHERE p.idThirdParty = tp.idThirdParty AND (p.firstname LIKE %?2% OR p.lastname LIKE %?2% OR CONCAT(p.firstname,' ',p.lastname) LIKE %?2%) ))) " +
            "ORDER BY f.favorite DESC, f.creationDate DESC ")
    Page<Folder> findFoldersAsAdmin(Long idThirdParty, String query, String type, Pageable pageable);

    @Modifying
    @Query("UPDATE Folder f SET f.deleted = true WHERE f.idFolder = :id")
    @Transactional
    void markFolderAsDeleted(@Param("id") Long id);

    Long countByStatus(boolean status);

    @Query(value = "SELECT COUNT(*) FROM Folder f WHERE f.deleted = false AND (fk_creator=?1 OR fk_client=?1)",nativeQuery = true)
    Long getTotalFoldersByThirdPartyId(Long id);
}
