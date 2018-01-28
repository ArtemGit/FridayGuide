package com.friday.guide.api.data.entity.base;

import org.hibernate.proxy.HibernateProxy;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

    public static final int BATCH_SIZE = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        // need to get superclass while comparing entity with Hibernate proxy
        // Super class is applicable because we are use CGLIB proxying.
        Class<?> objClass = (obj instanceof HibernateProxy)
                ? obj.getClass().getSuperclass()
                : obj.getClass();
        if (getClass() != objClass)
            return false;
        BaseEntity other = (BaseEntity) obj;
        if (this.getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!this.getId().equals(other.getId()))
            return false;
        return true;
    }
}
