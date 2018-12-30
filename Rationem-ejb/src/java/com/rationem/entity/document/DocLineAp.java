/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.config.arap.PaymentTerms;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.fi.arap.ArBankAccount;
import com.rationem.entity.fi.arap.FiApPeriodBalance;
import com.rationem.entity.mdm.PartnerPerson;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Inheritance;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;



import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.NamedQueries;
import static javax.persistence.TemporalType.DATE;

/**
 *
 * @author Chris
 */
@Entity
@NamedQueries({
 @NamedQuery(name="docsFiByVend",query="select d from DocFi d, DocLineAp l "
  + "where d.company.id = :compId and l.apAccount.id = :apAcntId and d.id = l.docHeaderBase.id"  ),
 @NamedQuery(name="docsFiApDocNum",query="select d from  DocLineAp l join l.docHeaderBase d "
  + "where d.company.id = :compId and d.docNumber >= :docNum order by d.docNumber"  ),
 @NamedQuery(name="docsFiApByDocFI",query="select d from  DocLineAp l join l.docHeaderBase d "
  + "where d.company.id = :compId and d.docNumber IN :docs order by d.docNumber"  ),
 @NamedQuery(name="docLinesForBal", query="Select l from DocLineAp l where "
   + "l.apDebitPeriodBalance.id = :drBalId or l.apCreditPeriodBalance.id = :crBalId"),
 @NamedQuery(name="apLinesbyIds", query="select l from DocLineAp l where l.id in :ids"),
// @NamedQuery(name = "apLinesCrBalByIdsUpdate", query="update DocLineAp l set l.apCreditPeriodBalance = :perBal"
//   + " where l.id in :ids "),
// @NamedQuery(name = "apLinesDrBalByIdsUpdate", query="update DocLineAp l "
//   + "set l.apDebitPeriodBalance = :perBal where l.id in :ids "),
 @NamedQuery(name="docLineAp4Doc",query="select l from DocLineAp l where l.docHeaderBase.id = :docId "),
 @NamedQuery(name="docLineApOpenByAcnt",
   query="select l from DocLineAp l where l.apAccount.id = :acntId and l.clearedLine = false and l.clearingLine = false and l.docAmount <> 0.0"),
 @NamedQuery(name="docLineApOpen4Comp",
   query="select l from DocLineAp l where l.clearedLine = false and l.clearingLine = false and l.docAmount <> 0.0 and l.comp.id = :compId "),
 @NamedQuery(name="docLineApOpenByAcntRef",
   query="select l from DocLineAp l where l.clearedLine = false and l.clearingLine = false and l.docAmount <> 0.0 and l.comp.id = :compId and l.apAccount.accountCode between :refFr and :refEnd"),
 @NamedQuery(name="docLineApByAcnt",
   query="select l from DocLineAp l where l.comp.id = :compId  and l.apAccount.id = :acntId"),
 @NamedQuery(name="docLineApByComp",
   query="select l from DocLineAp l where l.comp.id = :compId "),
 @NamedQuery(name="docLineApChqUnIssued",
   query="Select l from DocLineAp l where l.clearingLine = true and l.comp.id = :compId "
     + "and l.postType.procCode = :procCode "
     + "and l.payType.id = :payTypeId"),
 @NamedQuery(name="docLineApPaidLines",
   query="Select l from DocLineAp l where l.clearedLine = true and l.comp.id = :compId and "
     + "l.clearedByLine.id = :paymntLnId"),
 @NamedQuery(name="docLineApPdChNoChqDoc",
   query="Select l from DocLineAp l where l.comp.id = :compId and l.clearingLine = true and l.payType.id in :payTypes "
     + "and l.docHeaderBase in :docs and l.bankPayRunLine is null "),
 @NamedQuery(name="apDayBk1",
   query="select l from DocLineAp l where l.comp.id = :compId and l.postType.id in :pstTypes "),
 @NamedQuery(name="apSelOptTrans1",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId  "),
 @NamedQuery(name="apSelOptTrans2",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto    "),
 @NamedQuery(name="apSelOptTrans3",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisPeriod between :fsPerfr and :fsPerto"),
 @NamedQuery(name="apSelOptTrans4",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "),
 @NamedQuery(name="apSelOptTrans5",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and  l.docFi.postingDate between :pstDateFr and :pstDateTo "),
 @NamedQuery(name="apSelOptTrans6",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.docType.id in :docTy "),
 @NamedQuery(name="apSelOptTrans7",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod between :fsPerfr and :fsPerto"),
 @NamedQuery(name="apSelOptTrans8",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo"),
 @NamedQuery(name="apSelOptTrans9",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.postingDate between :pstDateFr and :pstDateTo"),
 @NamedQuery(name="apSelOptTrans10",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.docType in :docTy "),
 @NamedQuery(name="apSelOptTrans11",
   query="select l from DocLineAp l where l.comp.id = :compId "
     + "and l.apAccount.id = :apAcntId and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo"),
  @NamedQuery(name="apSelOptTrans12",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "),
  @NamedQuery(name="apSelOptTrans13",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.docType in  :docTy "),
  @NamedQuery(name="apSelOptTrans14",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.docType in :docTy "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "),
  @NamedQuery(name="apSelOptTrans15",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "),
  @NamedQuery(name="apSelOptTrans16",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "),
  @NamedQuery(name="apSelOptTrans17",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and l.docFi.docType in :docTy " ),
  @NamedQuery(name="apSelOptTrans18",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and l.docFi.docType in :docTy " ),
  @NamedQuery(name="apSelOptTrans19",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " ),
  @NamedQuery(name="apSelOptTrans20",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " ),
  @NamedQuery(name="apSelOptTrans21",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.docType in :docTy " ),
  @NamedQuery(name="apSelOptTrans22",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo "),
  @NamedQuery(name="apSelOptTrans23",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.docType in :docTy "),
  @NamedQuery(name="apSelOptTrans24",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.docType in :docTy "),
  @NamedQuery(name="apSelOptTrans25",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.docType in :docTy "),
  @NamedQuery(name="apSelOptTrans26",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " ),
  @NamedQuery(name="apSelOptTrans27",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.docType in :docTy " ),
  @NamedQuery(name="apSelOptTrans28",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo "
    + "and l.docFi.docType in :docTy " ),
  @NamedQuery(name="apSelOptTrans29",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.docType in :docTy " ),
  
// paid set 
  @NamedQuery(name="apSelOptTrans30",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId and  "
     + " ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans31",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto    "
     + " and ( l.clearedLine = true or l.clearingLine = true ) "),
 @NamedQuery(name="apSelOptTrans32",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisPeriod between :fsPerfr and :fsPerto"
     + " and ( l.clearedLine = true or l.clearingLine = true ) "),
 @NamedQuery(name="apSelOptTrans33",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + " and l.docFi.documentDate between :docDateFr and :docDateTo "
     + " and ( l.clearedLine = true or l.clearingLine = true ) "),
 @NamedQuery(name="apSelOptTrans34",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and  l.docFi.postingDate between :pstDateFr and :pstDateTo "
     + " and ( l.clearedLine = true or l.clearingLine = true ) "),
 @NamedQuery(name="apSelOptTrans35",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.docType.id in :docTy "
     + " and ( l.clearedLine = true or l.clearingLine = true ) "),
 @NamedQuery(name="apSelOptTrans36",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod between :fsPerfr and :fsPerto "
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
 @NamedQuery(name="apSelOptTrans37",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
 @NamedQuery(name="apSelOptTrans38",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.postingDate between :pstDateFr and :pstDateTo "
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
 @NamedQuery(name="apSelOptTrans39",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.docType in :docTy "
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
 @NamedQuery(name="apSelOptTrans40",
   query="select l from DocLineAp l where l.comp.id = :compId "
     + "and l.apAccount.id = :apAcntId and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans41",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans42",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.docType in  :docTy "
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans43",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.docType in :docTy "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans44",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans45",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans46",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and l.docFi.docType in :docTy " 
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans47",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and l.docFi.docType in :docTy " 
     + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans48",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.documentDate between :docDateFr and :docDateTo "
    + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans49",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans50",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.docType in :docTy " 
    + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans51",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo "
    + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans52",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.docType in :docTy "
    + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans53",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.docType in :docTy "
    + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans54",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.docType in :docTy "
    + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans55",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo "
    + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans56",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.docType in :docTy " 
    + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans57",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo "
    + "and l.docFi.docType in :docTy "
    + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  @NamedQuery(name="apSelOptTrans58",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.docType in :docTy " 
    + "and ( l.clearedLine = true or l.clearingLine = true ) "),
  
  // open doc
  @NamedQuery(name="apSelOptTrans59",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId and  "
     + " ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans60",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto    "
     + " and ( l.clearedLine = false and l.clearingLine = false ) "),
 @NamedQuery(name="apSelOptTrans61",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisPeriod between :fsPerfr and :fsPerto"
     + " and ( l.clearedLine = false and l.clearingLine = false ) "),
 @NamedQuery(name="apSelOptTrans62",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + " and l.docFi.documentDate between :docDateFr and :docDateTo "
     + " and ( l.clearedLine = false and l.clearingLine = false ) "),
 @NamedQuery(name="apSelOptTrans63",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and  l.docFi.postingDate between :pstDateFr and :pstDateTo "
     + " and ( l.clearedLine = false and l.clearingLine = false ) "),
 @NamedQuery(name="apSelOptTrans64",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.docType.id in :docTy "
     + " and ( l.clearedLine = false and l.clearingLine = false ) "),
 @NamedQuery(name="apSelOptTrans65",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod between :fsPerfr and :fsPerto "
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
 @NamedQuery(name="apSelOptTrans66",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
 @NamedQuery(name="apSelOptTrans67",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.postingDate between :pstDateFr and :pstDateTo "
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
 @NamedQuery(name="apSelOptTrans68",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.docType in :docTy "
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
 @NamedQuery(name="apSelOptTrans69",
   query="select l from DocLineAp l where l.comp.id = :compId "
     + "and l.apAccount.id = :apAcntId and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans70",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans71",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.docType in  :docTy "
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans72",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.docType in :docTy "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans73",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans74",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans75",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and l.docFi.docType in :docTy " 
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans76",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId "
     + "and l.docFi.fisYear between :fsYrfr and :fsYrto "
     + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
     + "and l.docFi.documentDate between :docDateFr and :docDateTo "
     + "and l.docFi.postingDate between :postDateFr and :postDateTo "
     + "and l.docFi.docType in :docTy " 
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans77",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.documentDate between :docDateFr and :docDateTo "
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans78",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans79",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.docType in :docTy " 
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans80",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo "
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans81",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.docType in :docTy "
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans82",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.docType in :docTy "
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans83",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.fisPeriod  between :fsPerfr and :fsPerto "
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.docType in :docTy "
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans84",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo "
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans85",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.docType in :docTy " 
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans86",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.documentDate between :docDateFr and :docDateTo " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo "
    + "and l.docFi.docType in :docTy "
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans87",
   query="select l from DocLineAp l where l.comp.id = :compId and l.apAccount.id = :apAcntId " 
    + "and l.docFi.postingDate between :postDateFr and :postDateTo " 
    + "and l.docFi.docType in :docTy " 
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans88",
   query="select l from DocLineAp l where l.comp.id = :compId  " 
    + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans89",
   query="select l from DocLineAp l where l.comp.id = :compId   " 
     + "and l.dueDate between :dueDateFr and :dueDateTo " 
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans90",
   query="select l from DocLineAp l join fetch l.docFi where l.comp.id = :compId     " 
     + " and  l.payType.id in :payTypeIds " 
     + "and ( l.clearedLine = false and l.clearingLine = false ) "),
  @NamedQuery(name="apSelOptTrans91",
   query="select l from DocLineAp l join fetch l.docFi where l.comp.id = :compId     " 
     + "and l.dueDate between :dueDateFr and :dueDateTo  "
     + "and  l.payType.id in :payTypeIdList " 
     + "and ( l.clearedLine = false and l.clearingLine = false ) ")
})
@Inheritance(strategy=JOINED )

@DiscriminatorValue("document.DocLineAP")
@PrimaryKeyJoinColumn(name="doc_line_id",referencedColumnName = "doc_line_id")
@Table(name="doc_line07")

public class DocLineAp extends DocLineBase implements DocLineSubLedgerIF {
 private static final long serialVersionUID = 1L;
 @OneToMany(mappedBy = "reconGlLnForApLine")
 private List<DocLineGl> reconDocLines;
 @Column(name="curr_id")
 private int currID;
 @Column(name="doc_amount")
 private double docAmount;
 
 @ManyToOne
 @JoinColumn(name="doc_fi_id", referencedColumnName="doc_id")
 private DocFi docFi;
 
 @Column(name="paid_amount")
 private double amountPaid;
 @Column(name="unpaid_amount")
 private double amountUnpaid;
 
 
 @Column(name="suppl_doc_file_data")
 private byte[] supplierDocFileContents;
 @Column(name="suppl_doc_file_name")
 private String supplierDocFileName;
 @Column(name="suppl_doc_file_type")
 private String supplierDocFileType;
 @Column(name="order_data")
 private byte[] orderFileData;
 @Column(name="order_name")
 private String oderFileName;
 @Column(name="order_type") 
 private String oderFileType;
 @Column(name="order_ref")
 private String orderReference;
 
 
 
 @ManyToOne
 @JoinColumn(name="ordered_by_id", referencedColumnName="partner_id")
 private PartnerPerson orderBy;
 
 @ManyToOne
 @JoinColumn(name="pay_terms_id", referencedColumnName="pay_terms_id")
 private PaymentTerms payTerms;

 @ManyToOne
 @JoinColumn(name="pay_type_id", referencedColumnName="pay_type_id")
 private PaymentType payType;
 
 @ManyToOne
 @JoinColumn(name="pay_bank_id", referencedColumnName="ar_bank_id")
 private ArBankAccount paymentBank;
 @Temporal(DATE)
 @Column(name="due_date")
 private Date dueDate;
  
 @ManyToOne
 @JoinColumn(name="ap_account_id", referencedColumnName="ap_account_id")
 private ApAccount apAccount;
 /*
  @ManyToOne
  @JoinColumn(name="AP_BANK_ID", referencedColumnName="AR_BANK_ID")
  private ArBank paymntBank;
*/
 
  @ManyToOne
  @JoinColumn(name="ap_credit_period_bal_id", referencedColumnName="ap_per_bal_id" )
  private FiApPeriodBalance apCreditPeriodBalance;
  
 
  @ManyToOne
  @JoinColumn(name="ap_debit_period_bal_id", referencedColumnName="ap_per_bal_id" )
  private FiApPeriodBalance apDebitPeriodBalance;
  
  @Column(name="gift_aid")
  private boolean giftAid;
   
  @ManyToOne
  @JoinColumn(name="bnk_pay_run_line_id", referencedColumnName="bank_trans_id" )
  private DocBankLineBase bankPayRunLine;

 

 public double getAmountPaid() {
  return amountPaid;
 }

 public void setAmountPaid(double amountPaid) {
  this.amountPaid = amountPaid;
 }

 public double getAmountUnpaid() {
  return amountUnpaid;
 }

 public void setAmountUnpaid(double amountUnpaid) {
  this.amountUnpaid = amountUnpaid;
 }
 
 
 

 public List<DocLineGl> getReconDocLines() {
  return reconDocLines;
 }

 public void setReconDocLines(List<DocLineGl> reconDocLines) {
  this.reconDocLines = reconDocLines;
 }

 public byte[] getSupplierDocFileContents() {
  return supplierDocFileContents;
 }

 public void setSupplierDocFileContents(byte[] supplierDocFileContents) {
  this.supplierDocFileContents = supplierDocFileContents;
 }

 public String getSupplierDocFileName() {
  return supplierDocFileName;
 }

 public void setSupplierDocFileName(String supplierDocFileName) {
  this.supplierDocFileName = supplierDocFileName;
 }

 public String getSupplierDocFileType() {
  return supplierDocFileType;
 }

 public void setSupplierDocFileType(String supplierDocFileType) {
  this.supplierDocFileType = supplierDocFileType;
 }
 
 

 public int getCurrID() {
  return currID;
 }

 public void setCurrID(int currID) {
  this.currID = currID;
 }

 public double getDocAmount() {
  return docAmount;
 }

 public void setDocAmount(double docAmount) {
  this.docAmount = docAmount;
 }


 
 
 public DocFi getDocFi() {
  return docFi;
 }

 
 public void setDocFi(DocFi docFi) {
  this.docFi = docFi;
 }

 

 
 public ArBankAccount getPaymentBank() {
  return paymentBank;
 }

 public void setPaymentBank(ArBankAccount paymentBank) {
  this.paymentBank = paymentBank;
 }

 
 public PaymentTerms getPayTerms() {
  return payTerms;
 }

 public void setPayTerms(PaymentTerms payTerms) {
  this.payTerms = payTerms;
 }

 public PaymentType getPayType() {
  return payType;
 }

 public void setPayType(PaymentType payType) {
  this.payType = payType;
 }

 public Date getDueDate() {
  return dueDate;
 }

 public void setDueDate(Date dueDate) {
  this.dueDate = dueDate;
 }

 public ApAccount getApAccount() {
  return apAccount;
 }

 public void setApAccount(ApAccount apAccount) {
  this.apAccount = apAccount;
 }

 public FiApPeriodBalance getApCreditPeriodBalance() {
  return apCreditPeriodBalance;
 }

 public void setApCreditPeriodBalance(FiApPeriodBalance apCreditPeriodBalance) {
  this.apCreditPeriodBalance = apCreditPeriodBalance;
 }

 public FiApPeriodBalance getApDebitPeriodBalance() {
  return apDebitPeriodBalance;
 }

 public void setApDebitPeriodBalance(FiApPeriodBalance apDebitPeriodBalance) {
  this.apDebitPeriodBalance = apDebitPeriodBalance;
 }

 public boolean isGiftAid() {
  return giftAid;
 }

 public void setGiftAid(boolean giftAid) {
  this.giftAid = giftAid;
 }

 public PartnerPerson getOrderBy() {
  return orderBy;
 }

 public void setOrderBy(PartnerPerson orderBy) {
  this.orderBy = orderBy;
 }

 
 public byte[] getOrderFileData() {
  return orderFileData;
 }

 public void setOrderFileData(byte[] orderFileData) {
  this.orderFileData = orderFileData;
 }

 public String getOderFileName() {
  return oderFileName;
 }

 public void setOderFileName(String oderFileName) {
  this.oderFileName = oderFileName;
 }

 public String getOderFileType() {
  return oderFileType;
 }

 public void setOderFileType(String oderFileType) {
  this.oderFileType = oderFileType;
 }

 public String getOrderReference() {
  return orderReference;
 }

 public void setOrderReference(String orderReference) {
  this.orderReference = orderReference;
 }

 
 
 public DocBankLineBase getBankPayRunLine() {
  return bankPayRunLine;
 }

 public void setBankPayRunLine(DocBankLineBase bankPayRunLine) {
  this.bankPayRunLine = bankPayRunLine;
 }

 
}
