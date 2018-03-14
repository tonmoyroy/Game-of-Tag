// BailiffFrame.java
// Fredrik Kilander, DSV
// 18-nov-2004/FK Adapted for PIS course.
// 2001-03-28/FK First version

package dsv.pis.gotag.bailiff;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/**
 * This class creates a rudimentary GUI for a Bailiff instance by wrapping a
 * JFrame around it and presenting a simple menu structure. The purpose is to
 * make the Bailiff visible and to provide an easy way to shut it down.
 */
public class BailiffFrame extends JFrame {

	/**
	 * The Bailiff service instance we front a GUI for.
	 */
	protected Bailiff bf;

	private JPanel textPanel, inputPanel;
	private JTextField textField;
	private String name, message;
	private Font meiryoFont = new Font("Meiryo", Font.PLAIN, 14);
	private Border blankBorder = BorderFactory.createEmptyBorder(10, 10, 20, 10);// top,r,b,l
	private JList<String> list;
	private DefaultListModel<String> listModel;

	protected JTextArea textArea, userArea;
	protected JFrame frame, frame2;
	protected JButton privateMsgButton, anyPlayer, anyAgent, anyIT;
	protected JPanel clientPanel, userPanel;

	/**
	 * Creates a new Bailiff service GUI.
	 * 
	 * @param managedBf
	 *            The Bailiff service instance we manage a GUI for.
	 */
	public BailiffFrame(Bailiff managedBf) {

		frame = new JFrame("Bailiff:: Room - " + managedBf.getRoom());
		frame2 = new JFrame("Client Chat Console");

		Container c = frame2.getContentPane();
		JPanel outerPanel = new JPanel(new BorderLayout());

		// outerPanel.add(getInputPanel(), BorderLayout.CENTER);
		outerPanel.add(getTextPanel(), BorderLayout.NORTH);

		c.setLayout(new BorderLayout());
		c.add(outerPanel, BorderLayout.CENTER);
		c.add(getUsersPanel(), BorderLayout.WEST);

		frame.add(c);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setLocation(150, 150);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);

		anyAgent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				textArea.append("Migrated Agent Key: \n");
				for (int i = 1; i <= managedBf.tagCounter; i++) {
					String K = "agent" + String.valueOf(i);
					if(managedBf.getProperty(K)!=null)
					textArea.append( managedBf.getProperty(K)+ "\n");
				}

			}
		});

		anyPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				textArea.append("Total Agents in Bailiff: " + managedBf.agentCount + "\n");
			}
		});

		anyIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				if(managedBf.getProperty("itStatus").isEmpty()) {
					textArea.append("There is no IT!\n");
				}else {
					textArea.append("There is a IT: \n");
					textArea.append(managedBf.getProperty("itStatus") + "\n");
				}
			}
		});

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(frame, "Are you sure to close this window?", "Really Closing?",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					managedBf.shutdown();
					System.exit(0);
				}
			}
		});

	}

	public JPanel getTextPanel() {
		// String welcome = "Welcome enter your name and press Start to begin\n";
		textArea = new JTextArea("", 14, 34);
		textArea.setMargin(new Insets(10, 10, 10, 10));
		textArea.setFont(meiryoFont);
		textArea.setForeground(Color.RED);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		textPanel = new JPanel();
		textPanel.add(scrollPane);

		textPanel.setFont(new Font("Meiryo", Font.PLAIN, 14));
		return textPanel;
	}

	/**
	 * Method to build the panel with input field
	 * 
	 * @return inputPanel
	 */
	public JPanel getInputPanel() {
		inputPanel = new JPanel(new GridLayout(1, 1, 5, 5));
		inputPanel.setBorder(blankBorder);
		textField = new JTextField();
		textField.setFont(meiryoFont);
		textField.setText("Enter Your Name");
		inputPanel.add(textField);
		return inputPanel;
	}

	public JPanel getUsersPanel() {

		userPanel = new JPanel(new BorderLayout());
		userPanel.add(makeButtonPanel(), BorderLayout.SOUTH);
		userPanel.setBorder(blankBorder);

		return userPanel;
	}

	public void setClientPanel(String[] currClients) {
		clientPanel = new JPanel(new BorderLayout());
		listModel = new DefaultListModel<String>();

		for (String s : currClients) {
			listModel.addElement(s);
		}
		if (currClients.length > 1) {
			privateMsgButton.setEnabled(true);
		}

		// Create the list and put it in a scroll pane.
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setVisibleRowCount(8);
		list.setFont(meiryoFont);
		JScrollPane listScrollPane = new JScrollPane(list);

		clientPanel.add(listScrollPane, BorderLayout.CENTER);
		userPanel.add(clientPanel, BorderLayout.CENTER);
	}

	/**
	 * Make the buttons and add the listener
	 * 
	 * @return
	 */
	public JPanel makeButtonPanel() {
		anyAgent = new JButton("Is it the Bailiff the agent is currently in?");
		anyPlayer = new JButton("Are there any players in it?");
		anyIT = new JButton("Is any of the players in the Bailiff 'it'?");

		JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(anyAgent);
		buttonPanel.add(anyPlayer);
		buttonPanel.add(anyIT);

		return buttonPanel;
	}

	/**
	 * The 'about' dialog.
	 */
	public void showAboutDialog() {
		// Note that a new thread is created here to run the dialogue.
		// That way control returns at once to the caller, while the user
		// interacts with the dialogue. This ok since its just a read-only
		// information box.
		new Thread(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(null, bf.toString(), "Bailiff information",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}).start();
	}

}
