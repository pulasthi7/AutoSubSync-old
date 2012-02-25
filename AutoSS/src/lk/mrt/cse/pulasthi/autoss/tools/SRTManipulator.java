package lk.mrt.cse.pulasthi.autoss.tools;

import java.util.Iterator;
import java.util.ListIterator;

import edu.ucsb.nmsl.tools.Caption;
import edu.ucsb.nmsl.tools.Transcript;

public class SRTManipulator {

	public void shift(Transcript t, int time) {
		for (ListIterator<Caption> i = t.captionIterator(0); i.hasNext();) {
			Caption cap = i.next();
			cap.setStartTime(cap.getTime() + time);
			cap.setFinishTime(cap.getFinishTime() + time);
		}
	}

	public void scale(Transcript t, float factor) {
		for (ListIterator<Caption> i = t.captionIterator(0); i.hasNext();) {
			Caption cap = i.next();
			cap.setStartTime(cap.getTime() * factor);
			cap.setFinishTime(cap.getFinishTime() * factor);
		}
	}

}
