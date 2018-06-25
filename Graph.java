import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Graph extends JFrame
{
	private JPanel panel1;
	private JPanel panel2;
	private int scaleC=0;
	private int scaleP=0;
	
	public Graph()
	{
		super("Election Calculator");
		setSize(700,800);
		setLocation(400,0);
		setVisible(true);	
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		panel1 = new JPanel();			// candidates
			panel1.setPreferredSize(new Dimension(650, 500));
			panel1.setBackground(Color.WHITE);
			panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			panel1.setLayout(null);
			add(panel1);
			
		panel2 = new JPanel();			// parties
			panel2.setPreferredSize(new Dimension(650, 220));
			panel2.setBackground(Color.WHITE);
			panel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			panel2.setLayout(null);
			add(panel2);
		
		DataBase base = new DataBase();
	
	// ------------------------------------------------------------------------
		// candidates
		String[][] candidates = base.getAllCandidates();
		
		for (String[] x : candidates)		// scaller for graphics, max length 350
		{
			if (Integer.valueOf(x[2]) > scaleC) scaleC = Integer.valueOf(x[2]);
		}
			scaleC = 350/scaleC;
			
		JLabel labelOpis1 = new JLabel("Results of candidates");
			labelOpis1.setSize(200,30);
			labelOpis1.setLocation(220,5);
			panel1.add(labelOpis1);
		
		JLabel[] labelCand = new JLabel[candidates.length];
		JPanel[] panelCand = new JPanel[candidates.length];
				
		int i = 0;		
		for (String[] x : candidates)
		{
			labelCand[i] = new JLabel(x[0]);
			labelCand[i].setSize(180,25);
			labelCand[i].setLocation(20,40+30*i);
			panel1.add(labelCand[i]);
			
			panelCand[i] = new JPanel();
			panelCand[i].setSize(Integer.valueOf(x[2])*scaleC,20);
			panelCand[i].setBackground(new Color(255, 10, 255-(255*Integer.valueOf(x[2])*scaleC/350)));
			panelCand[i].setLocation(230,40+30*i);
			panel1.add(panelCand[i]);
			
			i++;
		}
	
	//------------------------------------------------------------------------------
		// invalid
		int invalid = base.getInvalid();
		
		JLabel labelInvalid = new JLabel("Invalid");
		labelInvalid.setSize(180,25);
		labelInvalid.setLocation(20,460);
		panel1.add(labelInvalid);
		
		JPanel panelInvalid = new JPanel();
		panelInvalid.setSize(invalid*scaleC,20);
		panelInvalid.setBackground(new Color(80,80,80));
		panelInvalid.setLocation(230,460);
		panel1.add(panelInvalid);
		
		
	
	//-----------------------------------------------------------------------------
		// parity
		i = 0;
		
		JLabel labelOpis2 = new JLabel("Results of parties");
			labelOpis2.setSize(200,30);
			labelOpis2.setLocation(220,5);
			panel2.add(labelOpis2);
		
		String[][] resultsParty = base.getResultsParty();
		
		for (String[] x : resultsParty)			// scaller for graphics, max length 350
		{
			if (Integer.valueOf(x[1]) > scaleP) scaleP = Integer.valueOf(x[1]);
		}
			scaleP = 350/scaleP;
		
		JLabel[] labelParty = new JLabel[resultsParty.length];
		JPanel[] panelParty = new JPanel[resultsParty.length];	
		
		for (String[] x : resultsParty)
		{
			labelParty[i] = new JLabel(x[0]);
			labelParty[i].setSize(180,25);
			labelParty[i].setLocation(20,40+30*i);
			panel2.add(labelParty[i]);
			
			panelParty[i] = new JPanel();
			panelParty[i].setSize(Integer.valueOf(x[1])*scaleP,20);
			panelParty[i].setBackground(new Color((255*Integer.valueOf(x[1])*scaleP/350)/2, (255*Integer.valueOf(x[1])*scaleP/350), 255));
			panelParty[i].setLocation(230,40+30*i);
			panel2.add(panelParty[i]);
			
			i++;
		}
	}
}
