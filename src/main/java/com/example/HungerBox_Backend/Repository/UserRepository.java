package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for handling CRUD operations related to User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User entity by its email ID.
     *
     * @param emailId the email ID of the user to find
     * @return the User entity with the specified email ID, or null if not found
     */
    User findByEmailId(String emailId);
}
