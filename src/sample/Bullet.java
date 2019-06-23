package sample;

import javafx.scene.shape.Circle;


/**
 * obiekty Klasy Bullet sa rowniez klasy CntrlblObjct i posiadaja wszysktie jej cechy
 */
public class Bullet extends CntrlblObjct {

    private double bulletSpeed = 10;
    private int bulletPenetration = 1;
    private int bulletDamage = 10;

    /**
     * wywołąnie konstruktora klasy nadzrzędnej
     */
    Bullet() {
        super(new Circle(2, 2, 2));
    }

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(double bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public int getBulletPenetration() {
        return bulletPenetration;
    }

    public void setBulletPenetration(int bulletPenetration) {
        this.bulletPenetration = bulletPenetration;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public void setBulletDamage(int bulletDamage) {
        this.bulletDamage = bulletDamage;
    }


}