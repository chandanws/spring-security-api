package br.com.jonyfs;

import br.com.jonyfs.privilege.Privilege;
import br.com.jonyfs.privilege.PrivilegeRepository;
import br.com.jonyfs.role.Role;
import br.com.jonyfs.role.RoleRepository;
import br.com.jonyfs.user.UserDto;
import br.com.jonyfs.user.UserRepository;
import br.com.jonyfs.user.UserService;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class BasicTests {

    public static boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Before
    public void createUsersRolesAndPrivileges() {
        if (alreadySetup) {
            return;
        }
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        Role userRole = createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        UserDto user = UserDto
                .builder()
                .firstName("user")
                .lastName("user")
                .password("password")
                .email("user@test.com")
                .build();
        UserDto admin = UserDto
                .builder()
                .firstName("admin")
                .lastName("admin")
                .password("password")
                .email("admin@test.com")
                .build();

        LOGGER.info("Registering a new user: {}...", user);
        userService.registerNewUserAccount(user, userRole);
        LOGGER.info("OK.");

        LOGGER.info("Registering a new user: {}...", admin);
        userService.registerNewUserAccount(admin, adminRole);
        LOGGER.info("OK.");

        alreadySetup = true;

    }

    @Transactional
    protected Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = Privilege.builder().name(name).build();
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    protected Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = Role.builder().name(name).build();
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}
