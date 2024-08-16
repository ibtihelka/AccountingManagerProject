package com.accounting_manager.accounting_management.Controller;

import com.accounting_manager.accounting_management.DTO.Request.FolderForExistingClientRequest;
import com.accounting_manager.accounting_management.DTO.Request.UpdateFolderRequest;
import com.accounting_manager.accounting_management.DTO.Response.ResponseDTO;
import com.accounting_manager.accounting_management.Entity.Folder;
import com.accounting_manager.accounting_management.Service.FolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for managing folders.
 */
@Log4j2
@RestController
@RequestMapping("/api/v1/management/folders")
@CrossOrigin(origins = "*")
@Profile("sql")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    /**
     * Retrieves folders belonging to a specific third party.
     *
     * @param idThirdParty ID of the third party
     * @param query        optional query parameter for filtering folders
     * @param type         optional type filter
     * @param page         page number for pagination (default: 0)
     * @param size         page size for pagination (default: 8)
     * @return ResponseEntity with a page of folders
     */
    @Operation(summary = "Get My Folders",
            description = "Retrieves folders belonging to a specific third party.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folders",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<Folder>> getMyFolders(
            @RequestParam Long idThirdParty,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        Page<Folder> folders = folderService.getMyFolders(idThirdParty, query, type, page, size);
        return ResponseEntity.ok(folders);
    }

    /**
     * Retrieves folders belonging to a specific client of a third party.
     *
     * @param idThirdParty ID of the third party
     * @param idClient     ID of the client
     * @param query        optional query parameter for filtering folders
     * @param page         page number for pagination (default: 0)
     * @param size         page size for pagination (default: 8)
     * @return ResponseEntity with a page of folders
     */
    @Operation(summary = "Get My Client's Folders",
            description = "Retrieves folders belonging to a specific client of a third party.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folders",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("my-client")
    public ResponseEntity<Page<Folder>> getMyClientFolders(
            @RequestParam Long idThirdParty,
            @RequestParam Long idClient,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        Page<Folder> folders = folderService.getMyClientFolders(idThirdParty, idClient, query, page, size);
        return ResponseEntity.ok(folders);
    }

    /**
     * Retrieves folders belonging to a specific accountant of a third party.
     *
     * @param idThirdParty  ID of the third party
     * @param idAccountant ID of the accountant
     * @param query         optional query parameter for filtering folders
     * @param page          page number for pagination (default: 0)
     * @param size          page size for pagination (default: 8)
     * @return ResponseEntity with a page of folders
     */
    @Operation(summary = "Get My Accountant's Folders",
            description = "Retrieves folders belonging to a specific accountant of a third party.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folders",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("my-accountant")
    public ResponseEntity<Page<Folder>> getMyAccountantFolders(
            @RequestParam Long idThirdParty,
            @RequestParam Long idAccountant,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        Page<Folder> folders = folderService.getMyAccountantFolders(idThirdParty, idAccountant, query, page, size);
        return ResponseEntity.ok(folders);
    }

    /**
     * Retrieves all folders for administrative purposes.
     *
     * @param creator optional filter by creator ID
     * @param query   optional query parameter for filtering folders
     * @param type    optional type filter
     * @param page    page number for pagination (default: 0)
     * @param size    page size for pagination (default: 8)
     * @return ResponseEntity with a page of folders
     */
    @Operation(summary = "Get All Folders for Admin",
            description = "Retrieves all folders for administrative purposes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folders",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/admin")
    public ResponseEntity<Page<Folder>> getAllFolders(
            @RequestParam(defaultValue = "") Long creator,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        Page<Folder> folders = folderService.getAllFolders(creator, query, type, page, size);
        return ResponseEntity.ok(folders);
    }

    /**
     * Retrieves a folder by its ID.
     *
     * @param id ID of the folder to retrieve
     * @return ResponseEntity with the retrieved folder or 404 if not found
     */
    @Operation(summary = "Get Folder by ID",
            description = "Retrieves a folder by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved folder",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Folder.class))),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Folder> getFolderById(@PathVariable Long id) {
        Optional<Folder> folderOptional = folderService.getFolderById(id);
        return folderOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new folder.
     *
     * @param folder request body containing folder details
     * @return ResponseEntity with a response message
     */
    @Operation(summary = "Create Folder",
            description = "Creates a new folder.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - An error occurred while creating the folder")
    })
    @PostMapping
    public ResponseEntity<ResponseDTO> createFolder(@RequestBody FolderForExistingClientRequest folder) {
        if (folderService.createFolder(folder)) {
            return ResponseEntity.ok().body(new ResponseDTO("Folder created successfully"));
        } else {
            return ResponseEntity.badRequest().body(new ResponseDTO("An error occurred! Folder is not created"));
        }
    }

    /**
     * Updates a folder by its ID.
     *
     * @param id                  ID of the folder to update
     * @param updateFolderRequest request body containing updated folder details
     * @return ResponseEntity with the updated folder or 404 if not found
     */
    @Operation(summary = "Update Folder",
            description = "Updates a folder by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Folder.class))),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Folder> updateFolder(@PathVariable Long id, @RequestBody UpdateFolderRequest updateFolderRequest) {
        Optional<Folder> updated = folderService.UpdateFolder(id, updateFolderRequest);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Marks a folder as deleted.
     *
     * @param id ID of the folder to mark as deleted
     * @return ResponseEntity indicating success
     */
    @Operation(summary = "Delete Folder",
            description = "Marks a folder as deleted.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder marked as deleted successfully")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long id) {
        folderService.markFolderAsDeleted(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Permanently deletes a folder.
     *
     * @param id ID of the folder to permanently delete
     * @return ResponseEntity indicating success
     */
    @Operation(summary = "Delete Folder Permanently",
            description = "Permanently deletes a folder.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder permanently deleted successfully")
    })
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> markFolderAsDeleted(@PathVariable Long id) {
        folderService.deleteFolderPermanently(id);
        return ResponseEntity.ok().build();
    }

}
