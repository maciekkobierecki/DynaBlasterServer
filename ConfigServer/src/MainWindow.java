import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;

public class MainWindow extends JFrame implements ActionListener, ServerStatusListener{

	private JButton startButton;
	private JLabel infoLabel, portLabel, amountMapsLabel;
	private JTextField portField;
	private JSpinner amountMaps;
	private Boolean isRunning;
	private static int amountOfMaps;
	ConfigServer server;
	MainWindow(){
		super("Dyna Blaster Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(300,150));
		infoLabel=new JLabel("not running");
		amountMapsLabel=new JLabel("amount of maps:");
		amountMaps=new JSpinner();
		((DefaultEditor) amountMaps.getEditor()).getTextField().setEditable(false);
		server=null;
		isRunning=false;
		startButton=new JButton("start");
		startButton.addActionListener(this);
		portField=new JTextField();
		portField.setPreferredSize(new Dimension(100,30));
		this.setLayout(new GridLayout(3,2));
		portLabel=new JLabel();
		portLabel.setText("port:");
		portLabel.setHorizontalAlignment(JLabel.CENTER);
		infoLabel.setHorizontalAlignment(JLabel.CENTER);
		add(portLabel);
		add(portField);
		add(amountMapsLabel);
		add(amountMaps);
		add(infoLabel);
		add(startButton,0,5);
		pack();
		setVisible(true);
	}
	public static int getAmountOfMaps(){
		return amountOfMaps;
	}
	public void turnON(){
		if(!isRunning){
			server= new ConfigServer(Integer.parseInt(portField.getText()));
			server.addListener(this);
			updateStatus("running");
			Thread thread=new Thread(server);	
			thread.start();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		amountOfMaps=(Integer)amountMaps.getValue();
		turnON();
		
	}

	@Override
	public void updateStatus(String str) {
		infoLabel.setText(str);
		if(str=="running")
			isRunning=true;
		else isRunning=false;
	}

	
}
