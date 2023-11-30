package com.dungeon.explorer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Screens.PlayScreen;

public class DemiBarrier extends InteractiveTileObject {

    public DemiBarrier(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(DungeonExplorer.DEMI_BARRIER_BIT);
    }

    @Override
    public void onPlayerContact() {
//        Gdx.app.log("DemiBarrier", "Collision");
    }
}
