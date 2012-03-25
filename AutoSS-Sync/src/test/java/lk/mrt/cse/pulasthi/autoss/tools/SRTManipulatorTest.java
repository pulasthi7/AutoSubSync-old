/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.mrt.cse.pulasthi.autoss.tools;

import edu.ucsb.nmsl.tools.Transcript;
import edu.ucsb.nmsl.tools.TranscriptFileReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author pulasthi
 */
public class SRTManipulatorTest extends TestCase {

    private static final int MAX_SHIFT = 10;
    private Transcript testSample;
    private TranscriptFileReader reader;

    public SRTManipulatorTest(String testName) {
        super(testName);
        reader = new SRTTransciptReader();
    }
    
    private Transcript loadTranscriptFrom(String filePath) throws IOException{
        InputStream fileIS = new FileInputStream(filePath);
        Transcript transcript = reader.readTranscript(fileIS);
        fileIS.close();
        return transcript;
    }

    @Override
    protected void setUp() {
        try {
            testSample = loadTranscriptFrom("src/test/resources/testSubtitle.srt");
        } catch (IOException ex) {
            Logger.getLogger(SRTManipulatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        testSample = null;
    }

    /**
     * Test of shift method, of class SRTManipulator.
     */
    public void testShift() {
        try {
            int time = new Random(System.nanoTime()).nextInt(MAX_SHIFT) + 1;
            System.out.println("shift for "+time);
            SRTManipulator instance = new SRTManipulator();
            instance.shift(testSample, time);
            instance.shift(testSample, -time);
            Transcript original = loadTranscriptFrom("src/test/resources/testSubtitle.srt");
            assertEquals(original, testSample);
        } catch (IOException ex) {
            Logger.getLogger(SRTManipulatorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } 
    }

    /**
     * Test of scale method, of class SRTManipulator.
     */
    public void testScale() {
        try {
            float factor = 0.75f + new Random(System.nanoTime()).nextFloat()/2; //will have range 0.75-1.25
            System.out.println("scale for "+factor);
            SRTManipulator instance = new SRTManipulator();
            instance.scale(testSample, factor);
            instance.scale(testSample, 1 / factor);

            Transcript original = loadTranscriptFrom("src/test/resources/testSubtitle.srt");
            assertEquals(original, testSample);
        } catch (IOException ex) {
            Logger.getLogger(SRTManipulatorTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
