package com.accounting_manager.accounting_auth.Repository;

import com.accounting_manager.accounting_auth.Entity.Physical;
import com.accounting_manager.accounting_auth.Entity.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhysicalRepository extends JpaRepository<Physical, Long> {
    Optional<ThirdParty> findByNic(String nic);

}
