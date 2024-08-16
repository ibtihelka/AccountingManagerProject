package com.accounting_manager.accounting_management.Controller;

import com.accounting_manager.accounting_management.DTO.Request.FolderForExistingClientRequest;
import com.accounting_manager.accounting_management.DTO.Request.UpdateFolderRequest;
import com.accounting_manager.accounting_management.DTO.Response.ResponseDTO;
import com.accounting_manager.accounting_management.Entity.FolderNoSql;
import com.accounting_manager.accounting_management.Service.FolderServiceNoSql;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/v1/management/folders")
@CrossOrigin(origins = "*")
@Profile("nosql")
public class FolderControllerNoSql {

    private final FolderServiceNoSql folderServiceNoSql;

    public FolderControllerNoSql(FolderServiceNoSql folderServiceNoSql) {
        this.folderServiceNoSql = folderServiceNoSql;
    }

   @Operation(summary = "Get My Folders",
            description = "Retrieves folders belonging to a specific third party.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folders",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<FolderNoSql>> getMyFolders(
            @RequestParam String idThirdParty,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        Page<FolderNoSql> folders = folderServiceNoSql.getMyFolders(idThirdParty, query, type, page, size);
        return ResponseEntity.ok(folders);
    }

    @Operation(summary = "Get My Client's Folders",
            description = "Retrieves folders belonging to a specific client of a third party.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folders",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("my-client")
    public ResponseEntity<Page<FolderNoSql>> getMyClientFolders(
            @RequestParam String idThirdParty,
            @RequestParam String idClient,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        Page<FolderNoSql> folders = folderServiceNoSql.getMyClientFolders(idThirdParty, idClient, query, page, size);
        return ResponseEntity.ok(folders);
    }

    @Operation(summary = "Get My Accountant's Folders",
            description = "Retrieves folders belonging to a specific accountant of a third party.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folders",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("my-accountant")
    public ResponseEntity<Page<FolderNoSql>> getMyAccountantFolders(
            @RequestParam String idThirdParty,
            @RequestParam String idAccountant,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        Page<FolderNoSql> folders = folderServiceNoSql.getMyAccountantFolders(idThirdParty, idAccountant, query, page, size);
        return ResponseEntity.ok(folders);
    }

    @Operation(summary = "Get All Folders for Admin",
            description = "Retrieves all folders for administrative purposes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folders",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/admin")
    public ResponseEntity<Page<FolderNoSql>> getAllFolders(
            @RequestParam(defaultValue = "") String creator,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        Page<FolderNoSql> folders = folderServiceNoSql.getAllFolders(creator, query, type, page, size);
        return ResponseEntity.ok(folders);
    }

    @Operation(summary = "Get Folder by ID",
            description = "Retrieves a folder by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folder",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FolderNoSql.class))),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FolderNoSql> getFolderById(@PathVariable String id) {
        Optional<FolderNoSql> folderOptional = folderServiceNoSql.getFolderById(id);
        return folderOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create Folder",
            description = "Creates a new folder.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - An error occurred while creating the folder")
    })


    @PostMapping
    public ResponseEntity<ResponseDTO> createFolder(@RequestBody FolderForExistingClientRequest folder) {
        if (folderServiceNoSql.createFolder(folder)) {
            return ResponseEntity.ok().body(new ResponseDTO("Folder created successfully"));
        } else {
            return ResponseEntity.badRequest().body(new ResponseDTO("An error occurred! Folder is not created"));
        }
    }

    @Operation(summary = "Update Folder",
            description = "Updates a folder by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FolderNoSql.class))),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FolderNoSql> updateFolder(@PathVariable String id, @RequestBody UpdateFolderRequest updateFolderRequest) {
        Optional<FolderNoSql> updated = folderServiceNoSql.updateFolder(id, updateFolderRequest);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete Folder",
            description = "Marks a folder as deleted.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder marked as deleted successfully")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable String id) {
        folderServiceNoSql.markFolderAsDeleted(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete Folder Permanently",
            description = "Permanently deletes a folder.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder permanently deleted successfully")
    })
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> markFolderAsDeleted(@PathVariable String id) {
        folderServiceNoSql.deleteFolderPermanently(id);
        return ResponseEntity.ok().build();
    }




    @PostMapping("/add")
    public ResponseEntity<FolderNoSql> addFolder(@RequestBody FolderNoSql folder) {
        FolderNoSql createdFolder = folderServiceNoSql.addFolder(folder);
        return new ResponseEntity<>(createdFolder, HttpStatus.CREATED);
    }
}


