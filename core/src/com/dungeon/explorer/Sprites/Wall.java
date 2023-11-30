package com.dungeon.explorer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Scenes.Hud;
import com.dungeon.explorer.Screens.PlayScreen;

public class Wall extends InteractiveTileObject {
    public Wall(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(DungeonExplorer.WALL_BIT);
    }

    @Override
    public void onPlayerContact() {
        Gdx.app.log("Wall", "-1 HP");
        setCategoryFilter(DungeonExplorer.DESTROYED_BIT);
        Player.loseLifePoint();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                setCategoryFilter(DungeonExplorer.WALL_BIT);
            }
        }, 2f);
    }
}
