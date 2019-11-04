package com.webchat.user;

import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.util.DigestFactory;
import org.bouncycastle.util.encoders.Base64;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "users")
public class User {
    private static final int hashLength = 20;

    @Id
    @GeneratedValue
    public UUID id;

    public String username;
    public String email;

    public LocalDateTime creationTimestamp;
    public boolean isActivated;
    public boolean isBanned;

    private String passwordHash;
    private String salt;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        setPassword(password);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        PKCS5S2ParametersGenerator kdf = new PKCS5S2ParametersGenerator(DigestFactory.createSHA256());
        kdf.init(password.getBytes(StandardCharsets.UTF_8), salt, 4096);
        byte[] hash = ((KeyParameter) kdf.generateDerivedMacParameters(8 * hashLength)).getKey();

        passwordHash = Base64.toBase64String(hash);
        this.salt = Base64.toBase64String(salt);
    }

    public boolean verifyPassword(String password) {
        byte[] hash = Base64.decode(passwordHash);
        byte[] salt = Base64.decode(this.salt);

        PKCS5S2ParametersGenerator kdf = new PKCS5S2ParametersGenerator(DigestFactory.createSHA256());
        kdf.init(password.getBytes(StandardCharsets.UTF_8), salt, 4096);
        byte[] hashToCheck = ((KeyParameter) kdf.generateDerivedMacParameters(8 * hashLength)).getKey();

        return hash.equals(hashToCheck);
    }
}
