package lk.mrt.cse.pulasthi.autoss.sync;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.cmu.sphinx.result.Result;
import edu.ucsb.nmsl.tools.Transcript;

public class Syncronizer {

	private Transcript original;
	// private Transcript detected;
	private Transcript syncronized;
	private List<ResultChunk> detected;

	public Syncronizer() {
		detected = new ArrayList<ResultChunk>();
	}

	public void addDetectedResult(Result res, int recognizedAt) {
		if (!"".equals(res.getBestFinalResultNoFiller())) {
			detected.add(new ResultChunk(recognizedAt, res.getBestFinalResultNoFiller()));
		} else if(!"".equals(res.getBestResultNoFiller())){
			detected.add(new ResultChunk(recognizedAt, res.getBestResultNoFiller()));
		}
	}

	public Transcript getSyncronizedTranscipt() {
		// TODO:remove tempory call
		try {
			OutputStream os = new FileOutputStream("detected.txt");
			printDetected(os);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void printDetected(OutputStream os) {
		PrintWriter pw = new PrintWriter(os);
		for (int i = 0; i < detected.size(); i++) {
			pw.println(formatTimeStamp(detected.get(i).getDetectedAt()) +" ==> "+detected.get(i).getDetectedString());
		}
		pw.flush();
		pw.close();
	}
	
	private String formatTimeStamp(int timeInSec) {
		int sec = timeInSec%60;
		int min = (timeInSec/60)%60;
		int hr = timeInSec/3600;
		return String.format("%2d", hr) + ":" + String.format("%2d", min) + ":" + String.format("%2d", sec);
	}
}
