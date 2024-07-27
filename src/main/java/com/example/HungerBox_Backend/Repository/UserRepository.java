package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailId(String emailId);
}
