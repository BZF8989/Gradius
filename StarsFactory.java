import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

/**
 * creates effect of stars in background
 *
 * Created by Bill on 11/5/2017.
 *
 * Based off of AsteroidFactory from CPSC 1181 - 003 Fall 2017
 *
 * REMOVED: moveBounds
 * Stars are created anywhere on the screen or slightly off screen on the right side.
 * They are then moved across in the -x direction until they are off screen. When they
 * have reach off screen, they are destoryed from the collection. The collection can
 * have at most 45 stars at a time.
 */
public class StarsFactory {

    private final static int STAR_SIZE= 1;
    private final static int STAR_VEL_MIN = 1;
    private final static int STAR_VEL_MAX = 2;

    private final static StarsFactory instance = new StarsFactory();

    private static Rectangle startBounds;

    private StarsFactory() {}

    public static StarsFactory getInstance() {
        return instance;
    }

    public void setStartBounds(Rectangle r) {
        startBounds = r;
    }


    public Star makeStar() {
        return new StarsImpl(random((int)startBounds.getMinX(), (int)(startBounds.getMaxX() + 1)),
                random((int)startBounds.getMinY(), (int)startBounds.getMaxY()),
                STAR_SIZE,
                STAR_SIZE,
                random(STAR_VEL_MIN, STAR_VEL_MAX ));
    }

    private static int random(int min, int max) {
        if(max-min == 0) { return min; }
        Random rand = java.util.concurrent.ThreadLocalRandom.current();
        return min + rand.nextInt(max + 1);
    }

    private static class StarsImpl extends SpriteImpl implements Star{
        private final static Color COLOR = Color.CYAN;

        public StarsImpl(int x, int y, int w, int h, float v) {
            super(new Ellipse2D.Float(x, y, w, h), startBounds, false, COLOR);
            setVelocity(-v, 0);

        }
    }
}
