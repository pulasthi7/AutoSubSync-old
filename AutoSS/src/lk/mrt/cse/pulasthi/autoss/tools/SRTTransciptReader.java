package lk.mrt.cse.pulasthi.autoss.tools;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.w3c.dom.Document;

import edu.ucsb.nmsl.tools.Caption;
import edu.ucsb.nmsl.tools.Transcript;
import edu.ucsb.nmsl.tools.TranscriptFileReader;

public class SRTTransciptReader implements TranscriptFileReader {

	@Override
	public Transcript readTranscript(InputStream in) {
		Scanner sc = new Scanner(in);
		List<Caption> transcipt = new ArrayList<Caption>();
		while (sc.hasNextLine()) {
			StringBuilder textChunk = new StringBuilder();
			String lineRead = sc.nextLine();	//ignored : the incrementing identifier
			lineRead = sc.nextLine();	//ignored : the timestamp
			while(!(lineRead=sc.nextLine()).equals("")){
				textChunk.append(lineRead);
			}
			String[] words = textChunk.toString().
					replaceAll("[#$%&()*+\\-,./:;<=>?@[\\\\]]", " ").
					replaceAll("\\s+", " ").
					split("\\s+");
			if (words.length>0) {
				Caption newCaption = new Caption();
				for (int i = 0; i < words.length; i++) {
					newCaption.appendCaption(-1.0, -1.0, words[i]);
				}
				transcipt.add(newCaption);
			}
		}
		return new Transcript(transcipt);
	}

	@Override
	public Transcript readTranscript(Document d) {
		throw new UnsupportedOperationException();
	}

}
