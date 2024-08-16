package com.accounting_manager.accounting_auth.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discriminator", //false error
        discriminatorType = DiscriminatorType.STRING, length = 8)
@Table(name = "ThirdParty") //false error
@SuperBuilder
@Log4j2
public class ThirdParty implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_third_party") //false error
    private Long idThirdParty;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(name="discriminator", insertable = false, updatable = false) //false error
    protected String discriminator;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_third_party_role",foreignKey=@ForeignKey(name = "fk_role_of_third_party"))
    private Type thirdPartyRole;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "accountant_client", //false error
            joinColumns = @JoinColumn(name = "accountant_id"), //false error
            inverseJoinColumns = @JoinColumn(name = "client_id") //false error
    )
    private Set<ThirdParty> clients = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "clients")
    private Set<ThirdParty> accountants = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getThirdPartyRole().getCodeType()));
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
