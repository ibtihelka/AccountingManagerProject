package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.ThirdPartyNoSql;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("nosql")
public interface ThirdPartyRepositoryNoSql extends MongoRepository<ThirdPartyNoSql, String> {

    Optional<ThirdPartyNoSql> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<ThirdPartyNoSql> findByPhoneNumber(String phoneNumber);

    @Query(value = "{ '$and': [ " +
            " { '$or': [ " +
            "   { 'firstname': { '$regex': ?0, '$options': 'i' } }, " +
            "   { 'lastname': { '$regex': ?0, '$options': 'i' } }, " +
            "   { 'legalName': { '$regex': ?0, '$options': 'i' } }, " +
            "   { 'email': { '$regex': ?0, '$options': 'i' } }, " +
            "   { 'nic': { '$regex': ?0, '$options': 'i' } }, " +
            "   { 'siretNumber': { '$regex': ?0, '$options': 'i' } } " +
            " ] }, " +
            " { 'discriminator': { '$regex': ?1, '$options': 'i' } } " +
            "] }")
    Page<ThirdPartyNoSql> findThirdParties(String query, String discriminator, Pageable pageable);


    @Query("{ 'accountants.clientId': ?0, " +
            "$or: [ { 'firstname': { $regex: ?1, $options: 'i' } }, " +
            "{ 'legalName': { $regex: ?1, $options: 'i' } }, " +
            "{ 'email': { $regex: ?1, $options: 'i' } }, " +
            "{ 'nic': { $regex: ?1, $options: 'i' } }, " +
            "{ 'siretNumber': { $regex: ?1, $options: 'i' } } ] }")
    Page<ThirdPartyNoSql> findAccountantClients(String accountantId, String query, Pageable pageable);

    @Query("{ 'accountants.accountantId': ?0, " +
            "$or: [ { 'firstname': { $regex: ?1, $options: 'i' } }, " +
            "{ 'legalName': { $regex: ?1, $options: 'i' } }, " +
            "{ 'email': { $regex: ?1, $options: 'i' } }, " +
            "{ 'nic': { $regex: ?1, $options: 'i' } }, " +
            "{ 'siretNumber': { $regex: ?1, $options: 'i' } } ] }")
    Page<ThirdPartyNoSql> findClientAccountants(String clientId, String query, Pageable pageable);

    @Query("{ 'role': 'CLIENT' }")
    Long countClients();

    @Query("{ 'role': 'ACCOUNTANT' }")
    Long countAccountants();

    @Query("{ 'accountants.accountantId': ?0 }")
    Long countClientsByAccountantId(String accountantId);

    @Query("{ 'accountants.clientId': ?0 }")
    Long countAccountantsByClientId(String clientId);



}
