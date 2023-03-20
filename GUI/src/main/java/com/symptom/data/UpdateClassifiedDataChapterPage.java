package com.symptom.data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.symptom.menu.MenuPage;
import com.symptom.model.Chapter;
import com.symptom.model.Subchapter;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.symptom.subchapter.SubchaptersPage;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class UpdateClassifiedDataChapterPage implements ActionListener{
    private JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JButton next = new JButton("Next");
    private JButton back = new JButton("Back");

    private JComboBox<Chapter> cb;
    private List<Chapter> chapterList = new ArrayList<>();

    private Font fontStyle =new Font("Monospaced Bold Italic",Font.BOLD,25);
    private GridBagConstraints a = new GridBagConstraints();
    private int selectedDefinitionId;
    public UpdateClassifiedDataChapterPage(int selectedDefinitionId){
        this.selectedDefinitionId = selectedDefinitionId;
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setMinimumSize(new Dimension(1200,1000));
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);
        a.gridy=1;
        chapterList = retrieveSpacyChapters();
        cb = new JComboBox<Chapter>();
        for(Chapter chapter:chapterList)
        {
            cb.addItem(chapter);
        }
        cb.setFont( fontStyle );
        cb.setPreferredSize( new Dimension(500,50) );
        cb.setVisible( true );
        menuPanel.add( cb,a);

        a.gridy=2;
        setButtonsStyle(next);
        menuPanel.add(next,a);

        a.gridy=3;
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
        if(e.getSource()==next) {
            menuFrame.dispose();
            Chapter selectedChapter = (Chapter) cb.getSelectedItem();
            System.out.println(selectedDefinitionId);
            new UpdateClassifiedDataSubchapterPage(selectedChapter.getId(),selectedDefinitionId);
        }
        if(e.getSource()==back){
            menuFrame.dispose();
            try {
                new ClassifiedDataPage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    private final String GET_SPACY_CHAPTERS_URL = "http://dagere.comiles.eu:8098/spacychapters";
    @SuppressWarnings("deprecation")
    public List<Chapter> retrieveSpacyChapters() {
        List<Chapter> chapters = new ArrayList<>();
        try {
            URL url = new URL(GET_SPACY_CHAPTERS_URL);
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
