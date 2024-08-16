package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.Type;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("sql")
public interface TypeRepository extends JpaRepository<Type,Long> {
    @Query("SELECT t FROM Type t JOIN Type parentT on t.parentType.idType=parentT.idType WHERE parentT.codeType=?1 AND t.codeType=?2")
    Type findOneByParentCodeTypeAndCodeType(String parentCodeType, String codeType);

//    @Query("SELECT t FROM Type t LEFT JOIN FETCH t.parentType WHERE t.parentType=null")
//    List<Type> findAll();

}
