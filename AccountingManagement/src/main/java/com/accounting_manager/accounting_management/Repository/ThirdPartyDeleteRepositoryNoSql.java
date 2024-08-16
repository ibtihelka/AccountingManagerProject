
package com.accounting_manager.accounting_management.Repository;
import com.accounting_manager.accounting_management.Entity.FolderNoSql;
import com.accounting_manager.accounting_management.Entity.InvoiceNoSql;
import com.accounting_manager.accounting_management.Repository.FolderRepositoryNoSql;
import com.accounting_manager.accounting_management.Repository.InvoiceRepositoryNoSql;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Profile("nosql")
public class ThirdPartyDeleteRepositoryNoSql {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FolderRepositoryNoSql folderRepositoryNoSql;

    @Autowired
    private InvoiceRepositoryNoSql invoiceRepositoryNoSql;

    public void deleteAccountantClient(String accountantId, String clientId) {
        try {
            // Delete the accountant-client relationship
            // This might be managed elsewhere if it’s a separate collection or relationship

            // Update the Folder records
            Query folderQuery = new Query(Criteria.where("creator.$id").is(accountantId)
                    .and("client.$id").is(clientId)
                    .and("deleted").is(false));
            Update folderUpdate = new Update().set("deleted", true);
            mongoTemplate.updateMulti(folderQuery, folderUpdate, FolderNoSql.class);

            // Update the Invoice records
            Query invoiceQuery = new Query(Criteria.where("folder.$id").in(
                            mongoTemplate.find(folderQuery, FolderNoSql.class).stream()
                                    .map(FolderNoSql::getIdFolder)
                                    .toArray())
                    .and("deleted").is(false));
            Update invoiceUpdate = new Update().set("deleted", true);
            mongoTemplate.updateMulti(invoiceQuery, invoiceUpdate, InvoiceNoSql.class);
        } catch (Exception e) {
            log.error("Error deleting accountant-client records: ", e);
        }
    }
}
