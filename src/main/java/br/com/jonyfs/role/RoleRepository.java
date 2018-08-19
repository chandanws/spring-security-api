package br.com.jonyfs.role;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

	Role findByName(String name);
}
