package com.accounting_manager.accounting_management.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@Profile("sql")
public class ThirdPartyDeleteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void deleteAccountantClient(Long accountantId, Long clientId) {
        try {
            // Delete the accountant_client record
            entityManager.createNativeQuery("DELETE FROM accountant_client " +
                            "WHERE accountant_id = :accountantId " +
                            "AND client_id = :clientId")
                    .setParameter("accountantId", accountantId)
                    .setParameter("clientId", clientId)
                    .executeUpdate();

            // Update the Folder records
            entityManager.createNativeQuery("UPDATE Folder f " +
                            "SET f.deleted = true " +
                            "WHERE f.fk_creator = :accountantId " +
                            "AND f.fk_client = :clientId")
                    .setParameter("accountantId", accountantId)
                    .setParameter("clientId", clientId)
                    .executeUpdate();

            // Update the Invoice records
            entityManager.createNativeQuery("UPDATE Invoice i " +
                            "JOIN Folder f on f.id_folder = i.fk_folder " +
                            "SET i.deleted = true " +
                            "WHERE f.fk_creator = :accountantId " +
                            "AND f.fk_client = :clientId")
                    .setParameter("accountantId", accountantId)
                    .setParameter("clientId", clientId)
                    .executeUpdate();
        }  catch (Exception e){
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error(e.getMessage());
        }
    }
}
