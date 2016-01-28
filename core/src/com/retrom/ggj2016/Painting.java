package com.retrom.ggj2016;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asaf on 28/01/2016.
 */
public class Painting {
    private PaintingTreeNode m_pt;
    private ArrayList<PaintingNode> m_allLines;

    private final int MAX_X = 1080;
    private final int MAX_Y = 1080;

    public Painting()
    {
        m_pt = new PaintingTreeNode();
        m_allLines = new ArrayList<PaintingNode>();
    }

    public addLine(PaintingLine obj, int startX, int startY, int endX, int endY)
    {
        PaintingNode pn = new PaintingNode;
        pn.obj = obj;
        pn.startX = startX;
        pn.endX = endX;
        pn.startY = startY;
        pn.endY = endY;
        m_allLines.add(pn);
        addLine(pn, startX, startY);
        addLine(pn, endX, endY);
    }

    private addLine(PaintingNode obj, int x, int y)
    {
        PaintingTreeNode curr = m_pt;
        boolean splitHor = true;
        int minX = 0;
        int maxX = MAX_X;
        int minY = 0;
        int maxY = MAX_Y;
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
        }
    }
}
