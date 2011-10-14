/*
 * PencilMeIn.java
 *
 * Created on July 14, 2001, 2:20 PM
 */


//import SimpleInput;
import java.util.StringTokenizer;
import java.lang.Integer;
import java.util.Date;
// import SimpleFileReader;
// import SimpleFileWriter;

/**
 * A drive class which interacts with users, and get information to create 
 * DateBook object, and print out a weekly schedule in List or Table format.
 *
 * @author  Yimin Li
 * @version
 */

public class PencilMeIn extends Object {
    
    
    private static SimpleInput input=new SimpleInput();
    private static   boolean bContinue=true;
    private static   int inputFileType=DateBook.INPUT_TXT; 
    
    public static void main(String[] argvs){
        
        while(bContinue){
            
            DateBook db=new DateBook(getInputFile(),inputFileType);
            db.setStartDate(getStartDate());
            db.printout(getOutputType(),getOutputFile());
            bContinue=continueOrNot();
        }
    }

// Get a SimpleFileReader instance and set the input file type.    
    private static SimpleFileReader getInputFile(){
        String def="events.txt";
        String prompt
        = "Whelcome to the Pencil Me In datebook program.\n"
        +"This program reads in event files and generates HTML weekly schedules. For\n"
        +"Convenience, when you are asked a question, the answer given in [brackets]\n"
        +"is the default used if you don't choose to enter a response.\n\n"
        +"Enter name of file to read: ";
        
	String fileName=input.readLine(prompt,def);
        fileName=fileName.trim();
        //see if the input file is an xml file

        SimpleFileReader reader=SimpleFileReader.openFileForReading(fileName);
        setInputFileType(fileName);
        while(reader==null){
	  prompt="Can't not open file "+fileName+
		".\n Please enter file name again:";
	  input.readLine(prompt,def);
          reader=SimpleFileReader.openFileForReading(fileName);
	}
	return reader;
    }
  
    //Set input file type based on the extension of input file
    private static void setInputFileType(String fileName){
        StringTokenizer tokens=new StringTokenizer(fileName,".");
        String fExt="";
        while(tokens.hasMoreTokens()){
            fExt=tokens.nextToken();
        }
        fExt=fExt.toLowerCase();
        inputFileType=DateBook.INPUT_TXT;
        if(fExt.equals("xml")) inputFileType=DateBook.INPUT_XML;
    }
        
    //Get the start date for user
    private static Date getStartDate(){
        String def="8/12/2001";
        String prompt="Now ready to output an HTML schedule.\n"
        +"Enter start date for schedule:";
	return input.readDate(prompt,def);

    }
    //get output file type. one of HTML LIST, HTML TABLE, and WML
    private static int getOutputType(){
        String def="2";
        String prompt="Do you wish to create 1) HTML table, 2) HTML list, "
                    +"or 3) WML output:";
        int type=input.readInteger(prompt,def);

        while(true){
            switch(type){
                case 1: return DateBook.OUTPUT_TABLE;
                case 2: return DateBook.OUTPUT_LIST;
                case 3: return DateBook.OUTPUT_WML;
            }
            prompt=" There is no option "+ type+". Please choose type again:";
            type=input.readInteger(prompt,def);
        }
    }
    
    //Get SimpeFileWrite object
    private static SimpleFileWriter getOutputFile(){
        String def="table.html";
        String prompt="Enter name of file to output: ";
        
        String fileName= input.readLine(prompt,def);
        SimpleFileWriter writer=SimpleFileWriter.openFileForWriting(fileName);
	while(writer==null){
	  prompt="Can not open file "+fileName
		+". \n Please enter file name again:";
	  fileName=input.readLine(prompt,def);
	  writer=SimpleFileWriter.openFileForWriting(fileName);
	}
	return writer;
    }
    
    private static boolean continueOrNot(){
        String def="n";
        String prompt="Wrote weekly schedule to file.\n\n"
        +" Do you wish to create other schedules? ";
        
        return input.readYesOrNo(prompt,def);
    }
}
