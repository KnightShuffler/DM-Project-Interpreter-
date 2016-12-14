import java.io.IOException;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		try {
			 ImageReader img = new ImageReader("./Hello World.png");
			 Interpreter i = new Interpreter(img);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
