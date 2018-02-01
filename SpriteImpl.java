import java.awt.*;
import java.awt.geom.*;
import java.util.logging.Logger;

public abstract class SpriteImpl implements Sprite {

    private final Logger LOG = Logger.getLogger(SpriteImpl.class.getName());

	// drawing
	private Shape shape;
	private final Color border;
	private final Color fill;

	// movement
	private float dx, dy;
	private final Rectangle2D bounds;
	private final boolean isBoundsEnforced;


	protected SpriteImpl(Shape shape, Rectangle2D bounds, boolean boundsEnforced, Color border, Color fill) {
		this.shape = shape;
		this.bounds = bounds;
		this.isBoundsEnforced = boundsEnforced;
		this.border = border;
		this.fill = fill;
	}
	protected SpriteImpl(Shape shape, Rectangle2D bounds, boolean boundsEnforced, Color fill) {
		this(shape, bounds, boundsEnforced, null, fill);
	}

	public Shape getShape() {
		return shape;
	}

	public void setVelocity(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void move() {
		if (isBoundsEnforced){

			if (isInBounds()) {
				shape = AffineTransform.getTranslateInstance(dx, dy).createTransformedShape(shape);
			} else {

				double xCoord = shape.getBounds2D().getX(); //gets current x position
				double yCoord = shape.getBounds2D().getY(); //gets current y position


				double w = bounds.getBounds2D().getWidth() - shape.getBounds2D().getWidth();
				double h = bounds.getBounds2D().getHeight() - shape.getBounds2D().getHeight();

                //on left side and trying to go left or on right side and trying to go right
				if ((xCoord <= 0 && dx < 0) || (xCoord >= w && dx > 0)) {
					dx = 0;
				}

                //on the top and trying to go up or on the bottom and trying to go down
				if ((yCoord <= 0 && dy < 0) || (yCoord >= h && dy > 0)) {
					dy = 0;
				}


				shape = AffineTransform.getTranslateInstance(dx, dy).createTransformedShape(shape);

			}
		}else{
			shape = AffineTransform.getTranslateInstance(dx, dy).createTransformedShape(shape);
		}
	}

	public boolean isOutOfBounds() {

		return !shape.intersects(bounds.getBounds2D());

	}

	public boolean isInBounds() {
		return isInBounds(bounds, shape);
	}

	private static boolean isInBounds(Rectangle2D bounds, Shape s) {
		return bounds.contains(s.getBounds());
	}

	public void draw(Graphics2D g2) {
		g2.setColor(fill);
		g2.fill(shape);
		g2.setColor(border);
		g2.draw(shape);

	}

	public boolean intersects(Sprite other) {
		if(intersects(other.getShape().getBounds2D())){
		    return intersects(new Area(shape), new Area(other.getShape()));
		}else {
		    return false;
        }
	}
	private boolean intersects(Shape other) {

	    return shape.intersects(other.getBounds2D());

	}
	private static boolean intersects(Area a, Area b) {
	    a.intersect(b);
		return !a.isEmpty();
	}


	public void fireShot(){
        int xCorrd = (int)shape.getBounds2D().getMaxX();
        int yCoord = (int)(shape.getBounds2D().getCenterY());

	}




}

