public class Main {
	public static void main(String[] args) {
//		FileInputGUI fgui = new FileInputGUI();
//		fgui.setVisible(true);
		
		try {
			ImageReader img = new ImageReader(args[0]);
			Interpreter i = new Interpreter(img);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
