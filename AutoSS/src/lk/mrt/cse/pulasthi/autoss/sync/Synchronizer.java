package lk.mrt.cse.pulasthi.autoss.sync;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import lk.mrt.cse.pulasthi.autoss.tools.SRTTransciptWriter;
import edu.cmu.sphinx.result.Result;
import edu.ucsb.nmsl.tools.Transcript;

/**
 * The Synchronizer is the Class which compare the original subtitle file with the 
 * results from speech recognition and create the result file with timestamps
 * corrected in the source file. 
 * @author Pulasthi Mahawithana <pulasthi7@gmail.com>
 *
 */
public class Synchronizer {

	private Transcript original;
	private Transcript syncronized;
	private List<ResultChunk> detected;

	public Synchronizer() {
		detected = new ArrayList<ResultChunk>();
	}

	/**
	 * Adds a detected text phrase from audio to Detected text list 
	 * @param res The Result from the recognizer
	 * @param recognizedAt The approx. time in seconds(from the start) the result was detected
	 */
	public void addDetectedResult(Result res, int recognizedAt) {
		if (!"".equals(res.getBestFinalResultNoFiller())) {
			detected.add(new ResultChunk(recognizedAt, res.getBestFinalResultNoFiller()));
		} else if(!"".equals(res.getBestResultNoFiller())){
			detected.add(new ResultChunk(recognizedAt, res.getBestResultNoFiller()));
		}
	}

	/**
	 * Gets the original transcript
	 * @return the original transcript
	 */
	public Transcript getOriginal() {
		return original;
	}

	/**
	 * Set the Original Transcript
	 * @param original the original transcript
	 */
	public void setOriginal(Transcript original) {
		this.original = original;
	}

	/**
	 * Gets the Corrected Transcript
	 * @return the corrected transcript
	 */
	public Transcript getSyncronizedTranscipt() {
		// TODO:remove tempory call
		try {
			OutputStream dos = new FileOutputStream("detected.txt");
			printDetected(dos);
			dos.close();
			SRTTransciptWriter stw = new SRTTransciptWriter();
			OutputStream oos = new FileOutputStream("original.txt"); 
			stw.writeTranscript(original, oos);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Print detected results to a output stream. Used for testing purposes
	 * @param os The output Stream to which detected text is written
	 */
	public void printDetected(OutputStream os) {
		PrintWriter pw = new PrintWriter(os);
		for (int i = 0; i < detected.size(); i++) {
			pw.println(formatTimeStamp(detected.get(i).getDetectedAt()) +" ==> "+detected.get(i).getDetectedString());
		}
		pw.flush();
		pw.close();
	}
	
	/**
	 * Convert the time(in seconds from start) to a formatted string.
	 * @param timeInSec The time in seconds from start
	 * @return Formatted Timestamp hh:mm:ss
	 */
	private String formatTimeStamp(int timeInSec) {
		int sec = timeInSec%60;
		int min = (timeInSec/60)%60;
		int hr = timeInSec/3600;
		return String.format("%2d", hr) + ":" + String.format("%2d", min) + ":" + String.format("%2d", sec);
	}
}
