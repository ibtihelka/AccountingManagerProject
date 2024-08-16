package com.accounting_manager.accounting_auth.DTO.Request;

import com.accounting_manager.accounting_auth.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    private String email;
    private String currentPassword;
    private String newPassword;
    private String phoneNumber;

    private String nic;
    private String firstname;
    private String lastname;
    private String gender;

    private String siretNumber;
    private String legalName;

    private Role role;
    private String discriminator;
}
