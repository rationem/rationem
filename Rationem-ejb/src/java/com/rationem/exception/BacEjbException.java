/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.exception;

import javax.ejb.EJBException;


/**
 *
 * @author Chris
 */
public class BacEjbException extends EJBException implements BacExceptionInterface {

 private String errorCode;

 public BacEjbException(String errorCode, String message) {
  super(message);
  this.errorCode = errorCode;
 }

 public BacEjbException() {
 }


 @Override
 public String getErrorCode() {
  return errorCode;
 }

 @Override
 public void setErrorCode(String errCode) {
  errorCode = errCode;

 }
}
