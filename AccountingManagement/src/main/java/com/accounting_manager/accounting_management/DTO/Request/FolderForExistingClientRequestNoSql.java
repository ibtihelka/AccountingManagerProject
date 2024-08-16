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
@Profile("nosql")
public class FolderForExistingClientRequestNoSql {

    private String name;
    private String description;
    private boolean status;
    private String type;
    private String creatorId; // Changement de Long à String pour MongoDB
    private String clientId; // Changement de Long à String pour MongoDB

}
