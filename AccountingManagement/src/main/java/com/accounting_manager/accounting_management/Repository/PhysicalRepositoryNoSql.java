package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.PhysicalNoSql;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("nosql")
public interface PhysicalRepositoryNoSql extends MongoRepository<PhysicalNoSql, String> {

    @Query("{ 'nic': ?0 }")
    Optional<PhysicalNoSql> findByNic(String nic);
}
