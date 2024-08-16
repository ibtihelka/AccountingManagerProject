package com.accounting_manager.accounting_management.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@SuperBuilder
@DiscriminatorColumn(name="discriminator",
        discriminatorType = DiscriminatorType.STRING)
@Table(name="document")
@Profile("sql")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="discriminator", insertable = false, updatable = false)
    protected String discriminator;

    @Column
    private String name;

    @CreationTimestamp
    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(nullable=false, updatable = false, columnDefinition = "mediumblob")
    private byte[] sourceImg;

    @Column(columnDefinition = "mediumblob")
    private byte[] detectionImg;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne(targetEntity = Folder.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_folder",foreignKey=@ForeignKey(name = "fk_folder_of_document"), updatable = false)
    private Folder folder;

    @ManyToOne(targetEntity = Type.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_type",foreignKey=@ForeignKey(name = "fk_type_of_document"))
    private Type type;

    @ManyToOne(targetEntity = Type.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_process_type",foreignKey=@ForeignKey(name = "fk_type_of_document_process"))
    private Type processType;

}