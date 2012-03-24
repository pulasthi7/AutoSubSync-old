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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import edu.ucsb.nmsl.tools.Caption.CaptionChunk;

/**
 * <p>
 * This class represents the basic abstraction of a Transcript: a collection of
 * text segments and a time stamp representing when the first word of the
 * collection segment was spoken. The responsibility of this class is to provide
 * the user with a mechanism for storing the text of spoken media and to map
 * these spoken words to the time they were spoken. This responsibility
 * necessarily implies that the class provides an interface to randomly access
 * any segment of the transcript and the time the segment was spoken.
 * </p>
 * 
 * <p>
 * At a lower level the Transcript class also provides a mechanism for iterating
 * through each chunk of the transcript. While these chunks usually represent
 * the individual words of each segment, any specialization of the class can
 * chose a different level of atomicity for the individual transcript chunks.
 * Also provided by this class an iterator functionality that allows the user to
 * treat the trascript as one large stream of transcript chunks with the
 * time-stamp for each chunk.
 * </p>
 * 
 * <p>
 * For the purposes of AutoCap, this implementation of the Transcript
 * abstraction breaks the transcript into segments as provided in the input file
 * and further breaks these segments into each individual word. Each segment can
 * then be thought of as a caption and usually includes a single sentence or
 * serveral sentences. Each chunk of these segements are words. Both levels of
 * organization include a times stamp of when speach started and ended.
 * </p>
 * 
 * @version 1.0
 */
public class Transcript {
	/**
	 * The responsibility of this inner class is to provide an iterator
	 * interface to the transcript class for the purpose of breaking a
	 * transcript into its individual chunks. These interators operate both
	 * forwards and backwards and across each individual segement of the
	 * transcript. The main purpose of this class is to allow the estimation
	 * process to count words between words that were recognized during the
	 * recognition phase and those that were not.
	 * 
	 * @version 1.0
	 */
	protected class TranscriptIterator implements ListIterator<CaptionChunk> {
		/** Index of the current word in the current segment in the transcript. */
		protected int index = 0;

		/** Indicates the iterator is currently moving backwards. */
		protected static final int BACKWARD = 0;

		/** Indicates the iterator is currently moving forward. */
		protected static final int FORWARD = 1;

		/**
		 * Indicates the current direction of the iterator as indicated by the
		 * last call to either previous() or next().
		 */
		protected int direction = FORWARD;

		/**
		 * This constructor creates a new instances of the Transcript iterator
		 * from a ListIterator instance and the index of the current chunk in
		 * the list. The ListIterator points to a segment with in the transcript
		 * and index indicates which word within that segemtn to start from whcn
		 * calling previous() and next().
		 * 
		 * @param iterator
		 *            - The ListIterator that points to the current segment in
		 *            the Transcript, offset by "index" words.
		 * @param index
		 *            - The index of the current word within the segment passed
		 *            as i.
		 */
		public TranscriptIterator(final ListIterator<Caption> iterator,
				final int index) {
			currCaption = iterator;
			nextCaptionWord();

			for (int j = 0; j < index; ++j) {
				next();
			}
		}

		/**
		 * This method returns the next transcript chunk if one exists. If there
		 * are no more chunks left within the current segment, theb the first
		 * word of the next segment is returned. The end of the transcript has
		 * been reached if no more segments exist.
		 * 
		 * @return The next caption chunk in the transcript.
		 */
		public CaptionChunk next() {
			++index;

			if (!currWord.hasNext()) {
				nextCaptionWord();
			}

			return currWord.next();
		}

		/**
		 * This method returns the previous transcript chunk if one exists. If
		 * there are no more chunks left within the current segment, then the
		 * last chunk of the previous segment is returned. The beginning of the
		 * transcript has been reached if no more segments exist.
		 * 
		 * @return The previous caption chunk in the transcript.
		 */
		public CaptionChunk previous() {
			--index;

			if (!currWord.hasPrevious()) {
				previousCaptionWord();
			}

			return currWord.previous();
		}

		/**
		 * This method returns the index of the transcript chunk for the next
		 * call to the next() method. This index represents the position of the
		 * chunk within the entire transcript. For example, if the index 3 is
		 * returned, then the next caption chunk is the 4th chunk, as the
		 * indices start from zero.
		 * 
		 * @return The index of the caption chunk for the next call to next().
		 */
		public int nextIndex() {
			return index;
		}

		/**
		 * This method returns the index of the transcript chunk for the next
		 * call to the previous() method. This index represents the position of
		 * the chunk within the entire transcript. For example, if the index 3
		 * is returned, then the next caption chunk is the 4th chunk, as the
		 * indices start from zero.
		 * 
		 * @return The index of the caption chunk for the next call to
		 *         previous().
		 */
		public int previousIndex() {
			return index;
		}

		/**
		 * This method determines whether another caption chunk exists if a
		 * subsequent call to next() is made.
		 * 
		 * @return True if there is a next caption chunk, false otherwise.
		 * @see edu.ucsb.nmsl.tools.Transcript.TranscriptIterator#next() next().
		 */
		public boolean hasNext() {
			if (!currWord.hasNext()) {
				if (!currCaption.hasNext()) {
					return false;
				}
				nextCaptionWord();
			}

			return currWord.hasNext();
		}

		/**
		 * This method determines whether another caption chunk exists if a
		 * subsequent call to previous() is made.
		 * 
		 * @return True if there is a previous caption chunk, false otherwise.
		 * @see edu.ucsb.nmsl.tools.Transcript.TranscriptIterator#previous()
		 *      previous().
		 */
		public boolean hasPrevious() {
			if (!currWord.hasPrevious()) {
				if (!currCaption.hasPrevious()) {
					return false;
				}
				previousCaptionWord();
			}

			return currWord.hasPrevious();
		}

		/**
		 * This method add a chunk at the current index within the transcript.
		 * 
		 * @param chunk
		 *            - The caption chunk to be inserted into the transcript.
		 */
		public void add(final CaptionChunk chunk) {
			currWord.add(chunk);
		}

		/**
		 * This method removes a chunk at the current index within the
		 * transcript.
		 */
		public void remove() {
			currWord.remove();
		}

		/**
		 * This method sets a chunk at the current index within the transcript
		 * to the value passed into the method.
		 * 
		 * @param chunk
		 *            - The caption chunk to be set at the current location.
		 */
		public void set(final CaptionChunk chunk) {
			currWord.set(chunk);
		}

		/**
		 * This method is a helper function that moves the current transcript
		 * segment forward to the next segment. It is called when the last word
		 * of the previous transcript segment was reached.
		 */
		protected void nextCaptionWord() {
			if (direction == BACKWARD) {
				currCaption.next();
			}

			direction = FORWARD;

			final Caption cap = currCaption.next();
			currWord = cap.listIterator(0);
		}

		/**
		 * This method is a helper function that moves the current transcript
		 * segment backward to the previous segment. It is called when the first
		 * word of the next transcript segment was reached.
		 */
		protected void previousCaptionWord() {
			Caption cap = null;
			if (direction == FORWARD) {
				cap = currCaption.previous();
			}

			direction = BACKWARD;

			if (currCaption.hasPrevious()) {
				cap = currCaption.previous();
			}
			currWord = cap.listIterator(cap.size());
		}

		protected ListIterator<Caption> currCaption;
		protected ListIterator<CaptionChunk> currWord;
	}

	/**
	 * This default contructor creates a default instance of a Transcript.
	 */
	public Transcript() {
	}

	/**
	 * This contructor creates an instance of a Transcript and creates a segment
	 * for each element of the Collection object passed to it.
	 * 
	 * @param transcriptSegments
	 *            - A Collection instance that contains segments of a
	 *            transcript.
	 */
	public Transcript(final Collection<Caption> transcriptSegments) {
		captions.addAll(transcriptSegments);
	}

	/**
	 * This method returns an Iterator instance that can be used to sequentially
	 * access each segment of the Transcript instance.
	 * 
	 * @return An iterator that allows sequential access to each segment of a
	 *         transcript.
	 */
	public Iterator<CaptionChunk> iterator() {
		return new TranscriptIterator(captions.listIterator(0), 0);
	}

	/**
	 * This method returns an Iterator instance that allows the sequential
	 * access to each chunk of a Transcript rather than each segment.
	 * 
	 * @param index
	 *            - The index of a chunk within the trasncript. Subsequent calls
	 *            to previous() or next() will be relative to this chunk.
	 * @return A ListIterator instance that points to the indexth chunk.
	 */
	public ListIterator<CaptionChunk> chunkIterator(int index) {
		return new TranscriptIterator(captions.listIterator(0), index);
	}

	/**
	 * This method returns a ListIterator instance that allows for bidirectional
	 * sequential access to each of the segments within a transcript.
	 * 
	 * @param index
	 *            - The index of a segment withing the transcript. Subsequent
	 *            calls to previous() or next() will be relative to the indexth
	 *            segment.
	 * @return A ListIterator instance that points to the indexth segment.
	 */
	public ListIterator<Caption> captionIterator(int index) {
		return captions.listIterator(index);
	}

	public int size()
	/**
	 * This method returns the number of segments within the transcript.
	 * 
	 * @return The number of segments that make up the transcript.
	 */
	{
		return captions.size();
	}

	/**
	 * This method returns the ith segment, or caption, withing the Transcript.
	 * 
	 * @param i
	 *            - The index of the desired segment or caption.
	 * @return The ith Caption instance from the Transcript.
	 */
	public Caption getCaption(int i) {
		return captions.get(i);
	}

	/**
	 * This method returns the entire transcript, minus time-stamps, as text to
	 * the caller.
	 * 
	 * @return A string with the text from each segment of the Transcript
	 *         instance.
	 */
	public String getAsText() {
		StringBuffer buf = new StringBuffer();
		for (Iterator<CaptionChunk> i = chunkIterator(0); i.hasNext();) {
			buf.append(((Caption.CaptionChunk) i.next()).getCaption() + " ");
		}

		return buf.toString();
	}

	/** The individual segments, or caption, that make up the Transcript. */
	protected LinkedList<Caption> captions = new LinkedList<Caption>();

}
