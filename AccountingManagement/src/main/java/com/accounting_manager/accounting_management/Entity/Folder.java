package com.accounting_manager.accounting_management.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Profile("sql")
public class Folder {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idFolder;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(length = 50,nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    //OPEN OR CLOSED FOLDER
    @Column(nullable = false)
    private boolean status;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean favorite;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean archived;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted;

    @Transient
    private long documentsNumber;

    @ManyToOne(targetEntity = Type.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_type",foreignKey=@ForeignKey(name = "fk_type_of_folder"))
    private Type type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_creator",foreignKey=@ForeignKey(name = "fk_creator_of_folder"))
    private ThirdParty creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_client",foreignKey=@ForeignKey(name = "fk_client_of_folder"))
    private ThirdParty client;

    @JsonIgnore
    @OneToMany(mappedBy = "folder")
    private List<Document> documentList;


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
