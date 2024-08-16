package com.accounting_manager.bank_statement_engine.DTO;

import com.accounting_manager.bank_statement_engine.Classes.facture.FactureInfo;

public class Converter {

    public static FactureInfoDTO FromFacturInfoToDTO (FactureInfo info){
        return new FactureInfoDTO(
                info.getNumeroFacture(),
                info.getNumeroTva(),
                info.getNumeroSiret(),
                info.getAccountNumber(),
                info.getBicNumber(),
                info.getRibNumber(),
                info.getIbanNumber(),
                info.getNumeroPage(),
                info.getTtc(),
                info.getHt(),
                info.getTva(),
                info.getTvaSupp(),
                info.getDiscount(),
                info.getDate(),
                info.getPeriodStartDate(),
                info.getPeriodEndDate()
//                info.isTypeFacture(),
//                info.isDetails()
        );
    }
}

