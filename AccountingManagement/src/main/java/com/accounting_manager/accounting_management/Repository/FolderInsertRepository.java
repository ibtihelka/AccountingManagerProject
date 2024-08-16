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
public class FolderInsertRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int saveFolder(
            String name,
            String description,
            boolean status,
            Long type,
            Long creator,
            Long client
    ){
        try {
            return entityManager.createNativeQuery("INSERT INTO folder (name, description, status, fk_type, fk_creator, fk_client) VALUES (?,?,?,?,?,?)")
                    .setParameter(1, name)
                    .setParameter(2, description)
                    .setParameter(3, status)
                    .setParameter(4, type)
                    .setParameter(5, creator)
                    .setParameter(6, client)
                    .executeUpdate();
        }
        catch (Exception e){
            log.error(e.getMessage());
            return 0;
        }

    };


}
