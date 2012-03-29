package lk.mrt.cse.pulasthi.autoss.tools;

import java.util.ListIterator;

import edu.ucsb.nmsl.tools.Caption;
import edu.ucsb.nmsl.tools.Transcript;

/**
 * This class provides manipulation methods for subtitles.
 * @author Pulasthi Mahawithana <pulasthi7@gmail.com>
 *
 */
public class SRTManipulator {

	/**
	 * Shift each timestamp of the subtitle parts by given no of seconds 
	 * @param t The transcript to be shifted
	 * @param time The amount of time to be shifted
	 */
	public void shift(Transcript t, int time) {
		for (ListIterator<Caption> i = t.captionIterator(); i.hasNext();) {
			Caption cap = i.next();
			cap.setStartTime(cap.getTime() + time);
			cap.setFinishTime(cap.getFinishTime() + time);
		}
	}

	/**
	 * Multiply each timestamp of the subtitle parts by given factor
	 * @param t The transcript to be scaled
	 * @param factor The scale to be scaled
	 */
	public void scale(Transcript t, float factor) {
		for (ListIterator<Caption> i = t.captionIterator(); i.hasNext();) {
			Caption cap = i.next();
                        float start = cap.getTime() * factor;
			cap.setStartTime(Math.round(start));
                        float finish = cap.getFinishTime() * factor;
			cap.setFinishTime(Math.round(finish));
		}
	}

}
