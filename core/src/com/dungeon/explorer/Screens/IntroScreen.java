package com.dungeon.explorer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dungeon.explorer.DungeonExplorer;

public class IntroScreen extends ScreenAdapter {
    private final DungeonExplorer game;
    private final Texture backgroundTexture;
    private final Texture buttonBeginTexture;
    private final Texture logoDungeonExplorerTexture;
    private final Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    public IntroScreen(final DungeonExplorer game) {
        this.game = game;
        backgroundTexture = new Texture("assetsIntro/backgroundIntro.png");
        buttonBeginTexture = new Texture("assetsIntro/buttonBegin.png");
        logoDungeonExplorerTexture = new Texture("assetsIntro/logoDungeonExplorer.png");

        camera = new OrthographicCamera();
        viewport = new FitViewport(DungeonExplorer.V_WIDTH, DungeonExplorer.V_HEIGHT, camera);
        viewport.apply();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Image buttonBeginImage = new Image(buttonBeginTexture);
        buttonBeginImage.setPosition(350, 100); // Position du bouton
        buttonBeginImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });
        stage.addActor(buttonBeginImage);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    game.setScreen(new PlayScreen(game));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, 960, 700);
        game.batch.draw(logoDungeonExplorerTexture, 200, 300, 550, 440);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        buttonBeginTexture.dispose();
        logoDungeonExplorerTexture.dispose();
        stage.dispose();
    }
}
