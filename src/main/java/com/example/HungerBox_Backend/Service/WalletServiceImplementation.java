package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Model.Wallet;
import com.example.HungerBox_Backend.Repository.UserRepository;
import com.example.HungerBox_Backend.Repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImplementation implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new wallet for the specified user if it does not already exist.
     *
     * @param userId the ID of the user for whom to create the wallet.
     * @return the newly created Wallet.
     * @throws Exception if the user is not found or the wallet already exists.
     */
    @Override
    public Wallet createWallet(long userId) throws Exception {
        // Check if user exists
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new Exception("User not found for ID: " + userId);
        }

        // Check if wallet already exists for the user
        Wallet existingWallet = walletRepository.findByUserUserId(userId);
        if (existingWallet != null) {
            throw new Exception("Wallet already exists for user ID: " + userId);
        }

        // Create and save new wallet
        Wallet newWallet = new Wallet();
        newWallet.setUser(user);
        newWallet.setPin(""); // Initialize with an empty pin
        walletRepository.save(newWallet);

        return newWallet;
    }

    /**
     * Adds a specified amount to the wallet balance of the user.
     *
     * @param userId the ID of the user.
     * @param amount the amount to add.
     * @throws Exception if the wallet is not found.
     */
    @Override
    public void addBalance(long userId, double amount) throws Exception {
        Wallet wallet = walletRepository.findByUserUserId(userId);
        if (wallet == null) {
            throw new RuntimeException("Wallet not found for user");
        }
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
    }

    /**
     * Updates the wallet balance of the user to a specified amount.
     * Can be used for both adding and deducting balance.
     *
     * @param userId the ID of the user.
     * @param amount the amount to set (can be positive or negative).
     * @throws Exception if the wallet is not found or if there is insufficient balance.
     */
    @Override
    public void updateBalance(long userId, double amount) throws Exception {
        Wallet wallet = walletRepository.findByUserUserId(userId);
        if (wallet == null) {
            throw new RuntimeException("Wallet not found for user");
        }

        // Check if the amount being deducted is valid
        if (amount < 0 && wallet.getBalance() + amount < 0) {
            throw new Exception("Insufficient balance in the wallet");
        }

        // Update the balance
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
    }

    /**
     * Retrieves or creates a wallet for the specified user. Initializes balance if creating a new wallet.
     *
     * @param userId the ID of the user.
     * @return the existing or newly created Wallet.
     * @throws Exception if the user is not found.
     */
    @Override
    public Wallet getOrCreateWallet(long userId) throws Exception {
        // Check if a wallet exists for the user
        Wallet existingWallet = walletRepository.findByUserUserId(userId);
        if (existingWallet != null) {
            return existingWallet;
        }

        // Create a new wallet if none exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        Wallet newWallet = new Wallet();
        newWallet.setUser(user);
        newWallet.setBalance(2000.0); // Initialize balance to 2000
        walletRepository.save(newWallet);

        return newWallet;
    }
}
