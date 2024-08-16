package com.accounting_manager.bank_statement_engine.DTO;

import java.util.Date;

public record FactureInfoDTO(
        String numeroFacture,
        String numeroTva,
        String numeroSiret,
        String accountNumber,
        String bicNumber,
        String ribNumber,
        String ibanNumber,
        Long numeroPage,
        Double ttc,
        Double ht,
        Double tva,
        Double tvaSupp,
        Double discount,
        Date date,
        Date periodStartDate,
        Date periodEndDate
//        boolean typeFacture,
//        boolean isDetails
) {
}
