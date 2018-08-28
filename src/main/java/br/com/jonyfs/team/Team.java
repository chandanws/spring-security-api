package br.com.jonyfs.team;

import br.com.jonyfs.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team extends AbstractAuditable<User, Long> implements Serializable {

    private static final long serialVersionUID = 7939574994859410419L;

    //@NotEmpty
    @Column(unique = true)
    private String name;

    @Nullable
    @ManyToOne
    private Team parent;

    @OneToMany(mappedBy = "parent")
    private Set<Team> children = new HashSet<>();

    @ManyToMany(mappedBy = "teams")
    private Set<User> users = new HashSet<>();

    @PrePersist
    public void prePersist() {
        checkChildren();
    }

    @PreUpdate
    public void preUpdate() {
        checkChildren();
    }

    private void checkChildren() {
        children
            .stream()
            .filter(child -> Objects.isNull(child.parent)).forEach(child -> child.setParent(this));
    }

}
