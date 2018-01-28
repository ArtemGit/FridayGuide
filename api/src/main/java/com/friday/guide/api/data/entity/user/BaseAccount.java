package com.friday.guide.api.data.entity.user;

import com.friday.guide.api.data.entity.base.ActivatedEntity;
import com.friday.guide.api.hibernate.type.CryptStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseAccount extends ActivatedEntity {

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "ip", nullable = false, unique = true)
    @Type(type = CryptStringType.NAME)
    private String ip;

    @Column(name = "password", nullable = false)
    private String password;
}
