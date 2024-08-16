package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.LegalNoSql;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("nosql")
public interface LegalRepositoryNoSql extends MongoRepository<LegalNoSql, String> {

    @Query("{ 'siretNumber': ?0 }")
    Optional<LegalNoSql> findBySiretNumber(String siretNumber);
}
