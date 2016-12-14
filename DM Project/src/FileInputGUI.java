
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileInputGUI extends JFrame {

	private JPanel contentPane;
	JFileChooser chooser;
	public JTextField filePath; // path of image file

	/**
	 * Create the frame.
	 */
	public FileInputGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				chooser = new JFileChooser();

				// filter file types
				// here only png is accepted
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Image", "png");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(FileInputGUI.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					filePath.setText(chooser.getSelectedFile().getPath());
				}

			}
		});

		JButton btnGo = new JButton("GO");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					ImageReader img = new ImageReader("./Hello World.png");
					Interpreter i = new Interpreter(img);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JLabel lblChooseAnInput = new JLabel("Choose an Input file");

		filePath = new JTextField();
		filePath.setEditable(false);
		filePath.setColumns(10);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(filePath, GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
										.addGap(18).addComponent(btnBrowse).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnGo).addGap(24))
								.addGroup(gl_contentPane
										.createSequentialGroup().addComponent(lblChooseAnInput,
												GroupLayout.PREFERRED_SIZE, 219, GroupLayout.PREFERRED_SIZE)
										.addContainerGap(295, Short.MAX_VALUE)))));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(lblChooseAnInput, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(btnBrowse)
								.addComponent(filePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnGo))
						.addContainerGap(91, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);
	}
}
