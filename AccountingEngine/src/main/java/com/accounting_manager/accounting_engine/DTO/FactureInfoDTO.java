package com.accounting_manager.accounting_engine.DTO;

import java.util.Date;

public record FactureInfoDTO(
        String numeroFacture,
        String numeroTva,
        String numeroSiret,
        String numeroRcs,
        Long numeroPage,
        Double ttc,
        Double ht,
        Double tva,
        Double tvaSupp,
        Double discount,
        Date date
//        boolean typeFacture,
//        boolean isDetails
) {
}
