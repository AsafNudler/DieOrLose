package com.retrom.ggj2016.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Asaf on 28/01/2016.
 */
public class Painting {
    private PaintingTreeNode m_pt;
    private ArrayList<PaintingNode> m_allLines;
    private ArrayList<SegmentStatus> m_target;
    private float m_precision;


    private final float MAX_X = 1080;
    private final float MAX_Y = 1080;
    private final float MAX_GAP = 18;

    public Painting(ArrayList<LineSegment> target, float precision)
    {
        m_pt = new PaintingTreeNode();
        m_allLines = new ArrayList<PaintingNode>();
        m_target = new ArrayList<SegmentStatus>();
        for (LineSegment lineSegment : target) {
            SegmentStatus seg = new SegmentStatus();
            seg.segment = lineSegment;
            seg.isDone = false;
            m_target.add(seg);
        }
        m_precision = precision;
    }

    public void addLine(PaintingLine obj, float startX, float startY, float endX, float endY)
    {
        boolean isRelated = false;
        obj.pathDone = false;
        ArrayList<SegmentStatus> relatedTo = new ArrayList<SegmentStatus>();
        for (SegmentStatus lineSegment : m_target) {
            if (dist(lineSegment.segment.startX, lineSegment.segment.startY, lineSegment.segment.endX, lineSegment.segment.endY, startX, startY) <= m_precision ||
                    dist(lineSegment.segment.startX, lineSegment.segment.startY, lineSegment.segment.endX, lineSegment.segment.endY, endX, endY) <= m_precision)
            {
                obj.onPath = true;
                isRelated = true;
                relatedTo.add(lineSegment);
            }
        }
        if (!isRelated)
        {
            obj.onPath = false;
            return;
        }
        PaintingNode pn = new PaintingNode();
        pn.obj = obj;
        pn.startX = startX;
        pn.endX = endX;
        pn.startY = startY;
        pn.endY = endY;
        pn.relatedTo = relatedTo;
        m_allLines.add(pn);
        addLine(pn, startX, startY);
        addLine(pn, endX, endY);

        for (SegmentStatus segmentStatus : relatedTo) {
            if ( !segmentStatus.isDone)
            {
                LineSegment line = segmentStatus.segment;
                ArrayList<PaintingNode> path = isBloodedPath(line.startX, line.startY, line.endX, line.endY, segmentStatus, null, null, 350);
                if (!path.isEmpty()) {
                    segmentStatus.isDone = true;
                    for (PaintingNode paintingNode : path) {
                        paintingNode.obj.pathDone = true;
                    }
                }
            }
        }
    }


    private void addLine(PaintingNode obj, float x, float y)
    {
        PaintingTreeNode curr = m_pt;
        boolean splitHor = true;
        float minX = -MAX_X/2;
        float maxX = MAX_X/2;
        float minY = -MAX_Y/2;
        float maxY = MAX_Y/2;
        int depth = 0;
        while (true)
        {
            depth++;
            if (depth >= 15) // a little bug to make the game faster
            {
                return;
            }
            if (curr.obj == null)
            {
                curr.x = x;
                curr.y = y;
                curr.obj = obj;
                return;
            }
            if (splitHor)
            {
                if (x >= (minX+maxX)/2)
                {
                    minX = (minX+maxX)/2;
                    if (curr.right == null)
                    {
                        curr.right = new PaintingTreeNode();
                    }
                    curr = curr.right;
                }
                else
                {
                    maxX = (minX+maxX - ((minX+maxX)/2));
                    if (curr.left == null)
                    {
                        curr.left = new PaintingTreeNode();
                    }
                    curr = curr.left;
                }
            }
            else
            {
                if (y >= (minY+maxY)/2)
                {
                    minY = (minY+maxY)/2;
                    if (curr.right == null)
                    {
                        curr.right = new PaintingTreeNode();
                    }
                    curr = curr.right;
                }
                else
                {
                    maxY = (minY+maxY - ((minY+maxY)/2));
                    if (curr.left == null)
                    {
                        curr.left = new PaintingTreeNode();
                    }
                    curr = curr.left;
                }
            }
            splitHor = !splitHor;
        }
    }

    public ArrayList<PaintingNode> findLines(float x, float y, float radius)
    {
        ArrayList<PaintingNode> res = new ArrayList<PaintingNode>();
        float minX = -MAX_X/2;
        float maxX = MAX_X/2;
        float minY = -MAX_Y/2;
        float maxY = MAX_Y/2;
        boolean splitHor = true;
        PaintingTreeNode curr = m_pt;
        findLines(x, y, radius, curr, minX, maxX, minY, maxY, splitHor, res);
        return res;
    }

    private float dist(float segX1, float segY1, float segX2, float segY2, float x, float y)
    {
        Vector2 v1 = new Vector2(x - segX1, y - segY1);
        Vector2 v2 = new Vector2(x - segX2, y - segY2);
        Vector2 v3 = new Vector2(segX1 - segX2, segY1 - segY2);
        Vector2 v4 = v3.cpy();
        v4.scl(-1);
        if (v3.dot(v2) > 0 && v4.dot(v1) > 0)
        {
            return Math.abs(v1.crs(v2))/(2*v4.len());
        }
        else
        {
            return Math.min(v1.len(), v2.len());
        }
    }

    public void findLines(float x, float y, float radius, PaintingTreeNode curr, float minX, float maxX, float minY, float maxY, boolean splitHor, ArrayList<PaintingNode> res)
    {
        if (curr.obj != null)
        {
            if (dist(curr.obj.startX, curr.obj.startY, curr.obj.endX, curr.obj.endY, x, y) <= radius)
            {
                res.add(curr.obj);
            }
        }

        if (splitHor)
        {
            if (x+radius >= (minX+maxX)/2)
            {
                if (curr.right != null)
                {
                    findLines(x, y, radius, curr.right, (minX+maxX)/2, maxX, minY, maxY, !splitHor, res);
                }
            }
            if (!(x-radius >= (minX+maxX)/2))
            {
                if (curr.left != null)
                {
                    findLines(x, y, radius, curr.left, minX, (minX+maxX - ((minX+maxX)/2)), minY, maxY, !splitHor, res);
                }
            }
        }
        else
        {
            if (y+radius >= (minY+maxY)/2)
            {
                if (curr.right != null)
                {
                    findLines(x, y, radius, curr.right, minX, maxX, (minY+maxY)/2, maxY, !splitHor, res);
                }
            }
            if (!(y-radius >= (minY+maxY)/2))
            {
                if (curr.left != null)
                {
                    findLines(x, y, radius, curr.left, minX, maxX, minY, (minY+maxY - ((minY+maxY)/2)), !splitHor, res);
                }
            }
        }
    }
    
    public void step()
    {

        /*for (int i = 0; i < m_target.size(); i++) {
            if ( !m_target.get(i).isDone)
            {
                LineSegment line = m_target.get(i).segment;
                ArrayList<PaintingNode> path = isBloodedPath(line.startX, line.startY, line.endX, line.endY, line, null, null);
                if (path.isEmpty()) {
                    m_target.get(i).isDone = false;
                } else {
                    m_target.get(i).isDone = true;
                    for (PaintingNode paintingNode : path) {
                        paintingNode.obj.pathDone = true;
                    }
                }
            }
        }*/
    }

    public boolean isDone()
    {
        for (SegmentStatus targetStatus : m_target) {
            if (!targetStatus.isDone)
            {
                return false;
            }
        }
        return true;
    }

    private ArrayList<PaintingNode> isBloodedPath(float startX, float startY, float endX, float endY, SegmentStatus relatedTo, ArrayList<PaintingNode> used, ArrayList<PaintingNode> end, int maxDepth)
    {
        if (maxDepth <= 0)
        {
            return new ArrayList<PaintingNode>();
        }
        if (null == used) {
            used = new ArrayList<PaintingNode>();
        }
        if (null == end) {
            end = findLines(endX, endY, MAX_GAP);
        }
        ArrayList<PaintingNode> potentioals = findLines(startX, startY, MAX_GAP);
        for (PaintingNode potentioal : potentioals) {
            if (end.contains(potentioal)) {
                ArrayList<PaintingNode> res = new ArrayList<PaintingNode>();
                res.add(potentioal);
                return res;
            }
            if (used.contains(potentioal) || !potentioal.relatedTo.contains(relatedTo)) {
                continue;
            }
            used.add(potentioal);
            ArrayList<PaintingNode> try1 = isBloodedPath(potentioal.startX, potentioal.startY, endX, endY, relatedTo, used, end, maxDepth-1);
            if (!try1.isEmpty()) {
                try1.add(potentioal);
                return try1;
            }
            try1 = isBloodedPath(potentioal.endX, potentioal.endY, endX, endY, relatedTo, used, end, maxDepth-1);
            if (!try1.isEmpty()) {
                try1.add(potentioal);
                return try1;
            }
        }
        return new ArrayList<PaintingNode>();
    }
}
