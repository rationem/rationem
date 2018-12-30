/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.company;

import com.rationem.busRec.user.UserRec;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class CalendarRuleMonthRec extends CalendarRuleBaseRec {

    private int startMonthNumber = 1;

    public CalendarRuleMonthRec() {
        super();
    }

    public CalendarRuleMonthRec(Long id, UserRec createdBy, Date createdOn,
            UserRec changedBy, Date changedOn, int version, int startMonthNumber) {
        super(id,createdBy,createdOn,changedBy, changedOn,version);
        this.startMonthNumber = startMonthNumber;
    }

    public int getStartMonthNumber() {
     
      
     
        return startMonthNumber;
    }

    public void setStartMonthNumber(int startMonthNumber) {
        this.startMonthNumber = startMonthNumber;
    }
    
public Long getStartMonthNumberL(){
 Integer mthNum = startMonthNumber;
 return mthNum.longValue();
}

public void setStartMonthNumberL(Long monthNumber) {
 if(monthNumber != null){
 startMonthNumber = monthNumber.intValue();
 }
}



}
