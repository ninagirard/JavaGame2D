package com.dungeon.explorer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dungeon.explorer.Scenes.Hud;
import com.dungeon.explorer.Screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;
    protected int lifePoints;
    protected boolean setToDestroy;
    protected boolean destroyed;
    protected float stateTime;
    protected Animation<TextureRegion> walkAnimation;
    protected Array<TextureRegion> frames;
    protected boolean invincible = false;
    protected float invincibilityTimer = 0;
    protected float blinkTimer = 0;
    public static int enemyCounter = 4;
    private Sound damageSound;


    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1, 0);
        damageSound = Gdx.audio.newSound(Gdx.files.internal("music/punch.mp3"));
        damageSound.setVolume(0, 0.2f);
    }

    protected abstract void defineEnemy();

    public abstract void update(float dt, Player player);

    public void hit() {
        damageSound.play();
        if (!invincible) {
            invincible = true;
            invincibilityTimer = 0;
            lifePoints--;
        }
        if (lifePoints <= 0) {
            setToDestroy = true;
        }
    }

    public void dispose() {
        enemyCounter--;
        Gdx.app.log("Enemy", "Disposed. " + enemyCounter + " instances left.");
    }

    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }
        if (y) {
            velocity.y = -velocity.y;
        }
    }

    @Override
    public void draw(Batch batch) {
        if (invincible) {
            blinkTimer += Gdx.graphics.getDeltaTime();
            if (blinkTimer < 0.2f) {
                setAlpha(0.5f); // Rend l'ennemi semi-transparent
            } else if (blinkTimer < 0.4f) {
                setAlpha(1f); // Rend l'ennemi opaque
            } else {
                blinkTimer = 0;
            }
        } else {
            setAlpha(1f); // Assurez-vous que l'ennemi est opaque quand il n'est pas invincible
        }
        super.draw(batch);
    }
}
