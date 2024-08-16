package com.accounting_manager.accounting_management.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "folders")
@Profile("nosql")
public class FolderNoSql {

    @Id
    private String idFolder; // Utilisation de String pour MongoDB

    @CreatedDate
    @Field(name = "creationDate")
    private LocalDateTime creationDate;

    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "status")
    private boolean status;

    @Field(name = "favorite")
    private boolean favorite;

    @Field(name = "archived")
    private boolean archived;

    @Field(name = "deleted")
    private boolean deleted;

    @Transient
    private long documentsNumber;

    @DBRef(lazy = true)
    private TypeNoSql type; // Utilisation de TypeNoSql pour MongoDB

    @DBRef(lazy = true)
    private ThirdPartyNoSql creator; // Assurez-vous que ThirdPartyNoSql est également adapté à MongoDB

    @DBRef(lazy = true)
    private ThirdPartyNoSql client; // Assurez-vous que ThirdPartyNoSql est également adapté à MongoDB

    @JsonIgnore
    @DBRef(lazy = true)
    private List<DocumentNoSql> documentList; // Assurez-vous que DocumentNoSql est adapté à MongoDB

    @JsonIgnore
    public Long getDocumentsNumber(boolean includeDeleted) {
        if (documentList != null) {
            if (includeDeleted) {
                return (long) documentList.size();
            } else {
                return documentList.stream()
                        .filter(document -> !document.isDeleted())
                        .count();
            }
        }
        return 0L;
    }

    public Long calculateDocumentsNumber(boolean includeDeleted) {
        return this.getDocumentsNumber(includeDeleted);
    }
}
