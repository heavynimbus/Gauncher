package gauncher.backend.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordService {
    MessageDigest md;

    public PasswordService() {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String hash(String message) {
        md.update(message.getBytes(StandardCharsets.UTF_8));
        return new String(md.digest());
    }
}
