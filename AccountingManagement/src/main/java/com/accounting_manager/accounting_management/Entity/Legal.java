package com.accounting_manager.accounting_management.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Profile;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("legal")
@Profile("sql")
public class Legal  extends ThirdParty{

    @Column(length = 20, unique = true)
    private String siretNumber;

    @Column(unique = true)
    private String legalName;

}
