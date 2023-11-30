package com.dungeon.explorer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dungeon.explorer.Game.GameState;
import com.dungeon.explorer.Screens.GameOverScreen;
import com.dungeon.explorer.Screens.IntroScreen;
import com.dungeon.explorer.Screens.PlayScreen;
import com.dungeon.explorer.Screens.WinScreen;

public class DungeonExplorer extends Game {
	public static final short GROUND_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short POTION_BIT = 4;
	public static final short WALL_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short STONE_BIT = 32;
	public static final short BARRIER_BIT = 64;
	public static final short OBJECT_BIT = 128;
	public static final short ENEMY_BIT = 256;
	public static final short PROJECTILE_BIT = 512;
	public static final short ALLY_PROJECTILE_BIT = 1024;
	public static final short ENEMY_PROJECTILE_BIT = 2048;
	public static final short TRESOR_BIT = 4096;
	public static final short DEMI_BARRIER_BIT = 8192;

	public static final int V_WIDTH = 960;
	public static final int V_HEIGHT = 700;

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new IntroScreen(this));
	}

	public static void resetStaticVariables() {
		GameState.reset();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
