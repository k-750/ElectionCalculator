import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame implements ActionListener
{
	private String name;
	private String lastName;
	private long pesel; 
	
	private JLabel labelName;
	private JButton buttonLogOut;
	
	private JPanel panelVote;
	private JPanel panelResults;
	private JLabel labelChoose;
	private JPanel panelChoise;
	private JButton buttonVote;
	private JButton buttonYes;
	private JButton buttonNo;
	private JScrollPane tableSc;
	private JTable tableResults;
	private JScrollPane tableScParty;
	private JTable tableResultsParty;
	private JLabel labelInvalid;
	private JButton buttonGraphics;
	private JButton buttonSave;
	
	private String[][] candidate;
	private int numberOfCandidates;
	private Checkbox[] choise;
	int invalid=0;
	String chosen =	null;
	
	private DataBase base;

	public MainWindow(String name, String lastName, long pesel)
	{
		super("Election Calculator");
		setSize(1000,700);
		setLocation(200,50);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);	
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		this.name = name;
		this.lastName = lastName;
		this.pesel = pesel;
		
		base = new DataBase();
		numberOfCandidates = base.numberOfCandidates();
	
	//-----------------------------------------------------------------------------	
		labelName = new JLabel("Welcome "+name+" "+lastName);
			add(labelName);
			
		buttonLogOut = new JButton("Log out");
			buttonLogOut.setPreferredSize(new Dimension(200,20));
			buttonLogOut.addActionListener(this);
			add(buttonLogOut);
			buttonLogOut.setVisible(true);
	
	//-----------------------------------------------------------------------------		
		panelVote = new JPanel();
			panelVote.setPreferredSize(new Dimension(950, 300));
			panelVote.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			add(panelVote);
			
		labelChoose = new JLabel("Choose a candidate:");
			panelVote.add(labelChoose);
			
		panelChoise = new JPanel();
			panelChoise.setPreferredSize(new Dimension(930, 180));
			panelChoise.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
			panelVote.add(panelChoise);
			
		buttonVote = new JButton("Vote");
			buttonVote.addActionListener(this);
			panelVote.add(buttonVote);
			buttonVote.setVisible(true);
			
		buttonYes = new JButton("YES");
			buttonYes.addActionListener(this);
			panelVote.add(buttonYes);
			buttonYes.setVisible(false);	
			
		buttonNo = new JButton("NO");
			buttonNo.addActionListener(this);
			panelVote.add(buttonNo);
			buttonNo.setVisible(false);	
			
	//--------------------------------------------------------------------------		
		panelResults = new JPanel();
			panelResults.setPreferredSize(new Dimension(950, 320));
			panelResults.setBackground(Color.WHITE);
			panelResults.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			panelResults.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
			add(panelResults);
			
			tableResults = new JTable();
			tableSc = new JScrollPane(tableResults);
			choise = new Checkbox[numberOfCandidates];
			tableResultsParty = new JTable();
			tableScParty = new JScrollPane(tableResultsParty);
			labelInvalid = new JLabel();
			buttonGraphics = new JButton("Show graphs");
			buttonSave = new JButton("Save results");
						
			candidate = base.getAllCandidates(); // name, party and value
			
			choise = new Checkbox[numberOfCandidates];
				for (int i = 0; i<numberOfCandidates; i++)
				{
					choise[i] = new Checkbox(candidate[i][0]+", "+candidate[i][1]);
					panelChoise.add(choise[i]);
				}
	}
	
	public void results()
	{
		buttonYes.setVisible(false);
		buttonNo.setVisible(false);
		labelChoose.setText("");
		tableSc.setVisible(true);
		tableScParty.setVisible(true);
		labelInvalid.setVisible(true);
		buttonGraphics.setVisible(true);
		buttonSave.setVisible(true);
		
	//-------------------------------------------------------- 
	//candidats results
		String[][] candidates = base.getAllCandidates();
		String[] tableColumns = {"Candidate", "Party", "Number of votes"};
		tableResults.setModel(new DefaultTableModel(candidates,tableColumns));
		tableResults.getColumnModel().getColumn(0).setPreferredWidth(140);
		tableResults.getColumnModel().getColumn(1).setPreferredWidth(140);
		tableSc.setPreferredSize(new Dimension(530,240));
		panelResults.add(tableSc);
		
	//---------------------------------------------------------
	// party results
		String[][] partyResults = base.getResultsParty();		// object to JTable
		String[] tableColumnsParty = {"Party", "Number of votes"};
		tableResultsParty.setModel(new DefaultTableModel(partyResults,tableColumnsParty));
		tableResultsParty.getColumnModel().getColumn(0).setPreferredWidth(120);
		tableScParty.setPreferredSize(new Dimension(300,110));
		panelResults.add(tableScParty);
		
	//---------------------------------------------------------
	// invalid
		labelInvalid.setText("Invalit votes: "+base.getInvalid());
		panelResults.add(labelInvalid);
		
	//--------------------------------------------------------------
		
			buttonGraphics.addActionListener(this);
			panelResults.add(buttonGraphics);
			buttonSave.addActionListener(this);
			panelResults.add(buttonSave);
	}
	
	@Override
	public void actionPerformed(ActionEvent ac)
	{
		Object source = ac.getSource();
			
		if (source == buttonLogOut)
		{
			this.setVisible(false);
			new Login();
		}
		
		if (source == buttonVote)
		{
			buttonVote.setVisible(false);
			panelChoise.setVisible(false);
			invalid=0;
			for (int i = 0; i<numberOfCandidates; i++)	// check number of chosen
			{
				if (choise[i].getState())
				{
					chosen = choise[i].getLabel();
					invalid++;
				}
			}
			if (invalid == 1) 	// chosen 1 candidate
			{
				labelChoose.setText("You voted for "+chosen+". Are you sure?");
				buttonYes.setVisible(true);
				buttonNo.setVisible(true);
			}
			else 				// chosen 0 or 2 or more candidates
			{
				labelChoose.setText("You cast your vote invalid. Are you sure?");
				buttonYes.setVisible(true);
				buttonNo.setVisible(true);
			}
		}
		
		if (source == buttonNo)		// Are you sure? No
		{
			buttonVote.setVisible(true);
			panelChoise.setVisible(true);
			buttonYes.setVisible(false);
			buttonNo.setVisible(false);
			labelChoose.setText("Choose a candidate:");
		}
		
		if (source == buttonYes)		// Are you sure? Yes
		{
			if (invalid == 1)			// vote ok
			{
				base.addVote(chosen);
				base.addUser(name, lastName, pesel);
				results();				
			}
			else					// vote invalid
			{
				base.addInvalid();
				base.addUser(name, lastName, pesel);
				results();
			}
		}
		
		if (source == buttonGraphics)
		{
			new Graph();
		}
		
		if (source == buttonSave)
		{
			new Save();
		}
	}
}
