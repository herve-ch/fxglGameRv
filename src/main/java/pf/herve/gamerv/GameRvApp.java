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
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.getWorldProperties;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.loopBGM;
import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGL.play;
import static com.almasb.fxgl.dsl.FXGL.run;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.time.TimerAction;
import java.util.Map;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author chahd
 */
public class GameRvApp extends GameApplication {

    private Entity player;

    public enum Type {
        PLAYER, GRASS
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

        // loop background music located in /resources/assets/music/
        //loopBGM("bgm.mp3");
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(Type.PLAYER, Type.GRASS, (player, grass) -> {

            // code in this block is called when there is a collision between Type.BUCKET and Type.DROPLET
            // remove the collided droplet from the game
            grass.removeFromWorld();
            getWorldProperties().increment("hpLeft", +1);

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

    @Override
    protected void initUI() {
        Text hpText = new Text();
        hpText.setTranslateX(50); // x = 50
        hpText.setTranslateY(100); // y = 100
        hpText.textProperty().bind(getWorldProperties().intProperty("hpLeft").asString());
        getGameScene().addUINode(hpText); // add to the scene graph      
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("hpLeft", 0);
    }

    @Override
    protected void onUpdate(double tpf) {

        // for each entity of Type.DROPLET translate (move) it down
        getGameWorld().getEntitiesByType(Type.GRASS).forEach(grass -> grass.translateY(150 * tpf));
        getGameWorld().getEntitiesByType(Type.GRASS).forEach(grass -> {
            if (grass.getBottomY() > getAppHeight()) {
                getWorldProperties().increment("hpLeft", -1);
                            grass.removeFromWorld();

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
