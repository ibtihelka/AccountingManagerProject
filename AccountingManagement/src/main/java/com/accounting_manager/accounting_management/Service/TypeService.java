package com.accounting_manager.accounting_management.Service;

import com.accounting_manager.accounting_management.Entity.Type;
import com.accounting_manager.accounting_management.Repository.TypeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("sql")
public class TypeService {

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    public Optional<Type> getTypeById(Long id) {
        return typeRepository.findById(id);
    }

    public Optional<Type> createType(Type type) {
        return Optional.of(typeRepository.save(type));
    }

    public Optional<Type> UpdateType(Long id, Type updatedType) {
        if (typeRepository.existsById(id)) {
            updatedType.setIdType(id);
            return Optional.of(typeRepository.save(updatedType));
        }
        return Optional.empty();
    }

    public void deleteType(Long id) {
        if (typeRepository.existsById(id)) {
            typeRepository.deleteById(id);
        }
    }
}
