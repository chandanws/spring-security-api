package br.com.jonyfs.privilege;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface PrivilegeRepository extends PagingAndSortingRepository<Privilege, Long> {

	Privilege findByName(String name);
}
