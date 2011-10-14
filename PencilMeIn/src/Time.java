/**
 * Time.java
 */


import java.util.StringTokenizer;
import java.text.DecimalFormat;

/**
 * This class give methods to compare two time in a day.
 *
 * @version      
 * @author     Yimin Li
 */ 

public class Time {
    //Static variables
    public static final int MINUTES_PER_HOUR=60;
    public static final int MINUTES_IN_A_HALF_DAY=12*MINUTES_PER_HOUR;
  /**
     * Constructor takes the arguments, the hour,
     * minute, and whether it is a am or pm time
     * (true is am, false is pm)
     */
	
    public Time(int hour, int minute, boolean isMorning)
    {
        setTime(hour,minute,isMorning);
    }
    
    public Time(String strTime){       
        setTime(strTime);        
    }

/**    Copier constructor    */
    public Time( Time t){
	timeInMinutes=t.timeInMinutes;
    }
      
    public boolean before(Time t){
        if(timeInMinutes < t.getTimeInMinutes()) return true;
        return false;
    }
    
    public void setLaterTime(Time t){
	if( t==null){
	  return;
	}
	else if(before(t)) {
	  timeInMinutes=t.timeInMinutes;
	}
    }
	    
    public void setEarlierTime(Time t){
	if( t==null){
	  return;
	}
	else if(t.before(this)) {
	  timeInMinutes=t.timeInMinutes;
	}
    }

    public void setTime(String strTime){
        boolean isMorning=strTime.endsWith("am");
        int endIndex=strTime.length()-2;
        StringTokenizer tTime=new StringTokenizer(strTime.substring(0,endIndex),":");
	if(tTime.countTokens()>2) 
	  System.err.println("Time class error: setTime(String)");
	int[] t= new int[2];
	int i=0;
	while(tTime.hasMoreTokens()){
         t[i]=Integer.parseInt(tTime.nextToken());
	 i++;
	}
        setTime(t[0],t[1],isMorning);
    }
    
    public void setTime(int hour,int minute,boolean isMorning){
	  
	if(hour==12) hour=0;  	
        timeInMinutes=hour*MINUTES_PER_HOUR+minute;
        timeInMinutes+=
		(isMorning)?0:MINUTES_IN_A_HALF_DAY;
    }

    public void setTimeInMinutes(int m){
	timeInMinutes=m;
    } 

    public int getTimeInMinutes(){
        return timeInMinutes;
    }
    
    public boolean isAM(){
        if(timeInMinutes>=MINUTES_IN_A_HALF_DAY) return false;
        return true;
    }

    public void add(int minutes){
	timeInMinutes+=minutes;
    }	

    public int diffInMinutes(Time t){
	return t.timeInMinutes-timeInMinutes;
    }

    public int diffInHours(Time t){
	return diffInMinutes(t)/MINUTES_PER_HOUR;
    }

    public void roundupToMinHour(){
	int h=timeInMinutes/MINUTES_PER_HOUR;
	timeInMinutes=h*MINUTES_PER_HOUR;
    }
    public void roundupToMaxHour(){
	int h =timeInMinutes/MINUTES_PER_HOUR;
	timeInMinutes=(h+1)*MINUTES_PER_HOUR;
    }
    
    public int getHour(){
        int hour=timeInMinutes/MINUTES_PER_HOUR;
        return (hour>12)? (hour-12) :hour;
    }
    
    public int getMinute(){
        return timeInMinutes%MINUTES_PER_HOUR;
    }
    
    public String toString(){
	DecimalFormat df=new DecimalFormat("00");
        String strTime=new String();
	if(getMinute()!=0) strTime=":"+df.format(getMinute());
        strTime=getHour()+strTime;
        if(isAM()){
            return strTime+"am";
        }
        else
            return strTime+"pm";
    }
        
    
    //Private variables
    private int timeInMinutes;

}
