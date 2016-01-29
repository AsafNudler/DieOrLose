package com.retrom.ggj2016.game;

/**
 * Created by Asaf on 28/01/2016.
 */
public class PaintingTreeNode {
    public PaintingTreeNode()
    {
        left = null;
        right = null;
        x = 0;
        y = 0;
        obj = null;
    }
    public PaintingTreeNode left;
    public PaintingTreeNode right;
    float x;
    float y;
    public PaintingNode obj;
}
