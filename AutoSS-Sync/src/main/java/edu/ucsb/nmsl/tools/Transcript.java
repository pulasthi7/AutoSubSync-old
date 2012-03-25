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
import java.util.Collection;
import java.util.ListIterator;

import java.util.List;

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

    private List<Caption> sentences;

    /**
     * This contructor creates an instance of a Transcript and creates a segment
     * for each element of the Collection object passed to it.
     * 
     * @param sentenceList
     *            - A Collection instance that contains segments of a
     *            transcript.
     */
    public Transcript(final Collection<Caption> sentenceList) {
        sentences = new ArrayList<Caption>();
        sentences.addAll(sentenceList);
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
    public ListIterator<Caption> captionIterator() {
        return sentences.listIterator();
    }

     /**
     * This method returns the number of segments within the transcript.
     * 
     * @return The number of segments that make up the transcript.
     */
    public int size()
    {
        return sentences.size();
    }

    /**
     * This method returns the ith segment, or caption, withing the Transcript.
     * 
     * @param i
     *            - The index of the desired segment or caption.
     * @return The ith Caption instance from the Transcript.
     */
    public Caption getCaption(int i) {
        return sentences.get(i);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Transcript) {
            Transcript other = (Transcript) obj;
            if (other.sentences.size() == sentences.size()) {
                for (int i = 0; i < sentences.size(); i++) {
                    if (!sentences.get(i).equals(other.sentences.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentences.size(); i++) {
            Caption cap = sentences.get(i);
            sb.append("[");
            sb.append(cap.getTime());
            sb.append("==>");
            sb.append(cap.getFinishTime());
            sb.append(" : ");
            if(cap.getCaption().length()>5){
                sb.append(cap.getCaption().substring(0, 5));
            } else{
                sb.append(cap.getCaption());
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
