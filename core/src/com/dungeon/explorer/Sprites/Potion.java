package com.dungeon.explorer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Scenes.Hud;
import com.dungeon.explorer.Screens.PlayScreen;

public class Potion extends InteractiveTileObject {
    public Potion(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(DungeonExplorer.POTION_BIT);
    }

    @Override
    public void onPlayerContact() {
        Gdx.app.log("Potion", "Collision");
        Hud.addLifePoints(2);
        setCategoryFilter(DungeonExplorer.DESTROYED_BIT);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(2);
        layer.getCell((int)(body.getPosition().x * Player.PPM / 32), (int)(body.getPosition().y * Player.PPM / 32));
        getCell().setTile(null);
    }
}
