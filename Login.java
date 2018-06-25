import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Login extends JFrame implements ActionListener
{
	private JLabel labelName;
	private JTextField fieldName;
	private JLabel labelLastName;
	private JTextField fieldLastName;
	private JButton buttonLogIn;
	private JLabel labelPesel;
	private JTextField fieldPesel;
	
	private CreatDataBase creatDataBase;
	
		public Login()
		{
			super("ElectionCalculator");
			setSize(800,150);
			setLocation(400,200);
			setVisible(true);	
			setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			
			creatDataBase = new CreatDataBase();

			labelName = new JLabel("Name:");
				add(labelName);
				
			fieldName = new JTextField();
				fieldName.setPreferredSize(new Dimension(150,25));
				add(fieldName);
				
			labelLastName = new JLabel("Last name:");
				add(labelLastName);
				
			fieldLastName = new JTextField();
				fieldLastName.setPreferredSize(new Dimension(150,25));
				add(fieldLastName);
			
			labelPesel = new JLabel("Pesel:");
				add(labelPesel);
				
			fieldPesel = new JTextField();
				fieldPesel.setPreferredSize(new Dimension(150,25));
				add(fieldPesel);
				
			buttonLogIn = new JButton("Log In");
				buttonLogIn.setPreferredSize(new Dimension(200,30));
				buttonLogIn.addActionListener(this);
				add(buttonLogIn);
				
		}
		
		public void mainWindow(String name, String lastName, long pesel)
		{
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				MainWindow mainWindow = new MainWindow(name, lastName, pesel);
			}
		});
	}
		
		@Override
		public void actionPerformed(ActionEvent ac)
		{
			Object source = ac.getSource();
			
			if (source == buttonLogIn)
			{
				if (fieldName.getText().equals("") || fieldLastName.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Give your name and last name.");
				} else
				{	
					long longPesel = 0;
					try
					{
						longPesel = Long.parseLong(fieldPesel.getText());
					} catch (Exception e) 
					{
						JOptionPane.showMessageDialog(null, "Incorrect Pesel, try again");		// if send pesel is not a number
					}
					
					if (longPesel != 0)				// if sended pesel is correct
					{
						CheckPesel checkPesel = new CheckPesel(fieldName.getText(), fieldLastName.getText(), longPesel);	// check blocked users
						if (checkPesel.getStatus()==false)
						{
							mainWindow(fieldName.getText(), fieldLastName.getText(), longPesel);
							
							this.setVisible(false);
						}
					}
				}	
			}
		}	
}
