package sample;

import javafx.scene.shape.Line;

/**
 * klasa barier, wywoluje klase nadrzedna
 */
public class Wall extends CntrlblObjct {
    Wall() {
        super(new Line(0, 0, 0, 600));
    }


}