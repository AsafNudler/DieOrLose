package com.retrom.ggj2016.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Asaf on 29/01/2016.
 */
public class SegmentStatus {
    public LineSegment segment;
    public boolean isDone()
    {
        return cover >= 0.85;
    }
    public ArrayList<SegmentProjs> projs = new ArrayList<SegmentProjs>();
    private float cover = 0;

    public int doneAnimationFrame = 0;
    public ArrayList<FireEffect> fires;

    public void mergeProjs()
    {
        Collections.sort(projs, new CustomComparator());
        ArrayList<SegmentProjs> newRes = new ArrayList<SegmentProjs>();
        cover = 0;
        for (int i = 0; i < projs.size(); i++) {
            float start = projs.get(i).start;
            float end = projs.get(i).end;
            while (i+1 < projs.size() && projs.get(i+1).start <= end)
            {
                ++i;
                end = Math.max(projs.get(i).end, end);
            }
            SegmentProjs n = new SegmentProjs();
            n.start = start;
            n.end = end;
            newRes.add(n);
            cover += end - start;
        }
        projs = newRes;
    }

    private class CustomComparator implements Comparator<SegmentProjs> {
        @Override
        public int compare(SegmentProjs o1, SegmentProjs o2) {
           if (o1.start < o2.start)
           {
               return -1;
           }
            return 1;
        }
    }
}
