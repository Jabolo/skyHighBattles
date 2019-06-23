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
    private boolean backPressed = false;
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
    private List<Bullet> bullets = new ArrayList<>();
    private List<Bullet> bulletsNoDamage = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<CntrlblObjct> walls = new ArrayList<CntrlblObjct>();
    private List<Bullet> bulletsEnemies = new ArrayList<>();
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
            if (keyEvent.getCode() == KeyCode.S) backPressed = true;
        });
        theStage.getScene().setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) spacePressed = false;
            if (keyEvent.getCode() == KeyCode.A) leftPressed = false;
            if (keyEvent.getCode() == KeyCode.D) rightPressed = false;
            if (keyEvent.getCode() == KeyCode.W) straightPressed = false;
            if (keyEvent.getCode() == KeyCode.S) backPressed = false;
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
        player.setHealthPointsMax(100);
        player.setFireSpeed(8);

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

    private void addBullet(Bullet bullet, double x, double y) {
        bullets.add(bullet);
        bullet.setBulletDamage(10);
        bullet.setBulletSpeed(3);
        bullet.setColor(Color.BLUEVIOLET);
        bullet.getView().toBack();
        addCntrlblObjct(bullet, x, y);
    }

    private void addBulletNoDamage(Bullet bullet, double x, double y) {
        bulletsNoDamage.add(bullet);
        bullet.setSpawnTime();
        bullet.setColor(player.getColor());
        bullet.getView().toBack();
        addCntrlblObjct(bullet, x, y);
    }

    private void addBulletEnemie(Bullet bullet, double x, double y) {
        bulletsEnemies.add(bullet);
        bullet.setBulletSpeed(1.5);
        bullet.setBulletDamage(10);
        bullet.setSpawnTime();
        addCntrlblObjct(bullet, x, y);
        bullet.setColor(Color.RED);
    }

    private void addEnemy(Enemy enemy, double x, double y) {
        enemies.add(enemy);
        enemy.setSpeed(0.7);
        enemy.setHealthPointsMax(40);
        enemy.setFireSpeed(3);
        addCntrlblObjct(enemy, x, y);
    }

    private void addWall(Wall wall, double x, double y, boolean isRotated) {
        if (isRotated) wall.setRotateObj();
        walls.add(wall);
        wall.getView().setStyle("-fx-stroke:" + "#ff1de3");
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
        obj.setHealthPoints(obj.getHealthPointsMax());
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
                player.subtractHealthPoints(enemy.getHealthPoints());
                enemy.subtractHealthPoints(player.getHealthPoints());
            }
            if (enemy.getHealthPoints() <= 0) {
                enemy.setAlive(false);
                root.getChildren().remove(enemy.getView());
            } else
                //collision bullet enemy
                for (Bullet bullet : bullets) {
                    if (enemy.isColliding_Contains(bullet)) {
                        enemy.subtractHealthPoints(bullet.getBulletDamage());
                        if (enemy.getHealthPoints() <= 0) {
                            enemy.setAlive(false);
                            root.getChildren().remove(enemy.getView());
                            bullet.setBulletDamage(bullet.getBulletDamage() * bullet.getBulletPenetration());
                            EXP.set(EXP.get() + 1000);
                            EXP.set(EXP.get() + 50);
                        } else {
                            bullet.setAlive(false);
                            EXP.set(EXP.get() + 50);
                            root.getChildren().remove(bullet.getView());
                        }
                        //remove from view
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
        for (Bullet bulletFromEnemie : bulletsEnemies) {
            if (player.isColliding_Contains(bulletFromEnemie)) {
                player.subtractHealthPoints(bulletFromEnemie.getBulletDamage());
                if (player.getHealthPoints() <= 0) {
                    player.setAlive(false);
                } else {
                    bulletFromEnemie.setAlive(false);
                    root.getChildren().remove(bulletFromEnemie.getView());
                }


            }
            if (System.currentTimeMillis() - bulletFromEnemie.getSpawnTime() > 3000) {
                bulletFromEnemie.setAlive(false);
                root.getChildren().remove(bulletFromEnemie.getView());
            }
        }
/**
 * spawn przeciwników
 */
        if (Math.random() < 0.02) {//how often it spawns
            addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
        //if(backPressed) addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());

        /**
         * obłsuga klawiszy
         */
        counterFireSpeed++;
        if (counterFireSpeed >= 60 / player.getFireSpeed()) fire = true;

        if (rightPressed)
            if (backPressed) player.rotateLeft(); //cause of the reason
            else player.rotateRight();
        if (leftPressed)
            if (backPressed) player.rotateRight();
            else player.rotateLeft();
        if (straightPressed) player.moveStraight();
        if (backPressed) player.moveBack();
        if (!straightPressed && !backPressed) player.setVelocity(new Point2D(0, 0));
        if (spacePressed == true && fire == true) {
            Bullet bullet = new Bullet();
            addBullet(bullet, player.getView().getTranslateX() + 20, player.getView().getTranslateY() + 20);
            bullet.setVelocity(new Point2D(Math.cos(Math.toRadians(player.getRotate())), Math.sin(Math.toRadians(player.getRotate()))).multiply(bullet.getBulletSpeed()));
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
        for (Enemy enemy : enemies) {
            Point2D enemyVel = new Point2D(player.getView().getTranslateX(), player.getView().getTranslateY())
                    .subtract(enemy.getView().getTranslateX(), enemy.getView().getTranslateY())
                    .normalize()
                    .multiply(enemy.getSpeed());

            enemy.setVelocity(enemyVel);
            //enemy bullet
            if (Math.random() < enemy.getFireSpeed() / 500) {
                Bullet bullet = new Bullet();
                addBulletEnemie(bullet, enemy.getView().getTranslateX() + 20, enemy.getView().getTranslateY() + 20);
                bullet.setVelocity(enemyVel.normalize().multiply(bullet.getBulletSpeed()));
            }
        }
        counterEnemySpeed = 0;

        if (player.getHealthPoints() <= 0) player.setAlive(false);
/**
 * czyszczenie list z niepotrzebnych już obiektow
 */
        //remove from model
        bullets.removeIf(CntrlblObjct::isNotAlive); //if is dead - remove it from list
        enemies.removeIf(CntrlblObjct::isNotAlive);
        bulletsEnemies.removeIf(CntrlblObjct::isNotAlive);
        bulletsNoDamage.removeIf(CntrlblObjct::isNotAlive);

        bullets.forEach(Bullet::update);
        enemies.forEach(Enemy::update);
        bulletsEnemies.forEach(Bullet::update);
        bulletsNoDamage.forEach(Bullet::update);
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

