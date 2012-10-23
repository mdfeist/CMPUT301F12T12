package ca.ualberta.cs.completemytask;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task>{
 
    public int compare(Task task1, Task task2) {
 
    	boolean shared1 = task1.isShared();
    	boolean shared2 = task2.isShared();
        
        // Compare if Shared
        if (shared1 && !shared2){
            return +1;
        }else if (!shared1 && shared2){
            return -1;
        }else{
        	String date1 = task1.getDateAsString();
            String date2 = task2.getDateAsString();
            
            int date = date1.compareTo(date2);
        	
        	if (date != 0) {
                return date;
            } else {
                
            	String name1 = task1.getName();
                String name2 = task2.getName();
                
                int name = name1.compareTo(name2);
                
                return name;
            }
        }
    }
}