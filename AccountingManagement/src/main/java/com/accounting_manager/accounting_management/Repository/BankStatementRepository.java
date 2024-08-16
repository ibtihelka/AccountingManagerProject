package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.BankStatement;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("sql")
public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {

    @Query(value = "SELECT doc FROM BankStatement doc WHERE doc.sourceImg = ?1 AND doc.folder.idFolder=?2")
    BankStatement getDocumentBySourceImg(byte[] sourceImg, Long folderId);

}
