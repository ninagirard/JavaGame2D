package com.dungeon.explorer.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Screens.PlayScreen;

public abstract class Projectile extends Sprite {

    public World world;
    public Body b2body;
    protected boolean toDestroy;
    protected Texture projectileTexture;
    private PlayScreen screen;
    protected float timeSinceCreation;

    public Projectile(PlayScreen screen, float x, float y, Texture texture, float directionX, float directionY) {
        this.projectileTexture = texture;
        setTexture(projectileTexture);
        this.world = screen.getWorld();
        defineProjectile(x, y);
        toDestroy = false;
        b2body.setLinearVelocity(new Vector2(directionX, directionY)); // Set the velocity based on the player's input
        timeSinceCreation = 0;
    }

    public void draw(Batch batch) {
        if (!toDestroy) {
            batch.draw(projectileTexture, b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2, getWidth(), getHeight());
         }
    }


    public void defineProjectile(float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(12 / Player.PPM);
        fdef.filter.categoryBits = DungeonExplorer.PROJECTILE_BIT;
        fdef.filter.maskBits = DungeonExplorer.ENEMY_BIT | DungeonExplorer.WALL_BIT | DungeonExplorer.ENEMY_BIT | DungeonExplorer.STONE_BIT; // Ajustez les bits de masque selon vos besoins

        fdef.shape = shape;
        b2body.createFixture(fdef);

        shape.dispose();
    }

    public void update(float dt) {
        timeSinceCreation += dt;
        // After 3s the projectile is destroyed
        if (timeSinceCreation > 3 && !toDestroy) {
            toDestroy = true;
        }
    }

    protected void setVelocity(float directionX, float directionY) {
        if (b2body != null) {
            b2body.setLinearVelocity(new Vector2(directionX, directionY));
        }
    }

    public void destroyBody() {
        if (b2body != null && !world.isLocked()) {
            world.destroyBody(b2body);
            b2body = null;
        }
    }

    public boolean isDestroyed() {
        return toDestroy;
    }


    public void dispose() {
        projectileTexture.dispose();
    }
}
