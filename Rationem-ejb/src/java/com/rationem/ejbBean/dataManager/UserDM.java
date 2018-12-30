/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.dataManager;

import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.mdm.CountryRec;
import java.util.ListIterator;
import javax.persistence.PersistenceException;
import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import java.util.List;
import javax.persistence.Query;
import com.rationem.entity.user.UserLogin;
import javax.persistence.TransactionRequiredException;
import javax.persistence.EntityExistsException;
import com.rationem.entity.mdm.Address;
import com.rationem.busRec.user.UserRec;
import com.rationem.exception.BacException;
import javax.annotation.PostConstruct;
import java.util.Date;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.busRec.user.UserLoginRec;
import com.rationem.entity.mdm.Country;
import com.rationem.entity.user.User;
import java.util.HashMap;
import javax.ejb.LocalBean;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class UserDM {

    @EJB
    private SysBuffer buffer;

    @EJB
    private MasterDataDM masterDM;
    
    private EntityManager em;
    private EntityTransaction trans;
    private static final Logger LOGGER = Logger.getLogger(UserDM.class.getName());


   @PostConstruct
   void init(){
    LOGGER.log(INFO,  "Loaded User DM");
    FacesContext fc = FacesContext.getCurrentInstance();
    String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
    HashMap properties = new HashMap();
    properties.put("tenantId", tenantId);
    properties.put("eclipselink.session-name", "sessionName"+tenantId);
    em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
   trans = em.getTransaction();
    }
   
   private EntityManager getEm(){
    
    FacesContext fc = FacesContext.getCurrentInstance();
    String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
    LOGGER.log(INFO, "tenantId from session {0}", tenantId);
    HashMap properties = new HashMap();
    properties.put("tenantId", tenantId);
    properties.put("eclipselink.session-name", "sessionName"+tenantId);
    em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
   trans = em.getTransaction();
    
    
    return em;
   }
   private UserLogin buildUserLogin(UserLoginRec rec, User usr){
        LOGGER.log(FINEST, "buildUserLogin called with {0}", rec);
        UserLogin login = new UserLogin();
        if(rec.getId() != null){
           login.setId(rec.getId());
        }
        //login.setCreatedOn(rec.);
        login.setLastLogin(rec.getLastLogin());
        login.setFailedAttempts(rec.getFailedAttempts());
        login.setLogonName(rec.getLogonName());
        login.setPass(rec.getPass());
        //login.setRevision(rec.getRevision());
        login.setUserId(usr.getId());
        
        return login;
    }

    private User buildUser(UserRec usr, UserRec crUsr, String pg){
        LOGGER.log(INFO, "buildUser with {0}", usr);
        Date curr = new Date();
        User u = new User();
        if(usr.getId() != null){
            u.setId(usr.getId());
        }
        
        u.setCreatedDate(curr);
        LOGGER.log(INFO, "usr.getDefaultAddress {0}", usr.getDefaultAddress());
        if(usr.getDefaultAddress() != null && usr.getDefaultAddress().getPostCode()!= null && !usr.getDefaultAddress().getPostCode().isEmpty()){
            Address addr = masterDM.buildAddressDM(usr.getDefaultAddress(),pg);
            u.setDefaultAddress(addr);
            LOGGER.log(FINEST, "Persist Address {0}", addr);
            
        }
        u.setFamilyName(usr.getFamilyName());
        u.setFirstName(usr.getFirstName());
        u.setId(usr.getId());
        u.setChangedOn(usr.getChangedOn());
        u.setMiddleName(usr.getMiddleName());
        u.setRef(usr.getRef());
        u.setRevision(usr.getRevision());
        u.setStartDate(usr.getStartDate());
        u.setSystemUser(usr.isSystemUser());
        u.setVatNumber(usr.getVatNumber());
        u.setVatRegisteredDate(usr.getVatRegisteredDate());
        

        return u;
    }

    private UserRec buildUserRec(User usr){
        LOGGER.log(FINEST, "UserDM.buildUser with {0}", usr);
        LOGGER.log(FINEST, "Country {0}", usr);
        UserRec u = new UserRec();
        u.setId(usr.getId());

        u.setCreatedDate(usr.getCreatedDate());
        if(usr.getDefaultAddress() != null){
        AddressRec addrRec = masterDM.getAddressRec(usr.getDefaultAddress());
        u.setDefaultAddress(addrRec);
        }
        u.setFamilyName(usr.getFamilyName());
        u.setFirstName(usr.getFirstName());
        u.setChangedOn(usr.getChangedOn());
        u.setMiddleName(usr.getMiddleName());
        u.setRef(usr.getRef());
        u.setRevision(usr.getRevision());
        u.setStartDate(usr.getStartDate());
        u.setSystemUser(usr.isSystemUser());
        u.setVatNumber(usr.getVatNumber());
        u.setVatRegisteredDate(usr.getVatRegisteredDate());
        
        /*
        if(usr.getCountry() != null){
         Country cntry = usr.getCountry();
         LOGGER.log(INFO, "User Country from DB {0}", usr.getCountry());
         CountryRec cntryRec = this.masterDM.buildCountryRecPvt(cntry);
         u.setCountry(cntryRec);
         LOGGER.log(INFO, "User Country {0}",u.getCountry());
        }
        */
        

        return u;
    }
/**
 * Actually save the user to DB
 * @param user
 * @throws BacException
 */
    public void saveUser(UserRec user, UserRec updtUsr, String pg) throws BacException {
        LOGGER.log(FINEST, "DB - saveUser called with {0}", user);
        User usr = this.buildUser(user,updtUsr,pg);
        
        try{
            Address adr = usr.getDefaultAddress();
            if(adr!=null && !adr.getPostCode().isEmpty()) {
                em.persist(adr);
            }else{
                adr = null;
                usr.setDefaultAddress(null);
            }
            em.persist(usr);
            LOGGER.log(FINEST, "Saved user");
            /*em.flush();
            LOGGER.log(FINEST, "About to persist usrLogin");
            em.persist(usrLgin);
            em.flush();*/
            
        }catch(EntityExistsException e){
            throw new BacException("Address already Exists");
        }catch(IllegalArgumentException e){
            throw new BacException("Internal error not a DB object "+e.getLocalizedMessage());
        }catch(TransactionRequiredException e){
            throw new BacException("Internal DB error no transaction "+e.getLocalizedMessage());
        }
    }
/**
 * Retrieve user record based on login
 * @param loginName
 * @return
 * @throws BacException
 */
  public UserRec getUserByLoginName(String loginName) throws BacException {
    LOGGER.log(INFO, "Get user by login rec by login {0}", loginName);
    UserRec usrRec;
    em = getEm();
    try{
      Query q = em.createNamedQuery("usrByName");
      q.setParameter("uName", loginName);
      
      List usrLst = q.getResultList();
      LOGGER.log(FINEST, "User login record found", usrLst);
      boolean found = false;
      ListIterator li = usrLst.listIterator();
      while(li.hasNext()&& !found){
        UserLogin usrLgn = (UserLogin)li.next();
        Long usrId = usrLgn.getUserId();
        User usr = em.find(User.class, usrId, LockModeType.READ);
        usrRec = this.buildUserRec(usr);
        return usrRec;
      }
    }catch(IllegalArgumentException e){
      throw new BacException("E:UM:01 - Query not found");
    } catch(IllegalStateException e){
      LOGGER.log(FINEST, "Error {0}", e.getLocalizedMessage());
      throw new BacException("E:UM:02 - SQL type incorrect");
    }catch(QueryTimeoutException e){
      throw new BacException("E:UM:03 - Request time out");
    }catch(TransactionRequiredException e){
      throw new BacException("E:UM:04 - Transaction missing");
    }catch(PessimisticLockException e){
      throw new BacException("E:UM:05 - Lock failed - transaction rolledback");
    }catch(LockTimeoutException e){
      throw new BacException("E:UM:06 - Lock failed - request rolledback");
    }catch(PersistenceException e){
      throw new BacException("E:UM:07 - Database error");
    }

    return null;
  }

  public UserRec getUserRecPvt(User usr) throws BacException {
    UserRec usrRec = this.buildUserRec(usr);
    LOGGER.log(FINEST, "getUserRecPvt return {0}", usr);
    return usrRec;
  }


    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

  /** 
   * Create the master user for a Tenant. The login must already have been created
   * @param usr
   * @return 
   */
 public UserRec createTenantDefaultUser(UserRec usrRec, String tenantId) {
  LOGGER.log(INFO, "createTenantDefaultUser called with usrRec {0} tenant {1}", new Object[]{usrRec,tenantId});
   HashMap<String, String> properties = new HashMap();
   properties.put("tenantId", tenantId);
   properties.put("eclipselink.session-name", "sessionName"+tenantId);
   EntityManager tEm = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
   try{
   trans = tEm.getTransaction();
   trans.begin();
   User usr = buildUser(usrRec, null, null);
   
   tEm.persist(usr);
   Query q = tEm.createNamedQuery("userAll");
   List rs = q.getResultList();
   LOGGER.log(INFO, "saved users {0}", rs);
   trans.commit();
   
  
  
   usrRec.setId(usr.getId());
   return usrRec;
   }catch(RollbackException ex){
    LOGGER.log(INFO, "Save user name {0} failed", usrRec.getRef());
   }
  
  return null;
 }

 public UserRec getUserByIdNum(Long id,String tenantId) {
  LOGGER.log(INFO, "getUserByIdNum {0}", id);
   //trans.begin();
   
   /*Query q = tEm.createNamedQuery("userAll");
   List rs = q.getResultList();
   
   LOGGER.log(INFO, "saved users {0}", rs);*/
   em = this.getEm();
   User usr = em.find(User.class, id);
   LOGGER.log(INFO, tenantId, trans);
   
   //trans.commit();
   LOGGER.log(INFO, "Find user returns user {0}", usr);
  if(usr != null){
   
   UserRec usrRec = this.buildUserRec(usr);
   Country cntry = usr.getCountry();
   //CountryRec cntryRec = masterDM.buildCountryRecPvt(cntry);
 //  usrRec.setCountry(cntryRec);
   
  return usrRec;
  }
   
   
   
  return null ;
  
 }

 
 
 
}
