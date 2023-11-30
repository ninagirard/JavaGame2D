package com.dungeon.explorer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dungeon.explorer.DungeonExplorer;

public class WinScreen implements Screen {
    private final DungeonExplorer game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    //private final Texture backgroundTexture;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private final Texture logoDungeonExplorerImgTexture;

    public WinScreen(DungeonExplorer game) {
        //backgroundTexture = new Texture("assetsIntro/backgroundIntro.png");
        this.game = game;
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false, DungeonExplorer.V_WIDTH, DungeonExplorer.V_HEIGHT);
        gamePort = new FitViewport(DungeonExplorer.V_WIDTH, DungeonExplorer.V_HEIGHT, gameCam);
        batch = new SpriteBatch();
        font = new BitmapFont();
        logoDungeonExplorerImgTexture = new Texture("assetsIntro/logoDungeonExplorerImg.png");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamePort.getCamera().combined);

        game.batch.begin();
        //game.batch.draw(backgroundTexture, 0, 0, 960, 700);
        game.batch.draw(logoDungeonExplorerImgTexture, 330, 260, 300, 300);
        font.draw(game.batch, "You Won!", 460, 240);
        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new IntroScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) && (!Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && (!Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) && (!Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) && (!Gdx.input.isKeyJustPressed(Input.Keys.UP)) && (!Gdx.input.isKeyJustPressed(Input.Keys.DOWN))) {
            game.setScreen(new IntroScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
        batch.dispose();
        font.dispose();
        logoDungeonExplorerImgTexture.dispose();
        //backgroundTexture.dispose();
    }
}
