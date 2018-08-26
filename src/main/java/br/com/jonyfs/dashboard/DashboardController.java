package br.com.jonyfs.dashboard;

import br.com.jonyfs.team.TeamRepository;
import br.com.jonyfs.user.User;
import br.com.jonyfs.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardDTO> me() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User loggedUser = userRepository.findByEmail(username);

        return ResponseEntity
            .ok(
                DashboardDTO
                    .builder()
                    .me(loggedUser)
                    .teamsIHaveCreated(teamRepository.findByCreatedBy(loggedUser))
                    .teamsIWasAssociated(teamRepository.findByUsersContaining(loggedUser))
                    .build()
            );

    }

}
