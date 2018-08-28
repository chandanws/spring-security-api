package br.com.jonyfs.privilege;

import br.com.jonyfs.role.Role;
import br.com.jonyfs.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToMany;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Privilege extends AbstractAuditable<User, Long> {

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;
}
