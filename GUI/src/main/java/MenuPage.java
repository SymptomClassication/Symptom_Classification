

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPage implements ActionListener {

    private JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JButton start = new JButton("Start");
    private JButton edit = new JButton("Edit");
    private JButton exit = new JButton("Exit");

    private JLabel title = new JLabel("Symptom Classifier");

    private Font buttonsFont=new Font("Monospaced Bold Italic",Font.BOLD,25);
    //to give a specific text style,size
    private GridBagConstraints a = new GridBagConstraints();
    // this line of code above, allow me to align the title and the buttons in rows and columns
    public MenuPage() {
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground(Color.darkGray);
        menuFrame.setBounds(100,200,1200,600);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets=new Insets(30,30,30,30);
        //set the general distance between components

        a.gridy=1;
        //vertically aligns the component
        title.setFont(new Font("Monospaced Bold Italic", Font.BOLD, 66));
        menuPanel.add(title,a);

        a.gridy=2;
        setButtonsStyle(start);
        menuPanel.add(start,a);

        a.gridy=3;
        setButtonsStyle( edit );
        menuPanel.add( edit,a );

        a.gridy=4;
        setButtonsStyle(exit);
        menuPanel.add(exit,a);

        panel.add( menuPanel );
        menuFrame.add( panel );
        menuFrame.setContentPane( panel );

        //menuFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //menuFrame.setUndecorated(true);
        //with this two line of codes above, the game frame turn to full screen
        //without the taskbar
        menuFrame.setVisible(true);
        menuFrame.setResizable( false );
    }
    public void setButtonsStyle (JButton button){
        button.setFont(buttonsFont);
        button.setBackground(Color.darkGray);
        button.setForeground(Color.white);
        button.setPreferredSize(new Dimension(300,60));
        button.addActionListener(this);
        button.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==start) {
            // open the next window, after clicking on the "START"
            menuFrame.dispose();
            new SymptomPage();

        }
        if(e.getSource()==edit){
            // open the next window, after clicking on the "START"
            menuFrame.dispose();
            new EditPage();
        }
        if(e.getSource()==exit){
            // exit the whole game, end the program after clicking on the "EXIT"
            menuFrame.dispose();
        }
    }
}


