package com.accounting_manager.accounting_auth.Service;

import com.accounting_manager.accounting_auth.Config.JwtService;
import com.accounting_manager.accounting_auth.DTO.Request.LoginRequest;
import com.accounting_manager.accounting_auth.DTO.Request.RegisterRequest;
import com.accounting_manager.accounting_auth.DTO.Request.UpdateRequest;
import com.accounting_manager.accounting_auth.DTO.Response.LoginResponse;
import com.accounting_manager.accounting_auth.Entity.*;
import com.accounting_manager.accounting_auth.Repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.accounting_manager.accounting_auth.Exception.IncompleteDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2

/**
 * This class represents the AuthenticationService in the AccountingAuth module.
 * It provides methods for user authentication, registration, and other related operations.
 * The AuthenticationService is responsible for handling user registration requests,
 * verifying user information, and sending confirmation emails.
 * It also provides methods for retrieving a list of all third parties and registering clients.
 */
public class AuthenticationService {

    private final ThirdPartyRepository thirdPartyRepository;
    private final PhysicalRepository physicalRepository;
    private final LegalRepository legalRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSenderService emailSenderService;
    private final TokenRepository tokenRepository;
    private final TypeRepository typeRepository;

    @Value("${application.security.confirmation-token.expiration-time-in-minutes}")
    private int confirmationTokenExpirationTimeInMinutes;

    @Value("${application.security.refresh-token.expiration-time-in-minutes}")
    private int refreshTokenExpirationTimeInDays;

    public String registerAccountant(RegisterRequest request) throws Exception {

        Type clientRole = typeRepository.findOneByParentCodeTypeAndCodeType("ROLE", "CLIENT");
        Type accountantRole = typeRepository.findOneByParentCodeTypeAndCodeType("ROLE", "ACCOUNTANT");

        thirdPartyRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            if (!(user.getThirdPartyRole() == clientRole )) {
                throw new IllegalArgumentException("Email already exists");
            }
        });

        thirdPartyRepository.findByPhoneNumber(request.getPhoneNumber()).ifPresent(user -> {
            if (!(user.getThirdPartyRole() == clientRole)) {
                throw new IllegalArgumentException("Used Phone number");
            }
        });

        Type confirmationTokenType = typeRepository.findOneByParentCodeTypeAndCodeType("TOKEN", "CONFIRMATION");

        if (request.getNic() != null && request.getFirstname() != null && request.getLastname() != null && request.getNic() != null) {
            Optional<ThirdParty> userOptional = physicalRepository.findByNic(request.getNic());
            if (userOptional.isPresent()) {
                if (!userOptional.get().getThirdPartyRole().equals(clientRole) && !userOptional.get().getEmail().equals(request.getEmail())) {
                    throw new IllegalArgumentException("National ID Card is already used!");
                }

                var newUser = Physical.builder()
                        .idThirdParty(userOptional.get().getIdThirdParty())
                        .nic(request.getNic())
                        .firstname(request.getFirstname())
                        .lastname(request.getLastname())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phoneNumber(request.getPhoneNumber())
                        .gender(request.getGender())

                        .thirdPartyRole(accountantRole)
                        .build();

                try {
                    physicalRepository.save(newUser);
                    var jwtToken = createToken(newUser, confirmationTokenType);
                    emailSenderService.sendConfirmationMail(newUser.getEmail(), jwtToken.getToken());
                    return "Please, Verify Your Email ";
                } catch (DataIntegrityViolationException e) {
                    throw new IncompleteDataException();
                }

            } else {
                var newUser = Physical.builder()
                        .nic(request.getNic())
                        .firstname(request.getFirstname())
                        .lastname(request.getLastname())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phoneNumber(request.getPhoneNumber())
                        .gender(request.getGender())
                        .thirdPartyRole(accountantRole)
                        .build();

                try {
                    physicalRepository.save(newUser);
                    var jwtToken = createToken(newUser, confirmationTokenType);
                    emailSenderService.sendConfirmationMail(newUser.getEmail(), jwtToken.getToken());
                    return "Please, Verify Your Email ";
                } catch (DataIntegrityViolationException e) {
                    throw new IncompleteDataException();
                }

            }

        } else if (request.getSiretNumber() != null && request.getLegalName() != null) {
            Optional<ThirdParty> userOptional = legalRepository.findBySiretNumber(request.getSiretNumber());
            if (userOptional.isPresent()) {
                if (!userOptional.get().getThirdPartyRole().equals(clientRole) || !userOptional.get().getEmail().equals(request.getEmail())) {
                    throw new IllegalArgumentException("Siret Number is already used!");
                }
                var newUser = Legal.builder()
                        .idThirdParty(userOptional.get().getIdThirdParty())
                        .siretNumber(request.getSiretNumber())
                        .legalName(request.getLegalName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phoneNumber(request.getPhoneNumber())

                        .thirdPartyRole(accountantRole)
                        .build();
                try {
                    legalRepository.save(newUser);
                    var jwtToken = createToken(newUser, confirmationTokenType);
                    emailSenderService.sendConfirmationMail(newUser.getEmail(), jwtToken.getToken());
                    return "Please, Verify Your Email ";
                } catch (DataIntegrityViolationException e) {
                    throw new IncompleteDataException();
                }
            } else {
                var newUser = Legal.builder()
                        .siretNumber(request.getSiretNumber())
                        .legalName(request.getLegalName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phoneNumber(request.getPhoneNumber())

                        .thirdPartyRole(accountantRole)
                        .build();

                try {
                    legalRepository.save(newUser);
                    var jwtToken = createToken(newUser, confirmationTokenType);
                    emailSenderService.sendConfirmationMail(newUser.getEmail(), jwtToken.getToken());
                    return "Please, Verify Your Email ";
                } catch (DataIntegrityViolationException e) {
                    throw new IncompleteDataException();
                }
            }

        } else {
            throw new IncompleteDataException();
        }
    }

    public Page<ThirdParty> getAllThirdParties(String query, String discriminator, int page, int size) {
        if (size <= 0) {
            size = 8;
        }
        return thirdPartyRepository.findAllThirdParties(query, discriminator, PageRequest.of(page, size));
    }

    public ThirdParty registerClient(Long id, RegisterRequest request) throws Exception {
        ThirdParty accountant = thirdPartyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Accountant not found"));

        Type clientRole = typeRepository.findOneByParentCodeTypeAndCodeType("ROLE", "CLIENT");

        var user = thirdPartyRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            if (request.getNic() != null && request.getFirstname() != null && request.getLastname() != null && request.getNic() != null) {
                var physical = physicalRepository.findByNic(request.getNic());
                if (!user.equals(physical)) {
                    throw new IllegalArgumentException("Client infos are wrong, Please check ur client's email or nic");
                }
                accountant.getClients().add(physical.get());
                thirdPartyRepository.save(accountant);
                return physical.get();
            } else if (request.getSiretNumber() != null && request.getLegalName() != null) {
                var legal = legalRepository.findBySiretNumber(request.getSiretNumber());
                if (!user.equals(legal)) {
                    throw new IllegalArgumentException("Client infos are wrong, Please check ur client's email or siret number");
                }
                accountant.getClients().add(legal.get());
                thirdPartyRepository.save(accountant);
                return legal.get();
            } else {
                throw new IncompleteDataException();
            }
        } else {
            String password = generatePassword();
            if (request.getNic() != null && request.getFirstname() != null && request.getLastname() != null && request.getNic() != null && request.getPassword() == null) {

                var newUser = Physical.builder()
                        .nic(request.getNic())
                        .firstname(request.getFirstname())
                        .lastname(request.getLastname())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(password))
                        .phoneNumber(request.getPhoneNumber())
                        .gender(request.getGender())
                        .thirdPartyRole(clientRole)
                        .build();

                try {
                    physicalRepository.save(newUser);
                    accountant.getClients().add(newUser);
                    thirdPartyRepository.save(accountant);
                    emailSenderService.sendPasswordMail(newUser.getEmail(), password);
                    return newUser;
                } catch (DataIntegrityViolationException e) {
                    throw new IncompleteDataException();
                }

            } else if (request.getSiretNumber() != null && request.getLegalName() != null && request.getPassword() == null) {
                var newUser = Legal.builder()
                        .siretNumber(request.getSiretNumber())
                        .legalName(request.getLegalName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(password))
                        .phoneNumber(request.getPhoneNumber())
                        .thirdPartyRole(clientRole)
                        .build();

                legalRepository.save(newUser);
                accountant.getClients().add(newUser);
                thirdPartyRepository.save(accountant);
                emailSenderService.sendConfirmationMail(newUser.getEmail(), password);
                return newUser;
            } else {
                throw new IncompleteDataException();
            }
        }
    }

    public LoginResponse authenticate(LoginRequest request) throws JsonProcessingException {

        var user = thirdPartyRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }
        Type confirmationTokenType = typeRepository.findOneByParentCodeTypeAndCodeType("TOKEN", "CONFIRMATION");
        Token confirmationToken = tokenRepository.findByThirdPartyIdThirdPartyAndTokenType(user.getIdThirdParty(), confirmationTokenType);
        if (confirmationToken != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account pending approval. Please verify your email to activate!");
        }
        // Create a confirmation token with token type "refresh_token"
        Token refreshTokenToken;
        Type refreshTokenType = typeRepository.findOneByParentCodeTypeAndCodeType("TOKEN", "REFRESH");
        Token existingToken = tokenRepository.findByThirdPartyIdThirdPartyAndTokenType(user.getIdThirdParty(), refreshTokenType);
        if (existingToken != null) {
            refreshTokenToken = updateToken(existingToken, refreshTokenType);
        } else {
            refreshTokenToken = createToken(user, refreshTokenType);
        }


        var jwtToken = jwtService.generateToken(user);
        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshTokenToken.getToken())
                .build();
    }

    public boolean changePassword(String password, String resetPwdToken) {
        Token existingToken = tokenRepository.findByToken(resetPwdToken);
        if (existingToken != null && existingToken.getExpirationDate().compareTo(new Date()) > 0) {
            ThirdParty thirdParty = existingToken.getThirdParty();
            thirdParty.setPassword(passwordEncoder.encode(password));
            thirdPartyRepository.save(thirdParty);
            tokenRepository.delete(existingToken);
            return true;
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        return this.thirdPartyRepository.existsByEmail(email);
    }

    public void confirmClient(String request) {
        Token confirmationToken = tokenRepository.findTokenByConfirmationToken(request);
        if (confirmationToken != null) {
            tokenRepository.delete(confirmationToken);
        } else {
            throw new IllegalArgumentException("Invalid or expired token.");
        }
    }

    public void resetPassword(String email) throws Exception {
        Token resetPwdToken;

        var user = thirdPartyRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No account associated with this email address"));

        Type resetTokenType = typeRepository.findOneByParentCodeTypeAndCodeType("TOKEN", "RESET");
        Token existingToken = tokenRepository.findByThirdPartyIdThirdPartyAndTokenType(user.getIdThirdParty(), resetTokenType);
        if (existingToken != null) {
            if (existingToken.getExpirationDate().compareTo(new Date()) > 0) {
                resetPwdToken = existingToken;
            } else {
                resetPwdToken = updateToken(existingToken, resetTokenType);
            }
        } else {
            resetPwdToken = createToken(user, resetTokenType);
        }

        String name = "";
        if (user.getDiscriminator().equals("physical")) {
            name = ((Physical) user).getFirstname();
        } else if (user.getDiscriminator().equals("legal")) {
            name = ((Legal) user).getLegalName();
        }
        emailSenderService.sendPasswordResetMail(user.getEmail(), resetPwdToken.getToken(), name);
    }

    public Token createToken(ThirdParty thirdParty, Type tokenType) {
        Token confirmationToken = new Token(
                thirdParty,
                tokenType,
                tokenType.getCodeType().equals("REFRESH") ? this.refreshTokenExpirationTimeInDays : this.confirmationTokenExpirationTimeInMinutes
        );
        return tokenRepository.save(confirmationToken);
    }

    public Token updateToken(Token confirmationToken, Type tokenType) {
        confirmationToken.updateToken(tokenType.getCodeType().equals("REFRESH") ? this.refreshTokenExpirationTimeInDays : this.confirmationTokenExpirationTimeInMinutes);
        return tokenRepository.save(confirmationToken);
    }

    public boolean isValidToken(String resetPwdToken) {
        Token existingToken = tokenRepository.findByToken(resetPwdToken);
        if (existingToken != null && existingToken.getExpirationDate().compareTo(new Date()) > 0) {
            return true;
        } else return false;
    }

    public boolean isValidConfirmationToken(String confirm) {
        Token existingToken = tokenRepository.findByToken(confirm);
        if (existingToken != null && existingToken.getExpirationDate().compareTo(new Date()) > 0 && existingToken.getTokenType().getCodeType() == "CONFIRMATION") {
            return true;
        } else return false;
    }

    public String refreshToken(String refreshToken) throws JsonProcessingException {
        if (isValidToken(refreshToken)) {
            ThirdParty thirdParty = tokenRepository.findThirdPartyByRefreshToken(refreshToken);
            // Generate a new access token
            return jwtService.generateToken(thirdParty);
        }
        return refreshToken;
    }

    private String generatePassword() {
        // Define sets of characters for different categories
        String specialCharacters = "!@#$%^&*";
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        // Combine all the character sets
        String allCharacters = specialCharacters + uppercaseLetters + lowercaseLetters + numbers;

        // Create a SecureRandom instance for generating random indices
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int randomIndex = random.nextInt(allCharacters.length());
            char randomChar = allCharacters.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }

    public Optional<ThirdParty> UpdateThirdParty(Long id, UpdateRequest updatedThirdParty) {
        thirdPartyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        if (updatedThirdParty.getDiscriminator().equals("physical") && !updatedThirdParty.getFirstname().isEmpty() && !updatedThirdParty.getLastname().isEmpty() && !updatedThirdParty.getPhoneNumber().isEmpty()) {
            ThirdParty physical = physicalRepository.findByNic(updatedThirdParty.getNic()).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
            if (!updatedThirdParty.getCurrentPassword().isEmpty() && !updatedThirdParty.getNewPassword().isEmpty()) {
                if (!passwordEncoder.matches(updatedThirdParty.getCurrentPassword(), physical.getPassword())) {
                    throw new IllegalArgumentException("Incorrect Current Password");
                } else if (!passwordEncoder.matches(updatedThirdParty.getNewPassword(), physical.getPassword())) {
                    var user = Physical.builder()
                            .idThirdParty(id)
                            .nic(updatedThirdParty.getNic())
                            .email(updatedThirdParty.getEmail())
                            .firstname(updatedThirdParty.getFirstname())
                            .lastname(updatedThirdParty.getLastname())
                            .gender(updatedThirdParty.getGender())
                            .password(passwordEncoder.encode(updatedThirdParty.getNewPassword()))
                            .phoneNumber(updatedThirdParty.getPhoneNumber())
                            .thirdPartyRole(physical.getThirdPartyRole())
                            .discriminator(updatedThirdParty.getDiscriminator())
                            .clients(physical.getClients())
                            .accountants(physical.getAccountants())
                            .build();
                    try {
                        physicalRepository.save(user);
                        return Optional.of(user);
                    } catch (DataIntegrityViolationException e) {
                        throw new IncompleteDataException();
                    }
                } else {
                    throw new IllegalArgumentException("Your New Password is the Same As the Old One, Please Use An other Password");
                }
            } else {
                var user = Physical.builder()
                        .idThirdParty(id)
                        .nic(updatedThirdParty.getNic())
                        .email(updatedThirdParty.getEmail())
                        .firstname(updatedThirdParty.getFirstname())
                        .lastname(updatedThirdParty.getLastname())
                        .password(physical.getPassword())
                        .gender(updatedThirdParty.getGender())
                        .phoneNumber(updatedThirdParty.getPhoneNumber())
                        .discriminator(updatedThirdParty.getDiscriminator())
                        .thirdPartyRole(physical.getThirdPartyRole())
                        .clients(physical.getClients())
                        .accountants(physical.getAccountants())
                        .build();
                try {
                    physicalRepository.save(user);
                    return Optional.of(user);
                } catch (DataIntegrityViolationException e) {
                    throw new IncompleteDataException();
                }
            }
        } else if (updatedThirdParty.getDiscriminator().equals("legal") && !updatedThirdParty.getLegalName().isEmpty() && !updatedThirdParty.getPhoneNumber().isEmpty()) {

            ThirdParty legal = legalRepository.findBySiretNumber(updatedThirdParty.getSiretNumber()).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
            if (!updatedThirdParty.getCurrentPassword().isEmpty() && !updatedThirdParty.getNewPassword().isEmpty()) {
                if (!passwordEncoder.matches(updatedThirdParty.getNewPassword(), legal.getPassword())) {
                    var user = Legal.builder()
                            .idThirdParty(id)
                            .email(updatedThirdParty.getEmail())
                            .siretNumber(updatedThirdParty.getSiretNumber())
                            .legalName(updatedThirdParty.getLegalName())
                            .password(passwordEncoder.encode(updatedThirdParty.getNewPassword()))
                            .phoneNumber(updatedThirdParty.getPhoneNumber())
                            .discriminator(updatedThirdParty.getDiscriminator())
                            .thirdPartyRole(legal.getThirdPartyRole())
                            .clients(legal.getClients())
                            .accountants(legal.getAccountants())
                            .build();
                    try {
                        legalRepository.save(user);
                        return Optional.of(user);
                    } catch (DataIntegrityViolationException e) {
                        throw new IncompleteDataException();
                    }
                } else {
                    throw new IllegalArgumentException("Your New Password is the Same As the Old One, Please Use An other Password");
                }
            } else {
                var user = Legal.builder()
                        .idThirdParty(id)
                        .email(updatedThirdParty.getEmail())
                        .siretNumber(updatedThirdParty.getSiretNumber())
                        .legalName(updatedThirdParty.getLegalName())
                        .password(legal.getPassword())
                        .phoneNumber(updatedThirdParty.getPhoneNumber())
                        .discriminator(updatedThirdParty.getDiscriminator())
                        .thirdPartyRole(legal.getThirdPartyRole())
                        .clients(legal.getClients())
                        .accountants(legal.getAccountants())
                        .build();
                try {
                    legalRepository.save(user);
                    return Optional.of(user);
                } catch (DataIntegrityViolationException e) {
                    throw new IncompleteDataException();
                }
            }
        } else {
            throw new IncompleteDataException();
        }
    }

}