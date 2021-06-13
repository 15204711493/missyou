package com.lin.missyou.until;

import com.lin.missyou.bo.PageCounter;

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
}
