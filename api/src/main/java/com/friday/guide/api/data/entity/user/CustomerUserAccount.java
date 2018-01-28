package com.friday.guide.api.data.entity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Artem
 */
@Entity
@Getter
@Setter
@Table(name = "customer")
public class CustomerUserAccount {

    @Column(name = "token")
    private String token;

    @Embedded
    private AccountSettings accountSettings;


}
