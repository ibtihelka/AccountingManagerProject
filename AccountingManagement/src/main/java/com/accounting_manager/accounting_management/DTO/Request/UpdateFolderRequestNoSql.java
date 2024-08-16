package com.accounting_manager.accounting_management.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Profile("nosql")
public class UpdateFolderRequestNoSql {
    private String name;
    private String description;
    private String type;  // Assuming this refers to an ID or type name in NoSQL as well
    private boolean favorite;
    private boolean status;
    private boolean archived;
}
