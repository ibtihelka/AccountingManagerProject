package com.accounting_manager.accounting_management.Controller;

import com.accounting_manager.accounting_management.Entity.*;
import com.accounting_manager.accounting_management.Service.DocumentServiceNoSql;
import com.accounting_manager.accounting_management.Service.FolderServiceNoSql;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Controller for managing documents within folders using NoSQL (MongoDB).
 */
@RestController
@RequestMapping("/api/v1/management")
@CrossOrigin(origins = "*")
@Log4j2
@Profile("nosql")
public class DocumentControllerNoSql {

    private final DocumentServiceNoSql documentService;
    private final FolderServiceNoSql folderService;

    public DocumentControllerNoSql(DocumentServiceNoSql documentService, FolderServiceNoSql folderService) {
        this.documentService = documentService;
        this.folderService = folderService;
    }

    /**
     * Retrieves all documents within a specific folder.
     *
     * @param folderId ID of the folder containing the documents
     * @param query    optional query parameter for filtering documents
     * @param page     page number for pagination (default: 0)
     * @param size     page size for pagination (default: 8)
     * @param token    authorization token
     * @return ResponseEntity with a page of documents
     */
    @Operation(summary = "Get All Documents",
            description = "Retrieves all documents within a specific folder.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved documents",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/{folderId}/documents")
    public ResponseEntity<Page<DocumentNoSql>> getAllDocuments(
            @PathVariable String folderId,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "8") int size,
            @RequestHeader(name = "Authorization") String token) {
        Page<DocumentNoSql> documents = documentService.getDocuments(token, folderId, query, page, size);
        return ResponseEntity.ok(documents);
    }

    /**
     * Retrieves all documents for administrative purposes.
     *
     * @param folder optional folder ID filter
     * @param query  optional query parameter for filtering documents
     * @param type   optional type filter
     * @param page   page number for pagination (default: 0)
     * @param size   page size for pagination (default: 8)
     * @return ResponseEntity with a page of documents
     */
    @Operation(summary = "Get All Documents for Admin",
            description = "Retrieves all documents for administrative purposes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved documents",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/documents/admin")
    public ResponseEntity<Page<DocumentNoSql>> getAllDocumentsForAdmin(
            @RequestParam(defaultValue = "") String folder,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        Page<DocumentNoSql> documents = documentService.getDocumentsForAdmin(folder, query, type, page, size);
        return ResponseEntity.ok(documents);
    }

    /**
     * Retrieves a document by its ID.
     *
     * @param id ID of the document to retrieve
     * @return ResponseEntity with the retrieved document or 404 if not found
     */
    @Operation(summary = "Get Document by ID",
            description = "Retrieves a document by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved document",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DocumentNoSql.class))),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @GetMapping("/documents/{id}")
    public ResponseEntity<DocumentNoSql> getDocumentById(@PathVariable String id) {
        Optional<DocumentNoSql> documentOptional = documentService.getDocumentById(id);
        return documentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates new documents in a specified folder.
     *
     * @param folderId ID of the folder where documents will be created
     * @param files    array of files to upload
     * @return ResponseEntity with a map containing upload status
     */
    @Operation(summary = "Create Documents",
            description = "Creates new documents in a specified folder.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documents created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "409", description = "Conflict - No files uploaded, all documents already exists in the current folder"),
            @ApiResponse(responseCode = "207", description = "Multi-Status - Partially successful uploads, some documents already exists in the current folder"),
    })
    @PostMapping(value = "/{folderId}/documents", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> createDocument(
            @PathVariable String folderId,
            @RequestParam("files") MultipartFile[] files) {

        Optional<FolderNoSql> folderOptional = folderService.getFolderById(folderId);
        if (folderOptional.isPresent()) {
            FolderNoSql folder = folderOptional.get();
            Map<String, Object> response = documentService.createDocuments(folderId, files, folder.getType().getCodeType());
            int successfulUploads = Integer.parseInt(response.get("successfullySaved").toString());
            if (successfulUploads == 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else if (successfulUploads == files.length) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates an invoice for a specific document ID.
     *
     * @param id              ID of the document/invoice to update
     * @param updatedDocument updated invoice details
     * @return ResponseEntity with the updated document or 404 if not found
     */



    /*
    @Operation(summary = "Update Invoice",
            description = "Updates an invoice for a specific document ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DocumentNoSql.class))),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @PutMapping("documents/{id}/invoices")
    public ResponseEntity<DocumentNoSql> updateInvoice(@PathVariable String id, @RequestBody Invoice updatedDocument) {
        Optional<DocumentNoSql> updated = documentService.updateInvoice(id, updatedDocument);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

     */

    /**
     * Updates a bank statement for a specific document ID.
     *
     * @param id              ID of the document/bank statement to update
     * @param updatedDocument updated bank statement details
     * @return ResponseEntity with the updated document or 404 if not found
     */



    /*
    @Operation(summary = "Update Bank Statement",
            description = "Updates a bank statement for a specific document ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank statement updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DocumentNoSql.class))),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @PutMapping("documents/{id}/bank-statements")
    public ResponseEntity<DocumentNoSql> updateBankStatement(@PathVariable String id, @RequestBody BankStatement updatedDocument) {
        Optional<DocumentNoSql> updated = documentService.updateBankStatement(id, updatedDocument);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

     */

    /**
     * Updates multiple invoices.
     *
     * @param updatedDocuments set of updated invoices
     * @return ResponseEntity indicating success
     */


    /*
    @Operation(summary = "Update Invoices",
            description = "Updates multiple invoices.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoices updated successfully")
    })
    @PutMapping("documents/invoices")
    public ResponseEntity<Void> updateInvoices(@RequestBody Set<Invoice> updatedDocuments) {
        updatedDocuments.forEach(invoice -> documentService.updateInvoice(invoice.getId(), invoice));
        return ResponseEntity.ok().build();
    }

     */

    /**
     * Updates multiple bank statements.
     *
     * @param updatedDocuments set of updated bank statements
     * @return ResponseEntity indicating success
     */

    /*
    @Operation(summary = "Update Bank Statements",
            description = "Updates multiple bank statements.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank statements updated successfully")
    })
    @PutMapping("documents/bank-statements")
    public ResponseEntity<Void> updateBankStatements(@RequestBody Set<BankStatement> updatedDocuments) {
        updatedDocuments.forEach(bankStatement -> documentService.updateBankStatement(bankStatement.getId(), bankStatement));
        return ResponseEntity.ok().build();
    }


     */

    /**
     * Marks a document as deleted.
     *
     * @param id ID of the document to mark as deleted
     * @return ResponseEntity indicating success
     */
    @Operation(summary = "Delete Document",
            description = "Marks a document as deleted.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document marked as deleted successfully")
    })
    @DeleteMapping("documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        documentService.markDocumentAsDeleted(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Permanently deletes a document.
     *
     * @param id ID of the document to permanently delete
     * @return ResponseEntity indicating success
     */
    @Operation(summary = "Delete Document Permanently",
            description = "Permanently deletes a document.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document permanently deleted successfully")
    })
    @DeleteMapping("documents/{id}/permanent")
    public ResponseEntity<Void> deleteDocumentPermanently(@PathVariable String id) {
        documentService.deleteDocumentPermanently(id);
        return ResponseEntity.ok().build();
    }
}
