package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.InvoiceNoSql;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("nosql")
public interface InvoiceRepositoryNoSql extends MongoRepository<InvoiceNoSql, String> {

    @Query("{ 'sourceImg': ?0, 'folder.idFolder': ?1 }")
    InvoiceNoSql getDocumentBySourceImg(byte[] sourceImg, String folderId);
}
