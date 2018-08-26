package br.com.jonyfs.team;

import br.com.jonyfs.user.User;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Team extends AbstractAuditable<User, Long> implements Serializable {

    private static final long serialVersionUID = 7939574994859410419L;

    @NotEmpty
    @Column(unique = true)
    private String name;

    @ManyToOne
    private Team parent;

    @OneToMany(mappedBy = "parent")
    private Set<Team> children = new HashSet<>();

    @ManyToMany(mappedBy = "teams")
    private Set<User> users = new HashSet<>();

    private void checkChildren() {
        if (this.children == null) {
            this.children = new HashSet<>();
        }

    }

    public void add(Team child) {
        checkChildren();
        child.setParent(this);
        children.add(child);
    }

    public void add(List<Team> teams) {
        for (Team team : teams) {
            add(team);
        }
    }
}
