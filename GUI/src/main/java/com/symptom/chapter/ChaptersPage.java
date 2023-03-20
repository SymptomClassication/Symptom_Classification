package com.symptom.chapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.symptom.menu.MenuPage;
import com.symptom.model.Chapter;
import com.symptom.subchapter.SubchaptersPage;

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

public class ChaptersPage extends com.symptom.model.GUI implements ActionListener {

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JButton add = new JButton("Add");
    private JButton update = new JButton("Update");
    private JButton subchapters = new JButton("Subchapters");
    private JButton back = new JButton("Back");

    private JComboBox<String> cb;
    private List<Chapter> chapterList = retrieveChapters();

    private Font fontStyle =new Font("Monospaced Bold Italic",Font.BOLD,25);
    private GridBagConstraints a = new GridBagConstraints();

    private String selectedChapter;
    private int selectedChapterId;

    public ChaptersPage(){
        createFrame();
        addComponents();
    }
    public void addComponents()
    {
        panel.add( menuPanel );
        add( panel );
        setContentPane( panel );
        a.insets = new Insets( 30,30,15,30);

        a.gridy=1;
        String[] choices = new String[chapterList.size()];
        for( int i=0; i<chapterList.size();i++){
            choices[i]=chapterList.get( i ).getName();
        }
        cb = new JComboBox<String>(choices);
        cb.setFont( fontStyle );
        cb.setPreferredSize( new Dimension(500,50) );
        cb.setVisible( true );
        menuPanel.add( cb,a);

        a.gridy=2;
        setButtonsStyle(add);
        menuPanel.add(add,a);

        a.gridy=3;
        setButtonsStyle( update );
        menuPanel.add( update,a );

        a.gridy=4;
        setButtonsStyle(subchapters);
        menuPanel.add(subchapters,a);

        a.gridy=5;
        setButtonsStyle( back );
        menuPanel.add( back,a );


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==add) {
            dispose();
            new AddChapterPage();
        }
        if(e.getSource()==update){
            dispose();
            selectedChapter=(String)cb.getSelectedItem();
            selectedChapterId=chapterList.get(cb.getSelectedIndex()).getId();
            System.out.println(selectedChapterId);
            new UpdateChapterPage(selectedChapter,selectedChapterId);
        }
        if(e.getSource()==subchapters){
            dispose();
            selectedChapter=(String)cb.getSelectedItem();
            selectedChapterId=chapterList.get(cb.getSelectedIndex()).getId();
            System.out.println(selectedChapterId);
            new SubchaptersPage(selectedChapter,selectedChapterId);
        }
        if(e.getSource()==back){
            dispose();
            new MenuPage();
        }
    }
    private static final String FETCH_CHAPTERS_API_URL = "http://dagere.comiles.eu:8098/chapters";
    @SuppressWarnings("deprecation")
    public static List<Chapter> retrieveChapters() {
        List<Chapter> chapters = new ArrayList<>();
        try {
            URL url = new URL(FETCH_CHAPTERS_API_URL);
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
                JsonElement chaptersJson = new JsonParser().parse(response.toString());
                JsonArray chaptersArray = chaptersJson.getAsJsonArray();
                for (JsonElement chapterJson : chaptersArray) {
                    JsonObject chapterObject = chapterJson.getAsJsonObject();
                    int id = chapterObject.get("id").getAsInt();
                    //System.out.println(id);
                    String name = chapterObject.get("name").getAsString();
                    Chapter chapter = new Chapter(id, name);
                    chapters.add(chapter);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while retrieving chapters: " + e.getMessage());
        }
        return chapters;
    }
}
