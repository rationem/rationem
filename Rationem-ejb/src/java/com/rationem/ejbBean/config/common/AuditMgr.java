/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.config.common;

import com.rationem.busRec.audit.AuditBaseRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.ejbBean.dataManager.AuditDM;
import com.rationem.exception.BacException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean

public class AuditMgr {

 @EJB
 AuditDM audDm;
 
 public List<AuditBaseRec> getAuditRecForDocFi(DocFiRec docFi) throws BacException {
  
  List<AuditBaseRec> retList = this.audDm.getDocFiAuditRecs(docFi);
  return retList;
 }

 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public List<AuditBaseRec> getDocLineAudit(DocLineBaseRec line) {
  
 List<AuditBaseRec> retList = this.audDm.getDocLineAudit(line);
  return retList;
 }

 
}
