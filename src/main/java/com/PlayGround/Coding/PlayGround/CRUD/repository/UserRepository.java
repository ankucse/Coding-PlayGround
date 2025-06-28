package com.PlayGround.Coding.PlayGround.CRUD.repository;


import com.PlayGround.Coding.PlayGround.CRUD.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 * <p>
 * This interface extends {@link JpaRepository}, which provides a rich set of standard
 * CRUD (Create, Read, Update, Delete) operations and pagination/sorting capabilities
 * for the {@code User} entity out of the box. No further implementation is needed for these
 * common operations.
 * <p>
 * The {@code @Repository} annotation indicates that this is a Spring-managed repository bean,
 * enabling component scanning and exception translation from JPA exceptions to Spring's
 * {@code DataAccessException} hierarchy. While optional for interfaces extending JpaRepository
 * in modern Spring versions, it is good practice for clarity.
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.PlayGround.Coding.PlayGround.CRUD.entity.User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}