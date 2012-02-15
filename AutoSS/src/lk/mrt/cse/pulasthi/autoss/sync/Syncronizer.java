package lk.mrt.cse.pulasthi.autoss.sync;

import java.util.List;

import edu.cmu.sphinx.result.Result;
import edu.ucsb.nmsl.tools.Transcript;

public class Syncronizer {
	
	private Transcript original;
	//private Transcript detected;
	private Transcript syncronized;
	private List<Result> detected;
	
	public void addDetectedResult(Result res, int recognizedAt) {
		detected.add(res);
	}
	
	public Transcript getSyncronizedTranscipt() {
		return null;
	}
}
