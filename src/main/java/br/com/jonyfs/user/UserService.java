package br.com.jonyfs.user;

import br.com.jonyfs.role.Role;

public interface UserService {

    User registerNewUserAccount(UserDto accountDto, Role... roles);
}
