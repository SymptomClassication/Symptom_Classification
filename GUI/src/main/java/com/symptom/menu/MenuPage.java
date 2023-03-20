package com.symptom.menu;
import com.symptom.classification.ChooseClassificationPage;
import com.symptom.edit.EditPage;
import com.symptom.histogram.HistogramPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPage extends com.symptom.model.GUI implements ActionListener {

    //private JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JButton start = new JButton("Start");
    private JButton edit = new JButton("Edit");
    private JButton histogram = new JButton("Histogram");
    private JButton exit = new JButton("Exit");

    private JLabel title = new JLabel("Symptom Classifier");

    //to give a specific text style,size
    private GridBagConstraints a = new GridBagConstraints();
    // this line of code above, allow me to align the title and the buttons in rows and columns
    public MenuPage() {
        createFrame();
        addComponents();
    }
    public void addComponents()
    {
        panel.add( menuPanel );
        add( panel );
        setContentPane( panel );
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
        setButtonsStyle(histogram);
        menuPanel.add(histogram,a);
        a.gridy=5;
        setButtonsStyle(exit);
        menuPanel.add(exit,a);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==start) {
            // open the next window, after clicking on the "START"
            dispose();
            new ChooseClassificationPage();

        }
        if(e.getSource()==edit){
            // open the next window, after clicking on the "EDIT"
            dispose();
            new EditPage();
        }
        if(e.getSource()==histogram)
        {
            new HistogramPage();
        }
        if(e.getSource()==exit){
            // exit the whole game, end the program after clicking on the "EXIT"
            dispose();
        }
    }
}


