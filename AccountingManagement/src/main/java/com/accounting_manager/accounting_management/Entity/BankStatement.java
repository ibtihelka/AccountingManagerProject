package com.accounting_manager.accounting_management.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Profile;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("bank_statement")
@Profile("sql")
public class BankStatement extends Document {

    @Column
    private String accountNumber;

    @Column
    private String iban;

    @Column
    private String rib;

    @Column
    private String bic;

    private Date bankStatementDate;

    private Date periodStartDate;

    private Date periodEndDate;

}
