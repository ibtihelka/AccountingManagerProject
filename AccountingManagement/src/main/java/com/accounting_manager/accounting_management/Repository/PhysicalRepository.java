package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.Physical;
import com.accounting_manager.accounting_management.Entity.ThirdParty;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("sql")
public interface PhysicalRepository extends JpaRepository<Physical, Long> {
    Optional<ThirdParty> findByNic(String nic);

}
