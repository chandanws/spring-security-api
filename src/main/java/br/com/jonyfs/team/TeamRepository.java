package br.com.jonyfs.team;

import br.com.jonyfs.user.User;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {

    Team findByName(String name);

    List<Team> findByCreatedBy(User user);

    //List<Team> findByChildrenIsNotNullAndChildrenUsersContaining(User user);

}
