package com.friday.guide.api.data.entity.user;

import com.friday.guide.api.data.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseAccount extends BaseEntity {
    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;
}
