package com.friday.guide.api.data.entity.user;

import com.friday.guide.api.data.entity.base.ActivatedEntity;
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
public class CustomerUserAccount extends ActivatedEntity {

    @Column(name = "token", nullable = false)
    private String token;

    @Embedded
    private AccountSettings accountSettings;

}
