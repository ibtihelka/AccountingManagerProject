package com.accounting_manager.accounting_auth.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"codeType", "fk_parent_type"})
})
public class Type {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idType;

    @Column(length = 20,nullable = false)
    private String codeType;

    @Column(length = 120)
    private String label;

    @ManyToOne(targetEntity = Type.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_parent_type ",foreignKey=@ForeignKey(name = "fk_parent_type"))
    private Type parentType;

    @JsonIgnore
    @OneToMany(mappedBy = "parentType", fetch = FetchType.EAGER)
    private Set<Type> subTypes = new HashSet<>();

}
