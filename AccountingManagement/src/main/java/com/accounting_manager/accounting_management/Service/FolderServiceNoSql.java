package com.accounting_manager.accounting_management.Service;

import com.accounting_manager.accounting_management.DTO.Request.FolderForExistingClientRequest;
import com.accounting_manager.accounting_management.DTO.Request.UpdateFolderRequest;
import com.accounting_manager.accounting_management.Entity.FolderNoSql;
import com.accounting_manager.accounting_management.Entity.TypeNoSql;
import com.accounting_manager.accounting_management.Entity.ThirdPartyNoSql;
import com.accounting_manager.accounting_management.Repository.FolderInsertRepositoryNoSql;
import com.accounting_manager.accounting_management.Repository.FolderRepositoryNoSql;
import com.accounting_manager.accounting_management.Repository.TypeRepositoryNoSql;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@Service
@Profile("nosql")
public class FolderServiceNoSql {

    private FolderRepositoryNoSql folderRepositoryNoSql;
    private FolderInsertRepositoryNoSql folderInsertRepositoryNoSql;
    private TypeRepositoryNoSql typeRepositoryNoSql;

    public FolderServiceNoSql(FolderRepositoryNoSql folderRepositoryNoSql, FolderInsertRepositoryNoSql folderInsertRepositoryNoSql, TypeRepositoryNoSql typeRepositoryNoSql) {
        this.folderRepositoryNoSql = folderRepositoryNoSql;
        this.folderInsertRepositoryNoSql = folderInsertRepositoryNoSql;
        this.typeRepositoryNoSql = typeRepositoryNoSql;
    }

    public Page<FolderNoSql> getMyFolders(String idThirdParty, String query, String type, int page, int size) {
        if (size <= 0) {
            size = 5;
        }
        Page<FolderNoSql> folders = folderRepositoryNoSql.findMyFolders(idThirdParty, query, type, PageRequest.of(page, size));
        folders.forEach(folder -> folder.setDocumentsNumber(folder.calculateDocumentsNumber(false)));
        return folders;
    }

    public Page<FolderNoSql> getMyClientFolders(String idThirdParty, String idClient, String query, int page, int size) {
        if (size <= 0) {
            size = 5;
        }
        Page<FolderNoSql> folders = folderRepositoryNoSql.findMyClientFolders(idThirdParty, idClient, query, PageRequest.of(page, size));
        folders.forEach(folder -> folder.setDocumentsNumber(folder.calculateDocumentsNumber(false)));
        return folders;
    }

    public Page<FolderNoSql> getMyAccountantFolders(String idThirdParty, String idAccountant, String query, int page, int size) {
        if (size <= 0) {
            size = 5;
        }
        Page<FolderNoSql> folders = folderRepositoryNoSql.findMyAccountantFolders(idThirdParty, idAccountant, query, PageRequest.of(page, size));
        folders.forEach(folder -> folder.setDocumentsNumber(folder.calculateDocumentsNumber(false)));
        return folders;
    }

    public Page<FolderNoSql> getAllFolders(String idThirdParty, String query, String type, int page, int size) {
        if (size <= 0) {
            size = 5;
        }
        Page<FolderNoSql> folders = folderRepositoryNoSql.findFoldersAsAdmin(idThirdParty, query, type, PageRequest.of(page, size));
        folders.forEach(folder -> folder.setDocumentsNumber(folder.calculateDocumentsNumber(true)));
        return folders;
    }

    public Optional<FolderNoSql> getFolderById(String id) {
        return folderRepositoryNoSql.findById(id);
    }

    public boolean createFolder(FolderForExistingClientRequest folderForExistingClientRequest) {
        int insertedRows = 0;
        try {
            TypeNoSql typeNoSql = typeRepositoryNoSql.findOneByParentCodeTypeAndCodeType("FOLDER_TYPE", folderForExistingClientRequest.getType());
            ThirdPartyNoSql creator = new ThirdPartyNoSql(); // Remplacez par la logique d'obtention du créateur
            ThirdPartyNoSql client = new ThirdPartyNoSql(); // Remplacez par la logique d'obtention du client

            FolderNoSql folder = FolderNoSql.builder()
                    .name(folderForExistingClientRequest.getName())
                    .description(folderForExistingClientRequest.getDescription())
                    .status(folderForExistingClientRequest.isStatus())
                    .type(typeNoSql)
                    .creator(creator)
                    .client(client)
                    .build();

            folderInsertRepositoryNoSql.saveFolder(folder.getName(), folder.getDescription(), folder.isStatus(), folder.getType(), folder.getCreator(), folder.getClient());
            insertedRows = 1; // Assurez-vous que l'insertion est réussie
        } catch (Exception e) {
            log.error("Error creating folder: ", e);
        }
        return insertedRows == 1;
    }

    public Optional<FolderNoSql> updateFolder(String id, UpdateFolderRequest updatedFolder) {
        if (folderRepositoryNoSql.existsById(id)) {
            TypeNoSql newType = typeRepositoryNoSql.findOneByParentCodeTypeAndCodeType("FOLDER_TYPE", updatedFolder.getType());
            Optional<FolderNoSql> oldFolderOpt = folderRepositoryNoSql.findById(id);
            if (oldFolderOpt.isPresent()) {
                FolderNoSql oldFolder = oldFolderOpt.get();
                oldFolder.setName(updatedFolder.getName());
                oldFolder.setStatus(updatedFolder.isStatus());
                oldFolder.setDescription(updatedFolder.getDescription());
                oldFolder.setArchived(updatedFolder.isArchived());
                oldFolder.setType(newType);
                if (updatedFolder.isArchived()) {
                    oldFolder.setFavorite(false);
                } else {
                    oldFolder.setFavorite(updatedFolder.isFavorite());
                }
                return Optional.of(folderRepositoryNoSql.save(oldFolder));
            }
        }
        return Optional.empty();
    }

    public void markFolderAsDeleted(String id) {
        if (folderRepositoryNoSql.existsById(id)) {
            folderRepositoryNoSql.markFolderAsDeleted(id);
        }
    }

    public void deleteFolderPermanently(String id) {
        if (folderRepositoryNoSql.existsById(id)) {
            folderRepositoryNoSql.deleteById(id);
        }
    }

    public Long getTotalFolders() {
        return folderRepositoryNoSql.count();
    }

    public Long getTotalFoldersByThirdPartyId(String idThirdParty) {
        return folderRepositoryNoSql.getTotalFoldersByThirdPartyId(idThirdParty);
    }

    public Long getCountByStatus(boolean status) {
        return folderRepositoryNoSql.countByStatus(status);
    }

    public FolderNoSql addFolder(FolderNoSql folder) {
        folder.setCreationDate(LocalDateTime.now());
        folder.setFavorite(false); // Set default values for other fields if necessary
        folder.setArchived(false);
        folder.setDeleted(false);
        return folderRepositoryNoSql.save(folder);
    }

}
