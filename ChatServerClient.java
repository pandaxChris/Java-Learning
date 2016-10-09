
package chatserverclient;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author Chris Li
 * 
 * Client code
 */
public class ChatServerClient {

    /**
     * @param args the command line arguments
     */
    static final String EXIT_VALUE = "abcdefghijklmnopqrstuvwxyz1234567890";
    static JFrame frame;
    static JLabel name;
    static JLabel ip;
    static JLabel portAsk;
    static JPanel nameHolder;   //Have to place textfield in jpanel cuz they don't size properly
    static JPanel ipHolder;
    static TextField enterName;
    static TextField targetIP;
    static TextField port;
    static JPanel panel2;
    static JPanel buttonHolder;
    static JPanel portHolder;
    static JButton connect;
    static JButton close;
    
    static JPanel holder;
    static JPanel chatPanel;
    static JPanel namePanel;
    
    static JPanel userText;
    static TextField userInput;
    static TextArea chat;
    static JLabel enterKey;
    static JButton sendButton;
    static JButton exitButton;
    static JPanel buttonPanel;
    static Socket s;
    static TextUpdater update;
    static PrintStream ps;
    public static void main(String[] args) {
        /* =========================== Panel to Establish Connection with Server ==========================================*/
        // Create the frame
        frame = new JFrame("Chat Server Connect");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(400,400);
        
        //Cardlayout used to switch between panels --- namePanel and chatPanel
        holder = new JPanel();
        holder.setLayout(new CardLayout());
        //Input box for client name
        namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel,BoxLayout.Y_AXIS));
        name = new JLabel("What is your name?");
        namePanel.add(name);
        enterName = new TextField(25);
        nameHolder = new JPanel();
        nameHolder.setSize(50,50);
        nameHolder.add(enterName);
        namePanel.add(nameHolder);
        
        //Input for target ip
        ip = new JLabel("Enter your target address:");
        namePanel.add(ip);
        ipHolder = new JPanel();
        targetIP = new TextField(30);
        ipHolder.add(targetIP);
        ipHolder.setSize(50,50);
        namePanel.add(ipHolder);
        
        //Input for target ip's port
        portAsk = new JLabel("What port number is it running on?");
        portHolder = new JPanel();
        portHolder.add(portAsk);
        port = new TextField(5);
        portHolder.add(port);
        portHolder.setSize(50,50);
        namePanel.add(portHolder);
        
        //Bottom layer of buttons 
        connect= new JButton("Connect");
        close = new JButton("Close");
        close.addActionListener(new EventListener());
        connect.addActionListener(new EventListener());
        buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
        buttonHolder.add(connect);
        buttonHolder.add(close);
        namePanel.add(buttonHolder);
        
        holder.add(namePanel, "NAME");
        
        /* =========================== Do the chat panel -- Where messages are sent/received ==========================================*/
        enterKey = new JLabel("Press enter to send message");
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel,BoxLayout.Y_AXIS));
        userText = new JPanel();
        userInput = new TextField(50);
        userInput.addActionListener(new EventListener());
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        sendButton = new JButton("SEND");
        exitButton = new JButton("EXIT");
        sendButton.addActionListener(new EventListener());
        exitButton.addActionListener(new EventListener());
        buttonPanel.add(sendButton);
        buttonPanel.add(exitButton);
        userText.add(enterKey);
        userText.add(userInput);
        userText.add(buttonPanel);
        chat = new TextArea();
        chat.setEditable(false);
        chatPanel.add(chat);
        chatPanel.add(userText);
        holder.add(chatPanel, "CHAT");
        
        
        frame.add(holder);
        
        frame.setVisible(true);
    }
    
   static class EventListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            if(ae.getSource() == close){
                frame.dispose();
            }
            else if(ae.getSource() == connect){     //Done
                //If one of the fields are missing
                if(enterName.getText().trim().equals("") || targetIP.getText().trim().equals("") || port.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(namePanel, "Error: Please fill in all fields");
                }
                else{   //All fields filled
                   try{
                        s = new Socket(targetIP.getText(), Integer.parseInt(port.getText()));
                        CardLayout c = (CardLayout)(holder.getLayout());
                        c.show(holder,"CHAT");
                        ps = new PrintStream(s.getOutputStream(),true);
                        ps.print(enterName.getText() + "\n\r");
                        update = new TextUpdater(enterName.getText());
                        chat.setText("Welcome to the chat(" + s.getInetAddress() + "), " + enterName.getText() + "!");
                        update.start();
                        enterName.setText("");
                        targetIP.setText("");
                        port.setText("");
                        
                   }
                   catch(Exception e){
                       JOptionPane.showMessageDialog(namePanel, "Error: Cannot connect to the server");
                       
                   }
               }
            }
            else if(ae.getSource() == sendButton){ //Server for some reason doesn't receive message
                try{
                    ps.println(userInput.getText());
                    userInput.setText("");
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(chatPanel, "Message cannot be sent");
                }
                
            }
            else if(ae.getSource()==exitButton){   
                try{
                    ps.println(EXIT_VALUE);
                    ps.close();
                    s.close();
                    update.interrupt();
                    CardLayout c = (CardLayout)(holder.getLayout());
                    c.show(holder,"NAME");
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(chatPanel, "Failed to close socket.");
                }
                
            }
        }
         
    }
    static class TextUpdater extends Thread{
        Scanner scan;
        String name;
        TextUpdater(String n){
            name = n;
            try{
                scan = new Scanner(s.getInputStream());
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        public synchronized void run(){
            try{
                while(true){
                    String recv = scan.nextLine();
                    chat.setText(chat.getText() + "\n\r" + recv);
                }
            }
            catch(Exception e){
               //System.out.println(e);
            }
        }
    }
}




