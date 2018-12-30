/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import java.io.Serializable;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
public class MenuBean implements Serializable {
 private static final Logger logger =
            Logger.getLogger("com.rationem.util.MenuBean");
private String shortCutCode;
 /**
  * Creates a new instance of MenuBean
  */
 public MenuBean() {
 }

 public String getShortCutCode() {
  return shortCutCode;
 }

 public void setShortCutCode(String shortCutCode) {
  this.shortCutCode = shortCutCode;
 }
 
 
 public String onShortCutAction(){
  logger.log(INFO, "shortCut is {0} ",shortCutCode);
  shortCutCode =  shortCutCode;
  logger.log(INFO, "shortCut is {0} ",shortCutCode);
  return shortCutCode;
 }
 
}
