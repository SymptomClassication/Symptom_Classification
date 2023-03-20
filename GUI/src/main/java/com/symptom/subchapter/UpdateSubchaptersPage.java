package com.symptom.subchapter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.symptom.menu.MenuPage;
import com.symptom.model.Subchapter;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class UpdateSubchaptersPage implements ActionListener{
    private JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JButton update = new JButton("Update");
    private JButton back = new JButton("Back");

    private JLabel newSubchapter;

    private JTextField inputNewSubchapter = new JTextField();
    private Font fontStyle =new Font("Monospaced Bold Italic",Font.BOLD,25);
    private GridBagConstraints a = new GridBagConstraints();
    private int selectedSubchapterId;
    private int selectedChapterId;
    private String selectedSubchapter;
    public UpdateSubchaptersPage(String selectedSubchapter,int selectedSubchapterId,int selectedChapterId){
        this.selectedChapterId = selectedChapterId;
        this.selectedSubchapterId = selectedSubchapterId;
        this.selectedSubchapter = selectedSubchapter;
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setMinimumSize(new Dimension(1200,1000));
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);

        a.gridy=3;
        newSubchapter=new JLabel("Change Subchapter Name ( for "+this.selectedSubchapter+") :");
        newSubchapter.setFont( fontStyle );
        menuPanel.add( newSubchapter,a);

        a.gridy=3;
        inputNewSubchapter.setPreferredSize( new Dimension(600,30) );
        menuPanel.add( inputNewSubchapter,a );

        a.gridy=5;
        setButtonsStyle( update );
        menuPanel.add( update,a );

        a.gridy=5;
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
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource()==update){
            menuFrame.dispose();
            Subchapter updatedSubChapter = new Subchapter(inputNewSubchapter.getText(),selectedChapterId);
            String json = gson.toJson(updatedSubChapter);
            System.out.println(json);
            try {
            updateSubchapters(updatedSubChapter,selectedSubchapterId);
            } catch (IOException e1) {
                e1.printStackTrace();
            } 
        }
        if(e.getSource()==back){
            menuFrame.dispose();
            new MenuPage();
        }
    }
    private final Gson gson = new GsonBuilder().create(); 
    private static String UPDATE_SUBCHAPTERS_API_URL = "http://dagere.comiles.eu:8098/subchapters/update/";
    public void updateSubchapters(Subchapter updatedSubchapter, int selectedSubchapterId) throws IOException
    {
        String json = gson.toJson(updatedSubchapter);
        UPDATE_SUBCHAPTERS_API_URL += String.valueOf(selectedSubchapterId);
        HttpPut httpPut = new HttpPut(UPDATE_SUBCHAPTERS_API_URL);
        httpPut.addHeader("Content-Type","application/json");
        httpPut.setEntity(new StringEntity(json));
        
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = httpClient.execute(httpPut);

        // parse the response JSON into a Chapter object
        HttpEntity entity = response.getEntity();
        String responseJson = EntityUtils.toString(entity);
        JsonObject jsonObject = JsonParser.parseString(responseJson).getAsJsonObject();
        Subchapter updatedSubChapterResponse = gson.fromJson(jsonObject, Subchapter.class);

        // print the updated chapter's ID and name
        System.out.println("Subchapter ID: " + updatedSubChapterResponse.getId());
        System.out.println("Subchapter Name: " + updatedSubChapterResponse.getName());
    }
}
