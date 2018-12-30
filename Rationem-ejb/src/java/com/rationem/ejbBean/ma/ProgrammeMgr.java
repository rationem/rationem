/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.ma;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.programme.ProgramAccountRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.dataManager.ProgrammeDM;
import com.rationem.exception.BacException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class ProgrammeMgr {
 private static final Logger LOGGER =  Logger.getLogger(ProgrammeMgr.class.getName());

 @EJB
 private ProgrammeDM progDM;
 
 @EJB
 private MasterDataManager mdmDM;
 
 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public ProgrammeRec updateProgramme(ProgrammeRec programme, UserRec user, String source) 
         throws BacException {
  if(programme.getResponsibilityOf() != null && programme.getResponsibilityOf().getId() == null){
   PartnerPersonRec resp = (PartnerPersonRec)this.mdmDM.updatePartner(programme.getResponsibilityOf(), source);
   programme.setResponsibilityOf(resp);
  }
  programme = progDM.updateProgramme(programme, user, source);
  return programme;
 }

 public int copyProgrammesToComp(CompanyBasicRec c1,CompanyBasicRec c2,UserRec usrRec, String pg){
  int rc = progDM.copyProgrammesToComp(c1, c2, usrRec, pg);
  
  return rc;
          
 }
 public List<ProgrammeRec> getAllProgrammes(CompanyBasicRec comp) {
  List<ProgrammeRec> progList = progDM.getAllProgrammes(comp);
  LOGGER.log(INFO,"getAllProgrammes DB layer returns {0} programs",progList.size());
  return progList;
 }

 public List<ProgrammeRec> getProgrammesByName(CompanyBasicRec comp, String name) throws BacException {
  if(comp == null){
   throw new BacException("Company must be specified for programme");
  }
  
  if(comp.getId() == null || comp.getId() == 0 ){
   throw new BacException("Company must must have a valid id");
  }
  if(name == null || name.isEmpty()){
   name = "%";
  }else{
   name = name + "%";
  }
  List<ProgrammeRec> progList = progDM.getProgrammesByName(comp, name);
  return progList;
 }
 
 public ProgrammeRec getProgrammeByRef(CompanyBasicRec comp, String ref){
   if(comp == null){
   throw new BacException("Company must be specified for programme");
  }
  
  if(comp.getId() == null || comp.getId() == 0 ){
   throw new BacException("Company must must have a valid id");
  }
  
  ProgrammeRec rec = this.progDM.getProgrammeByRef(comp, ref);
  return rec;
 }
 public List<ProgrammeRec> getProgrammesByRef(CompanyBasicRec comp, String ref) throws BacException {
  if(comp == null){
   throw new BacException("Company must be specified for programme");
  }
  
  if(comp.getId() == null || comp.getId() == 0 ){
   throw new BacException("Company must must have a valid id");
  }
  if(ref == null || ref.isEmpty()){
   ref = "%";
  }else{
   ref = ref + "%";
  }
  List<ProgrammeRec> progList = progDM.getProgrammesByRef(comp, ref);
  return progList;
 }

 public ProgramAccountRec getProgramActForGlAct(FiGlAccountCompRec glAct, ProgrammeRec program, 
         boolean autoCreate, UserRec Usr, Date crDate, String page) {
  LOGGER.log(INFO, "ProgramMgr.getProgramActForGlAct called with gl Ac id {0} prog {1} autoCr {2} usr {3} dt {4}",
          new Object[]{glAct.getId(),program.getId(),autoCreate,Usr.getId(),crDate});
  ProgramAccountRec progAc = progDM.getProgActRecByGlAc(glAct, program, autoCreate, Usr, crDate, page);
  LOGGER.log(INFO, "ProgramMgr.getProgramActForGlAct returns {0}",progAc);
  return progAc;
 }
 
 

 
}
