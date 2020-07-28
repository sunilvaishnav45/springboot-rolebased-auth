package com.example.roleauth.dao;

import com.example.roleauth.entity.Role;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class RoleRepository {

    @Autowired
    private EntityManager entityManager;

    public Session getSession(){
        return entityManager.unwrap(Session.class);
    }

    public List<Role> getUserRoles(Long userId){
        Query query = entityManager.createNativeQuery("select * from roles as r INNER JOIN users_roles as ur ON ur.role_id=r.id where ur.user_id = ?;",Role.class);
        query.setParameter(1,userId);
        return query.getResultList();
    }
}
