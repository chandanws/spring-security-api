package br.com.jonyfs.team;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {

    Team findByName(String name);

    @Query("select o from Team o where o.createdBy.email = ?#{principal.email}")
    List<Team> findTeamsCreatedByCurrentUser();

}
