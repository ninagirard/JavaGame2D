package com.dungeon.explorer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Scenes.Hud;
import com.dungeon.explorer.Sprites.*;
import com.dungeon.explorer.Tools.B2WorldCreator;
import com.dungeon.explorer.Tools.WorldContactListener;

import java.util.HashMap;
import java.util.Iterator;

public class PlayScreen implements Screen {
    private DungeonExplorer game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player;
    private Ninja ninja;
    private Ninja ninja2;
    private Ninja ninja3;
    private Ninja ninja4;
    private Ninja ninja5;
    private Men men;
    private Men men2;
    private Men men3;
    private Men men4;
    private Men men5;
    private PinkFish bobby;
    private TextureAtlas atlas;
    private Music backgroundMusic;
    private Array<EnemyProjectile> enemyProjectiles;
    private B2WorldCreator worldCreator;
    public static int currentLevel = 1;
    private boolean shouldMoveCamera = false;
    private float cameraMoveTime = 0;
    private Sound wooshShound;
    private Sound breakStoneSound;
    private Sound winSound;
    private boolean gameWin = false;
    private boolean hasLevelChangedRecently = false;

    public PlayScreen(DungeonExplorer game) {
        Gdx.input.setInputProcessor(null);
        atlas = new TextureAtlas("linkAndEnemies.atlas");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(DungeonExplorer.V_WIDTH / Player.PPM, DungeonExplorer.V_HEIGHT / Player.PPM,
                gameCam);
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/DungeonRoom.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Player.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, 0), true);
        createBorders();
        wooshShound = Gdx.audio.newSound(Gdx.files.internal("music/woosh.mp3"));
        breakStoneSound = Gdx.audio.newSound(Gdx.files.internal("music/breakStone.mp3"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("music/victory.mp3"));

        // Uncomment this line and the b2dr.render(world, gameCam.combined); below to see the Box2D debug lines
//        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(this, player);

        player = new Player(this);
        world.setContactListener(new WorldContactListener(player));

        //Level 1
        ninja = new Ninja(this, 2.92f, 2.92f);
        ninja2 = new Ninja(this, 6.92f, 3.92f);
        men = new Men(this, 4.92f, 4.92f);
        men2 = new Men(this, 8.92f, 4.92f);

//        Level 2
        ninja3 = new Ninja(this, 2.92f, 11.92f);
        ninja4 = new Ninja(this, 6.92f, 11.92f);
        ninja5 = new Ninja(this, 6.92f, 11.92f);
        men3 = new Men(this, 4.92f, 10.92f);
        men4 = new Men(this, 3.42f, 10.92f);
        men5 = new Men(this, 7.52f, 10.92f);

        //Level 3 - Boss Level
        bobby = new PinkFish(this, 6.92f, 15.22f, player);

        enemyProjectiles = new Array<EnemyProjectile>();
        worldCreator = new B2WorldCreator(this, player);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void setShouldMoveCamera(boolean moveCamera) {
        this.shouldMoveCamera = moveCamera;
        if (moveCamera) {
            cameraMoveTime = 0;
        }
        wooshShound.play();
    }

    public void addEnemyProjectile(EnemyProjectile projectile) {
        enemyProjectiles.add(projectile);
    }

    @Override
    public void show() {
//        Gdx.graphics.setWindowedMode(DungeonExplorer.V_WIDTH, DungeonExplorer.V_HEIGHT);
//        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/dungeonBoss.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();
    }

    public void handleInput(float dt) {
        float velocity = 150.0f; // Player speed
        Vector2 movement = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movement.y += velocity;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.x += velocity;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.x -= velocity;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movement.y -= velocity;
        }

        player.b2body.setLinearVelocity(movement.scl(dt));
    }

    public void update(float dt) {
        handleInput(dt);

//        System.out.println("CurrentLevel" + currentLevel);
//        System.out.println("Enemy counter: " + Enemy.enemyCounter);

        if (Enemy.enemyCounter <= 0 && !hasLevelChangedRecently && currentLevel == 1) {
            hasLevelChangedRecently = true;
            System.out.println("Destroying level 1 stones");
            breakStoneSound.play();

            TiledMapTileLayer layerToRemove = (TiledMapTileLayer) map.getLayers().get(8);
            if (layerToRemove != null) {
                map.getLayers().remove(layerToRemove);
            }

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    hasLevelChangedRecently = false;
                }
            }, 10);
        }

        if (Enemy.enemyCounter <= 0 && !hasLevelChangedRecently && currentLevel == 2) {
            hasLevelChangedRecently = true;
            System.out.println("Destroying level 2 stones");
            breakStoneSound.play();

            TiledMapTileLayer layerToRemove = (TiledMapTileLayer) map.getLayers().get(8);
            if (layerToRemove != null) {
                map.getLayers().remove(layerToRemove);
            }

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    hasLevelChangedRecently = false;
                }
            }, 10);
        }


        if (Enemy.enemyCounter <= 0 && !hasLevelChangedRecently && currentLevel == 3) {
            hasLevelChangedRecently = true;
            System.out.println("Destroying level 3 stones");
            breakStoneSound.play();

            TiledMapTileLayer layerToRemove = (TiledMapTileLayer) map.getLayers().get(8);
            if (layerToRemove != null) {
                map.getLayers().remove(layerToRemove);
            }

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    hasLevelChangedRecently = false;
                }
            }, 120);
        }


        if (shouldMoveCamera) {
            cameraMoveTime += dt;
            if (cameraMoveTime <= 2f) {
                float targetY = gameCam.position.y + 0.1f; // Par exemple, 5 unités plus haut que la position actuelle

                Vector3 targetPosition = new Vector3(gameCam.position.x, targetY, 0);
                gameCam.position.lerp(targetPosition, cameraMoveTime / 2);
            } else {
                cameraMoveTime = 0;
                shouldMoveCamera = false;
            }
        }

        if (win() && !gameWin) {
            gameWin = true;
            Gdx.app.log("Win", "true");
            game.setScreen(new WinScreen(game));
            backgroundMusic.pause();
            winSound.play();
        }

        world.step(1 / 60f, 6, 2);
        player.update(dt);
        ninja.update(dt, player);
        ninja2.update(dt, player);
        ninja3.update(dt, player);
        ninja4.update(dt, player);
        ninja5.update(dt, player);
        men.update(dt, player);
        men2.update(dt, player);
        men3.update(dt, player);
        men4.update(dt, player);
        men5.update(dt, player);
        bobby.update(dt, player);
        hud.update(dt);
        gameCam.update();
        renderer.setView(gameCam);

        for (Iterator<EnemyProjectile> iter = enemyProjectiles.iterator(); iter.hasNext(); ) {
            EnemyProjectile projectile = iter.next();
            projectile.update(dt);
            if (projectile.isDestroyed()) {
                iter.remove(); // Supprime le projectile de la liste
                projectile.destroyBody();
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
//        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        player.draw(game.batch);
        ninja.draw(game.batch);
        ninja2.draw(game.batch);
        ninja3.draw(game.batch);
        ninja4.draw(game.batch);
        ninja5.draw(game.batch);
        men.draw(game.batch);
        men2.draw(game.batch);
        men3.draw(game.batch);
        men4.draw(game.batch);
        men5.draw(game.batch);
        bobby.draw(game.batch);
        for (Projectile projectile : player.getProjectiles()) {
            projectile.draw(game.batch);
        }
        for (EnemyProjectile projectile : enemyProjectiles) {
            projectile.draw(game.batch);
        }

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (gameOver()) {
            System.out.println("game over true");
            game.setScreen(new GameOverScreen(game));
        }
    }

    public boolean win(){
        if (Tresor.win){
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        gameCam.update();
    }

    public TiledMap getMap() {
        return map;
    }

    private void createBorders() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        EdgeShape edge = new EdgeShape();

        bdef.type = BodyDef.BodyType.StaticBody;

        // Créé frontières
        float mapWidth = DungeonExplorer.V_WIDTH / Player.PPM;
        float mapHeight = DungeonExplorer.V_HEIGHT / Player.PPM;

        // Créer les bords
        Body body = world.createBody(bdef);

        // Bas
        edge.set(new Vector2(0, 0), new Vector2(mapWidth, 0));
        fdef.shape = edge;
        body.createFixture(fdef);

        // Gauche
        edge.set(new Vector2(0, 0), new Vector2(0, mapHeight));
        fdef.shape = edge;
        body.createFixture(fdef);

        // Droite
        edge.set(new Vector2(mapWidth, 0), new Vector2(mapWidth, mapHeight));
        fdef.shape = edge;
        body.createFixture(fdef);
        edge.dispose();
    }

    public World getWorld() {
        return world;
    }

    public boolean gameOver() {
        if (player.isDead() && player.getStateTimer() > 0) {
            backgroundMusic.pause();
            DungeonExplorer.resetStaticVariables();
            return true;
        }
        return false;
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
//        b2dr.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        hud.dispose();
        ninja.dispose();
        backgroundMusic.dispose();
        breakStoneSound.dispose();
        winSound.dispose();
        ninja2.dispose();
        ninja3.dispose();
        ninja4.dispose();
        ninja5.dispose();
        men.dispose();
        men2.dispose();
        men3.dispose();
        men4.dispose();
        men5.dispose();
        bobby.dispose();
        atlas.dispose();
        for (Projectile projectile : player.getProjectiles()) {
            projectile.dispose();
        }
        for (EnemyProjectile projectile : enemyProjectiles) {
            projectile.dispose();
        }
    }
}