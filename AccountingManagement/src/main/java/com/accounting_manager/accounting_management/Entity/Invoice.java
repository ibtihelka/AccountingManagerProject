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
@DiscriminatorValue("invoice")
@Profile("sql")
public class Invoice extends Document {

    @Column
    private String invoiceNumber;

    @Column
    private String tvaNumber;

    @Column
    private String siretNumber;

    @Column
    private String supplierNumber;

    @Column
    private int pagesNumber;

    @Column
    private double ttc;

    @Column
    private double ht;

    @Column
    private double tva;

    @Column(name="support_tva")
    private double supportTva;

    @Column
    private double discount;

    @Column
    private Date invoiceDate;

}
