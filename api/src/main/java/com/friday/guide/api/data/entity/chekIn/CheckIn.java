package com.friday.guide.api.data.entity.chekIn;


import com.friday.guide.api.data.entity.audit.AuditedEntity;
import com.friday.guide.api.data.entity.user.CustomerUserAccount;
import com.friday.guide.api.hibernate.type.CryptStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Artem
 */
@Entity
@Getter
@Setter
@Table(name = "check_in",
        indexes = {
                @Index(columnList = "longitude, latitude")
        })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CheckIn extends AuditedEntity {

    @Column(name = "ip")
    @Type(type = CryptStringType.NAME)
    private String ip;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "city")
    private String city;

    @Column(name = "region")
    private String region;

    @Column(name = "country")
    private String country;

    @Column(name = "longitude")
    @Type(type = CryptStringType.NAME)
    private String longitude;

    @Column(name = "latitude")
    @Type(type = CryptStringType.NAME)
    private String latitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @Column(name = "account_id", nullable = false)
    private CustomerUserAccount account;

}