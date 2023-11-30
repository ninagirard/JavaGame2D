package com.dungeon.explorer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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

public class Men extends Enemy {

    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private float moveTimer;
    private float moveInterval = 1.8f;
    private float moveSpeed = 1f;
    private float shootTimer;
    private float shootCooldown;
    private Sound damageSound;


    public Men(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i <= 7; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("men"), i * 70 - 20, 0, 70, 80));
        }
        walkAnimation = new Animation(0.2f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 75 / Player.PPM, 90 / Player.PPM);
        setToDestroy = false;
        destroyed = false;
        lifePoints = 4;
        damageSound = Gdx.audio.newSound(Gdx.files.internal("music/punch.mp3"));
        damageSound.setVolume(0, 0.5f);
        resetShootCooldown();

    }

    public void update(float dt, Player player) {

        if (!destroyed) {
        shootTimer += dt;
        if (shootTimer >= shootCooldown) {
            fireProjectile();
            resetShootCooldown();
            }
        }

        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(walkAnimation.getKeyFrame(stateTime, true));

        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        } else if (!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }

        moveTimer += dt;

        if (moveTimer > moveInterval) {
            moveRandomly();
            moveTimer = 0;
        }

        if (!setToDestroy && !destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }

        if (invincible) {
            invincibilityTimer += dt;
            if (invincibilityTimer > 1.5f) { // invincibility duration
                invincible = false;
            }
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

    private void moveRandomly() {
        float randomAngle = MathUtils.random(0f, 2 * MathUtils.PI);
        Vector2 movement = new Vector2(MathUtils.cos(randomAngle), MathUtils.sin(randomAngle)).scl(moveSpeed);
        b2body.setLinearVelocity(movement);
    }

    private void resetShootCooldown() {
        shootCooldown = MathUtils.random(1.0f, 3.0f); // Random duration between 1 and 3 seconds
        shootTimer = 0;
    }

    private void fireProjectile() {
        float directionX = MathUtils.random(-1.0f, 1.0f);
        float directionY = MathUtils.random(-1.0f, 1.0f);
        Vector2 direction = new Vector2(directionX, directionY).nor();
        float speed = 2.5f; // Projectile speed
        direction.scl(speed);

        EnemyProjectile projectile = new EnemyProjectile(screen, b2body.getPosition().x, b2body.getPosition().y, direction.x, direction.y);
        screen.addEnemyProjectile(projectile);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        fdef.density = 1000f;
        fdef.filter.categoryBits = DungeonExplorer.ENEMY_BIT;
        fdef.filter.maskBits = DungeonExplorer.GROUND_BIT | DungeonExplorer.POTION_BIT | DungeonExplorer.WALL_BIT | DungeonExplorer.ENEMY_BIT | DungeonExplorer.OBJECT_BIT | DungeonExplorer.PLAYER_BIT | DungeonExplorer.ALLY_PROJECTILE_BIT | DungeonExplorer.BARRIER_BIT | DungeonExplorer.STONE_BIT | DungeonExplorer.DEMI_BARRIER_BIT;

        PolygonShape menBody = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-25, 40).scl(1 / Player.PPM);
        vertice[1] = new Vector2(25, 40).scl(1 / Player.PPM);
        vertice[2] = new Vector2(-25, -40).scl(1 / Player.PPM);
        vertice[3] = new Vector2(25, -40).scl(1 / Player.PPM);
        menBody.set(vertice);

        fdef.shape = menBody;
        fdef.restitution = 0f;
        fdef.filter.categoryBits = DungeonExplorer.ENEMY_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void hit() {
        damageSound.play();
        lifePoints--;
        if (lifePoints == 0) {
            setToDestroy = true;
            dispose();
        }
    }
}