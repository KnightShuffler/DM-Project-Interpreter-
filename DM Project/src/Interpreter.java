import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;


public class Interpreter {
	// The coordinates of the pixel of the image that the program parser is on
	// and the direction of parsing
	private ProgramParseState programState;

	// The tape on which the program runs
	private byte[] tape;

	// The location of the tape head
	private int head;

	// The accumulator bytes
	private byte AC, MQ;

	// The return addresses for the blocks
	private Stack<ProgramParseState> blockAddresses;

	// Used for taking input from the console
	private BufferedReader br;
	private StringBuilder consoleInput;

	// Dimensions of the image
	private int width, height;

	// The instructions' pixel values:
	public final static int LFT = 0x550000;
	public final static int RGHT = 0x555500;
	public final static int INC = 0x005500;
	public final static int DEC = 0x005555;
	public final static int ADD = 0x000055;
	public final static int SUB = 0x2a0055;
	public final static int MUL = 0x550055;

	public final static int DIV = 0x920000;
	public final static int AND = 0x929200;
	public final static int OR = 0x009200;
	public final static int NOT = 0x009292;
	public final static int XOR = 0x000092;
	public final static int LAC = 0x490092;
	public final static int SAC = 0x920092;

	public final static int LMQ = 0xff0000;
	public final static int SMQ = 0xffff00;
	public final static int BSTART = 0x00ff00;
	public final static int BEND = 0x00ffff;
	public final static int RCW = 0x0000ff;
	public final static int RCCW = 0x7f00ff;
	public final static int END = 0xff00ff;

	public final static int IN = 0xffffff;
	public final static int OUT = 0x000000;
	public final static int COMMENT = 0x929292;

	public Interpreter(ImageReader img) throws IOException {
		programState = new ProgramParseState();
		tape = new byte[30000];
		head = 0;
		AC = 0;
		MQ = 0;
		blockAddresses = new Stack<>();
		br = new BufferedReader(new InputStreamReader(System.in));
		consoleInput = new StringBuilder();

		width = img.width;
		height = img.height;

		runInterpreter(img.pixels);
	}

	private void runInterpreter(int[][] pixels) throws IOException {
		boolean done = false;
		boolean debug = false;
		StringBuilder consoleOutput = new StringBuilder();
		// Used for debugging step by step
		String l;
		while (!done) {
			if (debug)
				l = br.readLine();

			int currentPixel = pixels[programState.Y][programState.X] & 0x00ffffff;
			if (debug)
				programState.displayState();
			switch (currentPixel) {
			case LFT:
				head--;
				updateProgramCoordinates();
				if (debug)
					System.out.println("LFT");
				break;

			case RGHT:
				head++;
				updateProgramCoordinates();
				if (debug)
					System.out.println("RGHT");
				break;

			case INC:
				tape[head]++;
				updateProgramCoordinates();
				if (debug)
					System.out.println("INC");
				break;

			case DEC:
				tape[head]--;
				updateProgramCoordinates();
				if (debug)
					System.out.println("DEC");
				break;

			case ADD:
				AC += tape[head];
				updateProgramCoordinates();
				if (debug)
					System.out.println("ADD");
				break;

			case SUB:
				AC -= tape[head];
				updateProgramCoordinates();
				if (debug)
					System.out.println("SUB");
				break;

			case MUL:
				short res = (short) (((short) AC & (short) 0xff) * ((short) tape[head] & (short) 0xff));
				AC = (byte) ((res & 0xff00) >> 8);
				MQ = (byte) (res & 0x00ff);

				updateProgramCoordinates();
				if (debug)
					System.out.println("MUL");
				break;

			case DIV:
				MQ = (byte) (((int) AC & 0xff) % ((int) tape[head] & 0xff));
				AC = (byte) (((int) AC & 0xff) / ((int) tape[head] & 0xff));

				updateProgramCoordinates();
				if (debug)
					System.out.println("DIV");
				break;

			case AND:
				AC &= tape[head];
				updateProgramCoordinates();
				if (debug)
					System.out.println("AND");
				break;

			case OR:
				AC |= tape[head];
				updateProgramCoordinates();
				if (debug)
					System.out.println("OR");
				break;

			case NOT:
				AC = (byte) ~tape[head];
				updateProgramCoordinates();
				if (debug)
					System.out.println("NOT");
				break;

			case XOR:
				AC ^= tape[head];
				updateProgramCoordinates();
				if (debug)
					System.out.println("XOR");
				break;

			case LAC:
				AC = tape[head];
				updateProgramCoordinates();
				if (debug)
					System.out.println("LAC");
				break;

			case SAC:
				tape[head] = AC;
				updateProgramCoordinates();
				if (debug)
					System.out.println("SAC");
				break;

			case LMQ:
				MQ = tape[head];
				updateProgramCoordinates();
				if (debug)
					System.out.println("LMQ");
				break;

			case SMQ:
				tape[head] = MQ;
				updateProgramCoordinates();
				if (debug)
					System.out.println("SMQ");
				break;

			case BSTART:
				if (tape[head] != 0) {
					updateProgramCoordinates();
					blockAddresses.push(new ProgramParseState(programState));
				} else {
					while (currentPixel != BEND) {
						updateProgramCoordinates();
					}
					updateProgramCoordinates();
				}
				if (debug)
					System.out.println("BSTART");
				break;

			case BEND:
				if (tape[head] == 0) {
					blockAddresses.pop();
					updateProgramCoordinates();
				} else {
					programState = new ProgramParseState(blockAddresses.peek());
				}
				if (debug)
					System.out.println("BEND");
				break;

			case RCW:
				programState.dir = (byte) (programState.dir == 0 ? 3 : programState.dir - 1);
				updateProgramCoordinates();
				if (debug)
					System.out.println("RCW");
				break;

			case RCCW:
				programState.dir = (byte) ((programState.dir + 1) % 4);
				updateProgramCoordinates();
				if (debug)
					System.out.println("RCCW");
				break;

			case IN:
				if (debug)
					System.out.println("IN");
				if (consoleInput.length() == 0) {
					consoleInput.append(br.readLine());
				}

				tape[head] = (byte) consoleInput.charAt(0);
				consoleInput.deleteCharAt(0);
				updateProgramCoordinates();
				break;

			case OUT:
				if (debug) {
					System.out.print("OUT ");
					System.out.println((char) (tape[head] & 0xff));
				} else {
					System.out.print((char) (tape[head] & 0xff));
				}
				updateProgramCoordinates();

				break;

			case END:
				done = true;
				if (debug)
					System.out.println("END");
				break;

			case COMMENT:
				if (debug)
					System.out.println("COMMENT");
				break;

			default:
				throw new IllegalStateException(String.format("Illegal color %06x in the file at position %s",
						pixels[programState.X][programState.Y], programState.getPosition()));
			}
			if (debug)
				this.debug();
		}
		// System.out.println();
		// this.debug();
	}

	private void debug() {
		System.out.println("head: " + head);
		System.out.println("AC: " + (AC & 0xff));
		System.out.println("MQ: " + (MQ & 0xff));
		for (int i = 0; i <= 13; i++) {
			System.out.println("C" + i + ": " + (tape[i] & 0xff));
		}
		System.out.println();
	}

	private void updateProgramCoordinates() {
		switch (programState.dir) {
		case ProgramParseState.DIR_RIGHT:
			programState.X = (programState.X + 1) % width;
			break;

		case ProgramParseState.DIR_UP:
			programState.Y = programState.Y == 0 ? height - 1 : programState.Y - 1;
			break;

		case ProgramParseState.DIR_LEFT:
			programState.X = programState.X == 0 ? width - 1 : programState.X - 1;
			break;

		case ProgramParseState.DIR_DOWN:
			programState.Y = (programState.Y + 1) % height;
			break;

		default:
			break;
		}
	}
}
