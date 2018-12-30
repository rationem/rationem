/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Chris
 */
public class DigestUtil {
 public static String generateMD5(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(value.getBytes());

            BigInteger number = new BigInteger(1, messageDigest);

            return number.toString(16);
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }

 public static String generateSHA256(String value)  {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
         try{   
          byte[] hash = md.digest(value.getBytes("UTF-8"));
          StringBuffer hexString = new StringBuffer();

          for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
         }

         return hexString.toString();
        }catch(UnsupportedEncodingException ex){
          return null;
        }
       } catch (NoSuchAlgorithmException nsae) {
           return null;
       }
    }
 
}
