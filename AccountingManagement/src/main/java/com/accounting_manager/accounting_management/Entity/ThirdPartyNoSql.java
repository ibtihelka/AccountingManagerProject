package com.accounting_manager.accounting_management.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Profile("nosql")
@Document(collection = "ThirdParty")
public class ThirdPartyNoSql {

    @Id
    @Field(name = "id_third_party")
    private String idThirdParty; // Utilisation de String pour MongoDB

    @Field(name = "email")
    private String email;

    @JsonIgnore
    @Field(name = "password")
    private String password;

    @Field(name = "phoneNumber")
    private String phoneNumber;

    @Field(name = "discriminator")
    private String discriminator;

    @JsonIgnore
    @DBRef(lazy = true)
    private List<FolderNoSql> folderListAsCreator;

    @JsonIgnore
    @DBRef(lazy = true)
    private List<FolderNoSql> folderListAsClient;

    @JsonIgnore
    @DBRef(lazy = true)
    private TypeNoSql thirdPartyRole;

    @JsonIgnore
    @DBRef(lazy = true)
    private Set<ThirdPartyNoSql> clients = new HashSet<>();

    @JsonIgnore
    @DBRef(lazy = true)
    private Set<ThirdPartyNoSql> accountants = new HashSet<>();

    // Getter and Setter for discriminator
    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }
}
