package com.friday.guide.api.service.security.user;

import com.friday.guide.api.data.entity.user.BaseAccount;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;


@Getter
public class _User<T extends BaseAccount> extends User {

    private Long accountId;
    private String login;

    public _User(T user, List<GrantedAuthority> authorities) {
        super(user.getLogin(), user.getPassword(), authorities);
        accountId = user.getId();
        login = user.getLogin();
    }
}
