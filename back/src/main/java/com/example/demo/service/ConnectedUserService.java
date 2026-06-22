package com.example.demo.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class ConnectedUserService {
    
    // Set thread-safe des userId connectés
    private final Set<Integer> connectedUsers = ConcurrentHashMap.newKeySet();

    public void userConnected(Integer userId) {
        connectedUsers.add(userId);
        System.out.println("✅ User connecté : " + userId);
    }

    public void userDisconnected(Integer userId) {
        connectedUsers.remove(userId);
        System.out.println("🔴 User déconnecté : " + userId);
    }

    public Set<Integer> getConnectedUsers() {
        return connectedUsers;
    }}