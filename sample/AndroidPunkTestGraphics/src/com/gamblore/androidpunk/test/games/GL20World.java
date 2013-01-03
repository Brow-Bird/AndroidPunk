package com.gamblore.androidpunk.test.games;

import javax.microedition.khronos.opengles.GL10;

import net.androidpunk.Entity;
import net.androidpunk.FP;
import net.androidpunk.World;
import net.androidpunk.graphics.atlas.Emitter;
import net.androidpunk.graphics.atlas.Image;
import net.androidpunk.graphics.atlas.SpriteMap;
import net.androidpunk.graphics.atlas.TileMap;
import net.androidpunk.graphics.atlas.TiledImage;
import net.androidpunk.graphics.atlas.TiledSpriteMap;
import net.androidpunk.graphics.opengl.SubTexture;
import net.androidpunk.graphics.opengl.shapes.Shape;
import net.androidpunk.utils.TaskTimer;
import android.graphics.Point;
import android.opengl.GLES20;

public class GL20World extends World {

	public class VignetteFilter extends Shape {
		public VignetteFilter() {
			useShaders(R.raw.shader_g_flat, R.raw.vignette_fragment_shader);
			
			float v[] = new float[8];
			
			setColor(0xff000000);
			
			setRect(0, 0, FP.width, FP.height, v);
			
			setVertices(v);
			
		}

		@Override
		public void render(GL10 gl, Point point, Point camera) {
			GLES20.glUseProgram(mProgram);
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			
			int resolutionHandler = GLES20.glGetUniformLocation(mProgram, "resolution");
			GLES20.glUniform2f(resolutionHandler, FP.displayWidth, FP.displayHeight);
			
			super.render(gl, point, camera);
		}
	}
	
	Emitter em;
	SpriteMap sm;
			
	@Override
	public void begin() {
		Shape rect = Shape.rect(0,0,50,50);
		
		rect.setColor(0xffffffff);
		
		Shape bg = Shape.rect(0, 0, FP.width, FP.height);
		bg.setColor(0xffC2C589);
		
		add(new Entity(0, 0, bg));
		
		add(new Entity(250, 250, rect));		
		
		Entity filter = new Entity(0, 0, new VignetteFilter());
		filter.setLayer(-1000);
		add(filter);
		
		add(new Entity(300, 250, new Image(Main.getAtlas().getSubTexture("menu_newgame"))));
		
		SubTexture ogmoSt = Main.getAtlas().getSubTexture("ogmo");
		sm = new SpriteMap(ogmoSt, ogmoSt.getWidth()/6, ogmoSt.getHeight());
		sm.add("run", FP.frames(0,5), 10);
		sm.play("run");
		//sm.scale = 2f;
		
		add(new Entity(300, 218, sm));
		
		SubTexture newGame = Main.getAtlas().getSubTexture("menu_newgame");
		add(new Entity(300, 300, new TiledImage(newGame, newGame.getWidth()*2, newGame.getHeight()*3)));
		
		SubTexture lightning = Main.getAtlas().getSubTexture("lightning");
		TiledSpriteMap tsm = new TiledSpriteMap(lightning, lightning.getWidth()/5, lightning.getHeight(), (lightning.getWidth()/5)*4, lightning.getHeight());
		tsm.add("run", FP.frames(0,4), 10);
		tsm.play("run");
		tsm.angle = 270;
		
		add(new Entity(348, 218, tsm));
		
		TileMap tm = new TileMap(Main.getAtlas().getSubTexture("desert"), 2*32, 2*32, 32, 32);
		tm.setTile(0, 0, 0);
		tm.setTile(1, 0, 2);
		tm.setTile(0, 1, 12);
		tm.setTile(1, 1, 14);
		tm.reload();
		
		add(new Entity(200,100, tm));
		
		em = new Emitter(Main.getAtlas().getSubTexture("enemy"), 32, 32);
		em.newType("thing2", FP.frames(0, 5)).setMotion(90, 100, 3.0f);
		em.newType("thing", FP.frames(0, 5)).setMotionVector(100, -100, 3.0f, 2.0f);
		
		add(new Entity(250, 250, em));
	}

	@Override
	public void update() {
		super.update();
		if (em.getParticleCount() < 10) {
			em.emit("thing", 0, 0);
			em.emit("thing2", 0, 0);
		}
		sm.angle += 0.1;
	}

	
	
}


