package edu.ucsb.nmsl.autocap;

import java.io.File;
import java.io.PrintWriter;

import junit.framework.TestCase;

public class AutoCaptionerTest extends TestCase {
    
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
    public void testWait(int hops) {
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
            Thread.sleep(1500);
            pw.close();
            testFile.delete();
            assertFalse(testThread.isAlive());
        } catch (Exception ex) {
            fail("Exception thrown: " + ex.getMessage());
        }
    }
}
