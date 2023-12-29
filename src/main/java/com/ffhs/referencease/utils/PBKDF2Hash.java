package com.ffhs.referencease.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class PBKDF2Hash {

  //  public static String createHash(String password) {
  //    byte[] salt = "12345678".getBytes();
  //
  //    try {
  //      KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
  //      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
  //      byte[] hash = factory.generateSecret(spec).getEncoded();
  //      return Base64.getEncoder().encodeToString(hash);
  //    } catch (Exception ignored) {
  //      // TODO: what happens if pw could not be encrypted?
  //    }
  //    throw new IllegalArgumentException();
  //  }

  /**
   * Erstellt einen Hash eines Passworts.
   *
   * @param password Das Passwort, das gehasht werden soll.
   * @return Der Hash des Passworts.
   */
  public static String createHash(String password) {
    return DigestUtils.sha256Hex(password);
  }

  /**
   * Überprüft, ob ein Passworthash mit einem anderen Hash übereinstimmt.
   *
   * @param hash1 Der erste Hash.
   * @param hash2 Der zweite Hash.
   * @return true, wenn die Hashes übereinstimmen, sonst false.
   */
  public static boolean checkPassword(String hash1, String hash2) {
    return hash1 != null && hash1.equals(hash2);
  }
}
