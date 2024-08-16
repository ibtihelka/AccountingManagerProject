package com.accounting_manager.accounting_management.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "folders")
@Profile("nosql")
public class test {

    @Id
    private String id;

    @Field(name = "discriminator")
    private String nom;

    @Field(name = "name")
    private String prenom;
}
