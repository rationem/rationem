/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.tax;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class GiftAidScheduleLine implements Serializable {
 private long id;
 private String donorRef;
 private String donorName;
 private String address;
 private String postCode;
 private Date donationDate;
 private double donationAmount;

 public GiftAidScheduleLine() {
 }

 public long getId() {
  return id;
 }

 public void setId(long id) {
  this.id = id;
 }

 public String getDonorRef() {
  return donorRef;
 }

 public void setDonorRef(String donorRef) {
  this.donorRef = donorRef;
 }

 public String getDonorName() {
  return donorName;
 }

 public void setDonorName(String donorName) {
  this.donorName = donorName;
 }

 public String getAddress() {
  return address;
 }

 public void setAddress(String address) {
  this.address = address;
 }

 public String getPostCode() {
  return postCode;
 }

 public void setPostCode(String postCode) {
  this.postCode = postCode;
 }

 public Date getDonationDate() {
  return donationDate;
 }

 public void setDonationDate(Date donationDate) {
  this.donationDate = donationDate;
 }

 public double getDonationAmount() {
  return donationAmount;
 }

 public void setDonationAmount(double donationAmount) {
  this.donationAmount = donationAmount;
 }

 @Override
 public int hashCode() {
  int hash = 7;
  hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
  return hash;
 }

 @Override
 public boolean equals(Object obj) {
  if (obj == null) {
   return false;
  }
  if (getClass() != obj.getClass()) {
   return false;
  }
  final GiftAidScheduleLine other = (GiftAidScheduleLine) obj;
  if (this.id != other.id) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "GiftAidScheduleLine{" + "id=" + id + '}';
 }
 
 
 
}
