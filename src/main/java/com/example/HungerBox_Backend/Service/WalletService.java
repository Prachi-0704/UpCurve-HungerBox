package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.Wallet;

public interface WalletService {

    public Wallet createWallet(long userId) throws Exception;

    public void addBalance(long userId,double amount) throws Exception;

    public void updateBalance(long userId, double amount) throws Exception;

    public Wallet getOrCreateWallet(long userId) throws Exception;

}
