package com.accounting_manager.accounting_management.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Profile("sql")
public class ThirdPartyRequest {
    private String email;
    private String phoneNumber;

    private String nic;
    private String firstname;
    private String lastname;
    private String gender;

    private String siretNumber;
    private String legalName;
}