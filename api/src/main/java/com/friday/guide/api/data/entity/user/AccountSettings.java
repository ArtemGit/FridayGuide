package com.friday.guide.api.data.entity.user;


import com.friday.guide.api.hibernate.type.CryptStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Artem
 */
@Embeddable
@Getter
@Setter
public class AccountSettings {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    @Type(type = CryptStringType.NAME)
    private String email;

    @Column(name = "mobile_phone", nullable = false)
    @Type(type = CryptStringType.NAME)
    private String mobilePhone;
}
