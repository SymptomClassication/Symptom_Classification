package com.symptom.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.symptom.model.Chapter;

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

public class AddClassifiedDataChapterPage implements ActionListener {
    private JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel( new GridBagLayout() );

    private JLabel label = new JLabel( "Symptom :" );
    public JLabel chapter = new JLabel( "Select a chapter :" );

    private JTextField input = new JTextField();

    private JButton next = new JButton( "Next" );
    private JButton back = new JButton( "Back" );

    private JComboBox<String> cbChapters;
    private java.util.List<Chapter> chapterList = retrieveChapters();

    private Font fontStyle = new Font( "Monospaced Bold Italic", Font.BOLD, 25 );
    private GridBagConstraints a = new GridBagConstraints();

    private String selectedChapter;
    private int selectedChapterId;
    private String newSymptom;

    public AddClassifiedDataChapterPage() {
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setMinimumSize( new Dimension( 1200, 1000 ) );
        menuFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        a.insets = new Insets( 30, 30, 15, 30 );

        a.gridy = 2;
        label.setFont( fontStyle );
        menuPanel.add( label, a );
        input.setPreferredSize( new Dimension( 600, 30 ) );
        menuPanel.add( input, a );

        a.gridy = 3;
        chapter.setFont( fontStyle );
        menuPanel.add( chapter, a );
        String[] choices = new String[chapterList.size()];
        for (int i = 0; i < chapterList.size(); i++) {
            choices[i] = chapterList.get( i ).getName();
        }
        cbChapters = new JComboBox<String>( choices );
        cbChapters.setFont( fontStyle );
        cbChapters.setPreferredSize( new Dimension( 500, 50 ) );
        cbChapters.setVisible( true );
        menuPanel.add( cbChapters, a );

        a.gridy = 5;
        setButtonsStyle( back );
        menuPanel.add( back, a );

        setButtonsStyle( next );
        menuPanel.add( next, a );

        panel.add( menuPanel );
        menuFrame.add( panel );
        menuFrame.setContentPane( panel );

        menuFrame.setVisible( true );
        menuFrame.setExtendedState( JFrame.MAXIMIZED_BOTH );
        menuFrame.setResizable( true );
    }

    public void setButtonsStyle(JButton button) {
        button.setFont( fontStyle );
        button.setBackground( Color.darkGray );
        button.setForeground( Color.white );
        button.setPreferredSize( new Dimension( 250, 60 ) );
        button.addActionListener( this );
        button.setEnabled( true );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == next) {
            menuFrame.dispose();
            selectedChapter= (String) cbChapters.getSelectedItem();
            selectedChapterId=chapterList.get(cbChapters.getSelectedIndex()).getId();
            newSymptom=input.getText();
            new AddClassifiedDataSubchapterPage(selectedChapter,selectedChapterId,newSymptom);
        }
        if (e.getSource() == back) {
            menuFrame.dispose();
            try {
                new ClassifiedDataPage();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static final String FETCH_CHAPTERS_API_URL = "http://dagere.comiles.eu:8098/spacychapters";

    @SuppressWarnings("deprecation")
    public static java.util.List<Chapter> retrieveChapters() {
        List<Chapter> chapters = new ArrayList<>();
        try {
            URL url = new URL( FETCH_CHAPTERS_API_URL );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod( "GET" );
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append( inputLine );
                }
                in.close();
                JsonElement chaptersJson = new JsonParser().parse( response.toString() );
                JsonArray chaptersArray = chaptersJson.getAsJsonArray();
                for (JsonElement chapterJson : chaptersArray) {
                    JsonObject chapterObject = chapterJson.getAsJsonObject();
                    int id = chapterObject.get( "id" ).getAsInt();
                    //System.out.println(id);
                    String name = chapterObject.get( "name" ).getAsString();
                    Chapter chapter = new Chapter( id, name );
                    chapters.add( chapter );
                }
            }
        } catch (Exception e) {
            System.out.println( "Error while retrieving chapters: " + e.getMessage() );
        }
        return chapters;
    }
}
