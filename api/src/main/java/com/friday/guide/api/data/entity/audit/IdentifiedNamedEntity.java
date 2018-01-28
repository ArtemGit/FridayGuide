package com.friday.guide.api.data.entity.audit;

import lombok.*;
import org.apache.ibatis.annotations.ConstructorArgs;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class IdentifiedNamedEntity implements Serializable {

    private static final long serialVersionUID = -1027823773585148278L;

    private Long id;
    private String name;
}
