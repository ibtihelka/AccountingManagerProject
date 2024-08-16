package com.accounting_manager.accounting_management.Repository;


import com.accounting_manager.accounting_management.Entity.test;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("nosql")

public interface testRepo extends MongoRepository<test, String> {
}
