import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import com.csvreader.CsvWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class Save extends JFrame implements ActionListener
{
	private JButton buttonCsv;
	private JButton buttonPdf;
	private JLabel labelName;
	private JTextField fieldName;
	
	public Save()
	{
		super("Election Calculator");
		setSize(400,180);
		setLocation(500,300);
		setVisible(true);	
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				
		labelName = new JLabel("File path:");
			add(labelName);
			
		fieldName = new JTextField();
			fieldName.setPreferredSize(new Dimension(190,25));
			add(fieldName);
			
		buttonCsv = new JButton("Save as CSV");
			buttonCsv.setSize(new Dimension(150,25));
			buttonCsv.addActionListener(this);
			add(buttonCsv);
			
		buttonPdf = new JButton("Save as PDF");
			buttonPdf.setSize(new Dimension(150,25));
			buttonPdf.addActionListener(this);
			add(buttonPdf);
	}
	
	private void creatPdf(String name)
	{
		DataBase base = new DataBase();
		String[][] candidates = base.getAllCandidates();
		String[][] resultsParty = base.getResultsParty();
		int invalid = base.getInvalid();
		int blocked = base.getBlockedLogin();
		int valid = 0;
		for (String[] x : resultsParty) valid += Integer.valueOf(x[1]);
		
			Document document = new Document(PageSize.A4, 40, 40, 40, 40);
			try
			{
				PdfWriter.getInstance(document,	new FileOutputStream(name+".pdf"));
			} catch (FileNotFoundException e)
			{
				JOptionPane.showMessageDialog(null, e);
				e.printStackTrace();
			} catch (DocumentException e)
			{
				JOptionPane.showMessageDialog(null, e);
				e.printStackTrace();
			}
			
			document.open();
			
				Paragraph paragraph1 = new Paragraph();
					paragraph1.add(new Chunk("Nymbers of valid votes: "+valid+".\n"));
					paragraph1.add(new Chunk("Number of invalid votes: "+invalid+".\n"));
					paragraph1.add(new Chunk("Attempts to vote without rights: "+blocked+"."));
				
				Paragraph paragraph2 = new Paragraph();
					paragraph2.add(new Chunk("\nResults of candidates:\n"));
					
					for (String[] x : candidates)
					{
						paragraph2.add(new Chunk("Candidate: "+x[0]+", party: "+x[1]+", number of votes: "+x[2]+".\n"));
					}
					
				Paragraph paragraph3 = new Paragraph();
					paragraph3.add(new Chunk("\nResults of parties:\n"));
					
					for (String[] x : resultsParty)
					{
						paragraph3.add(new Chunk("Party: "+x[0]+", number of votes: "+x[1]+".\n"));
					}
					
			try
			{
				document.add(paragraph1);
				document.add(paragraph2);
				document.add(paragraph3);
			} catch (DocumentException e)
			{
				JOptionPane.showMessageDialog(null, e);
				e.printStackTrace();
			}	
			
			document.close();
	}
	
	private void creatCsv(String name)
	{
		DataBase base = new DataBase();
		String[][] candidates = base.getAllCandidates();
		String[][] resultsParty = base.getResultsParty();
		int invalid = base.getInvalid();
		int blocked = base.getBlockedLogin();
		int valid = 0;
			for (String[] x : resultsParty) valid += Integer.valueOf(x[1]);
		try {
			
			File file = new File(name+".csv");
			
			CsvWriter csvOutput = new CsvWriter(new FileWriter(file), ',');
			
			csvOutput.write("Nymbers of valid votes");
			csvOutput.write(String.valueOf(valid));
			
			csvOutput.endRecord();
			csvOutput.write("Number of invalid votes");
			csvOutput.write(String.valueOf(invalid));
			csvOutput.endRecord();
			
			csvOutput.write("Attempts to vote without rights");
			csvOutput.write(String.valueOf(blocked));
			csvOutput.endRecord();
			csvOutput.endRecord();
			
			csvOutput.write("Results of candidates");
			csvOutput.endRecord();
			
				csvOutput.write("Name");
				csvOutput.write("Party");
				csvOutput.write("Number of votes");
				csvOutput.endRecord();
				
			for (String[] x : candidates)
			{
				for (String y : x)
				{
				csvOutput.write(y);
				}
				csvOutput.endRecord();
			}

			csvOutput.endRecord();
			csvOutput.write("Results of parties");
			csvOutput.endRecord();
			
			csvOutput.write("Party");
			csvOutput.write("Number of votes");
			csvOutput.endRecord();	
			
			for (String[] x : resultsParty)
			{
				for (String y : x)
				{
					csvOutput.write(y);
				}
				csvOutput.endRecord();
			}
			
			csvOutput.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		
		if (source == buttonCsv)
		{
			creatCsv(fieldName.getText());
			setVisible(false);
		}
		
		if (source == buttonPdf)
		{
			creatPdf(fieldName.getText());
			setVisible(false);
		}
		
	}

}
