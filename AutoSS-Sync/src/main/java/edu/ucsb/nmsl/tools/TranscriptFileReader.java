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

import java.io.InputStream;
import org.w3c.dom.Document;

/**

 This class was created to allow for the easy integration of many file formats
 for specifying captions. The responsibility of any class implementing this
 interface is to take a Transcript object and read the captions along with 
 their time-stamp from a file or stream. The input can then be used to display
 captioned media.


 @version 1.0

 */
public interface TranscriptFileReader
{
  /**
   
   This method accepts an InputStream instance to read captions and time stamps
   from in order to create an instance of a Transcript object.
   
   @param in - An InputStream instance that Transcript information will be read
               from. 
               
   @return A newly created Transcript instance created based on the information
           read from the InputStream.
  

  
   */
  public Transcript readTranscript(InputStream in);

  /**
  
   This method accepts a Document instance to read captions and time stamps
   from in order to create an instance of a Transcript object. The Document
   object is defined as part of the default XML DOM implementation that 
   accompanies the JVM from Sun.

   @param d - An instance of a Document object that contains the caption
              information in an XML DOM object.
              
   @return A newly created Transcript instance created based on the information
           read from the InputStream.
  

  
  */
  public Transcript readTranscript(Document d);
}
