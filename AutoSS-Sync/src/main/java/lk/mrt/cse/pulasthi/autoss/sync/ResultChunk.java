package lk.mrt.cse.pulasthi.autoss.sync;

/**
 * Data Class to store the results detected from the speech recognizer
 * @author pulasthi
 *
 */
public class ResultChunk {
	private int detectedAt;
	private String detectedString;
	
	public ResultChunk(int detectedAt, String detectedString) {
		this.detectedAt = detectedAt;
		this.detectedString = detectedString;
	}

	/**
	 * Get the time the string is detected
	 * @return
	 */
	public int getDetectedAt() {
		return detectedAt;
	}

	/**
	 * Get the detected string
	 * @return
	 */
	public String getDetectedString() {
		return detectedString;
	}
}
