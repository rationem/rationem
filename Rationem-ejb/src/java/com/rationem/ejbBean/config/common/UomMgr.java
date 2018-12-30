/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.config.common;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;



import static java.util.logging.Level.INFO;

import com.rationem.busRec.config.common.UomRec;
import com.rationem.entity.config.common.Uom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class UomMgr {
    
    private String uomCode;
    
    private UomRec uom = null;
    private ArrayList<UomRec> uoms;
    
    @PersistenceContext
    private EntityManager em;
    
    private static final Logger logger =
            Logger.getLogger("com.rationem.dataConnector.config.common.UnitOfMeasureDC");

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public String getUomCode() {
        uom = new UomRec();
        uomCode = this.uom.getUomCode();
        return uomCode;
    }

    public UomRec getUom() {
        if(uom == null) {
            uom = new UomRec();
        }
        return uom;
        
    
    }

    public boolean createNewUom(UomRec u) {
        logger.log(INFO, "Called EJB createNewUom");
         
        boolean rc = false;
        try{
        Date currDt = new Date();
        Uom uomDb = new Uom();
        uomDb.setUomCode(u.getUomCode());
        //uomDb.setLangCode("EN");
        uomDb.setDescription(u.getDescription());
        uomDb.setName(u.getName());
        uomDb.setProcessCode(u.getProcessCode());
        uomDb.setCreateDate(currDt);
        uomDb.setChangeDate(currDt);
        logger.log(INFO, "uom entity: {0}", uomDb);
        em.persist(uomDb);
        rc = true;
        } catch(EntityExistsException e){
            logger.log(INFO, "Entity exist exception: {0}", e.getMessage());
            rc = false;
        }
        
        return rc;
    }
    
    

    public ArrayList<UomRec> getUoms() {
        return uoms;
    }

    public void setUoms(ArrayList<UomRec> uoms) {
        this.uoms = uoms;
    }

    public ArrayList<UomRec> getUnitOfMeasues() {
        logger.log(INFO, "getUnitOfMeasues called");
        if(uoms == null){
            uoms = new ArrayList<UomRec>();
        }
        Query q = em.createNamedQuery("findAllUoms");
        List uomList = q.getResultList();
        ListIterator it = uomList.listIterator();
        while(it.hasNext()){
            Uom rec = (Uom)it.next();
            UomRec u = new UomRec();
            u.setId(rec.getId());
            u.setName(rec.getName());
            u.setProcessCode(rec.getProcessCode());
            u.setUomCode(rec.getUomCode());
            u.setChangeDate(rec.getChangeDate());
            uoms.add(u);
            
                    
        }
        logger.log(INFO, "Number of Uoms found: {0}", uoms.size());
        return null;
        
    }

    public UomRec getUomByCode(String code) {
        logger.log(INFO, "getUomByCode called with: {0}", code);
        UomRec unitOfMeasure = new UomRec();
        try{
        Query q = em.createNamedQuery("UomByCode");
        
        q.setParameter("UomCode", code + '%');
        List l = q.getResultList();
        int numRecs = l.size();
        logger.log(INFO, "Number of uom records found: {0}", numRecs);
        if(numRecs > 0){
            
            Uom u = (Uom)l.get(0);
            logger.log(INFO, "UOM returned from DB was {0}", u);
            unitOfMeasure = buildUomRec(u);
            logger.log(INFO, "UOM from DB converted with: {0}", unitOfMeasure);
            this.uom = unitOfMeasure;
            logger.log(INFO, "UOM from DB converted with: {0}", this.uom.getUomCode());
            return unitOfMeasure;
        }
        }catch(IllegalArgumentException e){
            logger.log(INFO, "Query does not exist: {0}",e.getMessage());
        }catch(IllegalStateException e){
            logger.log(INFO, "Query type incorrect: {0}", e.getMessage());
            
        }
        
        return this.uom;
    }
    
    private UomRec buildUomRec(Uom u){
        logger.log(INFO, "buildUomRec called with code: {0}", u.getUomCode());
        UomRec uomRec = new UomRec();
        uomRec.setChangeDate(u.getChangeDate());
        uomRec.setCreateDate(u.getCreateDate());
        uomRec.setDescription(u.getDescription());
        uomRec.setId(u.getId());
        uomRec.setName(u.getName());
        uomRec.setProcessCode(u.getProcessCode());
        uomRec.setUomCode(u.getUomCode());
        logger.log(INFO, "Return UomRec with uomCode {0}", uomRec.getUomCode());
        return uomRec;
    }

    

    
    
    
    
    
    
}
