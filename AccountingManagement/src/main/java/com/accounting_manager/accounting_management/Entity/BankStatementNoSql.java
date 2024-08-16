package com.accounting_manager.accounting_management.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Document(collection = "documents")
@Profile("nosql")
public class BankStatementNoSql extends DocumentNoSql {

    @Field("account_number")
    private String accountNumber;

    @Field("iban")
    private String iban;

    @Field("rib")
    private String rib;

    @Field("bic")
    private String bic;

    @Field("bank_statement_date")
    private Date bankStatementDate;

    @Field("period_start_date")
    private Date periodStartDate;

    @Field("period_end_date")
    private Date periodEndDate;
}
