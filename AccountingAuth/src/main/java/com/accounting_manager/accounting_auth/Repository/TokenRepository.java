package com.accounting_manager.accounting_auth.Repository;

import com.accounting_manager.accounting_auth.Entity.ThirdParty;
import com.accounting_manager.accounting_auth.Entity.Token;
import com.accounting_manager.accounting_auth.Entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository
        extends JpaRepository<Token, Long> {

    Token findByToken(String token);

    Token findByThirdPartyIdThirdPartyAndTokenType(Long idThirdParty, Type tokenType);

    @Query("SELECT t.thirdParty " +
            "FROM Token t " +
            "WHERE t.token=?1 " +
            "AND t.tokenType.parentType.codeType = 'TOKEN' " +
            "AND t.tokenType.codeType = 'REFRESH'")
    ThirdParty findThirdPartyByRefreshToken(String token);

    @Query("SELECT t " +
            "FROM Token t " +
            "WHERE t.token=?1 " +
            "AND t.tokenType.parentType.codeType = 'TOKEN' " +
            "AND t.tokenType.codeType = 'CONFIRMATION'")
    Token findTokenByConfirmationToken(String token);
}