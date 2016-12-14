// This class gives the current parsing state of the program
public class ProgramParseState {
	// Which pixel the parser is on
	public int X, Y;
	// Which direction the parser is reading in:
	// 0 - Right, 1 - Up, 2 - Left, 3 - Down
	public byte dir;

	public ProgramParseState() {
		X = Y = dir = 0;
	}

	public ProgramParseState(int x, int y, byte d) {
		X = x;
		Y = y;
		dir = d;
	}

	public ProgramParseState(ProgramParseState p) {
		this.X = p.X;
		this.Y = p.Y;
		this.dir = p.dir;
	}

	public String getPosition() {
		return String.format("(%d, %d)", X + 1, Y + 1);
	}

	public void displayState() {
		System.out.println("Position: " + getPosition());
		System.out.println("Direction: " + dir);
	}

	public final static byte DIR_RIGHT = 0;
	public final static byte DIR_UP = 1;
	public final static byte DIR_LEFT = 2;
	public final static byte DIR_DOWN = 3;
}