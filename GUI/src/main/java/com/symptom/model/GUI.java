package com.symptom.model;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;

public abstract class GUI extends JFrame {
    private Font buttonsFont=new Font("Monospaced Bold Italic",Font.BOLD,25);

    public void createFrame()
    {
        setTitle( "Symptom Classifier" );
        setBackground(Color.darkGray);
        //menuFrame.setBounds(100,200,1200,600);
        setMinimumSize(new Dimension(1200,1000));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable( true );
    }
    public void setButtonsStyle (JButton button)
    {
        button.setFont(buttonsFont);
        button.setBackground(Color.darkGray);
        button.setForeground(Color.white);
        button.setPreferredSize(new Dimension(300,60));
        button.addActionListener((ActionListener) this);
        button.setEnabled(true);
    }
}
