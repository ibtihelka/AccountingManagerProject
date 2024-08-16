package com.accounting_manager.accounting_management.Controller;

import com.accounting_manager.accounting_management.DTO.Response.ResponseDTO;
import com.accounting_manager.accounting_management.DTO.Request.ThirdPartyRequest;
import com.accounting_manager.accounting_management.Entity.ThirdParty;
import com.accounting_manager.accounting_management.Service.ThirdPartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for managing third parties related to management operations.
 */
@RestController
@RequestMapping("/api/v1/management/thirdparty")
@CrossOrigin(origins = "*")
@Profile("sql")
public class ThirdPartyController {

    private ThirdPartyService thirdPartyService;

    public ThirdPartyController(ThirdPartyService thirdPartyService) {
        this.thirdPartyService = thirdPartyService;
    }

    /**
     * Retrieves all third parties optionally filtered by query and discriminator.
     *
     * @param query        Optional query parameter
     * @param discriminator Optional discriminator parameter
     * @param page         Page number
     * @param size         Page size
     * @return ResponseEntity containing a page of third parties
     */
    @Operation(summary = "Get All Third Parties",
            description = "Retrieves a list of all third parties optionally filtered by query and discriminator.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of third parties")
    })
    @GetMapping
    public ResponseEntity<Page<ThirdParty>> getAllThirdParties(@RequestParam(required = false, defaultValue = "") String query,
                                                               @RequestParam(required = false, defaultValue = "") String discriminator,
                                                               @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                               @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        Page<ThirdParty> thirdParties = thirdPartyService.getAllThirdParties(query, discriminator.toLowerCase(), page, size);
        return ResponseEntity.ok(thirdParties);
    }

    /**
     * Retrieves a third party by ID.
     *
     * @param id ID of the third party to retrieve
     * @return ResponseEntity containing the retrieved third party
     */
    @Operation(summary = "Get Third Party by ID",
            description = "Retrieves a third party by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved third party"),
            @ApiResponse(responseCode = "404", description = "Third party not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ThirdParty> getThirdPartyById(@PathVariable Long id) {
        Optional<ThirdParty> thirdPartyOptional = thirdPartyService.getThirdPartyById(id);

        if (thirdPartyOptional.isPresent()) {
            return ResponseEntity.ok(thirdPartyOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves clients of an accountant third party by ID.
     *
     * @param id    ID of the accountant third party
     * @param query Optional query parameter
     * @param page  Page number
     * @param size  Page size
     * @return ResponseEntity containing a page of client third parties
     */
    @Operation(summary = "Get Accountant Clients",
            description = "Retrieves clients of an accountant third party by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved clients of accountant third party"),
            @ApiResponse(responseCode = "404", description = "Accountant third party not found")
    })
    @GetMapping("/{id}/clients")
    public ResponseEntity<Page<ThirdParty>> getAccountantClients(@PathVariable Long id,
                                                                 @RequestParam(name = "query", required = false, defaultValue = "") String query,
                                                                 @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                 @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        try {
            Page<ThirdParty> thirdParties = thirdPartyService.getAccountantClients(id, query, page, size);
            return ResponseEntity.ok(thirdParties);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves accountants of a client third party by ID.
     *
     * @param id    ID of the client third party
     * @param query Optional query parameter
     * @param page  Page number
     * @param size  Page size
     * @return ResponseEntity containing a page of accountant third parties
     */
    @Operation(summary = "Get Client Accountants",
            description = "Retrieves accountants of a client third party by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved accountants of client third party"),
            @ApiResponse(responseCode = "404", description = "Client third party not found")
    })
    @GetMapping("/{id}/accountants")
    public ResponseEntity<Page<ThirdParty>> getClientAccountants(@PathVariable Long id,
                                                                 @RequestParam(name = "query", required = false, defaultValue = "") String query,
                                                                 @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                 @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        try {
            Page<ThirdParty> thirdParties = thirdPartyService.getClientAccountants(id, query, page, size);
            return ResponseEntity.ok(thirdParties);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a client for an accountant third party by ID.
     *
     * @param id      ID of the accountant third party
     * @param request Request body containing details of the client to create
     * @return ResponseEntity containing the created client third party
     */
    @Operation(summary = "Create Client for Accountant",
            description = "Creates a client for an accountant third party by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/{id}")
    public ResponseEntity<?> createClientForAccountant(
            @PathVariable Long id,
            @RequestBody ThirdPartyRequest request
    ) {
        try {
            ThirdParty response = thirdPartyService.createThirdParty(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage()));
        }
    }

    /**
     * Deletes a third party by ID as an admin.
     *
     * @param id ID of the third party to delete
     * @return ResponseEntity indicating success
     */
    @Operation(summary = "Delete Third Party as Admin",
            description = "Deletes a third party by its ID as an admin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Third party deleted successfully")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThirdPartyAsAdmin(@PathVariable Long id) {
        thirdPartyService.deleteThirdPartyAsAdmin(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a third party as an accountant.
     *
     * @param accountantId ID of the accountant third party
     * @param clientId     ID of the client third party
     * @return ResponseEntity indicating success
     */
    @Operation(summary = "Delete Third Party as Accountant",
            description = "Deletes a third party as an accountant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Third party deleted successfully")
    })
    @DeleteMapping()
    public ResponseEntity<Void> deleteThirdPartyAsAAccountant(@RequestParam Long accountantId, @RequestParam Long clientId) {
        thirdPartyService.deleteThirdPartyAsAccountant(accountantId, clientId);
        return ResponseEntity.ok().build();
    }

}
