package com.ffhs.referencease.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Hilfsklasse zur Handhabung von Passworthashes. Diese Klasse bietet statische Methoden, um
 * Passwörter zu hashen und die Übereinstimmung von Passworthashes zu überprüfen. Sie nutzt Apache
 * Commons Codec für die Hash-Erstellung. Der Konstruktor ist privat, da diese Klasse nicht
 * instanziiert werden soll.
 */
public class PWHash {

  /**
   * Privater Konstruktor, der eine Ausnahme auslöst, um die Instanziierung der Klasse zu
   * verhindern.
   *
   * @throws IllegalStateException wenn versucht wird, die Klasse zu instanzieren.
   */
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
