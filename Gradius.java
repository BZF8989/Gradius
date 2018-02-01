import javax.swing.JFrame;
import java.awt.*;

@SuppressWarnings("serial")
public class Gradius extends JFrame {

	private final static int WIDTH = 900;
	public final static int HEIGHT = 600;

	private final GradiusComp comp;

	public Gradius() {
		setResizable(false);
		comp = new GradiusComp();
		setContentPane(comp);
	}

	public static void main(String[] args) {
		Gradius frame = new Gradius();
		frame.setSize(WIDTH, HEIGHT);
		frame.setBackground(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.comp.start();
	}
}
