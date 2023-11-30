package com.dungeon.explorer.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dungeon.explorer.DungeonExplorer;
import com.dungeon.explorer.Screens.PlayScreen;
import com.dungeon.explorer.Sprites.Player;

import java.util.ArrayList;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private Integer worldTimer;
    private float timeCount;
    private Integer score;
    public static Integer level;
    public static Integer dungeon;

    public static int lifePoints;
    public static ArrayList<Image> lifeImages;
    private static Sound damageSound;

    private static Table bottomTable;
    Label counterLabel;
    Label dungeonNumberLabel;
    Label timeLabel;
    static Label roomNumberLabel;
    Label roomLabel;
    static Label dungeonLabel;


    public Hud(SpriteBatch sb) {
        worldTimer = 0;
        timeCount = 0;
        score = 0;
        dungeon = 1;
        level = 1;
        lifePoints = 6;
        lifeImages = new ArrayList<Image>();
        Texture heartTexture = new Texture("textures/heart.png");
        damageSound = Gdx.audio.newSound(Gdx.files.internal("music/damageHit.mp3"));

        viewport = new FitViewport(DungeonExplorer.V_WIDTH, DungeonExplorer.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table topTable = new Table();
        topTable.top();
        topTable.setFillParent(true);

        timeLabel = new Label("TIMER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        counterLabel = new Label(String.format("%04d", worldTimer), new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle(new BitmapFont(), Color.WHITE));
        dungeonLabel = new Label("DUNGEON", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        dungeonNumberLabel = new Label(String.format("%01d", dungeon), new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle(new BitmapFont(), Color.WHITE));
        roomLabel = new Label("ROOM", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        roomNumberLabel = new Label(String.format("%01d", PlayScreen.currentLevel), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        topTable.add(timeLabel).expandX().padTop(10);
        topTable.add(dungeonLabel).expandX().padTop(10);
        topTable.add(roomLabel).expandX().padTop(10);
        topTable.row();
        topTable.add(counterLabel).expandX();
        topTable.add(dungeonNumberLabel).expandX();
        topTable.add(roomNumberLabel).expandX();

        stage.addActor(topTable);

        bottomTable = new Table();
        bottomTable.bottom();
        bottomTable.setFillParent(true);

        for (int i = 0; i < lifePoints; i++) {
            lifeImages.add(new Image(heartTexture));
            bottomTable.add(lifeImages.get(i)).padBottom(10);
        }

        stage.addActor(bottomTable);
    }

    public static void addLifePoints(int HP) {
        for (int i = 0; i < HP; i++) {
            lifePoints++;
            Texture heartTexture = new Texture("textures/heart.png");
            Image heartImage = new Image(heartTexture);
            lifeImages.add(heartImage);
            bottomTable.add(heartImage).padBottom(10);
        }
        System.out.println("Your life points increased!");

    }

    public static void removeLifePoints(int HP) {
        for (int i = 0; i < HP; i++) {
            if (lifePoints > 0) {
                lifePoints--;
                bottomTable.removeActor(lifeImages.get(lifePoints));
                lifeImages.remove(lifePoints);
                damageSound.setVolume(0, 0.2f);
                damageSound.play();
                System.out.println("Damage taken");
            }


            if (lifePoints <= 0) {
                System.out.println("You died");
                Player.setPlayerIsDead(true);
            }
        }
        System.out.println("Your life points decreased.");
    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            worldTimer++;
            counterLabel.setText(String.format("%04d", worldTimer));
            timeCount = 0;
        }
        if (PlayScreen.currentLevel != level) {
            level = PlayScreen.currentLevel;
            roomNumberLabel.setText(String.format("%01d", level));
        }
    }

    public static void addDungeon() {
        dungeon++;
        dungeonLabel.setText(String.format("%01d", dungeon));
    }

    public static void addLevel() {
        level++;
        roomNumberLabel.setText(String.format("%01d", level));
    }

    public void dispose() {
        stage.dispose();
    }
}
