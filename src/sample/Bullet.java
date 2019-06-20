package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * obiekty Klasy Bullet sa rowniez klasy CntrlblObjct i posiadaja wszysktie jej cechy
 */
public class Bullet extends CntrlblObjct {
    /**
     * wywołąnie konstruktora klasy nadzrzędnej
     */
    private double bulletSpeed = 5;

    Bullet() {
        super(new Circle(2, 2, 2, Color.BLACK));
    }

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(double bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }


}