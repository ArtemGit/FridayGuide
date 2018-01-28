package com.friday.guide.api.data.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class ActivatedEntity extends BaseEntity {

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private boolean active;

}
