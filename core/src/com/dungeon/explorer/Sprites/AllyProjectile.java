package com.dungeon.explorer.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Screens.PlayScreen;

public class AllyProjectile extends Projectile {

    public AllyProjectile(PlayScreen screen, float x, float y, float directionX, float directionY) {
        super(screen, x, y, new Texture("textures/egg.png"), directionX, directionY);
        defineProjectile(x, y);
        setVelocity(directionX, directionY); // Set the projectile's velocity
        setBounds(0, 0, 48 / Player.PPM, 48 / Player.PPM);
    }

    @Override
    public void defineProjectile(float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(12 / Player.PPM);

        fdef.filter.categoryBits = DungeonExplorer.ALLY_PROJECTILE_BIT;
        fdef.filter.maskBits = DungeonExplorer.ENEMY_BIT | DungeonExplorer.BARRIER_BIT | DungeonExplorer.STONE_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        shape.dispose();
    }

    @Override
    public void draw(Batch batch) {
        if (b2body != null) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            batch.draw(projectileTexture, b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2, getWidth(), getHeight());
        }
    }
}
