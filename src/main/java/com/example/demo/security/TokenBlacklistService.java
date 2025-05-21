package com.example.demo.security;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklistService {
    private final ConcurrentHashMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();
    
    /**
     * Add a token to the blacklist with its expiration time
     */
    public void blacklistToken(String token, long expirationTimeMillis) {
        blacklistedTokens.put(token, expirationTimeMillis);
        
        // Cleanup expired tokens periodically (simple implementation)
        cleanupExpiredTokens();
    }
    
    /**
     * Check if a token is blacklisted
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }
    
    /**
     * Clean up expired tokens
     */
    private void cleanupExpiredTokens() {
        long currentTimeMillis = System.currentTimeMillis();
        
        Set<String> tokensToRemove = new HashSet<>();
        
        for (java.util.Map.Entry<String, Long> entry : blacklistedTokens.entrySet()) {
            if (entry.getValue() < currentTimeMillis) {
                tokensToRemove.add(entry.getKey());
            }
        }
        
        for (String token : tokensToRemove) {
            blacklistedTokens.remove(token);
        }
    }
}
