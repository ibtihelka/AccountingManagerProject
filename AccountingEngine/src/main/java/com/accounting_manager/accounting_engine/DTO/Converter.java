package com.accounting_manager.accounting_engine.DTO;

import com.accounting_manager.accounting_engine.Classes.facture.FactureInfo;

public class Converter {

    public static FactureInfoDTO FromFacturInfoToDTO (FactureInfo info){
        return new FactureInfoDTO(
                info.getNumeroFacture(),
                info.getNumeroTva(),
                info.getNumeroSiret(),
                info.getNumeroRcs(),
                info.getNumeroPage(),
                info.getTtc(),
                info.getHt(),
                info.getTva(),
                info.getTvaSupp(),
                info.getDiscount(),
                info.getDate()
//                info.isTypeFacture(),
//                info.isDetails()
        );
    }
}

