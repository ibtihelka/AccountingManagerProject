package com.accounting_manager.accounting_management.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
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
public class InvoiceNoSql extends DocumentNoSql {

    @Field("invoice_number")
    private String invoiceNumber;

    @Field("tva_number")
    private String tvaNumber;

    @Field("siret_number")
    private String siretNumber;

    @Field("supplier_number")
    private String supplierNumber;

    @Field("pages_number")
    private int pagesNumber;

    @Field("ttc")
    private double ttc;

    @Field("ht")
    private double ht;

    @Field("tva")
    private double tva;

    @Field("support_tva")
    private double supportTva;

    @Field("discount")
    private double discount;

    @Field("invoice_date")
    private Date invoiceDate;

}
