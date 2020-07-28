package com.example.roleauth.dao;

import com.example.roleauth.entity.Role;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository("roleRepository")
public class RoleRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleRepository.class);

    @Autowired
    private EntityManager entityManager;

    public Session getSession(){
        return entityManager.unwrap(Session.class);
    }

    public List<Role> getUserRoles(Long userId){
        LOGGER.info("userId {} ",userId);
        Query query = entityManager.createNativeQuery("select r.id,r.name from roles as r INNER JOIN users_roles as ur ON ur.role_id=r.id where ur.user_id = ?;",Role.class);
        query.setParameter(1,userId);
        return query.getResultList();
    }
}
