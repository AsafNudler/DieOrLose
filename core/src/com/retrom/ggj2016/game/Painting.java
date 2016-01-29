package com.retrom.ggj2016.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Asaf on 28/01/2016.
 */
public class Painting {
    private PaintingTreeNode m_pt;
    private ArrayList<PaintingNode> m_allLines;
    private ArrayList<LineSegment> m_target;
    private ArrayList<Boolean> m_targetStatus;
    private float m_precision;

    private final float MAX_X = 1080;
    private final float MAX_Y = 1080;
    private final float MAX_GAP = 7;

    public Painting(ArrayList<LineSegment> target, float precision)
    {
        m_pt = new PaintingTreeNode();
        m_allLines = new ArrayList<PaintingNode>();
        m_target = new ArrayList<LineSegment>();
        m_targetStatus = new ArrayList<Boolean>();
        for (LineSegment lineSegment : target) {
            m_target.add(lineSegment);
            m_targetStatus.add(false);
        }
        m_precision = precision;
    }

    public void addLine(PaintingLine obj, float startX, float startY, float endX, float endY)
    {
        boolean isRelated = false;
        for (LineSegment lineSegment : m_target) {
            System.out.print("(");
            System.out.print(lineSegment.startX);
            System.out.print(",");
            System.out.print(lineSegment.startY);
            System.out.print(")(");
            System.out.print(startX);
            System.out.print(",");
            System.out.print(endY);
            System.out.print(")");
            System.out.println(dist(lineSegment.startX, lineSegment.startY, lineSegment.endX, lineSegment.endY, startX, startY));
            if (dist(lineSegment.startX, lineSegment.startY, lineSegment.endX, lineSegment.endY, startX, startY) <= m_precision ||
                    dist(lineSegment.startX, lineSegment.startY, lineSegment.endX, lineSegment.endY, endX, endY) <= m_precision)
            {
                obj.onPath = true;
                System.out.print("A");
                isRelated = true;
                break;
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
        m_allLines.add(pn);
        addLine(pn, startX, startY);
        addLine(pn, endX, endY);
    }

    private void addLine(PaintingNode obj, float x, float y)
    {
        PaintingTreeNode curr = m_pt;
        boolean splitHor = true;
        float minX = -MAX_X/2;
        float maxX = MAX_X/2;
        float minY = -MAX_Y/2;
        float maxY = MAX_Y/2;
        while (true)
        {
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
        for (PaintingNode line : m_allLines) {
            line.obj.pathDone = false;
        }
        for (int i = 0; i < m_target.size(); i++) {
            LineSegment line = m_target.get(i);
            ArrayList<PaintingNode> path = isBloodedPath(line.startX, line.startY, line.endX, line.endY, null, null);
            if (path.isEmpty())
            {
                m_targetStatus.set(i, false);
            }
            else
            {
                m_targetStatus.set(i, true);
                for (PaintingNode paintingNode : path) {
                    paintingNode.obj.pathDone = true;
                }
            }
        }
    }

    public boolean isDone()
    {
        for (Boolean targetStatus : m_targetStatus) {
            if (!targetStatus)
            {
                return false;
            }
        }
        return true;
    }
    
    private ArrayList<PaintingNode> isBloodedPath(float startX, float startY, float endX, float endY, ArrayList<PaintingNode> used, ArrayList<PaintingNode> end)
    {
        if (null == used)
        {
            used = new ArrayList<PaintingNode>();
        }
        if (null == end)
        {
            end = findLines(endX, endY, MAX_GAP);
        }
        ArrayList<PaintingNode> potentioals = findLines(startX, startY, MAX_GAP);
        for (PaintingNode potentioal : potentioals) {
            if (end.contains(potentioal))
            {
                ArrayList<PaintingNode> res = new ArrayList<PaintingNode>();
                res.add(potentioal);
                return res;
            }
            if (used.contains(potentioal))
            {
                continue;
            }
            used.add(potentioal);
            ArrayList<PaintingNode> try1 = isBloodedPath(potentioal.startX, potentioal.startY, endX, endY, used, end);
            if (!try1.isEmpty())
            {
                try1.add(potentioal);
                return try1;
            }
            try1 = isBloodedPath(potentioal.endX, potentioal.endY, endX, endY, used, end);
            if (!try1.isEmpty())
            {
                try1.add(potentioal);
                return try1;
            }
        }
        return new ArrayList<PaintingNode>();
    }
}
