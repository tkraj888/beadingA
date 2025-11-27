package com.spring.jwt.userForm.repository;

import com.spring.jwt.userForm.entity.UserForm;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;



import java.util.List;

@Repository
public interface UserFormRepo extends JpaRepository<UserForm, Integer> {


    Page<UserForm> findByInspectorIdOrderByUserFormIdDesc(Integer inspectorId, Pageable pageable);

    List<UserForm> findByStatusOrderByUserFormIdDesc(String status);

    Page<UserForm> findBySalesPersonIdOrderByUserFormIdDesc(Integer salesPersonId, Pageable pageable);


    List<UserForm> findByUserIdOrderByUserFormIdDesc(Integer userId);

    Page<UserForm> findAllByOrderByUserFormIdDesc(Pageable pageable);

}
