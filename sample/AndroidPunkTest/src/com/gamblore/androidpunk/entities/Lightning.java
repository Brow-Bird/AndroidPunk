package com.gamblore.androidpunk.entities;

import net.androidpunk.Entity;
import net.androidpunk.FP;
import net.androidpunk.graphics.atlas.TiledSpriteMap;
import net.androidpunk.graphics.opengl.SubTexture;
import net.androidpunk.masks.Hitbox;
import android.graphics.Point;
import android.util.Log;

import com.gamblore.androidpunk.Main;

public class Lightning extends Entity {
	
	private static final String TAG = "Lightning";
	
	private TiledSpriteMap mMap;
	private static final String ANIM_SHOCK = "shock";
	
	private Point mRenderPoints[];
	
	public Lightning(int x, int y, int width, int height, int angle) {
		super(x, y);
		
		SubTexture lightning = Main.mAtlas.getSubTexture("lightning");
		
		int gridWidth = lightning.getWidth()/5;
		mMap = new TiledSpriteMap(lightning, gridWidth, (int) lightning.getHeight(), width, height);
		//mMap.add(ANIM_STANDING, new int[] {0}, 0);
		mMap.add(ANIM_SHOCK, FP.frames(0, 4), 15);
		mMap.play(ANIM_SHOCK);
		setGraphic(mMap);
		
		if (width != gridWidth && height != lightning.getHeight()) {
			Log.e(TAG, "Cannot resize in both directions");
			return;
		}
		switch(angle) {
		case 0:
			setHitbox(width, height);
		case 90:
			setHitbox(height, width, -height, y);
		case 180:
			setHitbox(width, height, -width, -height);
		case 270:
			setHitbox(height, width, x, -width);
		}
		
		setType("danger");
	}
}
