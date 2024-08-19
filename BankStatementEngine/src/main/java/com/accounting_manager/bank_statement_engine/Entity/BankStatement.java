package com.accounting_manager.bank_statement_engine.Entity;


import com.accounting_manager.bank_statement_engine.Classes.facture.FactureInfo;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("bank_statement")
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

    public BankStatement copyFromBankStatementInfo(FactureInfo bankStatementInfo) {
          this.accountNumber = bankStatementInfo.getAccountNumber();
          this.bic = bankStatementInfo.getBicNumber();
          this.rib = bankStatementInfo.getRibNumber();
          this.iban = bankStatementInfo.getIbanNumber();
          this.bankStatementDate = bankStatementInfo.getDate();
          this.periodStartDate = bankStatementInfo.getPeriodStartDate();
          this.periodEndDate = bankStatementInfo.getPeriodEndDate();
//        this.supplierNumber = invoiceInfo.getNumeroRcs();
//        this.pagesNumber = invoiceInfo.getNumeroPage();
//        this.ttc = invoiceInfo.getTtc();
//        this.ht = invoiceInfo.getHt();
//        this.tva = invoiceInfo.getTva();
//        this.supportTva = invoiceInfo.getTvaSupp();
//        this.discount = invoiceInfo.getDiscount();
//        this.invoiceDate = invoiceInfo.getDate();

        return this;
    }

    @Lob
    @Column(name = "source_xml", columnDefinition = "LONGBLOB")
    private byte[] sourceXml;



    public void setSourceXml(byte[] sourceXml) {
        this.sourceXml = sourceXml;
    }

}
