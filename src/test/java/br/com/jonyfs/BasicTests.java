package br.com.jonyfs;

import br.com.jonyfs.privilege.Privilege;
import br.com.jonyfs.privilege.PrivilegeRepository;
import br.com.jonyfs.role.Role;
import br.com.jonyfs.role.RoleRepository;
import br.com.jonyfs.user.User;
import br.com.jonyfs.user.UserDto;
import br.com.jonyfs.user.UserRepository;
import br.com.jonyfs.user.UserService;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class BasicTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private UserService userService;

    @Transactional
    protected Role createRoleAdminIfNotFound() {

        Privilege readPrivilege = createPrivilegeIfNotFound(Privileges.READ_PRIVILEGE.name());
        Privilege writePrivilege = createPrivilegeIfNotFound(Privileges.WRITE_PRIVILEGE.name());

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        Role adminRole = createRoleIfNotFound(Roles.ROLE_ADMIN.name(), adminPrivileges);
        return adminRole;
    }

    @Transactional
    protected Role createRoleUserIfNotFound() {

        Privilege readPrivilege = createPrivilegeIfNotFound(Privileges.READ_PRIVILEGE.name());

        Role userRole = createRoleIfNotFound(Roles.ROLE_USER.name(), Arrays.asList(readPrivilege));
        return userRole;
    }

    @Transactional
    protected User createUserIfNotFound(String email, Role... roles) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            UserDto userDto = UserDto
                    .builder()
                    .firstName(email.substring(0, email.indexOf("@")))
                    .lastName("Rogers")
                    .password("password")
                    .email(email)
                    .build();

            LOGGER.info("Registering a new user: {}...", userDto);
            user = userService.registerNewUserAccount(userDto, roles);
            LOGGER.info("OK.");
        }
        return user;

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
