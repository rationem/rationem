/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.manager;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
public class PasswordUtil implements Serializable {
 
 /**
  * 
  */
 private static final long serialVersionUID = 1L;
 private static final Logger logger = Logger.getLogger(PasswordUtil.class.getName());
 private static final int workLoad = 12;
 
  
 public static String generateNewPasswordHash(String password)
   {
  logger.log(INFO, "Plain password {0}", password);
  SecureRandom sr = new SecureRandom();//.getInstance("SHA1PRNG");
  String hashPwd = BCrypt.hashpw(password, BCrypt.gensalt(workLoad,sr));
  
  // check password 
  boolean pwCheck = BCrypt.checkpw(password, hashPwd);
  logger.log(INFO, "pwCheck returns {0}", pwCheck);
  logger.log(INFO, "generateNewPasswordHash returns {0}",hashPwd);
  
  /* 
   
   int iterations = 1000;
  char[] chars = password.toCharArray();
  byte[] salt = getSalt().getBytes();
  String hashPwd = null;
  PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
  SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
  byte[] hash = skf.generateSecret(spec).getEncoded();
  
  */
  return hashPwd;
 }
  
 /*
 public static String getCurrHash(String enteredPw, String salt, int iterations, int storedLen) 
         throws NoSuchAlgorithmException, InvalidKeySpecException {
  PBEKeySpec spec = new PBEKeySpec(enteredPw.toCharArray(), fromHex(salt), iterations, 64 * 8);
  SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
  byte[] hash = skf.generateSecret(spec).getEncoded();
  
  return toHex(hash);
 }
 */
 public static String generateStorngPasswordHash(String password) 
          {
  String passwordHash = PasswordUtil.generateNewPasswordHash(password); 
  logger.log(INFO, "generateStorngPasswordHash hash {0}", passwordHash);
  return passwordHash;
  /* 
  int iterations = 1000;
  char[] chars = password.toCharArray();
  byte[] salt = getSalt().getBytes();

  PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
  SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
  byte[] hash = skf.generateSecret(spec).getEncoded();*/
  
  
  //return iterations + ":" + toHex(salt) + ":" + toHex(hash);
 }

 /*
 public static String getHashForPass(String originalPassword, String salt, int iterations )
 throws NoSuchAlgorithmException, InvalidKeySpecException{
  
  byte[] currHash = null;
  char[] chars = originalPassword.toCharArray();
  byte[] saltBytes = salt.getBytes();
  PBEKeySpec spec = new PBEKeySpec(chars, saltBytes, iterations, 64 * 8);
  SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
  currHash = skf.generateSecret(spec).getEncoded();
  
  return currHash.toString();
  
 } 
 */
 public static boolean validatePassword(String originalPassword, String storedPassword)  {
  boolean validPw = BCrypt.checkpw(originalPassword, storedPassword);
  return validPw;
  /*String[] parts = storedPassword.split(":");
  int iterations = Integer.parseInt(parts[0]);
  byte[] salt = fromHex(parts[1]);
  byte[] hash = fromHex(parts[2]);

  PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
  SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
  byte[] testHash = skf.generateSecret(spec).getEncoded();
  logger.log(INFO, "testHash {0}", testHash);
  int diff = hash.length ^ testHash.length;
  for (int i = 0; i < hash.length && i < testHash.length; i++) {
   diff |= hash[i] ^ testHash[i];
  }
  return diff == 0;
  */
 }

 /*public static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
  byte[] bytes = new byte[hex.length() / 2];
  for (int i = 0; i < bytes.length; i++) {
   bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
  }
  return bytes;
 }
 */

 /*
 private static String getSalt() throws NoSuchAlgorithmException {
  SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
  byte[] salt = new byte[16];
  sr.nextBytes(salt);
  return salt.toString();
 }

 private static String toHex(byte[] array) throws NoSuchAlgorithmException {
  BigInteger bi = new BigInteger(1, array);
  String hex = bi.toString(16);
  int paddingLength = (array.length * 2) - hex.length();
  if (paddingLength > 0) {
   return String.format("%0" + paddingLength + "d", 0) + hex;
  } else {
   return hex;
  }
 }
 */
 
}
