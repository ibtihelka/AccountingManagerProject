package com.accounting_manager.accounting_management.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Profile("nosql")
public class ResponseDTONoSql {

    private String message;

}
