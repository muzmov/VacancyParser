package net.kuryshev.model.dao;

import net.kuryshev.model.entity.User;
import net.kuryshev.model.entity.UserRole;

public interface UserDao {

    UserRole getRole(User user);

    void addUser(User user);

    void deleteUser(String login);
}
