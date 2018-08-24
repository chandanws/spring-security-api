package br.com.jonyfs.dashboard;

import br.com.jonyfs.team.Team;
import br.com.jonyfs.user.User;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DashboardDTO {

    User me;

    List<Team> teamsIHaveCreated;

    List<Team> teamsIWasAssociated;

}
