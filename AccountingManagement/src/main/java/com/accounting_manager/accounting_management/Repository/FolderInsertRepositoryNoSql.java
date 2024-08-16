package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.FolderNoSql;
import com.accounting_manager.accounting_management.Entity.ThirdPartyNoSql;
import com.accounting_manager.accounting_management.Entity.TypeNoSql;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@Profile("nosql")
public class FolderInsertRepositoryNoSql {

    @Autowired
    private MongoTemplate mongoTemplate;

    public FolderNoSql saveFolder(
            String name,
            String description,
            boolean status,
            TypeNoSql type,  // Utilisation de TypeNoSql ici
            ThirdPartyNoSql creator,
            ThirdPartyNoSql client
    ){
        try {
            FolderNoSql folder = FolderNoSql.builder()
                    .name(name)
                    .description(description)
                    .status(status)
                    .type(type)
                    .creator(creator)
                    .client(client)
                    .build();

            return mongoTemplate.save(folder);
        } catch (Exception e) {
            log.error("Error saving folder: ", e);
            return null;
        }
    }
}
