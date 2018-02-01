import java.awt.Rectangle;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class AsteroidFactory {

	private final static int ASTEROID_SIZE_MIN = 10;
	private final static int ASTEROID_SIZE_MAX = 40;
	private final static int ASTEROID_VEL_MIN = 1;
	private final static int ASTEROID_VEL_MAX = 4;

	private final static AsteroidFactory instance = new AsteroidFactory();

	private static Rectangle startBounds;
	private static Rectangle moveBounds;

	private AsteroidFactory() {}

	public static AsteroidFactory getInstance() {
		return instance;
	}

	public void setStartBounds(Rectangle r) {
		startBounds = r;
	}

	public void setMoveBounds(Rectangle r) {
        moveBounds = r;
    }

	public Asteroid makeAsteroid() {
		return new AsteroidImpl(899,
				random(0, 600),
				random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX),
                random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX),
				random(ASTEROID_VEL_MIN, ASTEROID_VEL_MAX ));
	}

	private static int random(int min, int max) {
		if(max-min == 0) { return min; }
		Random rand = java.util.concurrent.ThreadLocalRandom.current();
		return min + rand.nextInt(max + 1);
	}

	private static class AsteroidImpl extends SpriteImpl implements Asteroid {
		private final static Color COLOR = Color.DARK_GRAY;

		public AsteroidImpl(int x, int y, int w, int h, float v) {
			super(new Ellipse2D.Float(x, y, w, h), moveBounds, false, Color.white,Color.BLACK);
			setVelocity(-v, 0);

		}
	}
}
