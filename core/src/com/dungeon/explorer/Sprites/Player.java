package com.dungeon.explorer.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Scenes.Hud;
import com.dungeon.explorer.Screens.PlayScreen;

public class Player extends Sprite {
    public enum State {GOINGUP, GOINGDOWN, GOINGRIGHT, GOINGLEFT, STANDINGDOWN, DEAD}

    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion playerStand;
    private PlayScreen screen;
    public static final float PPM = 100;
    private Animation<TextureRegion> playerStandingDown;
    private Animation<TextureRegion> playerGoingUp;
    private Animation<TextureRegion> playerGoingDown;
    private Animation<TextureRegion> playerGoingRight;
    private Animation<TextureRegion> playerGoingLeft;
    private float stateTimer;
    private Array<AllyProjectile> projectiles;
    public static boolean invincible = false;
    public static float invincibilityTimer = 0;
    private float blinkTimer = 0;
    public static boolean playerIsDead = false;

    public Player(PlayScreen screen) {
        super(screen.getAtlas().findRegion("link"));
        this.world = screen.getWorld();
        currentState = State.STANDINGDOWN;
        previousState = State.STANDINGDOWN;
        stateTimer = 0;
        projectiles = new Array<AllyProjectile>();
        this.screen = screen;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //STANDING DOWN
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 96, 1016, 96, 106));
        }
        playerStandingDown = new Animation(0.1f, frames);
        frames.clear();

        //GOING DOWN
        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), i * 96, 1440, 96, 106));
        }
        playerGoingDown = new Animation(0.1f, frames);
        frames.clear();

        //GOING RIGHT
        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), i * 96, 1744, 96, 106));
        }
        playerGoingRight = new Animation(0.1f, frames);
        frames.clear();

        //GOING LEFT
        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), i * 96, 1538, 96, 106));
        }
        playerGoingLeft = new Animation(0.1f, frames);
        frames.clear();

        //GOING UP
        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), i * 96, 1646, 96, 106));
        }
        playerGoingUp = new Animation(0.1f, frames);
        frames.clear();

        definePlayer();
        playerStand = new TextureRegion(getTexture(), 2, 1016, 96, 106);
        setBounds(0, 0, 64 / Player.PPM, 64 / Player.PPM);
        setRegion(playerStand);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        if (invincible) {
            invincibilityTimer += dt;
            if (invincibilityTimer > 1.5) {
                invincible = false;
            }
        }

        if (invincible) {
            blinkTimer += dt;
            if (blinkTimer < 0.2f) {
                setAlpha(0); // Make the sprite transparent
            } else if (blinkTimer < 0.4f) {
                setAlpha(1); // Make the sprite opaque
            } else {
                blinkTimer = 0;
            }
        } else {
            setAlpha(1); // Make sure the sprite is opaque when not invincible
        }

        handleInput();
        updateProjectiles(dt);
    }


    private void updateProjectiles(float dt) {
        for (AllyProjectile projectile : projectiles) {
            projectile.update(dt);
            if (projectile.isDestroyed()) {
                projectiles.removeValue(projectile, true);
            }
        }
    }

    private void fireProjectile() {
        boolean moveUp = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean moveDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);

        float directionX = 0;
        float directionY = 0;

        // Calculate direction based on pressed keys
        if (moveUp && !moveDown) {
            directionY = 1;
        } else if (moveDown && !moveUp) {
            directionY = -1;
        }

        if (moveRight && !moveLeft) {
            directionX = 1;
        } else if (moveLeft && !moveRight) {
            directionX = -1;
        }

        // Check if the direction has changed (avoid creating null projectiles if no key is pressed)
        if (directionX != 0 || directionY != 0) {
            float speedMultiplier = 3.5f;
            directionX *= speedMultiplier;
            directionY *= speedMultiplier;
            // Create a projectile with the calculated direction
            projectiles.add(new AllyProjectile(screen, b2body.getPosition().x, b2body.getPosition().y, directionX, directionY));
        } else {
            // Create a projectile with a default direction
            projectiles.add(new AllyProjectile(screen, b2body.getPosition().x, b2body.getPosition().y, 0, -1));
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            fireProjectile();
        }
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case GOINGUP:
                region = (TextureRegion) playerGoingUp.getKeyFrame(stateTimer, true);
                break;
            case GOINGDOWN:
                region = (TextureRegion) playerGoingDown.getKeyFrame(stateTimer, true);
                break;
            case GOINGRIGHT:
                region = (TextureRegion) playerGoingRight.getKeyFrame(stateTimer, true);
                break;
            case GOINGLEFT:
                region = (TextureRegion) playerGoingLeft.getKeyFrame(stateTimer, true);
                break;
            case STANDINGDOWN:
                region = (TextureRegion) playerStandingDown.getKeyFrame(stateTimer, true);
            default:
                region = playerStand;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (b2body.getLinearVelocity().y < 0) {
            return State.GOINGDOWN;
        } else if (b2body.getLinearVelocity().y > 0) {
            return State.GOINGUP;
        } else if (b2body.getLinearVelocity().x > 0) {
            return State.GOINGRIGHT;
        } else if (b2body.getLinearVelocity().x < 0) {
            return State.GOINGLEFT;
        } else {
            return State.STANDINGDOWN;
        }

    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(484 / Player.PPM, 90 / Player.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(18 / Player.PPM);
        fdef.filter.categoryBits = DungeonExplorer.PLAYER_BIT;
        fdef.filter.maskBits = DungeonExplorer.GROUND_BIT | DungeonExplorer.POTION_BIT | DungeonExplorer.WALL_BIT | DungeonExplorer.OBJECT_BIT | DungeonExplorer.ENEMY_BIT | DungeonExplorer.ENEMY_PROJECTILE_BIT | DungeonExplorer.BARRIER_BIT | DungeonExplorer.STONE_BIT | DungeonExplorer.TRESOR_BIT | DungeonExplorer.DEMI_BARRIER_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("playerBody");
    }

    public static void loseLifePoint() {
        if (!invincible) {
            Hud.removeLifePoints(1);
            invincible = true;
            invincibilityTimer = 0;
        }
    }

    public static void setPlayerIsDead(boolean isDead) {
        playerIsDead = isDead;
    }

    public boolean isDead() {
        return playerIsDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public Array<AllyProjectile> getProjectiles() {
        return projectiles;
    }

}
