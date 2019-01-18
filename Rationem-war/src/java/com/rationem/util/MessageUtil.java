/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static javax.faces.application.FacesMessage.SEVERITY_WARN;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import javax.faces.context.FacesContext;

/**
 *
 * @author Chris
 */
public class MessageUtil {

 public static void addClientErrorMessage(String clientId,String str, String resourceName) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  context.addMessage(clientId, new FacesMessage(SEVERITY_ERROR, message, ""));
 }
 
 public static void addClientErrorMessage(String clientId,String str, String resourceName, String param1) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  message += param1;
  context.addMessage(clientId, new FacesMessage(SEVERITY_ERROR, message, ""));
 }
 public static void addClientInfoMessage(String clientId,String str, String resourceName) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  context.addMessage(clientId, new FacesMessage(SEVERITY_INFO, message, ""));
 }
 
 public static void addClientInfoMessage(String clientId,String str, String resourceName,String param1) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  message += param1;
  context.addMessage(clientId, new FacesMessage(SEVERITY_INFO, message, ""));
 }
 
 public static void addClientInfoDetMessage(String clientId,String str, String resourceName, String det){
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String msgSumm = bundle.getString(str);
  FacesMessage msg = new FacesMessage();
  msg.setSeverity(SEVERITY_INFO);
  msg.setSummary(msgSumm);
  msg.setDetail(det);
  context.addMessage(clientId, msg);
  
 }
 
 public static void addClientWarnMessage(String clientId,String str, String resourceName) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  context.addMessage(clientId, new FacesMessage(SEVERITY_WARN, message, ""));
 }
 
 /**
  * Add Faces Message to faces context with summary only
  *
  * @param str Key in resources bundle
  * @param resourceName Resource bundle base name
  */
 public static void addErrorMessage(String str, String resourceName) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  context.addMessage(null, new FacesMessage(SEVERITY_ERROR, message, ""));
  
 }
 
 public static void addErrorMessageClient(String clientId,String str, String resourceName) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  context.addMessage(clientId, new FacesMessage(SEVERITY_ERROR, message, ""));
  
 }
 
 public static void addErrorMessageParam1(String str, String resourceName, String param1) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  message += " "+param1;
  context.addMessage(null, new FacesMessage(SEVERITY_ERROR, message, ""));
 }

 /**
  * Add Faces Message to faces context with summary only
  *
  * @param summary Key in resource bundle
  * @param detail Detail text
  * @param resourceName Resource bundle
  */
 public static void addErrorMessage(String summary, String detail, String resourceName) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(summary);
  String msgDet = bundle.getString(detail);
  context.addMessage(null, new FacesMessage(SEVERITY_ERROR, message, msgDet));
 }

 public static void addErrorMessageWithoutKey(String summary, String detail) {
  FacesContext context = FacesContext.getCurrentInstance();
  context.addMessage(null, new FacesMessage(SEVERITY_ERROR, summary, detail));
 }

 public static void addInfoMessage(String str, String resourceName ) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  context.addMessage(null, new FacesMessage(SEVERITY_INFO, message, ""));
 }
 
 public static void addInfoMessageVar1(String str, String resourceName, String var ) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  message += " "+var;
  context.addMessage(null, new FacesMessage(SEVERITY_INFO, message, ""));
 }

 public static void addInfoMessage(String summary, String detail, String resourceName) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(summary);
  context.addMessage(null, new FacesMessage(SEVERITY_INFO, message, detail));
 }

 public static void addInfoMessageWithoutKey(String summary, String detail) {
  FacesContext context = FacesContext.getCurrentInstance();
  context.addMessage(null, new FacesMessage(SEVERITY_INFO, summary, detail));
 }

 public static void addWarnMessage(String str, String resourceName) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  context.addMessage(null, new FacesMessage(SEVERITY_WARN, message, ""));
 }

 public static void addWarnMessageParam(String str, String resourceName, String p1) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(str);
  message +=" "+p1;
  context.addMessage(null, new FacesMessage(SEVERITY_WARN, message, ""));
 }
 public static void addWarnMessage(String summary, String detail, String resourceName) {
  FacesContext context = FacesContext.getCurrentInstance();
  ResourceBundle bundle = context.getApplication().getResourceBundle(context, resourceName);
  String message = bundle.getString(summary);
  context.addMessage(null, new FacesMessage(SEVERITY_WARN, message, detail));
 }

 public static void addWarnMessageWithoutKey(String summary, String detail) {
  FacesContext context = FacesContext.getCurrentInstance();
  context.addMessage(null, new FacesMessage(SEVERITY_WARN, summary, detail));
 }
}
