package com.ffhs.referencease.utils;

import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2Hash {

  public static String createHash(String password) {
    byte[] salt = "12345678".getBytes();

    try {
      KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      byte[] hash = factory.generateSecret(spec).getEncoded();
      return Base64.getEncoder().encodeToString(hash);
    } catch (Exception ignored) {
      // TODO: what happens if pw could not be encrypted?
    }
    throw new IllegalArgumentException();
  }

  public static boolean checkPassword(String hash1, String hash2) {
    //        String hashInsertedPassword = PBKDF2Hash.createHash(password);
    return hash1.equals(hash2);
  }
}
