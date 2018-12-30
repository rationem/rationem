/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.exception;

import javax.ejb.ApplicationException;
import java.util.logging.Logger;



import static java.util.logging.Level.INFO;


/**
 *
 * @author Chris
 */
@ApplicationException(rollback=true)
public class BacException extends RuntimeException{
 private static final Logger logger =
            Logger.getLogger("com.rationem.util.BacException");
 private String errorCode;

 public BacException() {
  super();
 }
    

 public BacException(String message) {
  super(message);
 }

 public BacException(String message, String errorCode) {
  super(message);
 }

 public BacException(String message,Exception ex, String errorCode) {
    this.errorCode = errorCode;
 }



 public BacException(String message, Exception ex) {
  super(message, ex);
 }

 public String getErrorCode() {
  return errorCode;
 }

 public void setErrorCode(String errorCode) {
  this.errorCode = errorCode;
 }

}
