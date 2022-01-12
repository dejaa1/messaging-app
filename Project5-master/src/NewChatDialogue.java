package guisss;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import basic.User;

public class NewChatDialogue extends JComponent implements Runnable {

	private JButton finalize;
	private JList<String> list;
	private JButton add;
	private NewChatDialogue chats;
	
	
	private ArrayList<String> usernames;
	private ArrayList<User> users;
	private TextField name;
	public JFrame frame;
	private ArrayList<User> copy;
	
	public NewChatDialogue(ArrayList<User> user) {
		copy =user;
	 usernames = new ArrayList<>();
	 
		for(int i=0; i<user.size(); i++) {
				usernames.add(i, user.get(i).getName());
		}
		

	}

	ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == finalize) {

			}
			if (e.getSource() == add) {
				//// This will be the selected list

				String toAdd = list.getSelectedValue();
				
				/// add to array
				boolean f = false;

				for (User s : copy) {
					if (s.getName().equals(toAdd)) {
						f = true;
					}
				}
				if (!f && toAdd != null) {
					for(User s: users) {
						if(s!=null && s.getName().equals(toAdd)) {
							users.add(s);
						}
					}
					
				}

			}

		}
	};

	@Override
	public void run() {
		 frame = new JFrame("New Chat Dialogue");

		
		
		add = new JButton("add");
		name = new TextField("added names here");

		Container content = frame.getContentPane();
		
		finalize = new JButton("FINALIZE");
		
		finalize.addActionListener(actionListener);
		
		add.addActionListener(actionListener);
		
		
		DefaultListModel<String> model = new DefaultListModel<>();
		list = new JList<>( model );
		
	
		for ( int i = 0; i < usernames.size(); i++ ){
		  model.addElement( usernames.get(i));
		}
		
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		JPanel pane = new JPanel();
		pane.add(name);
		pane.add(add);
		
		panel.add(finalize);
		
		content.add(panel, BorderLayout.NORTH);
		
		
		content.add(pane, BorderLayout.SOUTH);

		// need to import the array of users
		

		
		
		frame.add(list);

	}
	

	public static void main(String[] args) {
		ArrayList<User> pa = new ArrayList<>();
		
		String name = "lll";
		String an = "fer4";
		User lol = new User(name, name);
		User lol2 = new User(an,an);
		pa.add(lol);
		pa.add(lol2);
	
		
		SwingUtilities.invokeLater(new NewChatDialogue(pa));
	}
	

}
