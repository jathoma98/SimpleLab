package com.org.simplelab.database.entities.interfaces;

import com.org.simplelab.database.entities.sql.User;

/**
 * Used to denote that an entity is created by users.
 */
public interface UserCreated {

    User getCreator();

    void setCreator(User creator);

}
