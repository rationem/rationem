/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper;

import java.util.Comparator;

/**
 *
 * @author user
 */
public class ConfigurationByOrder implements Comparator<DefaultConfigSetting> {

 @Override
 public int compare(DefaultConfigSetting conf1, DefaultConfigSetting conf2) {
  
   return (conf1.getOrder() < conf2.getOrder() ) ? -1: (conf1.getOrder() > 
           conf2.getOrder() ) ? 1:0 ;

 }
 
 
}
