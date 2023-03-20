package com.symptom.subchapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.symptom.chapter.ChaptersPage;
import com.symptom.model.Subchapter;

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

public class SubchaptersPage implements ActionListener {
    private JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JLabel chapter;

    private JButton add = new JButton("Add");
    private JButton update = new JButton("Update");
    private JButton back = new JButton("Back");

    private JComboBox<String> cbSubchapters;
    private List<Subchapter> subchapterList;

    private Font fontStyle =new Font("Monospaced Bold Italic",Font.BOLD,25);
    private GridBagConstraints a = new GridBagConstraints();

    private String selectedChapter;
    private int selectedChapterId;


    private static String FETCH_SUBCHAPTERS_API_URL = "http://dagere.comiles.eu:8098/subchapters/chapter/";
    private String GET_SELECTED_SUBCHAPTER_URL = "http://dagere.comiles.eu:8098/subchapters/subchapter/";
    private String chosenSubchapter;
    private int selectedSubchapterId;
    public SubchaptersPage(String selectedChapternew, int selectedChapterId ){
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setMinimumSize(new Dimension(1200,1000));
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);

        a.gridy=1;
        this.selectedChapter=selectedChapternew;
        chapter= new JLabel(selectedChapter);
        this.selectedChapterId=selectedChapterId;
        chapter.setFont( fontStyle );
        menuPanel.add( chapter,a );

        a.gridy=2;
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
        for( int i=0; i<subchapterList.size();i++){
            choices[i]=subchapterList.get( i ).getName();
        }
        cbSubchapters = new JComboBox<String>(choices);

        FETCH_SUBCHAPTERS_API_URL="http://dagere.comiles.eu:8098/subchapters/chapter/";
        cbSubchapters.setFont( fontStyle );
        cbSubchapters.setPreferredSize( new Dimension(500,50) );
        cbSubchapters.setVisible( true );
        menuPanel.add( cbSubchapters,a);

        a.gridy=3;
        setButtonsStyle(add);
        menuPanel.add(add,a);

        a.gridy=4;
        setButtonsStyle( update );
        menuPanel.add( update,a );

        a.gridy=5;
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
            new AddSubchapterPage(selectedChapterId);
        }
        if(e.getSource()==update){
            menuFrame.dispose();
            chosenSubchapter = (String) cbSubchapters.getSelectedItem();
            String[] formatSubchapter = chosenSubchapter.split(" ");
            for(String a : formatSubchapter)
            {
                GET_SELECTED_SUBCHAPTER_URL += a + "%20";
            }
            selectedSubchapterId = getSubchapter().getId();
            GET_SELECTED_SUBCHAPTER_URL = "http://dagere.comiles.eu:8098/subchapters/subchapter/";
            new UpdateSubchaptersPage(chosenSubchapter,selectedSubchapterId,selectedChapterId);
        }
        if(e.getSource()==back){
            menuFrame.dispose();

            new ChaptersPage();
        }
    }
    @SuppressWarnings("deprecation")
    public static List<Subchapter> retrieveSubchapters() {
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

    public Subchapter getSubchapter()
    {
        
        try {
            URL url = new URL(GET_SELECTED_SUBCHAPTER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) 
                {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                Subchapter subchapter = gson.fromJson(response.toString(), Subchapter.class);
                return subchapter;
            } 
            else
            {
                System.out.println("error");
                System.out.println(responseCode);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
}
