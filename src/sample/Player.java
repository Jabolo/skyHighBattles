package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * utworzenie samolotu gracza
 */
public class Player extends CntrlblObjct {

    private int expPoints = 0;
    Player() {

        super(new Polygon(40, 20,
                0, 40,
                10, 25,
                0, 20,
                10, 15,
                0, 0));
        this.setColor(Color.web("ADD8E6"));
        this.getView().setStroke(Color.BLACK);
    }

    public void update() {
        super.update();
        if (getHealthPoints() < 0.75 * getHealthPointsMax()) super.setColor(Color.web("40E0D0"));
        if (getHealthPoints() < 0.5 * getHealthPointsMax()) super.setColor(Color.web("4682B4"));
        if (getHealthPoints() < 0.25 * getHealthPointsMax()) super.setColor(Color.web("191970"));
    }

}