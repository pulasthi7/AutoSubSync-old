package lk.mrt.cse.pulasthi.autoss.tools;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.Element;

import edu.ucsb.nmsl.tools.Caption;
import edu.ucsb.nmsl.tools.Transcript;
import edu.ucsb.nmsl.tools.TranscriptFileWriter;

public class SRTTransciptWriter implements TranscriptFileWriter {

	@Override
	public void writeTranscript(Transcript t, OutputStream out) {
		int counter = 1;
		PrintWriter pw = new PrintWriter(out);
		for(Iterator i = t.captionIterator(0); i.hasNext(); )
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
	}
	
	protected String createTimeString(int value)
	  {
	    int sec = value % 60;
	    int min = (value / 60) % 60;
	    int hr  = value / 3600;
	    
	    String formattedHr = String.format("%d", hr);
	    String formattedMin = String.format("%2d", min);
	    String formattedSec = String.format("%d", sec);

	    return formattedHr+":"+formattedMin+":"+formattedSec+","+"000";
	  }

}
