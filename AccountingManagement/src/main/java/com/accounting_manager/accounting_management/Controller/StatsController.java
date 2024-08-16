package com.accounting_manager.accounting_management.Controller;

import com.accounting_manager.accounting_management.DTO.Response.StatsResponse;
import com.accounting_manager.accounting_management.Service.DocumentService;
import com.accounting_manager.accounting_management.Service.FolderService;
import com.accounting_manager.accounting_management.Service.ThirdPartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for managing statistics related to management operations.
 */
@Log4j2
@RestController
@RequestMapping("/api/v1/management/stats")
@CrossOrigin(origins = "*")
@Profile("sql")
public class StatsController {

    private final ThirdPartyService thirdPartyService;
    private final DocumentService documentService;
    private final FolderService folderService;

    public StatsController(ThirdPartyService thirdPartyService, DocumentService documentService, FolderService folderService) {
        this.thirdPartyService = thirdPartyService;
        this.documentService = documentService;
        this.folderService = folderService;
    }

    /**
     * Retrieves overall statistics.
     *
     * @return StatsResponse containing folder, user, and document statistics
     */
    @Operation(summary = "Get Overall Statistics",
            description = "Retrieves overall statistics including folder, user, and document statistics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved overall statistics")
    })
    @GetMapping()
    public StatsResponse getStats(){
        Map<String,Long> folderStats = new HashMap<>();
        folderStats.put("total",folderService.getTotalFolders());
        folderStats.put("open",folderService.getCountByStatus(true));
        folderStats.put("closed",folderService.getCountByStatus(false));

        Map<String,Long> thirdPartyStats = new HashMap<>();
        Long accountantsCount = thirdPartyService.getAccountantsCount();
        Long clientsCount = thirdPartyService.getClientsCount();
        thirdPartyStats.put("total",accountantsCount+clientsCount);
        thirdPartyStats.put("accountants",accountantsCount);
        thirdPartyStats.put("clients",clientsCount);

        Map<String,Object> documentsStats = new HashMap<>();
        documentsStats.put("total",this.documentService.getTotalDocuments());
        documentsStats.put("autoProcessEfficiency",documentService.autoProcessEfficiency());
        documentsStats.put("processType",documentService.getProcessTypeCount());
        documentsStats.put("countByMonthCurrentYear",documentService.getDocumentsCountByMonthCurrentYear());

        return StatsResponse.builder()
                .folderStats(folderStats)
                .userStats(thirdPartyStats)
                .documentStats(documentsStats)
                .build();
    }

    /**
     * Retrieves statistics for a specific user identified by ID.
     *
     * @param id ID of the user (third party) to retrieve statistics for
     * @return StatsResponse containing folder, user, and document statistics specific to the user
     */
    @Operation(summary = "Get User Statistics",
            description = "Retrieves statistics for a specific user identified by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user statistics")
    })
    @GetMapping("/{id}")
    public StatsResponse getUserStats(@PathVariable Long id){
        Map<String,Long> folderStats = new HashMap<>();
        folderStats.put("total",folderService.getTotalFoldersByThirdPartyId(id));

        Map<String,Long> thirdPartyStats = new HashMap<>();
        Long clientsCount = thirdPartyService.getClientsCountByThirdPartyId(id);
        Long accountantsCount = thirdPartyService.getAccountantsCountByThirdPartyId(id);
        thirdPartyStats.put("clients",clientsCount);
        thirdPartyStats.put("accountants",accountantsCount);

        Map<String,Object> documentsStats = new HashMap<>();
        documentsStats.put("total",this.documentService.getTotalDocumentsByThirdPartyId(id));

        return StatsResponse.builder()
                .folderStats(folderStats)
                .userStats(thirdPartyStats)
                .documentStats(documentsStats)
                .build();
    }

}
