package com.symptom.classification;
import com.google.gson.*;
import com.symptom.model.ClassifiedSymptom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MLSymptomPage implements ActionListener {

    public JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JLabel label = new JLabel("Symptom :");
    private JLabel classification = new JLabel("Classification :");
    public JLabel chapter = new JLabel("Chapter :");
    public JLabel subchapter = new JLabel("Subchapter :");
    private static JLabel databaseRetrieved=new JLabel("");
    private JLabel successful = new JLabel("");
    private JTextField input = new JTextField( );

    private JButton back = new JButton("Back");
    private JButton next = new JButton("Next");
    private JButton retrain = new JButton("Retrain");
    List<ClassifiedSymptom> results = new ArrayList<>();

    private Font fontStyle=new Font("Monospaced Bold Italic",Font.BOLD,25);
    private GridBagConstraints a = new GridBagConstraints();
    private static String CLASSIFY_SYMPTOM_API_URL = "http://dagere.comiles.eu:8090/spacy/classifySymptom/";
    private static String[] myInput;
    public MLSymptomPage() {
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setMinimumSize(new Dimension(1200,1000));
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

        a.gridy=7;
        setButtonsStyle(retrain);
        menuPanel.add(retrain,a);

        panel.add( menuPanel );
        menuFrame.add( panel );
        menuFrame.setContentPane( panel );

        //menuPanel.setBorder( BorderFactory.createEmptyBorder(20, 100, 20, 100) );
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
        if(e.getSource()==back) {
            menuFrame.dispose();
            new ChooseClassificationPage();
        }
        if(e.getSource()==next){
            a.gridy=3;
            String unknown = "[{\"result\":\"0\"}]";
            classification.setFont( fontStyle );
            menuPanel.add(classification,a);
            menuPanel.remove( databaseRetrieved );
            menuPanel.remove( successful );
            //databaseRetrieved = new JLabel(result.__getitem__(0).toString());
            //might have to change .equals("") --> .equals("unknown")
            try
            {
                myInput = input.getText().split(" ");
                //com.symptom.data.ClassifiedDataPage.setInput(input.getText());
                for(String a : myInput)
                {
                    CLASSIFY_SYMPTOM_API_URL += a +"%20";
                }
                String results = "<html>";
                //results=classifySymptom();
                for (int i=0;i<classifySymptom().size();i++){
                    results+="<br/>"+classifySymptom().get( i ).getChapter() +" ( "+classifySymptom().get( i ).getSubchapter()+" )";
                }
                results+="</html>";
                databaseRetrieved.setText( results );
                System.out.println(results);
                //System.out.println(results.get( 0 ).getSubchapter()); //debugging
                if(!databaseRetrieved.getText().equals(unknown))
                {
                    //com.symptom.data.ClassifiedDataPage.setChapter(databaseRetrieved.getText());
                    successful=new JLabel("Chapter Found");
                    successful.setForeground( Color.green );
                    databaseRetrieved.setFont( fontStyle );
                    menuPanel.add(databaseRetrieved,a);
                }
                if(databaseRetrieved.getText().equals(unknown))
                {
                    successful = new JLabel("Chapter Not Found", SwingConstants.CENTER);
                    successful.setForeground(Color.red);
                }
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            a.gridy=5;
            successful.setFont( fontStyle );
            menuPanel.add( successful,a );

            //menuPanel.setBorder( BorderFactory.createEmptyBorder(40, 100, 40, 100) );
            menuFrame.setVisible( true );
            menuFrame.setResizable( true );

            CLASSIFY_SYMPTOM_API_URL = "http://dagere.comiles.eu:8090/spacy/classifySymptom/";

        }
    }
    @SuppressWarnings("deprecation")
    public static List<ClassifiedSymptom> classifySymptom() throws IOException
    {
        List<ClassifiedSymptom> results = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        URL url = new URL(CLASSIFY_SYMPTOM_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        //Gson gson = new Gson();
        for (String line; (line = reader.readLine()) != null;)
        {
            result.append(line);
        }
        reader.close();
        JsonElement resultsJson = new JsonParser().parse(result.toString());
        JsonArray resultArray = resultsJson.getAsJsonArray();
        for (JsonElement resultJson : resultArray) {
            JsonObject resultObject = resultJson.getAsJsonObject();
            String chapterName;
            String subchapterName;
            try{
                chapterName = resultObject.get("ChapterName").getAsString();
                subchapterName = resultObject.get("SubchapterName").getAsString();
                ClassifiedSymptom data = new ClassifiedSymptom( chapterName,subchapterName );
                results.add(data);
                System.out.println(chapterName);
            }catch(Exception e){
                    String invalid= resultObject.get( "result" ).getAsString();
                    ClassifiedSymptom data = new ClassifiedSymptom( invalid );
                    results.add(data);
            }
            //System.out.println(chapterName+":"+subchapterName);
        }
        return results;
    }
}
