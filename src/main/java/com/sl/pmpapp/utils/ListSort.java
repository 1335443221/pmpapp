package com.sl.pmpapp.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListSort {
	   public static void ListSort(List<Map<String,Object>> list) {  
        Collections.sort(list, new Comparator<Map>() {  
		            @Override  
		            public int compare(Map o1, Map o2) {  
		                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		                try {  
		                    Date dt1 = format.parse(o1.get("date").toString());  
		                    Date dt2 = format.parse(o2.get("date").toString());  
		                    if (dt1.getTime() > dt2.getTime()) {  
		                        return 1;  
		                    } else if (dt1.getTime() < dt2.getTime()) {  
		                        return -1;  
		                    } else {  
		                        return 0;  
		                    }  
		                } catch (Exception e) {  
		                    e.printStackTrace();  
		                }  
		                return 0;  
		            }  
		        });  
		    }  
	   
	   
}
