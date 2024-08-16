package com.accounting_manager.accounting_management.Service;

import com.accounting_manager.accounting_management.DTO.Request.ThirdPartyRequest;
import com.accounting_manager.accounting_management.Entity.Legal;
import com.accounting_manager.accounting_management.Entity.Physical;
import com.accounting_manager.accounting_management.Entity.ThirdParty;
import com.accounting_manager.accounting_management.Exception.IncompleteDataException;
import com.accounting_manager.accounting_management.Repository.LegalRepository;
import com.accounting_manager.accounting_management.Repository.PhysicalRepository;
import com.accounting_manager.accounting_management.Repository.ThirdPartyDeleteRepository;
import com.accounting_manager.accounting_management.Repository.ThirdPartyRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@Profile("sql")
public class ThirdPartyService {

    private ThirdPartyRepository thirdPartyRepository;
    private PhysicalRepository physicalThirdPartyRepository;
    private LegalRepository legalThirdPartyRepository;
    private ThirdPartyDeleteRepository thirdPartyDeleteRepository;

    public ThirdPartyService(ThirdPartyRepository thirdPartyRepository, PhysicalRepository physicalThirdPartyRepository, LegalRepository legalThirdPartyRepository, ThirdPartyDeleteRepository thirdPartyDeleteRepository) {
        this.thirdPartyRepository = thirdPartyRepository;
        this.physicalThirdPartyRepository = physicalThirdPartyRepository;
        this.legalThirdPartyRepository = legalThirdPartyRepository;
        this.thirdPartyDeleteRepository = thirdPartyDeleteRepository;
    }

    public Page<ThirdParty> getAllThirdParties(String query, String discriminator, int page, int size) {
        if (size <= 0) {
            size = 8;
        }
        log.warn(query);
        return thirdPartyRepository.findThirdParties(query, discriminator, PageRequest.of(page, size));
    }

    public Optional<ThirdParty> getThirdPartyById(Long id) {
        return thirdPartyRepository.findById(id);
    }

    public Page<ThirdParty> getAccountantClients(Long id, String query, int page, int size) {
        if (size <= 0) {
            size = 8;
        }
        return thirdPartyRepository.findAccountantClients(id, query, PageRequest.of(page, size));
    }

    public Page<ThirdParty> getClientAccountants(Long id, String query, int page, int size) {
        if (size <= 0) {
            size = 8;
        }
        return thirdPartyRepository.findClientAccountants(id, query, PageRequest.of(page, size));
    }

    public ThirdParty createThirdParty(Long id, ThirdPartyRequest request) {
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
            var newUser = Physical.builder()
                    .nic(request.getNic())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .gender(request.getGender())
                    .build();
            try {

                // Get the accountant entity
                ThirdParty accountant = thirdPartyRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Accountant not found"));

                // Save the new ThirdParty entity
                ThirdParty savedClient = thirdPartyRepository.save(newUser);

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
            var newUser = Legal.builder()
                    .siretNumber(request.getSiretNumber())
                    .legalName(request.getLegalName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())

                    .build();

            // Get the accountant entity
            ThirdParty accountant = thirdPartyRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Accountant not found"));

            // Save the new ThirdParty entity
            ThirdParty savedClient = thirdPartyRepository.save(newUser);

            // Associate the new client with the accountant
            accountant.getClients().add(savedClient);
            thirdPartyRepository.save(accountant);
            return savedClient;

        } else {
            throw new IncompleteDataException();
        }

    }

    //    public Optional<ThirdParty> UpdateThirdParty(Long id, ThirdParty updatedThirdParty) {
//        if (thirdPartyRepository.existsById(id)) {
//            updatedThirdParty.setIdThirdParty(id);
//            return Optional.of(thirdPartyRepository.save(updatedThirdParty));
//        }
//        return Optional.empty();
//    }
//
    public void deleteThirdPartyAsAdmin(Long id) {
        thirdPartyRepository.deleteById(id);

    }

    public void deleteThirdPartyAsAccountant(Long accountantId, Long clientId) {
        thirdPartyDeleteRepository.deleteAccountantClient(accountantId, clientId);
    }


    public Long getClientsCount() {
        return this.thirdPartyRepository.getClientsCount();
    }

    public Long getAccountantsCount() {
        return this.thirdPartyRepository.getAccountantsCount();
    }

    public Long getClientsCountByThirdPartyId(Long id) {
        return this.thirdPartyRepository.getClientsCountByThirdPartyId(id);
    }

    public Long getAccountantsCountByThirdPartyId(Long id) {
        return this.thirdPartyRepository.getAccountantsCountByThirdPartyId(id);
    }
}
