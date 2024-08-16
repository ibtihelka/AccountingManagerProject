package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.DocumentNoSql;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("nosql")
public interface DocumentRepositoryNoSql extends MongoRepository<DocumentNoSql, String> {
    @Query("{ 'sourceImg': ?0, 'folder.idFolder': ?1 }")
    boolean documentIsPresent(byte[] sourceImg, String folderId);
    @Query("{ 'sourceImg': ?0, 'folder.idFolder': ?1 }")
    DocumentNoSql getDocumentBySourceImg(byte[] sourceImg, String folderId);

    @Query("{ 'deleted': false, " +
            "'folder.idFolder': :#{#folder}, " +
            "'$or': [ " +
            "{'name': { $regex: :#{#query}, $options: 'i' }}, " +
            "{ :#{#query} : { $exists: false }} " +
            "] }")
    Page<DocumentNoSql> findAll(@Param("folder") String folder, @Param("query") String query, Pageable pageable);

    @Query("{ " +
            "'$and': [ " +
            "{ '$or': [ { 'folder.idFolder': ?0 }, { ?0 : { $exists: false } } ] }, " +
            "{ '$or': [ { 'name': { $regex: ?1, $options: 'i' } }, { ?1 : { $exists: false } } ] } " +
            "] " +
            "} " +
            "ORDER BY { 'creationDate': -1 }")
    Page<DocumentNoSql> findAllForAdmin(String folder, String query, Pageable pageable);

    @Query("{ '_id': ?0 }")
    void markDocumentAsDeleted(String id);

    @Query("{ 'status': true, 'processType.parentType.codeType': 'DOCUMENT_PROCESS', 'processType.codeType': 'AUTO' }")
    Long findAutoProcessedDocuments();

    @Query("{ 'processType.codeType': { $exists: true } }")
    List<DocumentNoSql> findProcessTypeCount();

    @Query("{ 'creationDate': { $gte: ?0, $lte: ?1 } }")
    List<DocumentNoSql> findDocumentsCountByMonthCurrentYear(Date startDate, Date endDate);

    @Query(value = "{ 'folder.fkClient': ?0, 'folder.fkCreator': ?0, 'deleted': false }")
    Long getTotalDocumentsByThirdPartyId(String id);
}
