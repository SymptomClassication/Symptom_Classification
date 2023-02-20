import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
public class SymptomPage implements ActionListener {

    public JFrame menuFrame = new JFrame();

    public JPanel panel = new JPanel();
    public JPanel menuPanel = new JPanel(new GridBagLayout());

    public JLabel label = new JLabel("Symptoms :");
    public JLabel classification = new JLabel("Classification :");
    //public JLabel chapter = new JLabel("Chapter :");
    //public JLabel subchapter = new JLabel("Subchapter :");
    public JLabel databaseRetrieved=new JLabel("");
    public JLabel successful = new JLabel("");

    public JTextField input = new JTextField( );

    public JButton back = new JButton("Back");
    public JButton next = new JButton("Next");

    public Font fontStyle=new Font("Monospaced Bold Italic",Font.BOLD,25);
    public GridBagConstraints a = new GridBagConstraints();
    private static String CLASSIFY_SYMPTOM_API_URL = "http://dagere.comiles.eu:8090/classifiedSymptoms/classifySymptom/";
    public SymptomPage() {
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setBounds( 100, 200, 1200, 600 );
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);
        //set the general distance between components

        a.gridy=2;
        label.setFont( fontStyle );
        menuPanel.add(label,a);

        a.gridy=2;
        input.setPreferredSize( new Dimension(600,30) );
        menuPanel.add( input,a );

        a.gridy=6;
        setButtonsStyle( back );
        menuPanel.add( back,a);

        a.gridy=6;
        setButtonsStyle( next );
        menuPanel.add( next,a);

        panel.add( menuPanel );
        menuFrame.add( panel );
        menuFrame.setContentPane( panel );

        menuFrame.setVisible(true);
        menuFrame.setResizable( false );

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
        if(e.getSource()==back) {
            menuFrame.dispose();
            new MenuPage();
        }
        if(e.getSource()==next){
            a.gridy=3;
            classification.setFont( fontStyle );
            menuPanel.add(classification,a);
            menuPanel.remove( databaseRetrieved );
            menuPanel.remove( successful );
            //databaseRetrieved = new JLabel(result.__getitem__(0).toString());
            //might have to change .equals("") --> .equals("unknown")
            if(!input.getText().isEmpty())
            {
                try 
                {
                    String[] myInput = input.getText().split(" ");
                    for(String a : myInput)
                    {
                        CLASSIFY_SYMPTOM_API_URL += a +"%20";
                    }
                    databaseRetrieved.setText(classifySymptom());
                    successful=new JLabel("Chapter Found");
                    successful.setForeground( Color.green );
                    databaseRetrieved.setFont( fontStyle );
                    menuPanel.add(databaseRetrieved,a);
                } 
                catch (IOException e1) 
                {
                    e1.printStackTrace();
                }
            }
            CLASSIFY_SYMPTOM_API_URL = "http://dagere.comiles.eu:8090/classifiedSymptoms/classifySymptom/";
            /* if(databaseRetrieved.getText().equals("")){
                successful= new JLabel("Chapter Not Found",SwingConstants.CENTER);
                successful.setForeground( Color.red );
                try {
                    classifySymptom(input.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else{
                successful=new JLabel("Chapter Found");
                successful.setForeground( Color.green );

            } */


/*                a.gridy=4;
                subchapter.setFont( fontStyle );
                menuPanel.add(subchapter,a);*/

            a.gridy=5;
            successful.setFont( fontStyle );
            menuPanel.add( successful,a );

            menuFrame.setVisible( true );
            menuFrame.setResizable( false );
        }
    }
    private final Gson gson = new GsonBuilder().create();
    public static String classifySymptom() throws IOException
    {
        StringBuilder result = new StringBuilder();
        URL url = new URL(CLASSIFY_SYMPTOM_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        for (String line; (line = reader.readLine()) != null;)
        {
            result.append(line);
        }
        //System.out.println(url.toString());
        //System.out.println(result.toString());
        return result.toString();
    }
}
