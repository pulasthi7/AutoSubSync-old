package lk.mrt.cse.pulasthi.autoss.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import lk.mrt.cse.pulasthi.autoss.tools.SRTManipulator;

import edu.ucsb.nmsl.tools.Caption;
import edu.ucsb.nmsl.tools.Transcript;

/**
 * This is a basic implementation of the SyncStratergy interface. This class
 * uses a basic statical calculation to detect the possible shift to correct the
 * subtitle No actions are taken to correct the scaling in this
 * implementation.(Will be added later with a separate class)
 * 
 * @author pulasthi
 * 
 */
public class DefaultSyncStratergy implements SyncStratergy {

	private Map<String, WordOccurence> originalFreq;
	private Transcript original;
	private List<ResultChunk> identified;
	private final static float AVG_WORD_TIME = 0.5f; // TODO: to be
														// changed
	private final static int IGNORE_MARGIN = 2;

	private int shift = 0;

	@Override
	public Transcript getSyncrosizedTranscript(final Transcript ori,
			final List<ResultChunk> identified) {
		this.original = ori;
		this.identified = identified;
		buildFreqMap();
		compareDetected();
		final SRTManipulator manipulator = new SRTManipulator();
		manipulator.shift(ori, shift);
		System.out.println("Corrected (shifted " + shift + " seconds)"); 
		return ori;
	}

	/**
	 * Get the amount that the subtitles are shifted as a result of
	 * synchronization.
	 * 
	 * @return
	 */
	public int getShift() {
		return shift;
	}

	/**
	 * Create the maps with the key-value pairs of word,time. Two maps are
	 * created. One map is created using the original subtitle file The other
	 * one is created with the data from the voice recognition
	 */
	private void buildFreqMap() {
		originalFreq = new HashMap<String, WordOccurence>();

		for (ListIterator<Caption> itr = original.captionIterator(0); itr
				.hasNext();) {
			final Caption currentCap = itr.next();
			final String[] words = currentCap.getCaption().split("\\s+");
			for (int i = 0; i < words.length; i++) {
				final String lowerCasedWord = words[i].toLowerCase();
				if (originalFreq.containsKey(lowerCasedWord)) {
					originalFreq.get(lowerCasedWord).addOccurence(
							(int) currentCap.getTime());
				} else {
					final WordOccurence occ = new WordOccurence(
							(int) currentCap.getTime());
					originalFreq.put(lowerCasedWord, occ);
				}
			}
		}

		for (int i = 0; i < identified.size(); i++) {
			ResultChunk curretResult = identified.get(i);
			final String[] words = curretResult.getDetectedString().split(
					"\\s+");
			int identifiedAt = curretResult.getDetectedAt();
			for (int j = 0; j < words.length; j++) {
				String currentWord = words[j].toLowerCase();
				if (originalFreq.containsKey(currentWord)) {
					int detectedAt = (int) (identifiedAt - (words.length - j)
							* AVG_WORD_TIME);
					originalFreq.get(currentWord).addVoiceDetection(detectedAt);
				}
			}
		}
	}

	/**
	 * Compare the original data with the voice recognized data to detect the
	 * amount of shift needed for correction.
	 */
	private void compareDetected() {
		Map<Integer, Integer> shiftFreq = new HashMap<Integer, Integer>();
		for (WordOccurence wordOcc : originalFreq.values()) {
			wordOcc.calculateDifferences();
			if (!wordOcc.isValuesSet()) {
				continue;
			}
			if (shiftFreq.containsKey(wordOcc.getPossibleShift())) {
				shiftFreq.put(wordOcc.getPossibleShift(),
						shiftFreq.get(wordOcc.getPossibleShift()) + 1);
			} else {
				shiftFreq.put(wordOcc.getPossibleShift(), 1);
			}
		}
		shift = getMaxValuedKey(shiftFreq);
	}

	/**
	 * Analyze and get the best matching key, which (including +-IGNORE_MARGIN)
	 * has the highest value.
	 * 
	 * @param map
	 *            The map of <Integer, Integer> to analyze
	 * @return The best matching key
	 */
	private int getMaxValuedKey(final Map<Integer, Integer> map) {
		int maxfreq = Integer.MIN_VALUE;
		int result = 0;
		for (Entry<Integer, Integer> currentPair : map.entrySet()) {
			int currentScore = 0;
			for (int i = currentPair.getKey() - IGNORE_MARGIN; i <= currentPair
					.getKey() + IGNORE_MARGIN; i++) {
				if (map.containsKey(i)) {
					currentScore += map.get(currentPair.getKey());
				}
			}
			if (currentScore > maxfreq) {
				maxfreq = currentScore;
				result = currentPair.getKey();
			}
		}
		return result;
	}

	/**
	 * Inner class to hold each occurrences of a word
	 * 
	 * @author pulasthi
	 * 
	 */
	private class WordOccurence {
		private final List<Integer> occurences;
		private final List<Integer> vDitected;
		private int possibleShift;
		private boolean valuesSet;

		/**
		 * Constructor of WordOccurence object
		 * 
		 * @param word
		 *            The word that occured
		 * @param time
		 *            The time it is detected at the first time
		 */
		public WordOccurence(final int time) {
			occurences = new ArrayList<Integer>();
			occurences.add(time);
			vDitected = new ArrayList<Integer>();
			valuesSet = false;
		}

		/**
		 * Add a occurrence to the word, when the word appears more than two
		 * times
		 * 
		 * @param time
		 *            The time the word occurs (in seconds)
		 */
		public void addOccurence(final int time) {
			occurences.add(time);
		}

		/**
		 * Adds a time where the word is detected by voice recognizer
		 * 
		 * @param time
		 *            The time the word is detected
		 */
		public void addVoiceDetection(final int time) {
			vDitected.add(time);
		}

		/**
		 * Gives the best value of the possible shift based on the calculation
		 * 
		 * @return The calculated Shift
		 */
		public int getPossibleShift() {
			return possibleShift;
		}

		/**
		 * Gives whether the shift and scale values are set for this word
		 */
		public boolean isValuesSet() {
			return valuesSet;
		}

		/**
		 * Calculate the best possible shift
		 */
		public void calculateDifferences() {
			Map<Integer, Integer> shifts = new HashMap<Integer, Integer>();
			for (int voiceDetected : vDitected) {
				for (int oldValue : occurences) {
					int shift = voiceDetected - oldValue;
					if (shifts.containsKey(shift)) {
						shifts.put(shift, shifts.get(shift) + 1);
					} else {
						shifts.put(shift, 1);
					}
				}
			}

			if (!shifts.isEmpty()) {
				possibleShift = getMaxValuedKey(shifts);
				valuesSet = true;
			}
		}

	}
}
