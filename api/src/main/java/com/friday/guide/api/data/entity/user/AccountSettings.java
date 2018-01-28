package com.friday.guide.api.data.entity.user;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @Author Artem
 */
@Embeddable
@Getter
@Setter
public class AccountSettings {
    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_phone")
    private String mobilePhone;
}
