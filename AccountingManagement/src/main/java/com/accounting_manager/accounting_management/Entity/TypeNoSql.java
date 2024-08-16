package com.accounting_manager.accounting_management.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "types")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Profile("nosql")
public class TypeNoSql {

    @Id
    private String idType;  // Peut être généré automatiquement par MongoDB si vous laissez vide lors de l'insertion

    private String codeType;

    private String label;

    @DBRef(lazy = true)  // Pour éviter le chargement immédiat des documents référencés
    private TypeNoSql parentType;

    @JsonIgnore
    @DBRef(lazy = true)  // Relations paresseuses pour les sous-types
    private Set<TypeNoSql> subTypes = new HashSet<>();

}
