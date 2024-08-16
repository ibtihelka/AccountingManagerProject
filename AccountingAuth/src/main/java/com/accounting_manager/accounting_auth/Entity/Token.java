package com.accounting_manager.accounting_auth.Entity;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
//THIS ENTITY IS USED FOR REFRESH AND RESET PWD TOKENS
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private long tokenId;

    @Column(name = "token")
    private String token;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "fk_token_type",foreignKey=@ForeignKey(name = "fk_type_of_token"))
    private Type tokenType;

    @OneToOne(targetEntity = ThirdParty.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "fk_third_party", foreignKey = @ForeignKey(name = "fk_thirdparty_of_token"))
    private ThirdParty thirdParty;

    public Token(ThirdParty thirdParty, Type tokenType, int expirationTimeInMinutes) {
        this.thirdParty = thirdParty;
        this.tokenType = tokenType;
        this.creationDate = new Date();
        this.expirationDate = calculateExpiryDate(expirationTimeInMinutes);
        this.token = UUID.randomUUID().toString();
    }

    public Token updateToken(int expirationTimeInMinutes) {
        this.creationDate = new Date();
        this.expirationDate = calculateExpiryDate(expirationTimeInMinutes);
        this.token = UUID.randomUUID().toString();
        return this;
    }

    private Date calculateExpiryDate(int expirationTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.DATE, 7);
        return new Date(calendar.getTime().getTime());
    }

}