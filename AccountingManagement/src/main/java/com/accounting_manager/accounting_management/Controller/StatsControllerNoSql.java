package com.accounting_manager.accounting_management.Controller;

import com.accounting_manager.accounting_management.DTO.Response.StatsResponse;
import com.accounting_manager.accounting_management.Service.DocumentServiceNoSql;
import com.accounting_manager.accounting_management.Service.FolderServiceNoSql;
import com.accounting_manager.accounting_management.Service.ThirdPartyServiceNoSql;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for managing statistics related to management operations in NoSQL environment.
 */
@Log4j2
@RestController
@RequestMapping("/api/v1/management/stats")
@CrossOrigin(origins = "*")
@Profile("nosql")
public class StatsControllerNoSql {

    private final ThirdPartyServiceNoSql thirdPartyServiceNoSql;
    private final DocumentServiceNoSql documentServiceNoSql;
    private final FolderServiceNoSql folderServiceNoSql;

    public StatsControllerNoSql(ThirdPartyServiceNoSql thirdPartyServiceNoSql,
                                DocumentServiceNoSql documentServiceNoSql,
                                FolderServiceNoSql folderServiceNoSql) {
        this.thirdPartyServiceNoSql = thirdPartyServiceNoSql;
        this.documentServiceNoSql = documentServiceNoSql;
        this.folderServiceNoSql = folderServiceNoSql;
    }


    /**
     * Retrieves overall statistics.
     *
     * @return StatsResponse containing folder, user, and document statistics
     */
    /*
    @Operation(summary = "Get Overall Statistics",
            description = "Retrieves overall statistics including folder, user, and document statistics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved overall statistics")
    })

    @GetMapping()
    public StatsResponse getStats(){
        Map<String,Long> folderStats = new HashMap<>();
        folderStats.put("total",folderServiceNoSql.getTotalFolders());
        folderStats.put("open",folderServiceNoSql.getCountByStatus(true));
        folderStats.put("closed",folderServiceNoSql.getCountByStatus(false));

        Map<String,Long> thirdPartyStats = new HashMap<>();
        Long accountantsCount = thirdPartyServiceNoSql.getAccountantsCount();
        Long clientsCount = thirdPartyServiceNoSql.getClientsCount();
        thirdPartyStats.put("total",accountantsCount + clientsCount);
        thirdPartyStats.put("accountants",accountantsCount);
        thirdPartyStats.put("clients",clientsCount);

        Map<String,Object> documentsStats = new HashMap<>();
        documentsStats.put("total",this.documentServiceNoSql.getTotalDocuments());
        documentsStats.put("autoProcessEfficiency",documentServiceNoSql.autoProcessEfficiency());
        documentsStats.put("processType",documentServiceNoSql.getProcessTypeCount());
        documentsStats.put("countByMonthCurrentYear",documentServiceNoSql.getDocumentsCountByMonthCurrentYear());

        return StatsResponse.builder()
                .folderStats(folderStats)
                .userStats(thirdPartyStats)
                .documentStats(documentsStats)
                .build();
    }

     */

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
    public StatsResponse getUserStats(@PathVariable String id){
        Map<String,Long> folderStats = new HashMap<>();
        folderStats.put("total",folderServiceNoSql.getTotalFoldersByThirdPartyId(id));

        Map<String,Long> thirdPartyStats = new HashMap<>();
        Long clientsCount = thirdPartyServiceNoSql.getClientsCountByThirdPartyId(id);
        Long accountantsCount = thirdPartyServiceNoSql.getAccountantsCountByThirdPartyId(id);
        thirdPartyStats.put("clients",clientsCount);
        thirdPartyStats.put("accountants",accountantsCount);

        Map<String,Object> documentsStats = new HashMap<>();
        documentsStats.put("total",this.documentServiceNoSql.getTotalDocumentsByThirdPartyId(id));

        return StatsResponse.builder()
                .folderStats(folderStats)
                .userStats(thirdPartyStats)
                .documentStats(documentsStats)
                .build();
    }
}
