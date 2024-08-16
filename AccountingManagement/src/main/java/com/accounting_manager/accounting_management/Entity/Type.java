package com.accounting_manager.accounting_management.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Profile;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"codeType", "fk_parent_type"})
})
@Profile("sql")
public class Type {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idType;

    @Column(length = 20,nullable = false)
    private String codeType;

    @Column(length = 120)
    private String label;

    @ManyToOne(targetEntity = Type.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_parent_type ",foreignKey=@ForeignKey(name = "fk_parent_type"), updatable=false)
    private Type parentType;

    @JsonIgnore
    @OneToMany(mappedBy = "parentType", fetch = FetchType.EAGER)
    private Set<Type> subTypes = new HashSet<>();

}
