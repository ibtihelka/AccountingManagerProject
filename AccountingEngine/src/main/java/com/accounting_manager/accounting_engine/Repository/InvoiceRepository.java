package com.accounting_manager.accounting_engine.Repository;

import com.accounting_manager.accounting_engine.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
}
