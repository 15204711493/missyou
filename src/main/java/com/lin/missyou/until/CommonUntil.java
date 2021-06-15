package com.lin.missyou.until;

import com.lin.missyou.bo.PageCounter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class CommonUntil {

     public static PageCounter convertToPageParameter(Integer start, Integer count){
         int pageNum = start/count;
         PageCounter pageCounter = PageCounter.builder()
                 .page(pageNum)
                 .count(count)
                 .build();
         return pageCounter;

     }

     public  static Boolean isInTimeLine(Date date, Date start,Date end){
         if(date.getTime()>start.getTime() && date.getTime()<end.getTime()){
             return true;
         }
         return false;
     }

     public static Calendar addSomeSeconds(Calendar calendar,int seconds){
         calendar.add(Calendar.SECOND,seconds);
         return calendar;
     }


     public static Boolean isOutDate(Date startTime,Long period){
         long now = Calendar.getInstance().getTimeInMillis();
         long startTimeStamp = startTime.getTime();
         Long periodMillis = period * 1000;
         if(now>(periodMillis+startTimeStamp)){
             return true;
         }
         return false;
     }


    public static Boolean isOutDate(Date expiredTime){
        long now = Calendar.getInstance().getTimeInMillis();
        long time = expiredTime.getTime();
        if(now>time){
            return true;
        }
        return false;
    }

    public static String yuanToFenPlainString(BigDecimal p){
      p = p.multiply(new BigDecimal("100"));
      return CommonUntil.toPlain(p);

    }

    public static String toPlain(BigDecimal p){
         return p.stripTrailingZeros().toPlainString();
     }
}
