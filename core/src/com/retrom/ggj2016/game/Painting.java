package com.retrom.ggj2016.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

import java.util.ArrayList;

/**
 * Created by Asaf on 28/01/2016.
 */
public class Painting {
    private ArrayList<SegmentStatus> m_target;
    private float m_precision;
    
     public float master_alpha = 1;

    public interface LineComplete
    {
        public void signale();
    }

    private LineComplete m_lineCompleteSignaller;


    public Painting(ArrayList<LineSegment> target, float precision, LineComplete signaller)
    {
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

    private void drawLine(ShapeRenderer renderer, SegmentStatus lineSegment, float start, float end, float r, float g, float b, float a)
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
    }

    public void render(ShapeRenderer renderer) {
        for (SegmentStatus lineSegment : m_target) {
            float pos = 0;
            for (SegmentProjs proj : lineSegment.projs) {
                if (pos < proj.start)
                {
                    drawLine(renderer, lineSegment, pos, proj.start, 1, 0.2f, 0.2f, 0.15f);
                }
                drawLine(renderer, lineSegment, proj.start, proj.end, 1, 0.2f, 0.2f, 0.9f);
                pos = proj.end;
            }
            if (pos < 1.0)
            {
                drawLine(renderer, lineSegment, pos, 1.0f, 1, 0.2f, 0.2f, 0.15f);
            }
            if (lineSegment.isDone()) {
            	drawLine(renderer, lineSegment, 0, 1, 0.86f, 0.32f, 0f, (lineSegment.doneAnimationFrame/7.0f) * 1f);
            }
        }

    }
    
    public void makeManyParticles() {
    	for (SegmentStatus lineSegment : m_target) {
    		for (int i=0; i < 100; i++) {
    			makeNewParticle(lineSegment);
    		}
    	}
    }

    public void renderFire(SpriteBatch batch)
    {
        for (SegmentStatus lineSegment : m_target) {
            if (lineSegment.isDone())
            {
                if (lineSegment.doneAnimationFrame < 7)
                {
                    lineSegment.doneAnimationFrame++;
                }
                while (Math.random() < 0.10 && !isDone())
                {
                    makeNewParticle(lineSegment);
                }
                ArrayList<FireEffect> remove = new ArrayList<FireEffect>();
                BatchUtils.setBlendFuncAdd(batch);
                for (FireEffect fire : lineSegment.fires) {
                    Sprite s = fire.ass;
                    s.setColor(fire.alpha, fire.alpha, fire.alpha, 1);
                    s.setRotation(fire.rotationRate * fire.frames);
                    s.setScale(fire.scale * fire.alpha); ///
                    utils.drawCenter(batch, s, fire.pos.x + fire.amplitude*(float)Math.cos(fire.xCurrElement + fire.phase), fire.pos.y);
                    
                    fire.frames++;
                    if (fire.frames <= 10)
                    {
                        fire.alpha = fire.frames / 10f;
                    }
                    else if (fire.frames >= 30 * fire.lifetime)
                    {
                        fire.alpha -= 0.05 / fire.lifetime;
                        fire.alpha = Math.max(0, fire.alpha);
                    }
                    if (fire.frames >= 50f * fire.lifetime)
                    {
                        remove.add(fire);
                    }
                    fire.pos.y += fire.ySpeed;
                    fire.xCurrElement += 0.05;
                }
                for (FireEffect fireEffect : remove) {
                    lineSegment.fires.remove(fireEffect);
                }
                BatchUtils.setBlendFuncNormal(batch);
            }
        }
    }

	private void makeNewParticle(SegmentStatus lineSegment) {
		FireEffect f = new FireEffect();
		System.out.println("f.ass="+f.ass);
		f.ySpeed = 0.2f + (float)Math.random() * 0.2f;
		f.xCurrElement = (float)(Math.random() * 2 * Math.PI);
		Vector2 fpos = new Vector2(lineSegment.segment.endX - lineSegment.segment.startX, lineSegment.segment.endY - lineSegment.segment.startY);
		fpos.scl((float)Math.random());
		fpos.add(new Vector2(lineSegment.segment.startX, lineSegment.segment.startY));
		f.pos = fpos;
		lineSegment.fires.add(f);
		f.ass.setScale(0.7f);
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
                    lineSegment.doneAnimationFrame = 0;
                    lineSegment.fires = new ArrayList<FireEffect>();
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
