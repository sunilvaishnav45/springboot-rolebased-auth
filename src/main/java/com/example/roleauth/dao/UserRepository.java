package com.example.roleauth.dao;

import com.example.roleauth.controller.AuthController;
import com.example.roleauth.entity.User;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository("userRepository")
public class UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    private EntityManager entityManager;

    public Session getSession(){
        return entityManager.unwrap(Session.class);
    }

    public User getUserByUsername(String name){
        Query query = entityManager.createNativeQuery("select * from users where full_name = ?",User.class);
        query.setParameter(1,name);
        User user = (User) query.getResultList().stream().findFirst().get();
       return user;
    }
}