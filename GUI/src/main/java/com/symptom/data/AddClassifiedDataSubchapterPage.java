package com.symptom.data;

import com.google.gson.*;
import com.symptom.chapter.ChaptersPage;
import com.symptom.model.Subchapter;
import com.symptom.subchapter.AddSubchapterPage;
import com.symptom.subchapter.UpdateSubchaptersPage;

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

public class AddClassifiedDataSubchapterPage implements ActionListener{
    private JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JLabel chapter;

    private JButton add = new JButton("Add");
    private JButton back = new JButton("Back");

    private JComboBox<String> cbSubchapters;
    private java.util.List<Subchapter> subchapterList;

    private Font fontStyle =new Font("Monospaced Bold Italic",Font.BOLD,25);
    private GridBagConstraints a = new GridBagConstraints();

    private String selectedChapter;
    private int selectedChapterId;
    private String newSymptom;

    private static String FETCH_SUBCHAPTERS_API_URL = "http://dagere.comiles.eu:8098/spacychapters/subchapters/";
    private String GET_SELECTED_SUBCHAPTER_URL = "http://dagere.comiles.eu:8098/subchapters/subchapter/";
    private String chosenSubchapter;
    private int selectedSubchapterId;
    public AddClassifiedDataSubchapterPage(String selectedChapternew, int selectedChapterId ,String symptom){
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setMinimumSize(new Dimension(1200,1000));
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);

        a.gridy=1;
        this.selectedChapter=selectedChapternew;
        chapter= new JLabel(selectedChapter);
        this.selectedChapterId=selectedChapterId;
        this.newSymptom=symptom;
        chapter.setFont( fontStyle );
        menuPanel.add( chapter,a );

        a.gridy=2;
        System.out.println(this.selectedChapterId+" : "+this.selectedChapter+" = "+this.newSymptom);
        //System.out.println(selectedChapter);
        String[] result = selectedChapter.split( " " );
        for (String a : result) {
            FETCH_SUBCHAPTERS_API_URL += a + "%20";
        }
        subchapterList=retrieveSubchapters();
        String[] choices ;
        if (subchapterList.isEmpty()){
            choices= new String[1];
            choices[0]="No Available Subchapters";
        }
        else{
            choices= new String[subchapterList.size()];
        }
        //System.out.println(subchapterList.size());
        for( int i=0; i<subchapterList.size();i++){
            choices[i]=subchapterList.get( i ).getName();
        }
        cbSubchapters = new JComboBox<String>(choices);

        FETCH_SUBCHAPTERS_API_URL="http://dagere.comiles.eu:8098/spacychapters/subchapters/";
        cbSubchapters.setFont( fontStyle );
        cbSubchapters.setPreferredSize( new Dimension(500,50) );
        cbSubchapters.setVisible( true );
        menuPanel.add( cbSubchapters,a);

        a.gridy=3;
        setButtonsStyle(add);
        menuPanel.add(add,a);

        a.gridy=4;
        setButtonsStyle(back);
        menuPanel.add(back,a);

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
        }
        if(e.getSource()==back){
            menuFrame.dispose();
            new AddClassifiedDataChapterPage();
        }
    }
    @SuppressWarnings("deprecation")
    public static java.util.List<Subchapter> retrieveSubchapters() {
        List<Subchapter> subchapters = new ArrayList<>();
        try {
            URL url = new URL(FETCH_SUBCHAPTERS_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JsonElement subchaptersJson = new JsonParser().parse(response.toString());
                JsonArray subchaptersArray = subchaptersJson.getAsJsonArray();
                for (JsonElement chapterJson : subchaptersArray) {
                    JsonObject subchapterObject = chapterJson.getAsJsonObject();
                    int id = subchapterObject.get("id").getAsInt();
                    String name = subchapterObject.get("name").getAsString();
                    int chapterId = subchapterObject.get( "chapterId" ).getAsInt();
                    System.out.println(id+" "+ name +" "+chapterId);
                    Subchapter subchapter = new Subchapter(id, name,chapterId);
                    subchapters.add(subchapter);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while retrieving chapters: " + e.getMessage());
        }
        return subchapters;
    }
}

