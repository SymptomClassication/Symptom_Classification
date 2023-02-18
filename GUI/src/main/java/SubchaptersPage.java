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
import java.util.Arrays;
import java.util.List;

public class SubchaptersPage implements ActionListener {
    public JFrame menuFrame = new JFrame();

    public JPanel panel = new JPanel();
    public JPanel menuPanel = new JPanel(new GridBagLayout());

    public JLabel chapter;

    public JButton add = new JButton("Add");
    public JButton update = new JButton("Update");
    public JButton back = new JButton("Back");

    public JComboBox<String> cb;
    public List<Subchapter> subchapterList;

    public Font fontStyle =new Font("Monospaced Bold Italic",Font.BOLD,25);
    public GridBagConstraints a = new GridBagConstraints();

    public String selectedChapter;
    //public String selectedSubchapter;

    private static String FETCH_SUBCHAPTERS_API_URL = "http://dagere.comiles.eu:8094/subchapters/chapter/";

    public SubchaptersPage(String selectedChapter ){
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setBounds( 100, 200, 1200, 600 );
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);

        a.gridy=1;
        this.selectedChapter=selectedChapter;
        chapter= new JLabel(selectedChapter);
        chapter.setFont( fontStyle );
        menuPanel.add( chapter,a );

        a.gridy=2;
        String[] result = selectedChapter.split( " " );
        for (String a : result) {
            FETCH_SUBCHAPTERS_API_URL += a + "%20";
            System.out.println( a );
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
            //System.out.println(choices[i]);
        }

        System.out.println(FETCH_SUBCHAPTERS_API_URL);
        cb = new JComboBox<String>(choices);
        FETCH_SUBCHAPTERS_API_URL="http://dagere.comiles.eu:8094/subchapters/chapter/";
        cb.setFont( fontStyle );
        cb.setPreferredSize( new Dimension(500,50) );
        cb.setVisible( true );
        menuPanel.add( cb,a);

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
        menuFrame.setResizable( false );
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
            //..
        }
        if(e.getSource()==update){
            menuFrame.dispose();
            //..
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
}
