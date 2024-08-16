package com.accounting_manager.accounting_management.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Document(collection = "third_parties")
@Profile("nosql")
public class LegalNoSql extends ThirdPartyNoSql {

    @Field("siretNumber")
    private String siretNumber;

    @Field("legalName")
    private String legalName;

}
