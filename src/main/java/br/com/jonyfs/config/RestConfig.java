package br.com.jonyfs.config;

import br.com.jonyfs.privilege.Privilege;
import br.com.jonyfs.role.Role;
import br.com.jonyfs.team.Team;
import br.com.jonyfs.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RestConfig {

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {

        return new RepositoryRestConfigurerAdapter() {
            @Override
            public void configureRepositoryRestConfiguration(
                RepositoryRestConfiguration config) {
                config.exposeIdsFor(Role.class, Privilege.class, User.class, Team.class);
            }
        };
    }

}
