package com.accounting_manager.accounting_management.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Profile("sql")
public class FolderForNewClientRequest {

    private String name;
    private String description;
    private boolean status;
    private Long creatorId;
    private Long clientId;

}
