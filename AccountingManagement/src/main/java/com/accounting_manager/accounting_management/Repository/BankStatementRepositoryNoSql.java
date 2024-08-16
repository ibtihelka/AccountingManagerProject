package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.BankStatementNoSql;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("nosql")
public interface BankStatementRepositoryNoSql extends MongoRepository<BankStatementNoSql, String> {

    @Query("{ 'sourceImg': ?0, 'folder.idFolder': ?1 }")
    BankStatementNoSql getDocumentBySourceImg(byte[] sourceImg, String folderId);

}
