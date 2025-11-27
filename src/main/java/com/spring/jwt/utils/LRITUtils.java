package com.spring.jwt.utils;

import com.spring.jwt.entity.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

@Component
public class LRITUtils implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        List<String> rolesToAdd = new ArrayList<>();
        rolesToAdd.add("USER");
        rolesToAdd.add("ADMIN");
        rolesToAdd.add("DEALER");
        rolesToAdd.add("INSPECTOR");
        rolesToAdd.add("SALESPERSON");

        List<String> existingRoles = entityManager.createQuery("SELECT r.name FROM Role r", String.class).getResultList();

        rolesToAdd.stream()
                .filter(role -> !existingRoles.contains(role))
                .forEach(role -> entityManager.persist(new Role(null, role)));

        existingRoles.stream()
                .filter(role -> !rolesToAdd.contains(role))
                .forEach(role -> {
                    Role roleEntity = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :roleName", Role.class)
                            .setParameter("roleName", role)
                            .getSingleResult();
                    entityManager.remove(roleEntity);
                });
    }
}
