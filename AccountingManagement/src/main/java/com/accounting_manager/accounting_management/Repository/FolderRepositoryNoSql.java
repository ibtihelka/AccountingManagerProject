package com.accounting_manager.accounting_management.Repository;

import com.accounting_manager.accounting_management.Entity.FolderNoSql;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("nosql")
public interface FolderRepositoryNoSql extends MongoRepository<FolderNoSql, String> {

    @Query("{ 'deleted': false, " +
            "'$and': [" +
            "    {" +
            "        '$or': [" +
            "            { 'archived': true, 'creator.$id': ?0 }, " +
            "            { 'creator.$id': ?0, 'client.$id': ?0 }, " +
            "            { 'archived': false, '$or': [ { 'creator.$id': ?0 }, { 'client.$id': ?0 } ] } " +
            "        ]" +
            "    }," +
            "    {" +
            "        '$or': [" +
            "            { 'status': ?#{[2].equals(\"open\") ? true : null} }, " +
            "            { 'status': ?#{[2].equals(\"closed\") ? false : null} }, " +
            "            { 'favorite': ?#{[2].equals(\"favorite\") ? true : null} }" +
            "        ]" +
            "    }," +
            "    {" +
            "        '$or': [" +
            "            { 'name': { '$regex': ?1, '$options': 'i' } }," +
            "            { 'client': { '$exists': true, '$regex': ?1, '$options': 'i' } }" + // Pour correspondre à Legal ou Physical
            "        ]" +
            "    }" +
            "]," +
            "'$orderby': { 'favorite': -1, 'creationDate': -1 }" +
            "}")
    Page<FolderNoSql> findMyFolders(String idThirdParty, String query, String type, Pageable pageable);

    @Query("{ '$or': [ " +
            "   { 'client.$id': ?0, 'creator.$id': ?0 }, " +
            "   { 'creator.$id': ?0, 'client.$id': ?0 } " +
            "], 'deleted': false, '$where': '?3 != null ? (this.name.indexOf(?3) != -1) : true'}")
    Page<FolderNoSql> findMyClientFolders(String idThirdParty, String idClient, String query, Pageable pageable);

    @Query("{ 'creator.$id': ?1, 'client.$id': ?0, 'deleted': false, '$where': '?3 != null ? (this.name.indexOf(?3) != -1) : true'}")
    Page<FolderNoSql> findMyAccountantFolders(String idThirdParty, String idAccountant, String query, Pageable pageable);

    @Query("{ 'archived': { $eq: true }, " +
            "'$where': '?3 == null || this.status == ?3 || this.favorite == ?3', " +
            "'$or': [ { 'creator.$id': ?0 }, { 'client.$id': ?0 } ] }")
    Page<FolderNoSql> findFoldersAsAdmin(String idThirdParty, String query, String type, Pageable pageable);

    @Query(value = "{ 'idFolder': ?0 }", delete = true)
    void markFolderAsDeleted(String idFolder);

    Long countByStatus(boolean status);

    @Query(value = "{ 'deleted': false, '$or': [ { 'creator.$id': ?0 }, { 'client.$id': ?0 } ] }", count = true)
    Long getTotalFoldersByThirdPartyId(String idThirdParty);
}
