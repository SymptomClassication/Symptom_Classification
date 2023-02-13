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
import java.util.List;

public class ChaptersPage implements ActionListener {
    public JFrame menuFrame = new JFrame();

    public JPanel panel = new JPanel();
    public JPanel menuPanel = new JPanel(new GridBagLayout());

    public JButton add = new JButton("Add");
    public JButton remove = new JButton("Remove");
    public JButton subchapters = new JButton("Subchapters");
    public JButton back = new JButton("Back");

    public JComboBox<String> cb;
    public List<Chapter> chapterList = retrieveChapters();

    public Font fontStyle =new Font("Monospaced Bold Italic",Font.BOLD,25);
    public GridBagConstraints a = new GridBagConstraints();

    public String selectedChapter;

    public ChaptersPage(){
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setBounds( 100, 200, 1200, 600 );
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        setButtonsStyle( remove );
        menuPanel.add( remove,a );

        a.gridy=4;
        setButtonsStyle(subchapters);
        menuPanel.add(subchapters,a);

        a.gridy=5;
        setButtonsStyle( back );
        menuPanel.add( back,a );

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
            new AddPage();
        }
        if(e.getSource()==remove){
            menuFrame.dispose();
            //..
        }
        if(e.getSource()==subchapters){
            menuFrame.dispose();
            selectedChapter=(String)cb.getSelectedItem();
            new SubchaptersPage(selectedChapter);
        }
        if(e.getSource()==back){
            menuFrame.dispose();
            new MenuPage();
        }
    }
    private static final String FETCH_CHAPTERS_API_URL = "http://dagere.comiles.eu:8090/chapters";
    @SuppressWarnings("deprecation")
    public static java.util.List<Chapter> retrieveChapters() {
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
