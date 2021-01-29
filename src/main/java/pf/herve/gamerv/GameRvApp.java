/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pf.herve.gamerv;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getWorldProperties;
import static com.almasb.fxgl.dsl.FXGL.inc;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import java.util.Map;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author chahd
 */
public class GameRvApp extends GameApplication {

    private Entity player;

    public enum EntityType {
        PLAYER, COIN
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(600);
        settings.setHeight(600);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");
    }

    @Override
    protected void initGame() {
        FXGL.entityBuilder()
                .type(EntityType.COIN)
                .at(500, 200)
                .viewWithBBox(new Circle(15, 15, 15, Color.YELLOW))
                .with(new CollidableComponent(true))            
                .buildAndAttach();

        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(300, 300)
                //.view(new Rectangle(25, 25, Color.BLUE))
                //.viewWithBBox("sheepou.png")
                .with(new CollidableComponent(true))
                .with(new AnimationComponent())
                .buildAndAttach();
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                coin.removeFromWorld();
            }
        });
    }

    /* @Override
    protected void initInput() {
        Input input = FXGL.getInput();

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                player.translateX(5); // move right 5 pixels
            }
        }, KeyCode.D);
    }*/
 /*@Override
    protected void initInput() {
        FXGL.onKey(KeyCode.D, () -> {
            player.translateX(5); // move right 5 pixels
            inc("pixelsMoved", +5);
        });

        FXGL.onKey(KeyCode.Q, () -> {
            player.translateX(-5); // move left 5 pixels
            inc("pixelsMoved", +5);
        });

        FXGL.onKey(KeyCode.Z, () -> {
            player.translateY(-5); // move up 5 pixels
            inc("pixelsMoved", +5);
        });

        FXGL.onKey(KeyCode.S, () -> {
            player.translateY(5); // move down 5 pixels
            inc("pixelsMoved", +5);
        });
    }*/
    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(AnimationComponent.class).moveRight();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(AnimationComponent.class).moveLeft();
            }
        }, KeyCode.A);
    }

    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50); // x = 50
        textPixels.setTranslateY(100); // y = 100

        textPixels.textProperty().bind(getWorldProperties().intProperty("pixelsMoved").asString());

        getGameScene().addUINode(textPixels); // add to the scene graph

        var brickTexture = FXGL.getAssetLoader().loadTexture("grass.png");
        brickTexture.setTranslateX(50);
        brickTexture.setTranslateY(450);

        FXGL.getGameScene().addUINode(brickTexture);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
