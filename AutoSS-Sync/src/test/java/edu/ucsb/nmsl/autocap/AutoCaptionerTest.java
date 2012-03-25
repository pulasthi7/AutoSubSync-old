package edu.ucsb.nmsl.autocap;

import edu.ucsb.nmsl.tools.Transcript;
import edu.ucsb.nmsl.tools.TranscriptFileReader;
import edu.ucsb.nmsl.tools.TranscriptFileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import lk.mrt.cse.pulasthi.autoss.tools.SRTManipulator;
import lk.mrt.cse.pulasthi.autoss.tools.SRTTransciptReader;
import lk.mrt.cse.pulasthi.autoss.tools.SRTTransciptWriter;

public class AutoCaptionerTest extends TestCase {
    
    private TranscriptFileReader reader;
    private TranscriptFileWriter writer;

    public AutoCaptionerTest() {
        reader = new SRTTransciptReader();
        writer = new SRTTransciptWriter();
    }
       
    
    /**
     * Invoke the testWait method with different values
     */
    public void testWaitForConversion() {
        int[] testHops = {7, 50, 21};
        for (int i = 0; i < testHops.length; i++) {
            testWait(testHops[i]);
        }
    }
    
    /**
     * Create a test file and keep adding some text to it for given no of hops
     * Meanwhile invoke the subject method in a separate thread.
     * Make sure that the subject is running until the text addition is finished
     * @param hops The no of hopes to test. a hop = 100 ms in this context
     */
    private void testWait(int hops) {
        try {
            final File testFile = File.createTempFile("test", "");
            PrintWriter pw = new PrintWriter(testFile);
            Runnable testRun = new Runnable() {

                public void run() {
                    AutoCaptioner subject = new AutoCaptioner();
                    try {
                        subject.waitForConversion(testFile);
                    } catch (InterruptedException e) {
                    }
                }
            };
            
            Thread testThread = new Thread(testRun);
            testThread.start();
            while (hops > 0) {
                pw.print("0123456789");
                pw.flush();
                Thread.sleep(100);
                hops--;
                assertTrue(testThread.isAlive());
            }
            Thread.sleep(2000);
            pw.close();
            testFile.delete();
            assertFalse(testThread.isAlive());
        } catch (Exception ex) {
            fail("Exception thrown: " + ex.getMessage());
        }
    }
    
    /**
     * Tests the AutoCaptioner.writeCorrectedToFile() method.
     * Procedure:
     *  Delete any existing test files and assert they are deleted
     *  load sample file and write it back to test file
     *  alter the subfile by shifting it and call the test subject method.
     *  assert test file and bak file exists now.
     *  assert the sample is equal to back file but not equal to test file.
     */
    public void testWriteCorrectedToFile(){
        try {
            File testFile = new File("src/test/resources/sample.srt");
            File bakFile = new File("src/test/resources/sample.srt.bak");
            testFile.delete();
            bakFile.delete();
            assertFalse(testFile.exists());
            assertFalse(bakFile.exists());
            Transcript testSample = loadTranscriptFrom("src/test/resources/modifiedTest.srt");
            writeTranscriptTo(testSample, testFile.getPath());
            new SRTManipulator().shift(testSample, 5);
            new AutoCaptioner().writeCorrectedToFile(testSample, testFile.toURI());
            assertTrue(testFile.exists());
            assertTrue(bakFile.exists());
            testSample = loadTranscriptFrom("src/test/resources/modifiedTest.srt");
            Transcript backup = loadTranscriptFrom(bakFile.getPath());
            Transcript newlyWritten = loadTranscriptFrom(testFile.getAbsolutePath());
            assertNotSame(testSample, newlyWritten);
            assertEquals(testSample, backup);
        } catch (IOException ex) {
            Logger.getLogger(AutoCaptionerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private Transcript loadTranscriptFrom(String filePath) throws IOException{
        InputStream fileIS = new FileInputStream(filePath);
        Transcript transcript = reader.readTranscript(fileIS);
        fileIS.close();
        return transcript;
    }
    
    private void writeTranscriptTo(Transcript transcript, String filePath) throws IOException{
        OutputStream fileOS = new FileOutputStream(filePath);
        writer.writeTranscript(transcript, fileOS);
        fileOS.close();
    }
}
