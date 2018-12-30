/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.exception;

import javax.persistence.PersistenceException;

/**
 *
 * @author Chris
 */
public class BacPersistenceException extends PersistenceException implements BacExceptionInterface {

 private String errorCode;

 @Override
 public String getErrorCode() {
  return errorCode;
 }

 @Override
 public void setErrorCode(String errCode) {
  errorCode = errCode;

 }
}
