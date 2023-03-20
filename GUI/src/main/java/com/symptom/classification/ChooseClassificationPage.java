package com.symptom.classification;
import javax.swing.*;

import com.symptom.menu.MenuPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class ChooseClassificationPage implements ActionListener {

    public JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JLabel label = new JLabel("Select a classification algorithm:");

    private JButton keyWord = new JButton("Key word");
    private JButton machineL = new JButton("Smart");
    private JButton back = new JButton("Back");

    private Font fontStyle=new Font("Monospaced Bold Italic",Font.BOLD,25);
    private GridBagConstraints a = new GridBagConstraints();
    public ChooseClassificationPage() {
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setMinimumSize(new Dimension(1200,1000));
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);
        //set the general distance between components

        a.gridy=2;
        label.setFont( fontStyle );
        menuPanel.add(label,a);

        a.gridy=4;
        setButtonsStyle( keyWord );
        menuPanel.add( keyWord,a);

        a.gridy=5;
        setButtonsStyle( machineL );
        menuPanel.add( machineL,a);

        a.gridy=6;
        setButtonsStyle( back );
        menuPanel.add( back,a);

        panel.add( menuPanel );
        menuFrame.add( panel );
        menuFrame.setContentPane( panel );

        menuPanel.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20) );
        menuFrame.setVisible(true);
        menuFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        menuFrame.setResizable( true );

    }
    public void setButtonsStyle (JButton button){
        button.setFont(fontStyle);
        button.setBackground(Color.darkGray);
        button.setForeground(Color.white);
        button.setPreferredSize(new Dimension(200,60));
        button.addActionListener(this);
        button.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()== keyWord) {
            menuFrame.dispose();
            new KWSymptomPage();
        }
        if(e.getSource()== machineL){
            menuFrame.dispose();
            new MLSymptomPage();
        }
        if(e.getSource()== back){
            menuFrame.dispose();
            new MenuPage();
        }
    }
}
