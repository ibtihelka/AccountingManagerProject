package com.accounting_manager.accounting_management.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Profile("nosql")
@Document(collection = "documents")
public class DocumentNoSql {

    @Id
    private String id;

    @Field(name = "discriminator")
    protected String discriminator;

    @Field(name = "name")
    private String name;

    @CreatedDate
    @Field(name = "creationDate")
    private LocalDateTime creationDate;

    @Field(name = "sourceImg")
    private byte[] sourceImg;

    @Field(name = "detectionImg")
    private byte[] detectionImg;

    @Field(name = "status")
    private boolean status;

    @Field(name = "deleted")
    private boolean deleted;

    @DBRef(lazy = true)
    private FolderNoSql folder;

    @DBRef(lazy = true)
    private TypeNoSql type;

    @DBRef(lazy = true)
    private TypeNoSql processType;

}
