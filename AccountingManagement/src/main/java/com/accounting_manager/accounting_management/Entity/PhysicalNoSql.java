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
public class PhysicalNoSql extends ThirdPartyNoSql {

    @Field("nic")
    private String nic;

    @Field("firstname")
    private String firstname;

    @Field("lastname")
    private String lastname;

    @Field("gender")
    private String gender;
}
