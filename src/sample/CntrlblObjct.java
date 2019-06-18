package sample;

import javafx.geometry.Point2D;
import javafx.scene.Node;


public class CntrlblObjct {

    private Node view;
    private Point2D velocity = new Point2D(0, 0);

    private boolean alive = true;

    public CntrlblObjct(Node view) {
        this.view = view;
    }

    public void update() {
        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Node getView() {
        return view;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
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

    public void rotateRight() {
        view.setRotate(view.getRotate() + 3);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())) * 3, Math.sin(Math.toRadians(getRotate())) * 3));
    }

    public void rotateLeft() {
        view.setRotate(view.getRotate() - 3);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())) * 3, Math.sin(Math.toRadians(getRotate())) * 3));
    }

    public boolean isColliding(CntrlblObjct sth) {
        return getView().getBoundsInParent().intersects(sth.getView().getBoundsInParent());
    }

}
