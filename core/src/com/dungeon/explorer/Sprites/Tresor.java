package com.dungeon.explorer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Scenes.Hud;
import com.dungeon.explorer.Screens.PlayScreen;

public class Tresor extends InteractiveTileObject {
    public static boolean win;

    public Tresor(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        win = false;
        fixture.setUserData(this);
        setCategoryFilter(DungeonExplorer.TRESOR_BIT);
    }

    @Override
    public void onPlayerContact() {
        Gdx.app.log("Tresor", "Collision");
        win = true;
    }
}
