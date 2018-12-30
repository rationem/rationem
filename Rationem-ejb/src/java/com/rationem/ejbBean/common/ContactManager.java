/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.common;

import com.rationem.busRec.cm.ContactRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.ejbBean.dataManager.ContactDM;
import com.rationem.util.ContactSelection;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class ContactManager {
 private static final Logger LOGGER = Logger.getLogger(ContactManager.class.getName());
 
 @EJB
 private ContactDM contDm;

    // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")
 
 
 
 
 public List<ContactRec> contactsForApAcnt(ContactSelection sel, ApAccountRec acnt ){
  
  if(sel != null){
   if(sel.getFrom() != null && sel.getTo() == null){
    sel.setTo(sel.getFrom());
   }else if(sel.getTo() != null && sel.getFrom() == null){
    sel.setFrom(sel.getTo());
   }
   if(sel.getFrom() == null){
    sel = null;
   }
  }
  List<ContactRec> retList = contDm.conactsForApAcnt(sel, acnt);
  return retList;
 }
 
 public List<ContactRec> conactsForArAccount(ArAccountRec ar){
  List<ContactRec> retList = contDm.conactsForArAccount(ar);
  return retList;
 }
 
 public List<ContactRec> contactsForDoc(ContactSelection sel, DocFiRec doc ){
  LOGGER.log(INFO, "ContMgr.contactsForDoc called with sel {0} doc id {1}", new Object[]{sel,doc.getId()});
  if(sel != null){
   if(sel.getFrom() != null && sel.getTo() == null){
    sel.setTo(sel.getFrom());
   }else if(sel.getTo() != null && sel.getFrom() == null){
    sel.setFrom(sel.getTo());
   }
   if(sel.getFrom() == null){
    sel = null;
   }
  }
  
  
  
  List<ContactRec> retList = contDm.conactsForDoc(sel, doc);
  return retList;
 }
 
 
 public List<ContactRec> contactsForPtnr(ContactSelection sel, PartnerBaseRec ptnr ){
  
  if(sel != null){
   if(sel.getFrom() != null && sel.getTo() == null){
    sel.setTo(sel.getFrom());
   }else if(sel.getTo() != null && sel.getFrom() == null){
    sel.setFrom(sel.getTo());
   }
   if(sel.getFrom() == null){
    sel = null;
   }
  }
  List<ContactRec> retList = contDm.contactsForPtnr(sel, ptnr);
  return retList;
 }
 
 public ContactRec contactUpdate(ContactRec cont, String pg){
  LOGGER.log(INFO, "contactUpdate called with cont {0}", cont.getSummary());
  cont = contDm.updateContact(cont, pg);
  LOGGER.log(INFO, "cont id {0}", cont.getId());
  return cont;
 }
}
