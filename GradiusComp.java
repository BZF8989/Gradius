import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.JComponent;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GradiusComp extends JComponent {

    private final Logger LOG = Logger.getLogger(GradiusComp.class.getName());

    private final static int STAR_NUMBER = 45;

	private final static int GAME_TICK = 1000 / 60;
	private final static int ASTEROID_MAKE_TICK = 1000/25;
    private final static int STAR_MAKE_TICK = 1000/15;
    private final static int LASER_COOLDOWN = 1000/2;
    private final static int SCORE_TICK = 1000;

	private final static int SHIP_INIT_X = 10;
	private final static int SHIP_INIT_Y = Gradius.HEIGHT/3;
	private final static int SHIP_VEL_BASE = 2;
	private final static int SHIP_VEL_FAST = 4;

	private boolean gameEnds =  false;
    private boolean laserDelayed = false;  //laser cooldown

	private String GAME_OVER = "GAME OVER";
	private final static int FONT_SIZE = 42;
	private static int score = 0;

	private Ship ship;
	private final Timer gameTick[] = new Timer[5];

	private Collection<Asteroid> roids;
	private Collection<Star> stars;
	private Collection<ShipLaser> shots;

	private static final int POINTS_PER_LEVEL = 100;
	private static int level = 0;
	
	public GradiusComp() {

        roids = new HashSet<>();
        stars = new HashSet<>();
        shots = new HashSet<>();
        ShipKeyListener listenerKeys = new ShipKeyListener();
        addKeyListener(listenerKeys);
        //start();

	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		paintComponent(g2);
	}

	private void paintComponent(Graphics2D g2) {
	    ship.draw(g2);
        stars.forEach(a -> a.draw(g2));
	    roids.forEach(a -> a.draw(g2));
	    shots.forEach(a->a.draw(g2));
        g2.setColor(Color.RED);
        g2.setFont(new Font("TimesRoman", Font.BOLD, FONT_SIZE/4));
        g2.drawString("SCORE: " + score, FONT_SIZE, FONT_SIZE);
	    if(gameEnds){
	        g2.setColor(Color.RED);
            g2.setFont(new Font("TimesRoman", Font.BOLD, FONT_SIZE));
            g2.drawString(GAME_OVER + " -  SCORE: " + score, Gradius.HEIGHT/4, Gradius.HEIGHT/2 );
        }

	}

	public void start() {

        ship = new ShipImpl(SHIP_INIT_X, SHIP_INIT_Y, getBounds(null));

        Rectangle r = new Rectangle((int)getBounds().getMaxX(),0,1,(int)getBounds().getHeight());

        AsteroidFactory.getInstance().setStartBounds(r);
        AsteroidFactory.getInstance().setMoveBounds(getBounds().union(r));

        StarsFactory.getInstance().setStartBounds(getBounds().union(r));

        //AsteroidFactory.getInstance().setMoveBounds(getBounds());
        gameTick[0] = new Timer(GAME_TICK, a-> update());
        gameTick[1] = new Timer(ASTEROID_MAKE_TICK, a->makeAsteroid());
        gameTick[2] = new Timer(STAR_MAKE_TICK, a->makeStar());
        gameTick[3] = new Timer(LASER_COOLDOWN, a->setLaser());
        gameTick[4] = new Timer(SCORE_TICK,a->scoreIncrease());//score increase ever second you are alive
        Arrays.stream(gameTick).forEach(Timer::start);
        gameTick[3].stop();


	}

	private void update() {
        requestFocusInWindow();

        //MOVE EVERYTHING
        ship.move();
        stars.parallelStream().forEach(Star::move);
        roids.parallelStream().forEach(Asteroid::move);
        shots.parallelStream().forEach(ShipLaser::move);

        //CHECK FOR OUT OF BOUNDS, IF SO, REMOVE
        roids.removeIf(Asteroid::isOutOfBounds);
        stars.removeIf(Star::isOutOfBounds);
        shots.removeIf(ShipLaser::isOutOfBounds);

        //CHECK IF A SHOT HAS HIT AN ASTEROID
        for (ShipLaser l : shots){
            int x = roids.size();
            roids.removeIf(asteroid -> asteroid.intersects(l));
            if(roids.size()< x) {
                l.toBeRemove();
                score+=5;
            }
        }
        shots.removeIf(ShipLaser::needsToBeRemoved);

        //Increase speed
        if(score > (level+1)*POINTS_PER_LEVEL){
            level++;
            gameTick[1] = new Timer((ASTEROID_MAKE_TICK*(level+1)), a->makeAsteroid());
            gameTick[1].start();
        }

        //CHECK IF SHIP HAS HIT ASTEROID
        if(roids.parallelStream().anyMatch(a->a.intersects(ship))) {
            Arrays.stream(gameTick).forEach(Timer::stop);
            //LOG.info("Got here, means collision"); //DEBUGGING
            gameEnds = true;
        }

        repaint();

        //Arrays.stream(stars).forEach(Stars::reset);

    }


    private void makeAsteroid(){
	    roids.add(AsteroidFactory.getInstance().makeAsteroid());
    }

    //do not create more than STAR_NUMBER of stars
    private void makeStar() {
        if(stars.size() < STAR_NUMBER){
            stars.add(StarsFactory.getInstance().makeStar());
        }
    }


    private void makeLaser(KeyEvent e){

        if(e.getKeyCode() == KeyEvent.VK_SPACE && !laserDelayed) {
//            Rectangle r = new Rectangle(0, 0 , (int)getBounds().getWidth(), (int)getBounds().getHeight());
//            r.grow(10, 0);
            shots.add(new ShipLaserImpl(ship.getX(), ship.getY(), getBounds()));
            laserDelayed = true; //laser cooldown
            gameTick[3].start();
        }
    }

    private void setLaser(){
        laserDelayed = false;
        gameTick[3].stop();
        gameTick[3] = new Timer(LASER_COOLDOWN, a->setLaser());
    }


    private void scoreIncrease(){
        score++;

    }

    /*
     * Code taken from BoxGameComp.java
     * CPSC 1181-003, Fall 2017
     */
    private class ShipKeyListener extends KeyAdapter {

        private boolean up;
        private boolean down;
        private boolean left;
        private boolean right;
        private boolean shoot;

        private void setVelocity(KeyEvent e) {
            setDirection(e);
            final int dp = e.isShiftDown() ? SHIP_VEL_FAST : SHIP_VEL_BASE;

            int dx = 0;
            int dy = 0;

            if(up && !down) {
                dy = -dp;
            } else if(down && !up) {
                dy = dp;
            }
            if(left && !right) {
                dx = -dp;
            } else if(right && !left) {
                dx = dp;
            }
            ship.setVelocity(dx, dy);
        }

        private void setDirection(KeyEvent e) {
            final boolean state;
            switch (e.getID()) {
                case KeyEvent.KEY_PRESSED: state = true; break;
                case KeyEvent.KEY_RELEASED: state = false; break;
                default: return;
            }
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                case KeyEvent.VK_KP_UP:
                    up = state;
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_KP_DOWN:
                    down = state;
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_KP_LEFT:
                    left = state;
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_KP_RIGHT:
                    right = state;
                    break;
                case KeyEvent.VK_SPACE:
                    shoot = state;
                    break;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            setVelocity(e);
            makeLaser(e);
        }
        @Override
        public void keyReleased(KeyEvent e) {
            setVelocity(e);
            makeLaser(e);
        }
	};
}
