package lk.mrt.cse.pulasthi.autoss.sync;

import java.util.List;

import edu.ucsb.nmsl.tools.Transcript;

public interface SyncStratergy {
	
	public Transcript getSyncrosizedTranscript(Transcript ori, List<ResultChunk> identified);

}
