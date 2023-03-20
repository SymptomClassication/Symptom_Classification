package com.symptom.data;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.symptom.edit.EditPage;
import com.symptom.model.ClassifiedSymptom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassifiedDataPage implements ActionListener {
    private JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JButton add = new JButton("Add");
    private JButton update = new JButton("Update");
    private JButton back = new JButton("Back");


    private String [] col = new String[]{"Chapter","Subchapter","Symptom"};
    private Object [][] data= getData();
    private JTable jt = new JTable(data,col);
    private JScrollPane js;

    //private JComboBox<String> cb;
    //private List<Chapter> chapterList = retrieveChapters();

    private Font fontStyle =new Font("Monospaced Bold Italic",Font.BOLD,25);
    private GridBagConstraints a = new GridBagConstraints();

    public ClassifiedDataPage() throws IOException {
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setMinimumSize(new Dimension(1500,1000));
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);

        a.gridx=0;
        a.gridy=0;
        a.gridwidth=2;
        a.gridheight=3;
        jt.getColumnModel().getColumn( 0).setPreferredWidth( 2 );
        //jt.getColumnModel().getColumn( 1).setPreferredWidth( 160 );
        //jt.getColumnModel().getColumn( 2).setPreferredWidth( 1050 );
        js= new JScrollPane(jt);
        js.setSize( 1500,800 );
        //js.setSize( 800,600 );
        menuPanel.add( js,a );

        a.gridx=2;
        a.gridy=0;
        a.gridheight=1;
        setButtonsStyle(add);
        menuPanel.add(add,a);

        a.gridx=2;
        a.gridy=1;
        a.gridheight=1;
        setButtonsStyle( update );
        menuPanel.add( update,a );

        a.gridx=2;
        a.gridy=2;
        a.gridheight=1;
        setButtonsStyle( back );
        menuPanel.add( back,a );

        panel.add( menuPanel );
        menuFrame.add( panel );
        menuFrame.setContentPane( panel );

        menuFrame.setVisible(true);
        menuFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        menuFrame.setResizable( true );
    }
    public void setButtonsStyle (JButton button){
        button.setFont( fontStyle );
        button.setBackground(Color.darkGray);
        button.setForeground(Color.white);
        button.setPreferredSize(new Dimension(250,60));
        button.addActionListener(this);
        button.setEnabled(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==add) {
            menuFrame.dispose();
            new AddClassifiedDataChapterPage();
        }
        if(e.getSource()==update)
        {
            menuFrame.dispose();
            int selectedDefinitionId = jt.getSelectedRow() + 1;//TODO FIND A BETTER SOLUTION FOR GETTING DEFINITION ID
            System.out.println(selectedDefinitionId);
            new UpdateClassifiedDataChapterPage(selectedDefinitionId);
        }
        if(e.getSource()==back) {
            menuFrame.dispose();
            new EditPage();
        }
    }
    private static final String FETCH_CLASSIFIED_DATA_API_URL = "http://dagere.comiles.eu:8098/trainings/definitions";
    @SuppressWarnings("deprecation")
    public static Object[][] getData() throws IOException {
        List<ClassifiedSymptom> results = new ArrayList<>();
        Object[][]dataArray;
        StringBuilder result = new StringBuilder();
        URL url = new URL(FETCH_CLASSIFIED_DATA_API_URL);
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
            String symptom;
            try{
                chapterName = resultObject.get("chapterName").getAsString();
                subchapterName = resultObject.get("subchapterName").getAsString();
                symptom = resultObject.get( "symptom" ).getAsString();
                ClassifiedSymptom data = new ClassifiedSymptom( chapterName,subchapterName,symptom );
                results.add(data);
                //System.out.println(results);
            }catch(Exception e){
                dataArray= new Object[1][1];
                String invalid= resultObject.get( "result" ).getAsString();
                ClassifiedSymptom data = new ClassifiedSymptom( invalid );
                //System.out.println("invalid ="+invalid);
                results.add(data);
                dataArray[0][0]=invalid;
                return dataArray;
            }
            //System.out.println(chapterName+":"+subchapterName);
        }
        dataArray = new Object[results.size()][3];
        for (int i =0; i<results.size();i++){
            dataArray[i][0]=results.get(i).getChapter();
            dataArray[i][1]=results.get(i).getSubchapter();
            dataArray[i][2]=results.get(i).getSymptom();
        }
        return dataArray;
    }
}
