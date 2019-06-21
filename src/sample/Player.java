package sample;

import javafx.scene.shape.Polygon;

/**
 * utworzenie samolotu gracza
 */
public class Player extends CntrlblObjct {

    Player() {

        super(new Polygon(40, 20,
                0, 40,
                10, 25,
                0, 20,
                10, 15,
                0, 0));
    }
}