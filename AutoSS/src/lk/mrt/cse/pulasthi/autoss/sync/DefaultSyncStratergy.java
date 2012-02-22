package lk.mrt.cse.pulasthi.autoss.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import edu.ucsb.nmsl.tools.Caption;
import edu.ucsb.nmsl.tools.Transcript;

public class DefaultSyncStratergy implements SyncStratergy {
	
	private Map<String,WordOccurence> originalFreq;	
	private Transcript original;
	private List<ResultChunk> identified;
	private int averageWordSpeakTime = 1;	//TODO: to be changed
	

	@Override
	public Transcript getSyncrosizedTranscript(Transcript ori,
			List<ResultChunk> identified) {
		// TODO Auto-generated method stub
		this.original = ori;
		this.identified = identified;
		buildFreqMap();
		compareDetected();
		return null;
	}
	
	private void buildFreqMap() {
		originalFreq = new HashMap<String,WordOccurence>();
		
		for (ListIterator<Caption> itr = original.captionIterator(0); itr.hasNext();) {
			Caption currentCap = itr.next();
			String[] words = currentCap.getCaption().split("\\s+");
			for (int i = 0; i < words.length; i++) {
				String lowerCasedWord = words[i].toLowerCase();
				if(originalFreq.containsKey(lowerCasedWord)){
					originalFreq.get(lowerCasedWord).addOccurence((int)currentCap.getTime());
				} else{
					WordOccurence occ = new WordOccurence(lowerCasedWord, (int)currentCap.getTime());
					originalFreq.put(lowerCasedWord, occ);
				}
			}
		}
		
		for (int i = 0; i < identified.size(); i++) {
			ResultChunk curretResult = identified.get(i);
			String[] words = curretResult.getDetectedString().split("\\s+");
			int identifiedAt = curretResult.getDetectedAt();
			for (int j = 0; j < words.length; j++) {
				String currentWord = words[j].toLowerCase();
				if(originalFreq.containsKey(currentWord)){
					int detectedAt = identifiedAt - (j+1)*averageWordSpeakTime;
					originalFreq.get(currentWord).addVoiceDetection(detectedAt);
				}
			}
		}
	}
	
	
	private void compareDetected() {
		for (WordOccurence wordOcc : originalFreq.values()) {
			wordOcc.calculateDifferences();
			//TODO The statical calculations
		}
	}
	
	
	private class WordOccurence{
		private String word;
		private List<Integer> occurences;
		private List<Integer> vDitected;
		private int possibleShift;
		private float possibleScale;
		private float accurateProb;
		
		public WordOccurence(String word, int time) {
			this.word = word;
			occurences = new ArrayList<Integer>();
			occurences.add(time);
			vDitected = new ArrayList<Integer>();
		}
		
		public void addOccurence(int time) {
			occurences.add(time);
		}
		
		public int getOccurenceCount(){
			return occurences.size();
		}
		
		public void addVoiceDetection(int time) {
			vDitected.add(time);
		}
		
		public int getDetectedOccurences() {
			return vDitected.size();
		}
		
		public int getPossibleShift(){
			return possibleShift;
		}
		
		public float getPossibleScale() {
			return possibleScale;
		}
		
		public String getWord() {
			return word;
		}
		
		public void calculateDifferences() {
			//TODO the comparision
		}
	}
}
