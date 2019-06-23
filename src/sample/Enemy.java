package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * klasa wywoluje konstruktor klasy nadrzednej
 * nadaje ksztalt przeciwnikom
 */
public class Enemy extends CntrlblObjct {

    Enemy() {
//        super(new Polygon(40, 20,
//                25, 25,
//                20, 40,
//                10, 40,
//                5, 30,
//                10, 25,
//                0, 20,
//                10, 15,
//                5, 10,
//                10, 0,
//                20,0,
//                25, 15));
        super(new Circle(15, 15, 15, Color.GOLD));
        //this.setColor(Color.web("00FF00"));
    }

    public void update() {
        super.update();
        if (getHealthPoints() < 0.75 * getHealthPointsMax()) setColor(Color.ORANGE);
        if (getHealthPoints() < 0.5 * getHealthPointsMax()) setColor(Color.INDIANRED);
        if (getHealthPoints() < 0.25 * getHealthPointsMax()) setColor(Color.DARKRED);
    }
}


