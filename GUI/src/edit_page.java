import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.google.gson.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class edit_page implements ActionListener {

    public JFrame menuFrame = new JFrame();

    public JPanel panel = new JPanel();
    public JPanel menuPanel = new JPanel(new GridBagLayout());

    public JButton chapters = new JButton("Chapters");
    public JButton classified_data = new JButton("Classified Data");
    public JButton back = new JButton("Back");

    public JLabel title = new JLabel("MY APP");

    public Font buttonsFont=new Font("Monospaced Bold Italic",Font.BOLD,25);
    //to give a specific text style,size
    public GridBagConstraints a = new GridBagConstraints();
    // this line of code above, allow me to align the title and the buttons in rows and columns

    public edit_page(){
        menuFrame.setTitle("MY APP");
        menuFrame.setBackground(Color.darkGray);
        menuFrame.setBounds(100,200,1200,600);

        a.insets=new Insets(30,30,30,30);
        //set the general distance between components

        a.gridy=1;
        //vertically aligns the component
        title.setFont(new Font("Monospaced Bold Italic", Font.BOLD, 66));
        menuPanel.add(title,a);

        a.gridy=2;
        setButtonsStyle(chapters);
        menuPanel.add(chapters,a);

        a.gridy=3;
        setButtonsStyle( classified_data );
        menuPanel.add( classified_data,a );

        a.gridy=5;
        setButtonsStyle(back );
        menuPanel.add(back,a);

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
        if(e.getSource()==chapters) {
            // open the next window, after clicking on the "START"
            menuFrame.dispose();
            new chapters_page();

        }
        if(e.getSource()==classified_data){
            // open the next window, after clicking on the "START"
            menuFrame.dispose();
        }
        if(e.getSource()==back){
            // exit the whole game, end the program after clicking on the "EXIT"
            menuFrame.dispose();
            new menu_page();
        }
    }
}
