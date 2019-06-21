package sample;

import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * klasa ktora odpowiada za rozgrywke
 * zawiera listy obiektow pociskow, przeciwnikow oraz kurtyn
 * zawiera funckcje do obslugi klawiatury
 */

public class Controller implements Initializable {


    boolean leftPressed = false;
    boolean rightPressed = false;
    boolean spacePressed = false;
    boolean straightPressed = false;
    boolean fire = false;
    int counterFireSpeed = 0;
    int counterEnemySpeed = 0;
    boolean startAnime = true;
    Text textPoints = new Text();
    Button reset = new Button("Wanna play again");
    Label score = new Label("Your score");
    Stage theStage;
    private IntegerProperty EXP = new SimpleIntegerProperty();
    private Pane root;
    private List<CntrlblObjct> bullets = new ArrayList<CntrlblObjct>();
    private List<CntrlblObjct> bulletsNoDamage = new ArrayList<CntrlblObjct>();
    private List<CntrlblObjct> enemies = new ArrayList<CntrlblObjct>();
    private List<CntrlblObjct> walls = new ArrayList<CntrlblObjct>();
    private List<CntrlblObjct> bulletsEnemies = new ArrayList<CntrlblObjct>();
    private CntrlblObjct player;

    /**
     * obluga klawisza w menu koncowym
     *
     * @param event
     * @throws Exception
     * @throws IOException
     */
    public void endButtonPushed(ActionEvent event) throws Exception, IOException {
        theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Pane root = new Pane(new ImageView(new Image("kwadraty.jpg")));
        root.setPrefSize(600, 600);
        Parent sceneBuild = FXMLLoader.load(getClass().getResource("menuStart.fxml"));
        root.getChildren().add(sceneBuild);
        theStage.setScene(new Scene(root, 600, 600));
        theStage.show();
    }

    /**
     * obługa klawiatury podczas gry
     *
     * @param event
     */
    public void startButtonPushed(ActionEvent event) {
        theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        theStage.setScene(new Scene(createContent()));

        theStage.getScene().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) spacePressed = true;
            if (keyEvent.getCode() == KeyCode.A) leftPressed = true;
            if (keyEvent.getCode() == KeyCode.D) rightPressed = true;
            if (keyEvent.getCode() == KeyCode.W) straightPressed = true;
        });
        theStage.getScene().setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) spacePressed = false;
            if (keyEvent.getCode() == KeyCode.A) leftPressed = false;
            if (keyEvent.getCode() == KeyCode.D) rightPressed = false;
            if (keyEvent.getCode() == KeyCode.W) straightPressed = false;
        });
        theStage.setResizable(false);
        theStage.show();
        //}));
    }

    /**
     * rysowanie pola gry, stanu początowego mapy
     *
     * @return
     */
    private Parent createContent() {
        root = new Pane(new ImageView(new Image("kwadraty.jpg")));
        root.setPrefSize(600, 600);

        player = new Player();
        player.setVelocity(new Point2D(0, 0));

        addCntrlblObjct(player, root.getPrefWidth() / 2, root.getPrefHeight() / 2);
        addWall(new Wall(), 10, 0, false);
        addWall(new Wall(), 590, 0, false);
        addWall(new Wall(), 300, -290, true);
        addWall(new Wall(), 300, 290, true);


        textPoints.setTranslateX(550);
        textPoints.setTranslateY(20);
        textPoints.textProperty().bind(EXP.asString());
        root.getChildren().add(textPoints);


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!player.isNotAlive())
                    onUpdate();
            }
        };
        if (startAnime) {
            timer.start();
            startAnime = false;
        }
        if (player.isNotAlive()) timer.stop();
        return root;
    }

    private void addBullet(CntrlblObjct bullet, double x, double y) {
        bullets.add(bullet);
        addCntrlblObjct(bullet, x, y);
    }

    private void addBulletNoDamage(CntrlblObjct bullet, double x, double y) {
        bulletsNoDamage.add(bullet);
        bullet.setSpawnTime();
        addCntrlblObjct(bullet, x, y);
    }

    private void addBulletEnemie(CntrlblObjct bullet, double x, double y) {
        bulletsEnemies.add(bullet);
        bullet.setSpawnTime();
        addCntrlblObjct(bullet, x, y);
        bullet.setColor(Color.RED);
    }

    private void addEnemy(CntrlblObjct enemy, double x, double y) {
        enemies.add(enemy);
        addCntrlblObjct(enemy, x, y);
    }

    private void addWall(CntrlblObjct wall, double x, double y, boolean isRotated) {
        if (isRotated) wall.setRotateObj();
        walls.add(wall);
        addCntrlblObjct(wall, x, y);
    }

    /**
     * dodanie obiektu do pola gry
     *
     * @param obj
     * @param x
     * @param y
     */
    private void addCntrlblObjct(CntrlblObjct obj, double x, double y) {
        obj.getView().setTranslateX(x);
        obj.getView().setTranslateY(y);
        //add obj to root Node
        root.getChildren().add(obj.getView());
    }

    /**
     * główna pętla
     * obsługa kolizji między:
     * pociskami
     * kurtynami
     * przeciwnikami
     * graczem
     */
    private void onUpdate() { //60times per sec
        for (CntrlblObjct enemy : enemies) {
            //collision enemy : player
            if (player.isColliding_Contains(enemy)) {
                player.setAlive(false);

            }
            //collision bullet enemy
            for (CntrlblObjct bullet : bullets) {
                if (enemy.isColliding_Contains(bullet)) {
                    bullet.setAlive(false);
                    enemy.setAlive(false);
                    //remove from view
                    root.getChildren().removeAll(bullet.getView(), enemy.getView());
                    EXP.set(EXP.get() + 50);
                }
            }
        }
        for (CntrlblObjct wall : walls) {
            //collision wall player
            if (player.isColliding(wall)) {
                player.setAlive(false);
            } else
            //collision bullet : wall
            {
                for (CntrlblObjct bullet : bullets) {
                    if (bullet.isColliding(wall)) {
                        bullet.setAlive(false);
                        root.getChildren().remove(bullet.getView());
                    }
                }
            }
            for (CntrlblObjct bulletFromEnemy : bulletsEnemies) {
                if (bulletFromEnemy.isColliding(wall)) {
                    bulletFromEnemy.setAlive(false);
                    root.getChildren().remove(bulletFromEnemy.getView());
                }
            }
        }
        //collision bulletEnemie : player
        for (CntrlblObjct bulletFromEnemie : bulletsEnemies) {
            if (player.isColliding_Contains(bulletFromEnemie)) {
                player.setAlive(false);
                bulletFromEnemie.setAlive(false);
                root.getChildren().remove(bulletFromEnemie.getView());
            }
            if (System.currentTimeMillis() - bulletFromEnemie.getSpawnTime() > 3000) {
                bulletFromEnemie.setAlive(false);
                root.getChildren().remove(bulletFromEnemie.getView());
            }
        }
/**
 * spawn przeciwników
 */
        if (Math.random() < 0.03) {//how often it spawns
            addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
        /**
         * obłsuga klawiszy
         */
        counterFireSpeed++;
        if (counterFireSpeed >= 60 / player.getFireSpeed()) fire = true;

        if (rightPressed) player.rotateRight();
        if (leftPressed) player.rotateLeft();
        if (straightPressed) player.moveStraight();
        if (!straightPressed) player.setVelocity(new Point2D(0, 0));
        if (spacePressed == true && fire == true) {
            Bullet bullet = new Bullet();
            bullet.setVelocity(new Point2D(Math.cos(Math.toRadians(player.getRotate())) * 2, Math.sin(Math.toRadians(player.getRotate())) * 2).normalize().multiply(3));
            addBullet(bullet, player.getView().getTranslateX() + 20, player.getView().getTranslateY() + 20);

            fire = false;
            counterFireSpeed = 0;
        }
        //smuga
        Bullet bullet2 = new Bullet();
        bullet2.setVelocity(player.getVelocity().normalize().multiply(0));
        addBulletNoDamage(bullet2, player.getView().getTranslateX() + 20, player.getView().getTranslateY() + 20);

        for (CntrlblObjct bulletNoDam : bulletsNoDamage) {
            if (System.currentTimeMillis() - bulletNoDam.getSpawnTime() > 2000) {
                bulletNoDam.setAlive(false);
                root.getChildren().remove(bulletNoDam.getView());
            }
        }
        //enemy vel
        /**
         * nadanie śledzenia, prędkości oraz możlwiości strzelania
         */
        for (CntrlblObjct enemy : enemies) {
            Point2D enemyVel = new Point2D(player.getView().getTranslateX(), player.getView().getTranslateY())
                    .subtract(enemy.getView().getTranslateX(), enemy.getView().getTranslateY())
                    .normalize()
                    .multiply(0.5);

            enemy.setVelocity(enemyVel);
            //enemy bullet
            if (Math.random() < enemy.getFireSpeed() / 500) {
                Bullet bullet = new Bullet();
                bullet.setBulletSpeed(5);
                bullet.setVelocity(enemyVel.multiply(bullet.getBulletSpeed()));
                addBulletEnemie(bullet, enemy.getView().getTranslateX() + 20, enemy.getView().getTranslateY() + 20);
            }
        }
        counterEnemySpeed = 0;

/**
 * czyszczenie list z niepotrzebnych już obiektow
 */
        //remove from model
        bullets.removeIf(CntrlblObjct::isNotAlive); //if is dead - remove it from list
        enemies.removeIf(CntrlblObjct::isNotAlive);
        bulletsEnemies.removeIf(CntrlblObjct::isNotAlive);
        bulletsNoDamage.removeIf(CntrlblObjct::isNotAlive);

        bullets.forEach(CntrlblObjct::update);
        enemies.forEach(CntrlblObjct::update);
        bulletsEnemies.forEach(CntrlblObjct::update);
        bulletsNoDamage.forEach(CntrlblObjct::update);
/**
 * Jeśli gracz nie przeszedł dalej, wyświetlenie podsumowania, ostatnie menu
 */
        if (player.isNotAlive()) {
            //theend
            bullets.removeAll(bullets); //if is dead - remove it from list
            enemies.removeAll(enemies);
            bulletsEnemies.removeAll(bulletsEnemies);
            root.getChildren().removeAll();
            try {
                root.getChildren().add(FXMLLoader.load(getClass().getResource("menuAfterGame.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            textPoints.prefWidth(100);
            textPoints.setTextAlignment(TextAlignment.CENTER);
            textPoints.setTranslateX(250);
            textPoints.setTranslateY(250);
            textPoints.setStyle("-fx-font-size: 50px");
        } else
            player.update();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

