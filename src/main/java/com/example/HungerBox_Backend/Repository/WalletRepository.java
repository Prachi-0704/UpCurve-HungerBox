package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for handling CRUD operations related to Wallet entities.
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    /**
     * Finds a Wallet entity by the user ID.
     *
     * @param userId the ID of the user associated with the wallet
     * @return the Wallet entity associated with the specified user ID, or null if not found
     */
    Wallet findByUserUserId(long userId);
}
