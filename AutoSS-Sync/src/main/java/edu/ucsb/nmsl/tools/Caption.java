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
    private String sentence;
    
    private int startTime;
    
    private int finishTime;

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
    public Caption(final int startTime, final int finishTime, final String captionText) {
        this.startTime = startTime;
        this.finishTime = finishTime;
        sentence = captionText;
    }

    /**
     * 
     * This accessor method returns the start time-stamp for the caption. This
     * time-stamp represents the start of the caption being spoken in the media.
     * 
     * @return The start time-stamp of the caption in seconds.
     * 
     */
    public int getTime() {
        return startTime;
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
    public void setStartTime(final int time) {
        this.startTime = time;
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
    public void setFinishTime(final int time) {
        this.finishTime = time;
    }

    /**
     * 
     * This mutator method sets the finish time-stamp for the caption. This
     * time-stamp represents the end of the caption being spoken in the media.
     * 
     * @return The finish time-stamp of the caption in seconds.
     * 
     */
    public int getFinishTime() {
        return finishTime;
    }


    /**
     * 
     * This accessor method returns the all the caption chunks as a String.
     * 
     * @return The text of the caption.
     * 
     */
    public String getCaption() {
        return sentence;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Caption) {
            Caption other = (Caption) obj;
            boolean capEquals = sentence.equals(other.sentence);
            boolean finishEquals = finishTime == other.finishTime;
            boolean startEquals = startTime==other.startTime;
            return capEquals && finishEquals && startEquals;
        }
        return false;
    }
}
