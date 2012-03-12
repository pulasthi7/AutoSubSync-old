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

	@Override
	public void writeTranscript(Transcript t, OutputStream out) {
		int counter = 1;
		PrintWriter pw = new PrintWriter(out);
		for(ListIterator<Caption> i = t.captionIterator(0); i.hasNext(); )
	      {
		      Caption cap = (Caption)i.next();
		      pw.println(counter);
		      pw.print(createTimeString((int)Math.round(cap.getTime())-1));
		      pw.print(" --> ");
		      pw.println(createTimeString((int)Math.round(cap.getFinishTime())-1));
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
