package lk.mrt.cse.pulasthi.autoss.tools;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ListIterator;

import edu.ucsb.nmsl.tools.Caption;
import edu.ucsb.nmsl.tools.Transcript;
import edu.ucsb.nmsl.tools.TranscriptFileWriter;

/**
 * Write the transcript to a file in srt format
 * @author Pulasthi Mahawithana <pulasthi7@gmail.com>
 *
 */
public class SRTTransciptWriter implements TranscriptFileWriter {

	public void writeTranscript(Transcript t, OutputStream out) {
		int counter = 1;
		PrintWriter pw = new PrintWriter(out);
		for(ListIterator<Caption> i = t.captionIterator(); i.hasNext(); )
	      {
		      Caption cap = (Caption)i.next();
		      pw.println(counter);
		      pw.print(createTimeString(cap.getTime()));
		      pw.print(" --> ");
		      pw.println(createTimeString(cap.getFinishTime()));
		      pw.println(cap.getCaption());
		      pw.println();
		      counter++;
	      }
		pw.flush();
	}
	
	/**
	 * Create formatted timestamp from the elapsed seconds
	 * @param value Elapsed seconds
	 * @return Formatted timestamp hh:mm:ss,000)
	 */
	private String createTimeString(int value)
	  {
		if(value<0){
			return "00:00:00,000";
		}
	    int sec = value % 60;
	    int min = (value / 60) % 60;
	    int hr  = value / 3600;
	    String fmtHr = ""+hr;
	    String fmtMin = ""+min;
	    String fmtSec = "" + sec;
	    if(hr<10){
	    	fmtHr = "0"+hr;
	    }
	    if(min<10){
	    	fmtMin = "0"+min;
	    }
	    if(sec<10){
	    	fmtSec = "0"+sec;
	    }

	    return fmtHr+":"+fmtMin+":"+fmtSec+","+"000";
	  }

}
