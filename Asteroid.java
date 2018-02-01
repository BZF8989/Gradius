import java.awt.*;

public interface Asteroid extends Sprite {
    @Override
    default Shape getShape() {
        return null;
    }

    @Override
    default void setVelocity(float dx, float dy) {

    }

    @Override
    default void move() {

    }

    @Override
    default boolean isOutOfBounds() {
        return false;
    }

    @Override
    default boolean isInBounds() {
        return false;
    }

    @Override
    default void draw(Graphics2D g2) {

    }

    @Override
    default boolean intersects(Sprite other) {
        return false;
    }
}
