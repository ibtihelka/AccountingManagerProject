package com.accounting_manager.accounting_management.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Profile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@SuperBuilder
@DiscriminatorColumn(name="discriminator",
        discriminatorType = DiscriminatorType.STRING)
@Table(name="ThirdParty")
@Profile("sql")

public class ThirdParty
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_third_party")
    private Long idThirdParty;

    @Column(nullable=false, unique=true)
    private String email;

    @JsonIgnore
    @Column(nullable=true)
    private String password;

    @Column(nullable=false)
    private String phoneNumber;

    @Column(name="discriminator", insertable = false, updatable = false)
    private String discriminator;

    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    private List<Folder> folderListAsCreator;

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Folder> folderListAsClient;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_third_party_role",foreignKey=@ForeignKey(name = "fk_role_of_third_party"))
    private Type thirdPartyRole;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "accountant_client",
            joinColumns = @JoinColumn(name = "accountant_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private Set<ThirdParty> clients = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "clients")
    private Set<ThirdParty> accountants = new HashSet<>();


    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

}
