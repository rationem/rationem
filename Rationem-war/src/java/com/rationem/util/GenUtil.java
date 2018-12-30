/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util;

import com.rationem.exception.BacException;
import com.lowagie.text.Document;
import java.math.BigDecimal;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;

import java.util.Locale;



/**
 *
 * @author Chris
 */
public class GenUtil {

    public static void addErrorMessage(String msg){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public static void addInfoMessage (String msg){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg,  null);
        
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public static void addInfoMessage (String header,String content ){
     FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, header,  content);
     FacesContext.getCurrentInstance().addMessage(null,message);
     
    }
    public GenUtil() {
    }
    
    public static synchronized String formatCurrency(double number, int decPlaces, Locale loc){
     NumberFormat nf = NumberFormat.getCurrencyInstance(loc);
     nf.setMinimumFractionDigits(decPlaces);
     return nf.format(number);
    }
    
    public static synchronized String formatCurrency(double number, Locale loc){
     NumberFormat nf = NumberFormat.getCurrencyInstance(loc);
     
     return nf.format(number);
    }
    
    public static synchronized String formatPercent(double number, int decPlaces, Locale loc){
     NumberFormat nf = NumberFormat.getPercentInstance(loc);
     nf.setMinimumFractionDigits(decPlaces);
     return nf.format(number);
    }
 
 public static synchronized String formatNumber2Dp(double number, Locale loc){
  NumberFormat nf = NumberFormat.getNumberInstance(loc);
  nf.setMinimumFractionDigits(2);
  nf.setMinimumIntegerDigits(1);
  String output = nf.format(number);
  return output;
 }
 
 public static synchronized String formatNumberLocDp(double number, Locale loc){
  NumberFormat nf = NumberFormat.getNumberInstance(loc);
  NumberFormat cf = NumberFormat.getCurrencyInstance(loc);
  nf.setMinimumFractionDigits(cf.getMinimumFractionDigits());
  nf.setMinimumIntegerDigits(cf.getMinimumIntegerDigits());
  nf.setMaximumFractionDigits(cf.getMaximumFractionDigits());
  String output = nf.format(number);
  return output;
 }
 
 public static double formatedNum2Double(String num, Locale loc) throws BacException{
  NumberFormat cf = NumberFormat.getNumberInstance(loc);
  try{
   Number number = null;
   double retDbl = 0;
   number = cf.parse(num);
   retDbl =  number.doubleValue();
   return retDbl;
  } catch (ParseException e){
   throw new BacException("Could not format num reason:"+e.getLocalizedMessage());
  }
 }
 
 /**
  * Check if string is numeric
  * @param str
  * @return 
  */
 public static boolean isNumeric(String str)
 {
  return str.matches("^(\\d*\\.?\\d+|\\d{1,3}(,\\d{3})*(\\.\\d+)?)$");  //match a number with optional '-' and decimal.
 }
 
/**
 * Round double number to specified decimal points 
 * round(yourNumber, 3, BigDecimal.ROUND_HALF_UP);
 * @param unrounded
 * @param precision  Number of decimal point
 * @param roundingMode eg. BigDecimal.ROUND_HALF_UP 
 * @return 
 */
 public static double round(double unrounded, int precision, int roundingMode)
 {
    BigDecimal bd = new BigDecimal(unrounded);
    BigDecimal rounded = bd.setScale(precision, roundingMode);
    return rounded.doubleValue();
 }
 
 public static double roundUp2Dp(double unrounded)
 {
  int precision = 2;
  int roundingMode = BigDecimal.ROUND_HALF_UP;
  BigDecimal bd = new BigDecimal(unrounded);
  BigDecimal rounded = bd.setScale(precision, roundingMode);
  return rounded.doubleValue();
 }

 private void writePDFToResponse(HttpServletResponse response, ByteArrayOutputStream baos, 
         
         String fileName) throws IOException, DocumentException {     
    	response.setContentType("application/pdf");
    	response.setHeader("Expires", "0");
        response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".pdf");
        response.setContentLength(baos.size());
        
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
    }
 
 public void export(FacesContext facesContext, 
         //DataTable table, 
         String filename, boolean pageOnly, int[] excludeColumns, String encodingType
         //, 
         //MethodExpression preProcessor, 
         //MethodExpression postProcessor
         ) 
         throws IOException { 
		try {
	        Document document = new Document();
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        PdfWriter.getInstance(document, baos);
	        
	//        if(preProcessor != null) {
	//    		preProcessor.invoke(facesContext.getELContext(), new Object[]{document});
	//    	}

            if(!document.isOpen()) {
                document.open();
            }
	        
	//		PdfPTable pdfTable = exportPDFTable(table, pageOnly,excludeColumns, encodingType);
	//    	document.add(pdfTable);
	    	
	//    	if(postProcessor != null) {
	//    		postProcessor.invoke(facesContext.getELContext(), new Object[]{document});
	//    	}
	    	
	        document.close();
	    	
	        writePDFToResponse(((HttpServletResponse) facesContext.getExternalContext().getResponse()), baos, filename);
	        
		} catch (DocumentException e) {
			throw new IOException(e.getMessage());
		}
	}


}
