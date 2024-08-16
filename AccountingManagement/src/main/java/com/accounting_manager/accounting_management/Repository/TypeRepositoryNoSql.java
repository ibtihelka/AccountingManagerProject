package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.TypeNoSql;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("nosql")
public interface TypeRepositoryNoSql extends MongoRepository<TypeNoSql, String> {

    @Query("{ 'parentType.codeType': ?0, 'codeType': ?1 }")
    TypeNoSql findOneByParentCodeTypeAndCodeType(String parentCodeType, String codeType);

}
