package com.dungeon.explorer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Screens.PlayScreen;

public class Ninja extends Enemy {

    private float timeSinceLastChange;
    private float timeToChangeDirection;

    public Ninja(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 1; i <= 3; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("ninja"), i * 100, 255, 100, 130));
        }
        walkAnimation = new Animation(1.8f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 75 / Player.PPM, 90 / Player.PPM);
        setToDestroy = false;
        destroyed = false;
        timeSinceLastChange = 0;
        timeToChangeDirection = 2.0f; //Change direction every 2 seconds
        lifePoints = 2;
    }

    public void update(float dt, Player player) {
        stateTime += dt;
        timeSinceLastChange += dt;

        if (timeSinceLastChange >= timeToChangeDirection) {
            // Change direction randomly
            velocity.x = MathUtils.random(-1, 1); // Random value between -1 and 1
            velocity.y = MathUtils.random(-1, 1); // Random value between -1 and 1
            velocity.nor().scl(1.8f); // Speed
            timeSinceLastChange = 0;
        }

        if (invincible) {
            invincibilityTimer += dt;
            if (invincibilityTimer > 1f) { // Invicibility duration
                invincible = false;
            }
        }

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(walkAnimation.getKeyFrame(stateTime, true));
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("ninja"), 300, 255, 100, 130));
            stateTime = 0;

            dispose();
        } else if (!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        TextureRegion region = walkAnimation.getKeyFrame(stateTime, true);

        if (b2body.getLinearVelocity().x < 0 && !region.isFlipX()) {
            region.flip(true, false);
        } else if (b2body.getLinearVelocity().x > 0 && region.isFlipX()) {
            region.flip(true, false);
        }
        setRegion(region);
    }

    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1) {
            super.draw(batch);
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = DungeonExplorer.ENEMY_BIT;
        fdef.filter.maskBits = DungeonExplorer.GROUND_BIT | DungeonExplorer.POTION_BIT | DungeonExplorer.WALL_BIT | DungeonExplorer.ENEMY_BIT | DungeonExplorer.OBJECT_BIT | DungeonExplorer.PLAYER_BIT | DungeonExplorer.ALLY_PROJECTILE_BIT | DungeonExplorer.BARRIER_BIT | DungeonExplorer.STONE_BIT | DungeonExplorer.DEMI_BARRIER_BIT;

        PolygonShape ninjaBody = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-25, 25).scl(1 / Player.PPM);
        vertice[1] = new Vector2(25, 25).scl(1 / Player.PPM);
        vertice[2] = new Vector2(-25, -25).scl(1 / Player.PPM);
        vertice[3] = new Vector2(25, -25).scl(1 / Player.PPM);
        ninjaBody.set(vertice);

        fdef.shape = ninjaBody;
        fdef.restitution = 0f;
        fdef.filter.categoryBits = DungeonExplorer.ENEMY_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }
}
