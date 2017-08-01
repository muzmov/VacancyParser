package net.kuryshev.model.dao;

import net.kuryshev.model.entity.User;
import net.kuryshev.model.entity.UserRole;

public abstract class UserDao extends Dao {

    public abstract UserRole getRole(User user);

    public abstract void addUser(User user);

    public abstract void deleteUser(String login);
}
