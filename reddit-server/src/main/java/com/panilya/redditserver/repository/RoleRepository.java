package com.panilya.redditserver.repository;

import com.panilya.redditserver.model.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<UserRole, Long> {
    UserRole findByName(String name);
}
