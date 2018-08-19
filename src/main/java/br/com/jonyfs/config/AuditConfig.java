package br.com.jonyfs.config;

import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.jonyfs.user.User;

@Configuration
@EnableJpaAuditing
@Slf4j
public class AuditConfig {

    @PostConstruct
    public void logMessage() {
        LOGGER.info("STARTED");
    }


    @Bean
    public AuditorAware<User> auditorProvider() {
        return new SecurityAuditor();
    }

    @Bean
    public AuditingEntityListener auditingListener() {
        return new AuditingEntityListener();
    }

    public static class SecurityAuditor implements AuditorAware<User> {

        @Override
        public Optional<User> getCurrentAuditor() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                return Optional.empty();
            } else {
                if (auth.getDetails() instanceof User) {
                    return Optional.ofNullable((User) auth.getDetails());
                } else {
                    return Optional.empty();
                }
            }

        }
    }

}
