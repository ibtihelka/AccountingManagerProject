package com.accounting_manager.accounting_engine.Repository;

import com.accounting_manager.accounting_engine.Entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type,Long> {
    @Query("SELECT t FROM Type t JOIN Type parentT ON t.parentType.idType = parentT.idType WHERE parentT.codeType=?1 AND t.codeType=?2")
    Type findOneByParentCodeTypeAndCodeType(String parentCodeType, String codeType);

    @Query(value = "SELECT t.* FROM type t JOIN type parentT ON t.fk_parent_type = parentT.id_type WHERE parentT.code_type=?1",nativeQuery = true)
    Type findOneByParentCodeType(String parentCodeType);
}
