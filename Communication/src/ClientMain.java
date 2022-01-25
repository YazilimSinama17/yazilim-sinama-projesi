import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class ClientMain extends Thread{
	// projenin uygulama ekraný 
	public static JFrame mainframe=new JFrame();//creating instance of JFrame  
    public static JPanel mainpanel = new JPanel();
    public static JPanel messagepanel = new JPanel();
    public static JPanel filepanel = new JPanel();
    public static CardLayout crd = new CardLayout();
    public static JTextArea areaPlainText=new JTextArea("");

    
    public void run() {
    	try {
			Client.startCommunication();
		} catch (IOException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public static void main() {
		
		/*Menu bar burada yer almakta*/
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem messagemenu = new JMenuItem("Message Transfer");
		JMenuItem filemenu = new JMenuItem("File Transfer");
		menu.add(messagemenu);
		menu.add(filemenu);
		menubar.add(menu);
		mainframe.setJMenuBar(menubar);
		
		messagemenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				crd.show(mainpanel, "message");
			}
		});
		
		filemenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				crd.show(mainpanel, "file");
			}
		});
		/*Menu bar*/
		
		ClientMain thread = new ClientMain();
		thread.start();
		
		/*GUI*/
		mainpanel.setLayout(crd);
        mainpanel.setSize(800,800);
        mainpanel.setBackground(Color.cyan);
        
        /*Message panel*/
        messagepanel.setSize(800,600);
        messagepanel.setLayout(new BoxLayout(messagepanel, BoxLayout.Y_AXIS));
        JPanel messageformpanel = new JPanel();
        
        /*Plain Panel*/
        JPanel plainpanel = new JPanel();
        plainpanel.setSize(400,600);
        messageformpanel.add(plainpanel);
        
        messageformpanel.setLayout(new BoxLayout(messageformpanel, BoxLayout.Y_AXIS));
        messageformpanel.setSize(400,600);
        
        ButtonGroup grpCrypted = new ButtonGroup();

        areaPlainText.setColumns(20);
        areaPlainText.setRows(10);
        
        JLabel lblSHA = new JLabel("SHA");
        JRadioButton rdbtnSHA = new JRadioButton();
        grpCrypted.add(rdbtnSHA);
        plainpanel.add(lblSHA);
        plainpanel.add(rdbtnSHA);
        
        JLabel lblSPN = new JLabel("SPN");
        JRadioButton rdbtnSPN = new JRadioButton();
        grpCrypted.add(rdbtnSPN);
        plainpanel.add(lblSPN);
        plainpanel.add(rdbtnSPN);
        
        JLabel lblPlainText = new JLabel("Plain Text");
        plainpanel.add(lblPlainText);
        plainpanel.add(areaPlainText);
        
        messageformpanel.add(plainpanel);
        /*Plain Panel*/
        
        /*Cipher Text*/
        JPanel cipherpanel = new JPanel();
        cipherpanel.setSize(400,600);
        cipherpanel.setBackground(Color.red);
        
        JPanel cipherformpanel = new JPanel();
        cipherformpanel.setSize(400,600);
        
        JLabel lblCipherText = new JLabel("Cipher Text");
        
        JTextArea areaCipherText =new JTextArea("");
        areaCipherText.setColumns(20);
        areaCipherText.setRows(10);
        
        JButton btnSend = new JButton("Decode");
        cipherformpanel.add(lblCipherText);
        cipherformpanel.add(areaCipherText);
        cipherformpanel.add(btnSend);
        
        btnSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i<Server.messages.size(); i++) {
					if(rdbtnSHA.isSelected() || rdbtnSPN.isSelected()) {						
						areaCipherText.append(Server.messages.get(i) + "\n");
					}else {
						JOptionPane.showMessageDialog(null, "Please select a decode method.");
					}
					System.out.println(Server.messages.get(i));
				}
			}
		});
        
        /*Cipher Text*/
        
        messagepanel.add(messageformpanel);
        messagepanel.add(cipherformpanel);
        /*Message panel*/
        
        /*File panel*/
        filepanel.setSize(800,600);
        filepanel.setBackground(Color.blue);
        /*File panel*/
        
        /*Screens*/
        mainpanel.add(messagepanel,"message");
        mainpanel.add(filepanel,"file");
        /*Screens*/
        
        crd.show(mainpanel, "message");
        
		mainframe.add(mainpanel);
		mainframe.setTitle("Client");
		mainframe.setSize(800,600);//400 width and 500 height  
		mainframe.setLayout(null);//using no layout managers  
		mainframe.setVisible(true);//making the frame visible
		/*GUI*/
	}
}
