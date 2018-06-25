import java.awt.EventQueue;

public class ElectionCalculator
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				Login login = new Login();
			}
		});
	}
}
