package com.accounting_manager.bank_statement_engine.Repository;

import com.accounting_manager.bank_statement_engine.Entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement,Long> {
}
