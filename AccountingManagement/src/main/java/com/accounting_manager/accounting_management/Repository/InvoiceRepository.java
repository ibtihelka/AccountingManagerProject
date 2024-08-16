package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.Invoice;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("sql")
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT doc FROM Invoice doc WHERE doc.sourceImg = ?1 AND doc.folder.idFolder=?2")
    Invoice getDocumentBySourceImg(byte[] sourceImg, Long folderId);

}
