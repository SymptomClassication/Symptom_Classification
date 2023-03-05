import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.*;
import java.io.IOException;
public class ClassifiedDataPage extends JFrame
{
    private static JPanel mainPanel = new JPanel(new GridLayout(0, 2));
    public ClassifiedDataPage() {
        setTitle( "Symptom Classifier" );
        setBackground( Color.darkGray );
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //TODO GET DATA AS STATIC FROM SYMPTOM PAGE
        //TODO Create Labels from ClassifySymptom
        
        add(mainPanel);
        setVisible(true);
        setResizable( true );
    }
    //ALL DATA HERE NEEDS TO BE ADDED TO DATABASE
    //need an api maybe? make it more logical
    //need to keep creating labels and they only go from left to right
    public static void setChapter(String chapter)
    {
        JLabel chapterLabel = new JLabel(chapter);
        mainPanel.add(chapterLabel);
    }
    public static void setInput(String input)
    {
        JLabel inputLabel = new JLabel(input);
        mainPanel.add(inputLabel);
    }
}
