package com.accounting_manager.accounting_management.Service;

import com.accounting_manager.accounting_management.DTO.Request.ThirdPartyRequest;
import com.accounting_manager.accounting_management.Entity.LegalNoSql;
import com.accounting_manager.accounting_management.Entity.PhysicalNoSql;
import com.accounting_manager.accounting_management.Entity.ThirdPartyNoSql;
import com.accounting_manager.accounting_management.Exception.IncompleteDataException;
import com.accounting_manager.accounting_management.Repository.LegalRepositoryNoSql;
import com.accounting_manager.accounting_management.Repository.PhysicalRepositoryNoSql;
import com.accounting_manager.accounting_management.Repository.ThirdPartyDeleteRepositoryNoSql;
import com.accounting_manager.accounting_management.Repository.ThirdPartyRepositoryNoSql;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@Profile("nosql")
public class ThirdPartyServiceNoSql {

    private final ThirdPartyRepositoryNoSql thirdPartyRepository;
    private final PhysicalRepositoryNoSql physicalThirdPartyRepository;
    private final LegalRepositoryNoSql legalThirdPartyRepository;
    private final ThirdPartyDeleteRepositoryNoSql thirdPartyDeleteRepository;

    public ThirdPartyServiceNoSql(
            ThirdPartyRepositoryNoSql thirdPartyRepository,
            PhysicalRepositoryNoSql physicalThirdPartyRepository,
            LegalRepositoryNoSql legalThirdPartyRepository,
            ThirdPartyDeleteRepositoryNoSql thirdPartyDeleteRepository) {
        this.thirdPartyRepository = thirdPartyRepository;
        this.physicalThirdPartyRepository = physicalThirdPartyRepository;
        this.legalThirdPartyRepository = legalThirdPartyRepository;
        this.thirdPartyDeleteRepository = thirdPartyDeleteRepository;
    }

    public Page<ThirdPartyNoSql> getAllThirdParties(String query, String discriminator, int page, int size) {
        if (size <= 0) {
            size = 8;
        }
        log.warn(query);
        return thirdPartyRepository.findThirdParties(query, discriminator, PageRequest.of(page, size));
    }

    public Optional<ThirdPartyNoSql> getThirdPartyById(String id) {
        return thirdPartyRepository.findById(id);
    }

    public Page<ThirdPartyNoSql> getAccountantClients(String accountantId, String query, int page, int size) {
        if (size <= 0) {
            size = 8;
        }
        return thirdPartyRepository.findAccountantClients(accountantId, query, PageRequest.of(page, size));
    }

    public Page<ThirdPartyNoSql> getClientAccountants(String clientId, String query, int page, int size) {
        if (size <= 0) {
            size = 8;
        }
        return thirdPartyRepository.findClientAccountants(clientId, query, PageRequest.of(page, size));
    }

    public ThirdPartyNoSql createThirdParty(String accountantId, ThirdPartyRequest request) {
        thirdPartyRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new IllegalArgumentException("Email already exists");
        });
        thirdPartyRepository.findByPhoneNumber(request.getPhoneNumber()).ifPresent(user -> {
            throw new IllegalArgumentException("Used Phone number");
        });

        if (request.getNic() != null && request.getFirstname() != null && request.getLastname() != null && request.getGender() != null) {
            physicalThirdPartyRepository.findByNic(request.getNic()).ifPresent(user -> {
                throw new IllegalArgumentException("National ID Card is already used!");
            });
            var newUser = PhysicalNoSql.builder()
                    .nic(request.getNic())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .gender(request.getGender())
                    .build();
            try {
                // Get the accountant entity
                ThirdPartyNoSql accountant = thirdPartyRepository.findById(accountantId)
                        .orElseThrow(() -> new IllegalArgumentException("Accountant not found"));

                // Save the new ThirdParty entity
                ThirdPartyNoSql savedClient = thirdPartyRepository.save(newUser);

                // Associate the new client with the accountant
                accountant.getClients().add(savedClient);
                thirdPartyRepository.save(accountant);
                return savedClient;
            } catch (DataIntegrityViolationException e) {
                throw new IncompleteDataException();
            }
        } else if (request.getSiretNumber() != null && request.getLegalName() != null) {
            legalThirdPartyRepository.findBySiretNumber(request.getSiretNumber()).ifPresent(user -> {
                throw new IllegalArgumentException("Siret number is already used!");
            });
            var newUser = LegalNoSql.builder()
                    .siretNumber(request.getSiretNumber())
                    .legalName(request.getLegalName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .build();

            // Get the accountant entity
            ThirdPartyNoSql accountant = thirdPartyRepository.findById(accountantId)
                    .orElseThrow(() -> new IllegalArgumentException("Accountant not found"));

            // Save the new ThirdParty entity
            ThirdPartyNoSql savedClient = thirdPartyRepository.save(newUser);

            // Associate the new client with the accountant
            accountant.getClients().add(savedClient);
            thirdPartyRepository.save(accountant);
            return savedClient;
        } else {
            throw new IncompleteDataException();
        }
    }

    public void deleteThirdPartyAsAdmin(String id) {
        thirdPartyRepository.deleteById(id);
    }

    public void deleteThirdPartyAsAccountant(String accountantId, String clientId) {
        thirdPartyDeleteRepository.deleteAccountantClient(accountantId, clientId);
    }

    public Long getClientsCount() {
        return this.thirdPartyRepository.countClients();
    }

    public Long getAccountantsCount() {
        return this.thirdPartyRepository.countAccountants();
    }

    public Long getClientsCountByThirdPartyId(String accountantId) {
        return this.thirdPartyRepository.countClientsByAccountantId(accountantId);
    }

    public Long getAccountantsCountByThirdPartyId(String clientId) {
        return this.thirdPartyRepository.countAccountantsByClientId(clientId);
    }
}
