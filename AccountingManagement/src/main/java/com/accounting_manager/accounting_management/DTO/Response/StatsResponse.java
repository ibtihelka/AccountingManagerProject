package com.accounting_manager.accounting_management.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Profile("sql")
public class StatsResponse {
    
    private Map<String,Long> userStats;
    private Map<String,Long> folderStats;
    private Map<String,Object> documentStats;

}
