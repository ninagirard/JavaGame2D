package com.dungeon.explorer.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Screens.PlayScreen;

public class EnemyProjectile extends Projectile {
    public EnemyProjectile(PlayScreen screen, float x, float y, float directionX, float directionY) {
        super(screen, x, y, new Texture("textures/fireball.png"), directionX, directionY);
        defineProjectile(x, y);
        setVelocity(directionX, directionY);
        setBounds(0, 0, 150 / Player.PPM, 100 / Player.PPM);
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

        fdef.filter.categoryBits = DungeonExplorer.ENEMY_PROJECTILE_BIT;
        fdef.filter.maskBits = DungeonExplorer.PLAYER_BIT | DungeonExplorer.BARRIER_BIT | DungeonExplorer.STONE_BIT;

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
