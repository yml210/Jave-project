/*
 * DateBook.java
 *
 * Created on July 11, 2001, 10:09 PM
 */

import java.util.Hashtable;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.lang.Character;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;
//import SimpleFileReader;
//import SimpleFileWriter;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import java.io.BufferedReader;
import java.io.IOException;


/**
 * This class creates a weekly schedule from the date specified by users.
 *
 * @author  Yimin Li
 * @version 
 */

public class DateBook extends Object {

    //Static variables
    public static final int INPUT_TXT=0;
    public static final int INPUT_XML=1;
    public static final int OUTPUT_TABLE=0;
    public static final int OUTPUT_LIST=1;
    public static final int OUTPUT_WML=2;


    /** Default constrator */
    public DateBook() {
        oneTimeEvents=new Hashtable();
        regularEvents=new DailySchedule[7];
        for(int i=0;i<7;++i){
            regularEvents[i]=new DailySchedule();
        }
        date=new GregorianCalendar();
    }
    
    /** Create a new object for an input file*/
    public DateBook(SimpleFileReader input,int inputType){
        this();
        int count=0;
        if(inputType==0){
            count=getEventsFromTxt(input);
        }
        else{
            count=getEventsFromXml(input);
        }
        System.out.println("Read "+count+" events from the file");
    }
            
 
    
    //Public instance methods
    
/** Print out a List or table of schedule to an output file*/
    public void printout(int type,SimpleFileWriter output){

        switch(type){
            case OUTPUT_LIST:
                output.println(toListFormat());
                break;
            case OUTPUT_TABLE:
                output.println(toTableFormat());
                break;
            case OUTPUT_WML:
                output.println(toWMLFormat());
        }
        output.close();
    }
    
    public String toListFormat(){
        Calendar cd=(GregorianCalendar) date.clone();
	SimpleDateFormat df=new SimpleDateFormat("EEE MM'/'dd");
        DailySchedule ds=null;
	Date d=null;

        String retVal=HTM_HEAD+"<ul>";

        for(int i=0;i<7;i++){
	    d=cd.getTime();
            retVal+="<li>"+ df.format(d);

            ds = getScheduleFor(cd);
            retVal+=ds.toListFormat()+"</li>";

            cd.add(Calendar.DATE,1);
        }
        return retVal+"</ul>"+HTM_END;
    }


    public String toTableFormat(){

	DailySchedule ws[]=getWeeklySchedule();

        Calendar cd=(GregorianCalendar) date.clone();
	SimpleDateFormat df=new SimpleDateFormat("EEE MM'/'dd");
	Date d=null;

        String retVal=HTM_HEAD +"<table "+TABLE_ATTRIBUTE+">"
			+"<tr><td valign=\"top\">"+firstCOL()+"</td>";

	for(int i=0;i<7;i++){

	    d=cd.getTime();
            retVal+="\n <td valign=top><table cellpadding=2 border=1>"
			+"<tr><th height="
			+ F_HEIGHT+">" +df.format(d)
		    	+"</th></tr>";

	    retVal+= (ws[i]).toTableFormat(earliest,latest) +
		"</table></td>\n";

	    cd.add(Calendar.DATE,1);
	}

        return retVal+"</tr></table>"+HTM_END;
    }

/** print a daily schedule in WML format*/
    
    public String toWMLFormat(){
        //Get the daily schedule for the date specified by user
        
        DailySchedule ds=getScheduleFor(date);
        SimpleDateFormat df=new SimpleDateFormat("EEE MM'/'dd");
        String d=df.format(date.getTime());
        
        String retVal="<?xml version =\"1.0\"?>\n"
            +"<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\" "
            +"\"http://www.wapforum.org/DTD/wml_1.1.xml\">\n"
            +"<wml>";
        String tmp=ds.toWMLFormat(d);

        retVal+=(tmp.equals(""))? "<card></card>":tmp;

        return retVal+"</wml>";
    }

/**
 * Return a schedule for a specified date*/
    
    public DailySchedule getScheduleFor(Calendar cd){
	
	int dw=cd.get(Calendar.DAY_OF_WEEK)-1;
        DailySchedule rds=regularEvents[dw];

        DailySchedule ods=(DailySchedule) oneTimeEvents.get(cd.getTime());       

        return DailySchedule.merge(rds,ods);
    }

    public void setStartDate(Date d){
	
        date.setTime(d);
    }

    
    //Private instance methods
    private int getEventsFromTxt(SimpleFileReader input){     
       String line;
        int count=0;
        while((line=input.readLine())!=null){
            line=line.trim();
            if((!line.equals(""))
		&&(line.charAt(0)!='#')){
                addEvent(new Event(line));
                count++;
            }
        }
        return count;
                           
    }
 // Create the event lists from an XML input   
    private int getEventsFromXml(SimpleFileReader input){
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        int count =0;
        try{
            //Create a DOM for the input XML and get a node list of the event node.
            DocumentBuilder db= dbf.newDocumentBuilder();
            Document d=db.parse(new InputSource(input.getReader()));
            NodeList events=d.getElementsByTagName("event");
            
            count=events.getLength();
            for(int i=0;i<count;i++){
       
                //Construct an Event object and add it to the DateBook
                addEvent(new Event(events.item(i))); 
            }
        
        }
        catch(ParserConfigurationException pCE){
            pCE .printStackTrace();
        }
        catch(SAXException sAE){
        }
        catch(IOException IOE){
        }
        return count;
    }
        
/**
 * Add a new event to DateBook */

    private void addEvent(Event e){

        if(e.isRegular()){
            String days=e.getDaysForRegularEvent();
            addRegularEvent(days,e);
        }
        else{
            Date d=e.getDateForOneTimeEvent();
            addOneTimeEvent(d,e);
        }
    }

/**
 * Add a regular event to DateBook*/ 
    
    private void addRegularEvent(String d, Event e){
        DailySchedule ds=regularEvents[0];
        for(int i=0;i<d.length();++i){
            switch(Character.toLowerCase(d.charAt(i))){
                case 's':
                    ds=regularEvents[0];
                    break;
                case 'm':
                    ds=regularEvents[1];
                    break;
                case 't':
                    ds=regularEvents[2];
                    break;
                case 'w':
                    ds=regularEvents[3];
                    break;
                case 'h':
                    ds=regularEvents[4];
                    break;
                case 'f':
                    ds=regularEvents[5];
                    break;
                case 'a':
                    ds=regularEvents[6];
                    break;
                default:
                    System.err.println("error: DateBook class: set regularEvents");
            }
            
            ds.addEvent(e);
        }
    }

//  Add an one-time event
    
    private void addOneTimeEvent(Date d,Event e){
        DailySchedule ds =(DailySchedule) oneTimeEvents.get(d);
        if(ds==null){ 
            ds=new DailySchedule();
            oneTimeEvents.put(d,ds);
        }
        ds.addEvent(e);
    }




// Print the first column in table output html file
    private String firstCOL(){

	Time etime=new Time(earliest);
	Time ltime=new Time(latest);
	String retVal="<table border=\"1\"><tr><td height=\""
			+F_HEIGHT+"\"></td></tr>";
	int height=Time.MINUTES_PER_HOUR/TIME_SLICE;
	int hours=etime.diffInHours(ltime);
	for(int h=0;h<hours;h++){
	  retVal+="<tr><th height=\""+height+"\">"+
		etime.toString()+"</th></tr>";
	  etime.add(60);
	}
	return retVal+"</table>";

    }

// Get a weekly schedule from the date specified by the instance variable date

    private DailySchedule[] getWeeklySchedule(){
        Calendar cd=(GregorianCalendar) date.clone();
	Time et=null,lt=null;
	DailySchedule[] ws=new DailySchedule[7];
	
	for(int i=0;i<7;i++){
	   ws[i]=getScheduleFor(cd);

	   et=(ws[i]).getStartTime();
	   lt=(ws[i]).getEndTime();

	   setTimeSpan(et,lt);

	   cd.add(Calendar.DATE,1);
	}
	return ws;
    }

// set the earliest start time and the latest end time of the events in the week
    private void setTimeSpan(Time et,Time lt){
	if(et==null || lt==null ){
	  return;
	}
	else if(earliest==null||latest==null){
	  earliest=new Time(et);
	  latest =new Time(lt);
	}
	else{
	  earliest.setEarlierTime(et);
	  latest.setLaterTime(lt);
	}
    }  
	 
    //Instance variables
    
    private DailySchedule[] regularEvents; //holds the seven day schedule for regular events
    private Hashtable oneTimeEvents;	   //holds the daily schedule for one time events
    private GregorianCalendar date;	   //the date from which the output weekly  
					   //schedule started
    private Time earliest,latest;	   //the earliest start time and latest end time in 
					   // the selected week

    //Constant variables
     public static final int F_WIDTH=50, F_HEIGHT=30,
		T_WIDTH=950,T_HEIGHT=500,T_BORDER=2,T_PADDING=1,
		C_WIDTH=(T_WIDTH-F_WIDTH)/7, TIME_SLICE=1;
     public  static Time  START_TIME=new Time(9,0,true);
     public  static Time  END_TIME=new Time(7,0,false);
     
     public static final String HTM_HEAD="<html><head><title></title></head><body>",
     			HTM_END="</body></html>",
     			TABLE_ATTRIBUTE="border="+T_BORDER
				+" cellpadding="+T_PADDING;
    
}
