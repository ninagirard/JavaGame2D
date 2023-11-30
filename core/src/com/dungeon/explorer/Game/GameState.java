package com.dungeon.explorer.Game;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Scenes.Hud;
import com.dungeon.explorer.Screens.PlayScreen;
import com.dungeon.explorer.Sprites.*;

public class GameState {

    public static void reset() {
        System.out.println("1");
        PlayScreen.currentLevel = 1;
        Hud.dungeon = 1;
        Hud.level = 1;
        Hud.lifePoints = 5;
        Hud.lifeImages = new ArrayList<Image>();
        Enemy.enemyCounter = 4;
        Player.invincible = false;
        Player.invincibilityTimer = 0;
        Player.playerIsDead = false;
        Tresor.win = false;
        System.out.println("2");
    }
}
