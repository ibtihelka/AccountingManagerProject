package com.accounting_manager.accounting_management.Service;

import com.accounting_manager.accounting_management.DTO.Request.FolderForExistingClientRequest;
import com.accounting_manager.accounting_management.DTO.Request.UpdateFolderRequest;
import com.accounting_manager.accounting_management.Entity.Folder;
import com.accounting_manager.accounting_management.Entity.Type;
import com.accounting_manager.accounting_management.Repository.FolderInsertRepository;
import com.accounting_manager.accounting_management.Repository.FolderRepository;
import com.accounting_manager.accounting_management.Repository.TypeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@Profile("sql")
public class FolderService {


    private FolderRepository folderRepository;
    private FolderInsertRepository folderInsertRepository;

    private TypeRepository typeRepository;

    public FolderService(FolderRepository folderRepository,FolderInsertRepository folderInsertRepository, TypeRepository typeRepository) {
        this.folderRepository = folderRepository;
        this.folderInsertRepository = folderInsertRepository;
        this.typeRepository = typeRepository;
    }

    public Page<Folder> getMyFolders(Long idThirdParty, String query, String type, int page, int size) {
        if (size <= 0) {
            size = 5;
        }
        Page<Folder> folders =  folderRepository.findMyFolders(idThirdParty, query, type, PageRequest.of(page, size));
        folders.forEach(folder -> folder.setDocumentsNumber(folder.calculateDocumentsNumber(false)));
        return folders;
    }

    public Page<Folder> getMyClientFolders(Long idThirdParty, Long idClient, String query, int page, int size) {
        if (size <= 0) {
            size = 5;
        }
        Page<Folder> folders = folderRepository.findMyClientFolders(idThirdParty, idClient, query, PageRequest.of(page, size));
        folders.forEach(folder -> folder.setDocumentsNumber(folder.calculateDocumentsNumber(false)));
        return folders;
    }


    public Page<Folder> getMyAccountantFolders(Long idThirdParty, Long idAccountant, String query, int page, int size) {
        if (size <= 0) {
            size = 5;
        }
        Page<Folder> folders = folderRepository.findMyAccountantFolders(idThirdParty, idAccountant, query, PageRequest.of(page, size));
        folders.forEach(folder -> folder.setDocumentsNumber(folder.calculateDocumentsNumber(false)));
        return folders;
    }

    public Page<Folder> getAllFolders(Long idThirdParty, String query, String type, int page, int size) {
        if (size <= 0) {
            size = 5;
        }
        Page<Folder> folders = folderRepository.findFoldersAsAdmin(idThirdParty, query, type, PageRequest.of(page, size));
        folders.forEach(folder -> folder.setDocumentsNumber(folder.calculateDocumentsNumber(true)));
        return folders;
    }

    public Optional<Folder> getFolderById(Long id) {
        return Optional.of(folderRepository.findById(id).get());
    }

    public boolean createFolder(FolderForExistingClientRequest folderForExistingClientRequest) {
        int insertedRows = 0;
        Long typeId;
        typeId= typeRepository.findOneByParentCodeTypeAndCodeType("FOLDER_TYPE",folderForExistingClientRequest.getType()).getIdType();

        try {
            insertedRows = folderInsertRepository.saveFolder(
                    folderForExistingClientRequest.getName(),
                    folderForExistingClientRequest.getDescription(),
                    folderForExistingClientRequest.isStatus(),
                    typeId,
                    folderForExistingClientRequest.getCreatorId(),
                    folderForExistingClientRequest.getClientId()
            );
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        return insertedRows==1?true:false;

    }


    public Optional<Folder> UpdateFolder(Long id, UpdateFolderRequest updatedFolder) {
        if (folderRepository.existsById(id)) {
            Type newType;
            newType= typeRepository.findOneByParentCodeTypeAndCodeType("FOLDER_TYPE",updatedFolder.getType());
            var oldFolder = folderRepository.findById(id).get();
            oldFolder.setName(updatedFolder.getName());
            oldFolder.setStatus(updatedFolder.isStatus());
            oldFolder.setDescription(updatedFolder.getDescription());
            oldFolder.setArchived(updatedFolder.isArchived());
            oldFolder.setType(newType);
            if(updatedFolder.isArchived())
                oldFolder.setFavorite(false);
            else oldFolder.setFavorite(updatedFolder.isFavorite());
            return Optional.of(folderRepository.save(oldFolder));
        }
        return Optional.empty();
    }

    public void markFolderAsDeleted(Long id) {
        if (folderRepository.existsById(id)) {
            folderRepository.markFolderAsDeleted(id);
        }
    }

    public void deleteFolderPermanently(Long id) {
        if (folderRepository.existsById(id)) {
            folderRepository.deleteById(id);
        }
    }

    public Long getTotalFolders(){
        return this.folderRepository.count();
    }

    public Long getTotalFoldersByThirdPartyId(Long id){
        return this.folderRepository.getTotalFoldersByThirdPartyId(id);
    }

    public Long getCountByStatus(boolean status){
        return this.folderRepository.countByStatus(status);
    }
}
