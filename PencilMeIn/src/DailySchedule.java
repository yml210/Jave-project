/*
 * DailySchedule.java
 *
 * Created on July 11, 2001, 9:06 PM
 */

import java.util.Iterator;
import java.util.Vector;


/**
 * This class holds the list of sorted event objects for one day. It has methods to 
 * print out a daily schedule in List and Table format, a method to add an event to
 * the list.
 *
 * @author  Yimin Li
 * @version 
 */

public class DailySchedule extends java.lang.Object {

/**
 * static constant variable
 */
    private static final int INITIAL_CAPACITY=10;
    

    /** Creates new DailySchedule */
    public DailySchedule() {
        eventList=new Vector(INITIAL_CAPACITY);
    }

    
    //Public static methods
    public static DailySchedule merge(DailySchedule ds1,DailySchedule ds2){
    
        if((ds1==null)&&(ds2==null)){
            return (DailySchedule) null;
        }
        else if(ds1==null){
            return (DailySchedule) ds2.clone();
        }
        else if(ds2==null){
            return (DailySchedule) ds1.clone();
        }
        else{
            DailySchedule ds= (DailySchedule) ds1.clone();
            Iterator iter=ds2.eventList.iterator();
            while(iter.hasNext()){
                Event e=(Event) iter.next();
                ds.addEvent(e);
            }
            return ds;
        }
 
    }
        
    //Public instance methods
/**
 * Add an event to daily schedule. The events are sorted as respect to 
 * the start time in one day
 */

    public void addEvent(Event e){
	Event event;
	Iterator iter=eventList.iterator();
        while(iter.hasNext()){
            event=(Event) iter.next();
            if(!event.before(e)){
                int i=eventList.indexOf(event);
                eventList.insertElementAt(e,i);
                return;
            }
        }
        eventList.add(e);
    }
    
    public Object clone(){
        DailySchedule ds=new DailySchedule();
        ds.eventList.addAll(eventList);
        return ds;
    }

    
    public String toListFormat(){
        String retVal="<ul>";
        Iterator iter=eventList.iterator();
        while(iter.hasNext()){
            Event e=(Event) iter.next();
            retVal+=e.toListFormat();
        }
        return retVal+"</ul>";
    }

    
    public String toTableFormat(Time eTime,Time lTime){
	String 	retVal="";
	Event 	e=null;

	Time  t0=eTime, t1=lTime,st=null,et=null;
	int height=t0.diffInMinutes(t1)/DateBook.TIME_SLICE;

	int th=0;
	Iterator iter=eventList.iterator();
	while(iter.hasNext()){
	  e= (Event) iter.next();
	  st=e.getStartTime();
	  et=e.getEndTime();

	  height=t0.diffInMinutes(st)/DateBook.TIME_SLICE;
	  retVal+="<tr><td height=\""+height+"\"> </td></tr>";
	  th+=height;


	  height=st.diffInMinutes(et)/DateBook.TIME_SLICE;
	  retVal+="<tr><td align=\"center\" valign=\"center\" height=\""+
			height+"\">"+
		e.toTableFormat()+"</td></tr>";
	  th+=height;
	  t0=et;

	}

	if(et!=null){
	  height=et.diffInMinutes(t1)/DateBook.TIME_SLICE;
	}
	th+=height;

	return retVal+"<tr><td height=\""+height+"\"></td></tr>";
	   
    }
 
    /** print a daily schedule in WML format*/
    public String toWMLFormat(String date){
        String retVal="";
        Event e=null;
        Iterator iter = eventList.iterator();
        int numLink=0,count=0;
        while(iter.hasNext()){
            e=(Event) iter.next();
            numLink=(iter.hasNext())? (count+1):0;
            retVal+="<card id=\"x"+count+"\" title =\""+date+"\"> \n"
                +"<p align=\"center\">"+e.toWMLFormat()+"<br/><a href=\"#x"
                +numLink+"\">Next</a></p> \n"
                +"</card>";
            count=numLink;
        }
        return retVal;
    }
    
    
    public Time getStartTime(){
	Time t=null;
	if(!eventList.isEmpty()){
	  Event fe=(Event) eventList.firstElement();
	  return fe.getStartTime();
	}
	return t;
    }

    public Time getEndTime(){

	Time t=null;

	if(!eventList.isEmpty()){
	  Event le=(Event) eventList.lastElement();
	  t=le.getEndTime();
	}
	return t;
    }
    
    //Private instance methods

    //Instance variables
    private Vector eventList;
}
