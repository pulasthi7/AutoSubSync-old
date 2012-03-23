//   Copyright (C) 2005 The Regents of the University of California.
//   All Rights Reserved.
//
//   Permission to use, copy, modify, and distribute this software and its
//   documentation for educational, research and non-profit purposes,
//   without fee, and without a written agreement is hereby granted,
//   provided that the above copyright notice, this paragraph and the
//   following three paragraphs appear in all copies.
//
//   Permission to incorporate this software into commercial products may
//   be obtained by contacting the University of California. For
//   information about obtaining such a license contact:
//
//   Chrisanna Waldrop
//   Copyright Officer
//   805-893-7773
//   waldrop@research.ucsb.edu
//
//   This software program and documentation are copyrighted by The Regents
//   of the University of California. The software program and
//   documentation are supplied "as is", without any accompanying services
//   from The Regents. The Regents does not warrant that the operation of
//   the program will be uninterrupted or error-free. The end-user
//   understands that the program was developed for research purposes and
//   is advised not to rely exclusively on the program for any reason.
//
//   IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
//   FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
//   INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND
//   ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN
//   ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF
//   CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT
//   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
//   A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS"
//   BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE
//   MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
package edu.ucsb.nmsl.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 * 
 * <p>
 * This class represents the basic components of a Transcritp, captions. For the
 * purposes of AutoCap, a caption is defined as a segment of a transcript that
 * has a time-stamp. The purpose of this time stamp is to mark the point in time
 * that the beginning of a caption was spoken. Furthermore, these captions may
 * represent different levels of organization within the transcript. For
 * example, the transcripts for QAD Inc.'s media materials represented groups of
 * related sentences. The number of these sentences is usually between two and
 * three. Other levels of organization are also possible to use such as
 * sentences or parts of sentences. It is not the responsibility of the Caption
 * or Transcript classes to segment the transcripts. The organization of
 * transcripts is expressed within the transcript files themselves.
 * <p>
 * 
 * <p>
 * Each Caption object is responsible for storing and providing acess to both
 * the text of the caption and a time-stamp. Each caption is further broken into
 * caption chunks. The size of these chunks is one word per chunk. Furthermore,
 * each chunk keeps the time-stamp of when the word begins and ends withing the
 * media file. This organization of captions allows for the user of the class to
 * iterate through each word of the caption which facilitates the estimation
 * phase of the AutoCap process. Specialized instances of this class may choose
 * a different level of organization.
 * </p>
 * 
 * see edu.ucsb.nmsl.tools.Transcript Transcript
 * 
 * @author Allan Knight
 * @version 1.0
 * 
 */
public class Caption {
	/**
	 * This inner class is responsible for storing the individual caption chunks
	 * that make up a caption. This implementation defines a caption chunk as a
	 * word.
	 * 
	 * @see edu.ucsb.nmsl.tools.Caption Caption
	 * 
	 * @author Allan Knight
	 * @version 1.0
	 * 
	 */
	public class CaptionChunk {
		/** The text of the caption */
		protected String caption;

		/**
		 * The time-stamp that indicates when the start of the caption chunk
		 * occured in the media file.
		 */
		protected double startTime = -1.0;

		/**
		 * The time-stamp that indicates when the end of the caption chunk
		 * occured in the media file.
		 */
		protected double finishTime = -1.0;

		/**
		 * The estimated maximum error for the start of the word. See the
		 * research paper associated with this work for more information
		 */
		protected double maxError = 0.0;

		/**
		 * 
		 * This contructor creates a CaptionChunk instance and sets the start
		 * and finish times of the caption along with the text of the caption.
		 * 
		 * @param startTime
		 *            The start time, in seconds, of the caption.
		 * @param finishTime
		 *            The finish time, in seconds, of the caption.
		 * @param captionText
		 *            The text of the caption, in this case a word.
		 * 
		 */
		public CaptionChunk(final double startTime, final double finishTime,
				final String captionText) {
			this.startTime = startTime;
			this.finishTime = finishTime;
			this.caption = captionText;
		}

		/**
		 * 
		 * This accessor method returns the start time-stamp of the caption.
		 * 
		 * @return The start time-stamp, in seconds, of the caption chunk.
		 * 
		 */
		public double getStartTime() {
			return startTime;
		}

		/**
		 * 
		 * This accessor method returns the finish time-stamp of the caption.
		 * 
		 * @return The finish time-stamp, in seconds, of the caption chunk.
		 * 
		 */
		public double getFinishTime() {
			return finishTime;
		}

		/**
		 * 
		 * This accessor method returns the estimated maximum error associated
		 * with the start time-stamp of a CaptionChunk instance.
		 * 
		 * @return The estimated maximum error in seconds.
		 * 
		 */
		public double getMaxError() {
			return maxError;
		}

		/**
		 * This accessor method returns the text of the caption chunk, or word.
		 * 
		 * @return The caption chunk text.
		 * 
		 */
		public String getCaption() {
			return caption;
		}

		/**
		 * 
		 * This mutator method sets the start time-stamp associated with the
		 * caption chunk.
		 * 
		 * @param time
		 *            The start time-stamp of the caption chunk in seconds.
		 * 
		 */
		public void setStartTime(final double time) {
			startTime = time;
		}

		/**
		 * 
		 * This mutator method sets the finish time-stamp associated with the
		 * caption chunk.
		 * 
		 * @param time
		 *            The finish time-stamp of the caption chunk in seconds.
		 */
		public void setFinishTime(final double time) {
			finishTime = time;
		}

		/**
		 * 
		 * This mutator method sets the maximum caption error associated with a
		 * caption chunk.
		 * 
		 * @param error
		 *            The estimated caption error in seconds.
		 * 
		 */
		public void setMaxError(final double error) {
			maxError = error;
		}

		/**
		 * 
		 * This mutator method sets the text of the caption chunk.
		 * 
		 * @param captionChunk
		 *            The caption chunk, or word, as a String.
		 * 
		 */
		public void setCaption(final String captionChunk) {
			caption = captionChunk;
		}
	}

	/**
	 * 
	 * This default contructor creates a default instance of a caption with the
	 * start and finish time-stamps set to -1 and the caption text set to the
	 * empty string.
	 * 
	 */
	public Caption() {
		captionBuffer = new StringBuffer();
	}

	/**
	 * 
	 * This constructor creates an instance with the given caption text and
	 * given start time-stamp.
	 * 
	 * @param timestamp
	 *            The start time-stamp associated with the caption.
	 * @param captionText
	 *            The caption text as a String. This caption chunk should be a
	 *            word.
	 * 
	 */
	public Caption(final double timestamp, final String captionText) {
		captions.add(new CaptionChunk(timestamp, -1.0, captionText));
		captionBuffer = new StringBuffer(captionText);
	}

	/**
	 * 
	 * This constructor creates an instance with the given caption text, given
	 * start time-stamp and given finish time-stamp.
	 * 
	 * @param startTime
	 *            The start time-stamp associated with the caption.
	 * @param finishTime
	 *            The finish time-stamp associated with the caption.
	 * @param captionText
	 *            The caption text as a String. This caption chunk should be a
	 *            word.
	 * 
	 */
	public Caption(final double startTime, final double finishTime, final String captionText) {
		captions.add(new CaptionChunk(startTime, finishTime, captionText));
		captionBuffer = new StringBuffer(captionText);
	}

	/**
	 * 
	 * This contructor creates an instance with the given caption text and given
	 * start time-stamp, which is in string form. This constructor is helpful
	 * when reading the captions from a file.
	 * 
	 * @param startTime
	 *            The start time-stamp of the caption given as a string.
	 * @param captionText
	 *            The caption text as a String. This caption chunk should be a
	 *            word.
	 * 
	 */
	public Caption(final String startTime, final String captionText) {
		captions.add(new CaptionChunk(parseTime(startTime), -1.0, captionText)); 
																// pulasthi on
																// 3/23/12 9:56
																// PM
		captionBuffer = new StringBuffer(captionText);
	}

	/**
	 * 
	 * This contructor creates an instance with the given caption text, given
	 * start and finish time-stamps, which are in string form. This constructor
	 * is helpful when reading the captions from a file.
	 * 
	 * @param startTime
	 *            The start time-stamp of the caption given as a string.
	 * @param finishTime
	 *            The finish time-stamp of the caption given as a string.
	 * @param captionText
	 *            The caption text as a String. This caption chunk should be a
	 *            word.
	 * 
	 */
	public Caption(final String startTime, final String finishTime, final String captionText) {
		captions.add(new CaptionChunk(parseTime(startTime), parseTime(finishTime), captionText)); 
																		// by
																		// pulasthi
																		// on
																		// 3/23/12
																		// 9:56
																		// PM
		captionBuffer = new StringBuffer(captionText);
	}

	/**
	 * 
	 * This method is a helper function that parses a time-stamp of the form
	 * "HH:MM:SS" from a string and returns the number of seconds rempresented
	 * by the string.
	 * 
	 * @param timestamp
	 *            The time-stamp string as read from a file.
	 * 
	 * @return The number of seconds rrepresented by the time string.
	 * 
	 */
	protected double parseTime(final String timestamp) {
		double timeValue = 0.0;

		final String parts[] = timestamp.split(":");
		for (int i = 0; i < parts.length; ++i) {
			timeValue += FACTORS[i] * Double.valueOf(parts[i]);
		}

		return timeValue;
	}

	/**
	 * 
	 * This method appends another caption chunk on the end of a Caption with
	 * the given start and finish time-stamps.
	 * 
	 * @param startTime
	 *            The start time-stamp associated with the caption.
	 * @param finishTime
	 *            The finish time-stamp associated with the caption.
	 * @param captionText
	 *            The caption text as a String. This caption chunk should be a
	 *            word.
	 * 
	 */
	public void appendCaption(final double startTime, final double finishTime, final String captionText) {
		captions.add(new CaptionChunk(startTime, finishTime, captionText.toLowerCase(Locale.US)));
		captionBuffer.append(captionText);
		captionBuffer.append(' ');
	}

	/**
	 * 
	 * This accessor method returns the start time-stamp for the caption. This
	 * time-stamp represents the start of the caption being spoken in the media.
	 * 
	 * @return The start time-stamp of the caption in seconds.
	 * 
	 */
	public double getTime() {
		return captions.get(0).getStartTime();
	}

	/**
	 * 
	 * This mutator method sets the start time-stamp for the caption. This
	 * time-stamp represents the start of the caption being spoken in the media.
	 * 
	 * @param time
	 *            The start time-stamp of the caption in seconds.
	 * 
	 */
	public void setStartTime(final double time) {
		captions.get(0).setStartTime(time);
	}

	/**
	 * 
	 * This accessor method returns the finish time-stamp for the caption. This
	 * time-stamp represents the end of the caption being spoken in the media.
	 * 
	 * @param time
	 *            The finish time-stamp of the caption in seconds.
	 * 
	 */
	public void setFinishTime(final double time) {
		captions.get(captions.size() - 1).setFinishTime(time);
	}

	/**
	 * 
	 * This mutator method sets the finish time-stamp for the caption. This
	 * time-stamp represents the end of the caption being spoken in the media.
	 * 
	 * @return The finish time-stamp of the caption in seconds.
	 * 
	 */
	public double getFinishTime() {
		return captions.get(captions.size() - 1).getFinishTime();
	}

	/**
	 * 
	 * This accessor function returns the maximum caption error associated with
	 * the start time-stamp of the caption.
	 * 
	 * @return The maximum caption error in seconds.
	 * 
	 */
	public double getMaxError() {
		return captions.get(0).getMaxError();
	}

	/**
	 * 
	 * This mutator method sets the maximum error associated with the caption.
	 * 
	 * @param error
	 *            The maximum caption error in seconds.
	 * 
	 */
	public void setMaxError(final double error) {
		captions.get(0).setMaxError(error);
	}

	/**
	 * 
	 * This accessor method returns the first caption chunk, which should be a
	 * word of the caption.
	 * 
	 * @return The first caption chunk of the caption.
	 * 
	 */
	public CaptionChunk getFirstChunk() {
		return captions.get(0);
	}

	/**
	 * 
	 * This accessor method returns the last caption chunk, which should be a
	 * word of the caption.
	 * 
	 * @return The last caption chunk of the caption.
	 * 
	 */
	public CaptionChunk getLastChunk() {
		return captions.get(captions.size() - 1);
	}

	/**
	 * 
	 * This accessor method returns the all the caption chunks as a String.
	 * 
	 * @return The text of the caption.
	 * 
	 */
	public String getCaption() {
		return captionBuffer.toString();
	}

	/**
	 * 
	 * This method returns an Iterator that allows for the sequential access to
	 * each chunk of the caption. Since each chunk is a word, this iterator
	 * allows the caller to iterate through each word of the caption.
	 * 
	 * @return An Iterator for sequential sequential access to each caption
	 *         chunk.
	 * 
	 */
	public Iterator<CaptionChunk> iterator() {
		return captions.iterator();
	}

	/**
	 * 
	 * This method returns an Iterator that allows for the sequential access to
	 * each chunk of the caption. Since each chunk is a word, this iterator
	 * allows the caller to iterate through each word of the caption.
	 * 
	 * @return An Iterator for sequential sequential access to each caption
	 *         chunk.
	 * 
	 */
	public ListIterator<CaptionChunk> listIterator(final int idx) {
		return captions.listIterator(idx);
	}

	/**
	 * 
	 * This method returns the number of chunks, or words in the caption.
	 * 
	 * @return The number of chunks in the caption.
	 * 
	 */
	public int size() {
		return captions.size();
	}

	/**
	 * The entire text of the caption. This parameter is for speed up when
	 * returning the caption text.
	 */
	protected StringBuffer captionBuffer;

	/** A list of each caption chunk that makes up the entire caption */
	protected List<CaptionChunk> captions = new ArrayList<CaptionChunk>();

	/** Multiplication factors for parsing a time from a string. */
	protected static final double FACTORS[] = { 3600, 60, 1 };
}
