package com.accounting_manager.accounting_management.Service;

import com.accounting_manager.accounting_management.Entity.TypeNoSql;
import com.accounting_manager.accounting_management.Repository.TypeRepositoryNoSql;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("nosql")
public class TypeServiceNoSql {

    private final TypeRepositoryNoSql typeRepositoryNoSql;

    public TypeServiceNoSql(TypeRepositoryNoSql typeRepositoryNoSql) {
        this.typeRepositoryNoSql = typeRepositoryNoSql;
    }

    public List<TypeNoSql> getAllTypes() {
        return typeRepositoryNoSql.findAll();
    }

    public Optional<TypeNoSql> getTypeById(String id) {
        return typeRepositoryNoSql.findById(id);
    }

    public Optional<TypeNoSql> createType(TypeNoSql typeNoSql) {
        return Optional.of(typeRepositoryNoSql.save(typeNoSql));
    }

    public Optional<TypeNoSql> UpdateType(String id, TypeNoSql updatedTypeNoSql) {
        if (typeRepositoryNoSql.existsById(id)) {
            updatedTypeNoSql.setIdType(id);
            return Optional.of(typeRepositoryNoSql.save(updatedTypeNoSql));
        }
        return Optional.empty();
    }

    public void deleteType(String id) {
        if (typeRepositoryNoSql.existsById(id)) {
            typeRepositoryNoSql.deleteById(id);
        }
    }
}
