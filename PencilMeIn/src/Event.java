/*
 * Event.java
 *
 * Created on July 11, 2001, 9:58 AM
 */

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.lang.Integer;
import java.util.StringTokenizer;
import org.w3c.dom.*;
import javax.xml.parsers.*;

//import org.w3c.dom.;
//import org.w3c.dom.Node;
/**
 * This class creates an Event object with name, the start time, duration.
 * It can print the information of event in table or list html format.
 * 
 * @author  Yimin Li
 * @version 
 */

public class Event extends Object {

/** Default constructor  */
    public Event() {}
    
/** Constructor*/
    public Event(String n,TimeInterval d,boolean r){
        name=n;
        duration=d;
        bRegularOrOneTime=r;
    }
    
/** Construct an object from a patterned string */
    public Event(String strEvent){
        StringTokenizer strTokens=new StringTokenizer(strEvent,";");
        name=strTokens.nextToken();
        parseDateField(strTokens.nextToken());
        setDuration(strTokens.nextToken(),strTokens.nextToken());
    }
        
/** Construct an object from a event node in a Document */
    public Event(Node event){
       //Set event name 
       NamedNodeMap atts=event.getAttributes();
       name=(atts.item(0)).getNodeValue();
       
       //Set days or date of the event
       setDate(event);
       //Set Time interval
       setDuration(event);
       
    }
        
        
    //Public instance methods

/** Print in List format*/
    public String toListFormat(){
	String ms="",me="";
	if(!bRegularOrOneTime){
	    ms="<b>";
	    me="</b>";
	}
        return "<li>"+ms+duration.toString()+" "+name+me+"</li>";
    }

/** Print in Table format*/
    public String toTableFormat(){
	String ms="",me="";
	if(!bRegularOrOneTime){
	    ms="<b>";
	    me="</b>";
	}

        return ms+"<font size=\"2\">"+
		name+"<br>"+duration.toString()+"</font>"+me;
    }
    
    /** Print in WML format*/
    public String toWMLFormat(){

        return name+"<br/>"+duration.toString();
    }
   
// Getters and mutters
    public void setDuration(String start,String end){
        duration=new TimeInterval(start,end);
    }
    
    public void setDuration(Node event){
        Node start=getChildNodeByTagName(event,"starttime"),
            end=getChildNodeByTagName(event,"endtime");
        setDuration(getTextField(start),getTextField(end));
    }
    
    public void setDateForOneTimeEvent(int y,int m,int d){
        Calendar c=new GregorianCalendar(y,m-1,d);
                dateForOneTimeEvent=c.getTime();
    }
        
    public static String getTextField(Node n){        
        NodeList nList=n.getChildNodes();
        Node text=nList.item(0);
        return text.getNodeValue();
    }
       
    public Time getStartTime(){
	return duration.getStartTime();
    }

    public Time getEndTime(){
	return duration.getEndTime();
    }

    public TimeInterval getDuration(){
        return duration;
    }
    
    public Date getDateForOneTimeEvent(){
        return dateForOneTimeEvent;
    }
    
    public String getDaysForRegularEvent(){
        return daysInWeek;
    }

    public boolean isRegular(){
        return bRegularOrOneTime;
    }

/** compare this object with object e to find the earlier one*/   
    public boolean before(Event e){
        return duration.before(e.duration);
    }
    

    //Private instance methods
    private void parseDateField(String str){
        StringTokenizer tokens=new StringTokenizer(str,"/");
        switch(tokens.countTokens()){
            case 1:
                bRegularOrOneTime=true; 
                daysInWeek=tokens.nextToken();
                break;
            case 3:
                bRegularOrOneTime=false;
                int m=Integer.parseInt(tokens.nextToken());
                int d=Integer.parseInt(tokens.nextToken());
                int y=Integer.parseInt(tokens.nextToken());
	        setDateForOneTimeEvent(y,m,d);
                break;
            default:
                System.err.println("err: Event class: parse date field");
        }     
    }
    
    //set days or date from a event node
    private void setDate(Node event){
        Node date=getChildNodeByTagName(event,"date");   
        if(date!=null){
            //set up an one-time event
            setupOneTimeEvent(date);
        }
        else{
            Node days=getChildNodeByTagName(event,"days");
            //setup a regular event
            setupRegularEvent(days);
        }
    }
    
    private static Node getChildNodeByTagName(Node event,String name){
        NodeList nList=event.getChildNodes();
        int ll=nList.getLength();
        Node n=null,ret=null;
        
        String tag=null;
        for(int i=0;i<ll;i++){
            n=nList.item(i);
            tag=n.getNodeName();
            if(tag.equals(name)){
                return n;
            }
        }
        return ret;
    }
            
    private void setupOneTimeEvent(Node date){
        //set the flag
        bRegularOrOneTime=false;
        
        //set date
        String [] tags={"month","day","year"};
        String [] mdy=new String[3];
        int [] dt=new int[3];
        Node nn=null;
        for(int i=0;i<3;i++){
            nn=getChildNodeByTagName(date,tags[i]);
            mdy[i]=getTextField(nn);
            dt[i]=Integer.parseInt(mdy[i]);
        }
        setDateForOneTimeEvent(dt[2],dt[0],dt[1]);
    }
    
    private void setupRegularEvent(Node days){
        //set the flag
        bRegularOrOneTime=true;
        
        //set days for regular event
        String [] dw={"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
        Node nn=null;
        daysInWeek="";
        for(int i=0;i<7;i++){
            nn=getChildNodeByTagName(days,dw[i]);
            if(nn!=null){
               daysInWeek+=getDayAbbr(dw[i]);
            }
        }
    }
            
    private static char getDayAbbr(String dw){
        
        String d=dw.toLowerCase();
        char c=dw.charAt(0);
        
        if(d.equals("thursday")) {return 'h';}
        else if(d.equals("saturday")){return 'a';}
        return c;
    }
     
                
    
           
    //Private Instance variables
    private String name;		//name of the event
    private TimeInterval duration;	//the duration of the event
    private boolean bRegularOrOneTime;  //the type of the event
    private String daysInWeek;		//the day in the week for regular event 
    private Date dateForOneTimeEvent;   // a date for one-time event
}

/** A helper class for calculate the time interval in a day*/

class TimeInterval{
    //Static variable
    public static final int TIME_UNIT=10;
    
    public TimeInterval(){
    }
    public TimeInterval(String start,String end){
        setStartTime(start);
        setEndTime(end);
    }
    //Public instance methods
    public String toString(){
        return startTime.toString()+" - "+endTime.toString();
    }
    public boolean before(TimeInterval ti){
        return startTime.before(ti.startTime);
    }

    public int getDurationInMinutes(){
        return endTime.getTimeInMinutes()
                -startTime.getTimeInMinutes();
    }

    public int getDurationInHours(){
        return getDurationInMinutes()/Time.MINUTES_PER_HOUR;
             
    }
    
    public Time getStartTime(){
        return startTime;
    }

    public void setStartTime(Time start){
	startTime=start;
    }

    public Time getEndTime(){
        return endTime;
    }

    public void setEndTime(Time end){
	endTime=end;
    }
    
    public void setStartTime(String t){
        startTime=new Time(t);
    }
    public void setEndTime(String t){
        endTime=new Time(t);
    }

    public void union(TimeInterval ti){
	if(ti==null) return;
	Time tis=ti.getStartTime(),
	     tie=ti.getEndTime();
	if(tis.before(startTime)) startTime=tis;
	if(endTime.before(tie)) endTime=tie;
    }
    public void roundup(){
	int ts=startTime.getTimeInMinutes();
	int te=endTime.getTimeInMinutes();
	ts=(ts/Time.MINUTES_PER_HOUR)*Time.MINUTES_PER_HOUR;
	te=(te/Time.MINUTES_PER_HOUR+1)*Time.MINUTES_PER_HOUR;
	startTime.setTimeInMinutes(ts);
	endTime.setTimeInMinutes(te);
    }

    //Private instance variables
    private Time startTime;
    private Time endTime;
}
