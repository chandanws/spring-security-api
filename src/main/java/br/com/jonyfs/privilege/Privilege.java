package br.com.jonyfs.privilege;

import br.com.jonyfs.role.Role;
import br.com.jonyfs.user.User;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractAuditable;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Privilege extends AbstractAuditable<User, Long> {

	private String name;

	@ManyToMany(mappedBy = "privileges")
	private Collection<Role> roles;
}
