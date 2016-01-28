package com.retrom.ggj2016;

/**
 * Created by Asaf on 28/01/2016.
 */
public class PaintingTreeNode {
    public PaintingTreeNode()
    {
        left = null;
        right = null;
        x = -1;
        y = -1;
        obj = null;
    }
    public PaintingTreeNode left;
    public PaintingTreeNode right;
    int x;
    int y;
    public PaintingNode obj;
}
