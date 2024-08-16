package com.accounting_manager.accounting_management.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Profile("sql")
public class UpdateFolderRequest {
    private String name;
    private String description;
    private String type;
    private boolean favorite;
    private boolean status;
    private boolean archived;
}
