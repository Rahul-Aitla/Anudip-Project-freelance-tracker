package com.anudip.tracker.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PortalLinkUtil {
    private static final String DEFAULT_PORTAL_SECRET = "freelancer-portal-v1-change-me";

    private PortalLinkUtil() {
    }

    public static String generateToken(int projectId, int userId) {
        String raw = projectId + ":" + userId + ":" + resolveSecret();
        return sha256Hex(raw);
    }

    public static boolean isTokenValid(String token, int projectId, int userId) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        String expected = generateToken(projectId, userId);
        return MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), token.trim().getBytes(StandardCharsets.UTF_8));
    }

    private static String resolveSecret() {
        String env = System.getenv("PORTAL_SECRET");
        if (env != null && !env.trim().isEmpty()) {
            return env.trim();
        }

        String property = System.getProperty("portal.secret");
        if (property != null && !property.trim().isEmpty()) {
            return property.trim();
        }

        return DEFAULT_PORTAL_SECRET;
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm not available", ex);
        }
    }
}
