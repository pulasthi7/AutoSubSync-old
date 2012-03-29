package lk.mrt.cse.pulasthi.autoss.sync;

import java.util.List;

import edu.ucsb.nmsl.tools.Transcript;

/**
 * Interface to support multiple stratergies with stratergy pattern
 * @author pulasthi
 *
 */
public interface SyncStratergy {
	/**
	 * Get the synchronized transcript when the detected strings and the original transcript is given
	 * @param ori Original transcript
	 * @param identified The identified results from the speech recognizer
	 * @return
	 */
	public Transcript getSyncrosizedTranscript(Transcript ori, List<ResultChunk> identified);

}
