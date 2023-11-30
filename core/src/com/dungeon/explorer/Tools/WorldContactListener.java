package com.dungeon.explorer.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Sprites.*;

public class WorldContactListener implements ContactListener {
    
    private Player player;
    
    public WorldContactListener(Player player) {
        this.player = player;
    }
    
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        if (fixtureA.getUserData() == "playerBody" || fixtureB.getUserData() == "playerBody") {
            Fixture playerBody = fixtureA.getUserData() == "playerBody" ? fixtureA : fixtureB;
            Fixture object = playerBody == fixtureA ? fixtureB : fixtureA;

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onPlayerContact();
            }
        }

        if ((fixtureA.getUserData() instanceof Projectile && fixtureB.getUserData() instanceof Enemy)
                || (fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Projectile)) {
        }

        switch (cDef) {
            case DungeonExplorer.ENEMY_BIT | DungeonExplorer.OBJECT_BIT:
                if (fixtureA.getFilterData().categoryBits == DungeonExplorer.ENEMY_BIT) {
                    ((Enemy) fixtureA.getUserData()).reverseVelocity(true, false);
                } else {
                    ((Enemy) fixtureB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case DungeonExplorer.PLAYER_BIT | DungeonExplorer.ENEMY_BIT:
                player.loseLifePoint();
                break;
            case DungeonExplorer.PLAYER_BIT | DungeonExplorer.ENEMY_PROJECTILE_BIT:
                if (fixtureA.getUserData() instanceof EnemyProjectile || fixtureB.getUserData() instanceof EnemyProjectile) {
                    player.loseLifePoint();
                }
                break;
            case DungeonExplorer.ALLY_PROJECTILE_BIT | DungeonExplorer.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == DungeonExplorer.ENEMY_BIT) {
                    ((Enemy) fixtureA.getUserData()).hit();
                } else {
                    ((Enemy) fixtureB.getUserData()).hit();
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
