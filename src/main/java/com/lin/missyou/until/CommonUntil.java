package com.lin.missyou.until;

import com.lin.missyou.bo.PageCounter;

public class CommonUntil {

     public static PageCounter convertToPageParameter(Integer start, Integer count){
         int pageNum = start/count;
         PageCounter pageCounter = PageCounter.builder()
                 .page(pageNum)
                 .count(count)
                 .build();
         return pageCounter;

     }
}
