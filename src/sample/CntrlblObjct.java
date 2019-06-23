package sample;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * wszystkie widoczne grafkiki podczas animacji sa wlasnie tej klasy
 * w tej klasie ustala sie predkosc obiektu, kat jego ruchu oraz to stwierdza czy koliduje z innym
 */
public class CntrlblObjct {

    private Shape view;
    private Point2D velocity = new Point2D(0, 0);
    private boolean alive = true;
    private int fireSpeed;
    private double spawnTime;
    private int healthPointsMax;
    private int healthPoints;
    private Color color;
    private double speed = 10;
    private String name;

    public CntrlblObjct(Shape view) {
        this.view = view;
    }

    public void update() {
        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Shape getView() {
        return view;
    }

    public Color getColor() {
        return color;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setColor(Color x) {
        view.setFill(x);
        color = x;
    }

    public boolean isNotAlive() {
        return !alive;
    }

    public double getRotate() {
        return view.getRotate();
    }

    public void setRotateObj() {
        view.setRotate(view.getRotate() - 90);
    }

    public void subtractHealthPoints(int x) {
        this.setHealthPoints(getHealthPoints() - x);
    }

    /**
     * obrot dodaje prędkosci, aby z poczatku gracz odrazu nie uderzyl w kurtynę
     */
    public void rotateRight() {
        view.setRotate(view.getRotate() + 5);
        //setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())) * 2, Math.sin(Math.toRadians(getRotate())) * 2));
    }

    public void rotateLeft() {
        view.setRotate(view.getRotate() - 5);
        //setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())) * 2, Math.sin(Math.toRadians(getRotate())) * 2));
    }

    public void moveStraight() {
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())) * 2, Math.sin(Math.toRadians(getRotate())) * 2));
    }

    public void moveBack() {
        //   setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate()+180)) * 2 , Math.sin(Math.toRadians(getRotate()+180)) * 2));
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())) * -2, Math.sin(Math.toRadians(getRotate())) * -2));
    }

    public boolean isColliding(CntrlblObjct sth) {
        return getView().getBoundsInParent().intersects(sth.getView().getBoundsInParent());
    }

    public boolean isColliding_Contains(CntrlblObjct sth) {
        return getView().getBoundsInParent().contains(sth.getView().getBoundsInParent());
    }

    public void setFireSpeed(int fireSpeed) {
        this.fireSpeed = fireSpeed;
    }

    public double getFireSpeed() {
        return fireSpeed;
    }

    public void setSpawnTime() {
        spawnTime = System.currentTimeMillis();
    }

    public double getSpawnTime() {
        return spawnTime;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getHealthPointsMax() {
        return healthPointsMax;
    }

    public void setHealthPointsMax(int healthPointsMax) {
        this.healthPointsMax = healthPointsMax;
    }

}
