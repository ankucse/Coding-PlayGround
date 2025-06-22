package com.PlayGround.Coding.PlayGround.CRUD.repository;


import com.PlayGround.Coding.PlayGround.CRUD.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}