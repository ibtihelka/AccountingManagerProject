package com.accounting_manager.accounting_engine.Entity;


import com.accounting_manager.accounting_engine.Classes.facture.FactureInfo;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("invoice")
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
    private Long pagesNumber;

    @Column
    private Double ttc;

    @Column
    private Double ht;

    @Column
    private Double tva;

    @Column(name="support_tva")
    private Double supportTva;

    @Column
    private Double discount;

    @Column
    private Date invoiceDate;

    public Invoice copyFromInvoiceInfo(FactureInfo invoiceInfo) {
        this.invoiceNumber = invoiceInfo.getNumeroFacture();
        this.tvaNumber = invoiceInfo.getNumeroTva();
        this.siretNumber = invoiceInfo.getNumeroSiret();
        this.supplierNumber = invoiceInfo.getNumeroRcs();
        this.pagesNumber = invoiceInfo.hasNumeroPage()?invoiceInfo.getNumeroPage():1L;
        this.ttc = invoiceInfo.getTtc();
        this.ht = invoiceInfo.getHt();
        this.tva = invoiceInfo.getTva();
        this.supportTva = invoiceInfo.getTvaSupp();
        this.discount = invoiceInfo.getDiscount();
        this.invoiceDate = invoiceInfo.getDate();

        return this;
    }

}
