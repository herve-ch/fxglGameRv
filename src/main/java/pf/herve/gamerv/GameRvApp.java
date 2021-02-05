/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pf.herve.gamerv;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.getWorldProperties;
import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGL.run;
import com.almasb.fxgl.entity.Entity;

import java.util.Map;

import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author chahd
 */
public class GameRvApp extends GameApplication {

    private Entity player;

    public enum Type {
        PLAYER, GRASS, BOMB
    }

    @Override
    protected void initSettings(GameSettings settings) {
        // initialize common game / window settings.
        settings.setTitle("Drop");
        settings.setVersion("1.0");
        settings.setWidth(480);
        settings.setHeight(800);
    }

    @Override
    protected void initGame() {
        spawnPlayer();
        // creates a timer that runs spawnDroplet() every second
        run(() -> spawnGrass(), Duration.seconds(1));
        run(() -> spawnBomb(), Duration.seconds(1));

        // loop background music located in /resources/assets/music/
        //loopBGM("bgm.mp3");
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(Type.PLAYER, Type.GRASS, (player, grass) -> {

            // code in this block is called when there is a collision between Type.BUCKET and Type.DROPLET
            // remove the collided droplet from the game
            grass.removeFromWorld();
            // getWorldProperties().increment("hpLeft", +1);

            // play a sound effect located in /resources/assets/sounds/
            //play("drop.wav");
        });
        
        onCollisionBegin(Type.PLAYER, Type.BOMB, (player, bomb) -> {

            // code in this block is called when there is a collision between Type.BUCKET and Type.DROPLET
            // remove the collided droplet from the game
            bomb.removeFromWorld();
            getWorldProperties().increment("hpLeft", -1);

            // play a sound effect located in /resources/assets/sounds/
            //play("drop.wav");
        });
    }

    private void spawnPlayer() {
        player = entityBuilder()
                .type(Type.PLAYER)
                .at(getAppWidth() / 2, getAppHeight() - 200)
                .viewWithBBox("sheepou.png")
                .collidable()
                .buildAndAttach();

        // bind bucket's X value to mouse X
        player.xProperty().bind(getInput().mouseXWorldProperty());
    }

    private void spawnGrass() {
        entityBuilder()
                .type(Type.GRASS)
                .at(FXGLMath.random(0, getAppWidth() - 64), 0)
                .viewWithBBox("grass.png")
                .collidable()
                .buildAndAttach();
    }

    private void spawnBomb() {
        int random = FXGLMath.random(0, 3);

        if (random == 3) {

            entityBuilder()
                    .type(Type.BOMB)
                    .at(FXGLMath.random(0, getAppWidth() - 64), 0)
                    .viewWithBBox("bomb.png")
                    .collidable()
                    .buildAndAttach();
        }
    }

    @Override
    protected void initUI() {
        Text hpText = new Text();
        hpText.setTranslateX(120); // x = 50
        hpText.setTranslateY(100); // y = 100
        Text hpLabel = new Text("Nombre de vies :");
        hpLabel.setTranslateX(20); // x = 50
        hpLabel.setTranslateY(100); // y = 100

        hpText.textProperty().bind(getWorldProperties().intProperty("hpLeft").asString());
        getGameScene().addUINode(hpText); // add to the scene graph  
        getGameScene().addUINode(hpLabel); // add to the scene graph      

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("hpLeft", 3);
    }

    @Override
    protected void onUpdate(double tpf) {

        // for each entity of Type.DROPLET translate (move) it down
        getGameWorld().getEntitiesByType(Type.GRASS).forEach(grass -> grass.translateY(150 * tpf));
        getGameWorld().getEntitiesByType(Type.BOMB).forEach(bomb -> bomb.translateY(300 * tpf));

        getGameWorld().getEntitiesByType(Type.GRASS).forEach(grass -> {
            if (grass.getBottomY() > getAppHeight()) {
                getWorldProperties().increment("hpLeft", -1);
                grass.removeFromWorld();

            }
        });
         getGameWorld().getEntitiesByType(Type.BOMB).forEach(bomb -> {
            if (bomb.getBottomY() > getAppHeight()) {
                bomb.removeFromWorld();
            }
        });

        if (getWorldProperties().getInt("hpLeft") == 0) {
            /*getGameWorld().getEntities().forEach(entity -> {
                entity.setVisible(false);
            });*/
            gameOver();
        }

    }

    private void gameOver() {
        getDialogService().showConfirmationBox("Cheepou a perdu :( \nContinuer?", yes -> {
            if (yes) {
                getGameController().startNewGame();
            } else {
                getGameController().exit();
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
