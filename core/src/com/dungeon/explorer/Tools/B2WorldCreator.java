package com.dungeon.explorer.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.dungeon.explorer.Screens.PlayScreen;
import com.dungeon.explorer.Sprites.*;

import java.util.HashMap;

public class B2WorldCreator {

    private HashMap<String, Stone> stoneMap;
    private Player player;

    public B2WorldCreator(PlayScreen screen, Player player) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        this.player = player;
        stoneMap = new HashMap<String, Stone>();

        //Tresor
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Tresor(screen, rect);
        }

        //Potion
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Potion(screen, rect);
        }

        //Wall
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Wall(screen, rect);
        }

        //Stone
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Stone(screen, rect);
            Stone stone = new Stone(screen, rect);
            stoneMap.put(object.getName(), stone);
        }

        //invisibleBarrier
        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Barrier(screen, rect);
        }

        //demiBarrier
        for (MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new DemiBarrier(screen, rect);
        }
    }

    public HashMap<String, Stone> getStoneMap() {
        return stoneMap;
    }
}
