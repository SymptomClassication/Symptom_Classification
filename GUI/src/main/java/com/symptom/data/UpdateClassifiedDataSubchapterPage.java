package com.symptom.data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.symptom.chapter.ChaptersPage;
import com.symptom.model.ClassifiedSubchapter;
import com.symptom.model.Subchapter;

import javax.swing.*;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jfree.data.json.impl.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateClassifiedDataSubchapterPage implements ActionListener{
    private JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JLabel chapter;

    private JButton update = new JButton("Update");
    private JButton back = new JButton("Back");

    private JComboBox<ClassifiedSubchapter> cb;
    private List<ClassifiedSubchapter> subchapterList;

    private Font fontStyle =new Font("Monospaced Bold Italic",Font.BOLD,25);
    private GridBagConstraints a = new GridBagConstraints();

    private String selectedChapter;
    private int selectedChapterId;


    private static String FETCH_SPACY_SUBCHAPTERS_API_URL = "http://dagere.comiles.eu:8098/spacychapters/subchapters/";
    private String UPDATE_DEFINITION_API_URL = "http://dagere.comiles.eu:8098/trainings/update/";
    private int selectedDefinitionId;
    private final Gson gson = new GsonBuilder().create();
    public UpdateClassifiedDataSubchapterPage(int selectedChapterId, int selectedDefinitionId){
        this.selectedChapterId=selectedChapterId;
        this.selectedDefinitionId = selectedDefinitionId;
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setMinimumSize(new Dimension(1200,1000));
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);

        a.gridy=1;
        chapter= new JLabel(selectedChapter);
        
        chapter.setFont( fontStyle );
        menuPanel.add( chapter,a );

        a.gridy=2;
        FETCH_SPACY_SUBCHAPTERS_API_URL += selectedChapterId;
        cb = new JComboBox<ClassifiedSubchapter>();
        subchapterList=retrieveSubchapters();
        if (subchapterList.isEmpty()){
            cb.addItem(new ClassifiedSubchapter("No available subchapters"));
        }
        for(ClassifiedSubchapter subchapter : subchapterList)
        {
            cb.addItem(subchapter);
        }

        FETCH_SPACY_SUBCHAPTERS_API_URL="http://dagere.comiles.eu:8098/spacychapters/subchapters/";
        cb.setFont( fontStyle );
        cb.setPreferredSize( new Dimension(500,50) );
        cb.setVisible( true );
        menuPanel.add( cb,a);


        a.gridy=3;
        setButtonsStyle( update );
        menuPanel.add( update,a );

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
        if(e.getSource()==update){
            menuFrame.dispose();
            ClassifiedSubchapter selectedSubchapter = (ClassifiedSubchapter) cb.getSelectedItem();
            Map<String, Integer> data = new HashMap<>();
            if(subchapterList.isEmpty())
            {
                selectedSubchapter.setLabel(selectedChapterId);
            }
            data.put("label",selectedSubchapter.getLabel());
            String json = gson.toJson(data);
            System.out.println(json);
            try {
                updateDefinition(json);
            } catch (IOException e1) {
                e1.printStackTrace();
            } 
            try {
                new ClassifiedDataPage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
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
    @SuppressWarnings("deprecation")
    public static List<ClassifiedSubchapter> retrieveSubchapters() {
        List<ClassifiedSubchapter> subchapters = new ArrayList<>();
        try {
            URL url = new URL(FETCH_SPACY_SUBCHAPTERS_API_URL);
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
                    int label = subchapterObject.get("id").getAsInt();
                    String name = subchapterObject.get("name").getAsString();
                    int chapterId = subchapterObject.get( "chapterId" ).getAsInt();
                    ClassifiedSubchapter subchapter = new ClassifiedSubchapter(label, name,chapterId);
                    subchapters.add(subchapter);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while retrieving chapters: " + e.getMessage());
        }
        return subchapters;
    }
    
    public void updateDefinition(String json) throws ClientProtocolException, IOException
    {
        UPDATE_DEFINITION_API_URL += String.valueOf(selectedDefinitionId);
        HttpPut httpPut = new HttpPut(UPDATE_DEFINITION_API_URL);
        httpPut.addHeader("Content-Type","application/json");
        httpPut.setEntity(new StringEntity(json));
        System.out.println(json);
        System.out.println(httpPut);
        
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = httpClient.execute(httpPut);

        // parse the response JSON into a Chapter object
        HttpEntity entity = response.getEntity();
        String responseJson = EntityUtils.toString(entity);
        JsonObject jsonObject = JsonParser.parseString(responseJson).getAsJsonObject();
        ClassifiedSubchapter updatedSubChapterResponse = gson.fromJson(jsonObject, ClassifiedSubchapter.class); 

/*         // Create the HttpURLConnection instance
        URL url = new URL(UPDATE_DEFINITION_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method to PUT
        connection.setRequestMethod("PUT");

        // Set the Content-Type header to application/json
        connection.setRequestProperty("Content-Type", "application/json");

        // Set the JSON payload as the request body
        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(json.getBytes());
        outputStream.flush();
        System.out.println(outputStream);
        outputStream.close();

        // Send the request and get the response code
        int responseCode = connection.getResponseCode();

        System.out.println("Response Code : " + responseCode); */
    }
}
