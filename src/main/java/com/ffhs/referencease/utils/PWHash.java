package com.ffhs.referencease.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class PWHash {

  private PWHash() {
    throw new IllegalStateException("PWHash class");
  }

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
