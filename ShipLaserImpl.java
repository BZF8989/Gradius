import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Bill on 11/5/2017.
 *
 * This class draws one laser beam.
 *
 *
 */
public class ShipLaserImpl extends SpriteImpl implements ShipLaser {

    private static final Color LASER_COLOR = Color.RED;
    private static final double LASER_HEIGHT = 1.2;
    private static final double LASER_LENGTH = 12;
    private static final float LASER_SPEED = 5;

    private boolean remove = false;


    ShipLaserImpl(int x, int y, Rectangle2D bounds){
        super(new Rectangle2D.Double((double)x, (double)y, LASER_LENGTH, LASER_HEIGHT), bounds,false, LASER_COLOR);
        setVelocity(LASER_SPEED, 0);
    }

    public void toBeRemove(){
        remove = true;
    }

    public boolean needsToBeRemoved(){
        return remove;
    }

}
