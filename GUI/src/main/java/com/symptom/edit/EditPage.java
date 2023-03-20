package com.symptom.edit;
import javax.swing.*;

import com.symptom.chapter.ChaptersPage;
import com.symptom.data.ClassifiedDataPage;
import com.symptom.menu.MenuPage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EditPage implements ActionListener {

    private JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JButton chapters = new JButton("Chapters");
    private JButton classifiedData = new JButton("Classified Data");
    private JButton back = new JButton("Back");

    //private JLabel title = new JLabel("MY APP");

    private Font buttonsFont=new Font("Monospaced Bold Italic",Font.BOLD,25);
    //to give a specific text style,size
    private GridBagConstraints a = new GridBagConstraints();
    // this line of code above, allow me to align the title and the buttons in rows and columns

    public EditPage(){
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground(Color.darkGray);
        menuFrame.setMinimumSize(new Dimension(1200,1000));
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets=new Insets(30,30,30,30);
        //set the general distance between components

        //a.gridy=1;
        //vertically aligns the component
        //title.setFont(new Font("Monospaced Bold Italic", Font.BOLD, 66));
        //menuPanel.add(title,a);

        a.gridy=2;
        setButtonsStyle(chapters);
        menuPanel.add(chapters,a);

        a.gridy=3;
        setButtonsStyle( classifiedData );
        menuPanel.add( classifiedData,a );

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
        menuFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        menuFrame.setResizable( true );
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
            new ChaptersPage();

        }
        if(e.getSource()==classifiedData){
            // open the next window, after clicking on the "START"
            menuFrame.dispose();
            try {
                new ClassifiedDataPage();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(e.getSource()==back){
            // exit the whole game, end the program after clicking on the "EXIT"
            menuFrame.dispose();
            new MenuPage();
        }
    }
}
