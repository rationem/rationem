/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.mdm;

import com.rationem.busRec.mdm.AddressRec;
import com.rationem.ejbBean.common.MasterDataManager;
import java.util.List;
import com.rationem.util.BaseBean;
import java.util.ListIterator;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
public class AddressMgtBean extends BaseBean {
 private static final Logger logger = Logger.getLogger(AddressMgtBean.class.getName());
 
 @EJB
 MasterDataManager addrMgr;
 
 private List<AddressRec> addrList;
 private AddressRec newAddr;
 private AddressRec editAddr;

 /**
  * Creates a new instance of AddressMgtBean
  */
 public AddressMgtBean()  {
 }

 public List<AddressRec> getAddrList() {
  if(addrList == null){
   addrList = addrMgr.getAllAddresses();
  }
  return addrList;
 }

 public void setAddrList(List<AddressRec> addrList) {
  this.addrList = addrList;
 }

 public AddressRec getEditAddr() {
  return editAddr;
 }

 public void setEditAddr(AddressRec editAddr) {
  this.editAddr = editAddr;
 }

 public AddressRec getNewAddr() {
  if(newAddr == null){
   newAddr = new AddressRec(); 
  }
  return newAddr;
 }

 public void setNewAddr(AddressRec newAddr) {
  this.newAddr = newAddr;
 }
 
 
public void onAddAdressBtn(){
 RequestContext rCtx = RequestContext.getCurrentInstance();
 rCtx.execute("PF('newAddrWv').show()");
 
} 

public void onAddAddressCloseBtn(CloseEvent evt){
 logger.log(INFO, "onAddAddressCloseBtn called");
 newAddr = null;
 RequestContext rCtx = RequestContext.getCurrentInstance();
 rCtx.update("newAddFrmId");
 rCtx.execute("PF('newAddrWv').hide()");
 
}

public void onAddAddressSaveBtn(){
 logger.log(INFO, "onAddAddressSaveBtn called");
 newAddr = this.addrMgr.addressUpdate(newAddr, this.getLoggedInUser(),getView());
 
 addrList = this.getAddrList();
 addrList.add(newAddr);
 RequestContext rCtx = RequestContext.getCurrentInstance();
 rCtx.update("addrList");
 rCtx.execute("PF('newAddrWv').hide()");
}

public void onEditAddrBtn(){
 logger.log(INFO, "onEditAddrBtn selected {0}", editAddr.getId());
 RequestContext rCtx = RequestContext.getCurrentInstance();
 rCtx.update("editAddrFrmId");
 rCtx.execute("PF('addrEditDlgWv').show()");
}

public void onEditAddrSave(){
 logger.log(INFO, "onEditAddrSave selected {0}", this.editAddr.getId());
 editAddr = addrMgr.addressUpdate(editAddr, this.getLoggedInUser(), getView());
 boolean found = false;
 ListIterator<AddressRec> addrListIt = addrList.listIterator();
 while(addrListIt.hasNext() && !found){
  AddressRec addr = addrListIt.next();
  if(addr.getId() == editAddr.getId()){
   addrListIt.set(editAddr);
   found = true;
  }
 }
 RequestContext rCtx = RequestContext.getCurrentInstance();
 rCtx.update("addrList");
 rCtx.execute("PF('addrEditDlgWv').hide()");
}
 
}
