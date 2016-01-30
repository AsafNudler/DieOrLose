package com.retrom.ggj2016.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
    private ArrayList<LineSegment> m_projects = new ArrayList<LineSegment>();
    
     public float master_alpha = 1;


    private final float MAX_X = 1080;
    private final float MAX_Y = 1080;
    private final float MAX_GAP = 18;

    public interface LineComplete
    {
        public void signale();
    }

    private LineComplete m_lineCompleteSignaller;


    public Painting(ArrayList<LineSegment> target, float precision, LineComplete signaller)
    {
        m_pt = new PaintingTreeNode();
        m_allLines = new ArrayList<PaintingNode>();
        m_target = new ArrayList<SegmentStatus>();
        for (LineSegment lineSegment : target) {
            SegmentStatus seg = new SegmentStatus();
            seg.segment = lineSegment;
            m_target.add(seg);
        }
        m_precision = precision;
        m_lineCompleteSignaller = signaller;
    }

    // projection of a on b
    private Vector2 project(Vector2 a, Vector2 b)
    {
        float scalarProj = a.len() * (float)Math.cos(a.angleRad(b));
        Vector2 res = b.cpy();
        res.nor();
        res.scl(scalarProj);
        return res;
    }

    private void drawLine(ShapeRenderer renderer, SegmentStatus lineSegment, float start, float end, float r, float g, float b, float a, int glow)
    {
        Vector2 seg = new Vector2(lineSegment.segment.endX - lineSegment.segment.startX, lineSegment.segment.endY - lineSegment.segment.startY);
        Vector2 pt1 = new Vector2(lineSegment.segment.startX, lineSegment.segment.startY).add(seg.cpy().scl(start));
        Vector2 pt2 = new Vector2(lineSegment.segment.startX, lineSegment.segment.startY).add(seg.cpy().scl(end));

        Gdx.gl.glLineWidth(5);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.begin(ShapeType.Line);

        renderer.setColor(r, g, b, a * master_alpha);

        renderer.line(pt1.x, pt1.y - 6, pt2.x, pt2.y - 6);
        renderer.end();
        if (glow > 0)
        {
            Vector2 norm = new Vector2(seg.y, -seg.x).nor();
            Vector2 startPt = new Vector2(lineSegment.segment.startX, lineSegment.segment.startY);
            Vector2 seg1 = startPt.cpy();
            Vector2 seg2 = startPt.cpy();
            norm.scl(1.5f);
            for (int i=0; i < glow; ++i)
            {
                seg1.add(norm);
                seg2.sub(norm);
                // TODO: master alpha
                Vector2 pt11 = seg1.cpy().add(seg.cpy().scl(start));
                Vector2 pt12 = seg1.cpy().add(seg.cpy().scl(end));

                Gdx.gl.glLineWidth(0.02f);
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                renderer.begin(ShapeType.Line);

                renderer.setColor(r, g, b, 0.12f - 0.12f * ((float)i / (float) glow));

                renderer.line(pt11.x, pt11.y - 6, pt12.x, pt12.y - 6);
                renderer.end();

                Vector2 pt21 = seg2.cpy().add(seg.cpy().scl(start));
                Vector2 pt22 = seg2.cpy().add(seg.cpy().scl(end));

                Gdx.gl.glLineWidth(0.02f);
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                renderer.begin(ShapeType.Line);

                renderer.setColor(r, g, b, 0.12f - 0.12f * ((float) i / (float) glow));

                renderer.line(pt21.x, pt21.y - 6, pt22.x, pt22.y - 6);
                renderer.end();

            }
        }
    }

    public void render(ShapeRenderer renderer) {
        for (SegmentStatus lineSegment : m_target) {
            float pos = 0;
            for (SegmentProjs proj : lineSegment.projs) {
                if (pos < proj.start)
                {
                    drawLine(renderer, lineSegment, pos, proj.start, 1, 0.2f, 0.2f, 0.15f, 0);
                }
                drawLine(renderer, lineSegment, proj.start, proj.end, 1, 0.2f, 0.2f, 0.9f, 20);
                pos = proj.end;
            }
            if (pos < 1.0)
            {
                drawLine(renderer, lineSegment, pos, 1.0f, 1, 0.2f, 0.2f, 0.15f, 0);
            }
        }

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

                Vector2 walkStart = new Vector2(startX, startY);
                Vector2 walkEnd = new Vector2(endX, endY);
                Vector2 segStart = new Vector2(lineSegment.segment.startX, lineSegment.segment.startY);
                Vector2 segEnd = new Vector2(lineSegment.segment.endX, lineSegment.segment.endY);

                Vector2 ac = walkStart.cpy().sub(segStart);
                Vector2 ab = segEnd.cpy().sub(segStart);
                Vector2 projStart = segStart.cpy().add(project(ac, ab));
                Vector2 cd = walkEnd.cpy().sub(walkStart);
                Vector2 projDiff = project(cd, ab);
                Vector2 projEnd = projStart.cpy().add(projDiff);

                if (ab.dot(projDiff) < 0)
                {
                    Vector2 temp = projStart;
                    projStart = projEnd;
                    projEnd = temp;
                }

                projStart.sub(segStart);
                projStart.scl(1 / ab.len());
                projEnd.sub(segStart);
                projEnd.scl(1 / ab.len());

                SegmentProjs segProj = new SegmentProjs();
                segProj.start = projStart.len();
                segProj.end = projEnd.len();

                boolean wasDone = lineSegment.isDone();

                lineSegment.projs.add(segProj);

                lineSegment.mergeProjs();

                if (!wasDone && lineSegment.isDone())
                {
                    m_lineCompleteSignaller.signale();
                }
            }
        }
        if (!isRelated)
        {
            obj.onPath = false;
            return;
        }
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



    public boolean isDone()
    {
        for (SegmentStatus targetStatus : m_target) {
            if (!targetStatus.isDone())
            {
                return false;
            }
        }
        return true;
    }


}
