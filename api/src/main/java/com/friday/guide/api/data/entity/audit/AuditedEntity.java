package com.friday.guide.api.data.entity.audit;

import com.friday.guide.api.data.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditedEntity extends BaseEntity {

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Embedded
    @CreatedBy
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "created_by_id")),
            @AttributeOverride(name = "name", column = @Column(name = "created_by_name")),
    })
    private IdentifiedNamedEntity createdBy;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Embedded
    @LastModifiedBy
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "last_modified_by_id")),
            @AttributeOverride(name = "name", column = @Column(name = "last_modified_by_name")),
    })
    private IdentifiedNamedEntity lastModifiedBy;
}
