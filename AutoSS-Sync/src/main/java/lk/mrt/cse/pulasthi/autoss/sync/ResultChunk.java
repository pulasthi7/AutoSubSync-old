package lk.mrt.cse.pulasthi.autoss.sync;

public class ResultChunk {
	private int detectedAt;
	private String detectedString;
	
	public ResultChunk(int detectedAt, String detectedString) {
		this.detectedAt = detectedAt;
		this.detectedString = detectedString;
	}

	public int getDetectedAt() {
		return detectedAt;
	}

	public String getDetectedString() {
		return detectedString;
	}
}
