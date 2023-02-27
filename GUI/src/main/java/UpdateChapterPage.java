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
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
public class UpdateChapterPage implements ActionListener {
    private JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JLabel newChapter;

    private JTextField inputNewChapter = new JTextField( );

    private JButton update = new JButton("Update");
    private JButton back = new JButton("Back");

    private Font fontStyle =new Font("Monospaced Bold Italic",Font.BOLD,25);
    private GridBagConstraints a = new GridBagConstraints();

    private String selectedChapter = "";
    private int selectedChapterId;
    private final Gson gson = new GsonBuilder().create();
    private static String UPDATE_CHAPTERS_API_URL = "http://dagere.comiles.eu:8090/chapters/update/";

    public UpdateChapterPage(String selectedChapternew, int selectedChapterId){
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setBounds( 100, 200, 1200, 600 );
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.selectedChapter=selectedChapternew;
        this.selectedChapterId=selectedChapterId;
        a.insets = new Insets( 30,30,15,30);

        a.gridy=3;
        newChapter=new JLabel("Change Chapter Name ( for "+selectedChapter+") :");
        newChapter.setFont( fontStyle );
        menuPanel.add( newChapter,a);

        a.gridy=3;
        inputNewChapter.setPreferredSize( new Dimension(600,30) );
        menuPanel.add( inputNewChapter,a );

        a.gridy=5;
        setButtonsStyle( back );
        menuPanel.add( back,a );

        a.gridy=5;
        setButtonsStyle( update );
        menuPanel.add( update,a );

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
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource()==update){
            menuFrame.dispose();
            System.out.println(selectedChapterId +" "+ inputNewChapter.getText());
            try {
                updateChapters(selectedChapterId, new Chapter( inputNewChapter.getText() ));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if(e.getSource()==back){
            menuFrame.dispose();
            new MenuPage();
        }
    }
    
    public void updateChapters(int chapterId, Chapter updatedChapter) throws IOException
    {
        String json = gson.toJson(updatedChapter);
        UPDATE_CHAPTERS_API_URL += String.valueOf(chapterId);
        System.out.println(UPDATE_CHAPTERS_API_URL);
        HttpPut httpPut = new HttpPut(UPDATE_CHAPTERS_API_URL);
        httpPut.addHeader("Content-Type","application/json");
        httpPut.setEntity(new StringEntity(json));

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = httpClient.execute(httpPut);

        // parse the response JSON into a Chapter object
        HttpEntity entity = response.getEntity();
        String responseJson = EntityUtils.toString(entity);
        JsonObject jsonObject = JsonParser.parseString(responseJson).getAsJsonObject();
        Chapter updatedChapterResponse = gson.fromJson(jsonObject, Chapter.class);

        // print the updated chapter's ID and name
        System.out.println("Chapter ID: " + updatedChapterResponse.getId());
        System.out.println("Chapter Name: " + updatedChapterResponse.getName());
    }
}