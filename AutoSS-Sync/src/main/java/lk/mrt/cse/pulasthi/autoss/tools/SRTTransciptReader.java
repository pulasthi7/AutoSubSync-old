package lk.mrt.cse.pulasthi.autoss.tools;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.w3c.dom.Document;

import edu.ucsb.nmsl.tools.Caption;
import edu.ucsb.nmsl.tools.Transcript;
import edu.ucsb.nmsl.tools.TranscriptFileReader;

/**
 * Reads the srt files and prepare them to the further processes
 * @author Pulasthi Mahawithana <pulasthi7@gmail.com>
 *
 */
public class SRTTransciptReader implements TranscriptFileReader {

	public Transcript readTranscript(InputStream in) {
		//Documentation Inherited
		Scanner sc = new Scanner(in);
		List<Caption> transcipt = new ArrayList<Caption>();
		while (sc.hasNextLine()) {
			StringBuilder textChunk = new StringBuilder();
			String lineRead = sc.nextLine();	//ignored : the incrementing identifier
			lineRead = sc.nextLine();	//ignored : the timestamp
			String[] spTimestamps = lineRead.split("-->");
			int startTime = buildIntTimeStamp(spTimestamps[0]);
			int endTime = buildIntTimeStamp(spTimestamps[1]);
			while(!(lineRead=sc.nextLine()).equals("")){
				textChunk.append(" ");
                                textChunk.append(lineRead);
			}
			String words = textChunk.toString().
					replaceAll("[#$%&()*+\\-,./:;<=>?@[\\\\]]", " ").
					replaceAll("\\s+", " ").trim();
			if (words.length()>0) {
				Caption newCaption = new Caption(startTime, endTime, words);
				transcipt.add(newCaption);
			}
		}
		return new Transcript(transcipt);
	}

	public Transcript readTranscript(Document d) {
		throw new UnsupportedOperationException();	//Not Supported nor needed.
	}

	/**
	 * Get the no of seconds from start given the timestamp hh:mm:ss
	 * @param timestamp Timestamp
	 * @return No of seconds from start
	 */
	private int buildIntTimeStamp(String timestamp) {
		timestamp = timestamp.trim();
		String[] splittedTimestamp = timestamp.split("\\D");
		int time = 0;
		time += 3600 * Integer.parseInt(splittedTimestamp[0]);
		time += 60 * Integer.parseInt(splittedTimestamp[1]);
		time += Integer.parseInt(splittedTimestamp[2]);
		return time;
	}
}
