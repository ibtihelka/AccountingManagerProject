package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.Document;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("sql")
public interface DocumentRepository extends JpaRepository<Document,Long> {

    @Query("select count(doc) = 1 from Document doc where doc.sourceImg = ?1 AND doc.folder.idFolder=?2 ")
    public boolean documentIsPresent(byte[] sourceImg, Long folderId);

    @Query(value = "SELECT doc FROM Document doc WHERE doc.sourceImg = ?1 AND doc.folder.idFolder=?2")
    Document getDocumentBySourceImg(byte[] sourceImg, Long folderId);

    @Query("SELECT doc FROM Document doc " +
            "JOIN Folder f ON doc.folder.idFolder = f.idFolder " +
            "WHERE " +
            "doc.deleted = false " +
            "AND f.idFolder = :folder " +
            "AND (:query IS NULL OR :query = '' OR doc.name LIKE CONCAT('%', :query, '%')) " +
            "ORDER BY doc.creationDate DESC")
    Page<Document> findAll(@Param("folder") Long folder, @Param("query") String query, Pageable pageable);


    @Query("SELECT doc FROM Document doc " +
            "WHERE " +
            "(COALESCE(?1, doc.folder.idFolder) = doc.folder.idFolder) " +
            "AND (COALESCE(TRIM(?2), '') = '' OR doc.name LIKE CONCAT('%', TRIM(?2), '%')) " +
            "ORDER BY doc.creationDate DESC")
    Page<Document> findAllForAdmin(Long folder, String query, String type, Pageable pageable);

    @Modifying
    @Query("UPDATE Document doc SET doc.deleted = true WHERE doc.id = :id")
    @Transactional
    void markDocumentAsDeleted(@Param("id") Long id);

    @Query("SELECT COUNT(doc) FROM Document doc WHERE doc.status=true AND doc.processType.parentType.codeType='DOCUMENT_PROCESS' AND doc.processType.codeType='AUTO'")
    Long findAutoProcessedDocuments();

    @Query("SELECT p.processType.codeType, COUNT(p) FROM Document p GROUP BY p.processType.codeType")
    List<Object[]> findProcessTypeCount();

    @Query("SELECT MONTH(doc.creationDate), COUNT(doc) FROM Document doc WHERE YEAR(doc.creationDate) = YEAR(CURRENT_DATE) GROUP BY MONTH(doc.creationDate)")
    List<Object[]> findDocumentsCountByMonthCurrentYear();

    @Query(value = "SELECT COUNT(*) FROM Document doc JOIN folder f ON doc.fk_folder = f.id_folder WHERE doc.deleted = false AND (f.fk_client =?1 OR f.fk_creator=?1)",nativeQuery = true)
    Long getTotalDocumentsByThirdPartyId(Long id);
}