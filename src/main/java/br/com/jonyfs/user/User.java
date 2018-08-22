package br.com.jonyfs.user;

import br.com.jonyfs.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
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
public class User extends AbstractAuditable<User, Long> {

    @NotEmpty
    @Email
    String email;

    @JsonIgnore
    @NotEmpty
    String password;

    @NotEmpty
    String firstName;

    @NotEmpty
    String lastName;

    boolean enabled;

    boolean tokenExpired;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;
}
