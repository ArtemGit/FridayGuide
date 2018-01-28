package com.friday.guide.api.data.entity.chekIn;


import com.friday.guide.api.data.entity.base.BaseEntity;
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
public class CheckIn extends BaseEntity {

    @Column(name = "ip")
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
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    /*@ManyToOne(fetch = FetchType.EAGER)
    @Column(name = "account_id", nullable = false)
    private CustomerAccount account;*/

}