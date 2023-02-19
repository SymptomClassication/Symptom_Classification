import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddSubchapterPage implements ActionListener {
    public JFrame menuFrame = new JFrame();

    public JPanel panel = new JPanel();
    public JPanel menuPanel = new JPanel(new GridBagLayout());

    public JLabel newChapter = new JLabel("New Subchapter Name :");
    //public JLabel newChapterId = new JLabel("Chapter ID :");
    public JLabel successful = new JLabel("");

    public JTextField inputNewSubchapter = new JTextField( );
    //public JTextField inputNewChapterId = new JTextField( );

    public JButton back = new JButton("Back");
    public JButton next = new JButton("Next");

    public Font fontStyle=new Font("Monospaced Bold Italic",Font.BOLD,25);
    public GridBagConstraints a = new GridBagConstraints();

    public int selectedChapterId;

    public AddSubchapterPage(int selectedChapterId){
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setBounds( 100, 200, 1200, 600 );
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.selectedChapterId=selectedChapterId;
        a.insets = new Insets( 30,30,15,30);
        //set the general distance between components

        a.gridy=3;
        newChapter.setFont( fontStyle );
        menuPanel.add( newChapter,a);

        a.gridy=3;
        inputNewSubchapter.setPreferredSize( new Dimension(600,30) );
        menuPanel.add( inputNewSubchapter,a );

        a.gridy=5;
        setButtonsStyle( back );
        menuPanel.add( back,a);

        a.gridy=5;
        setButtonsStyle( next );
        menuPanel.add( next,a);

        panel.add( menuPanel );
        menuFrame.add( panel );
        menuFrame.setContentPane( panel );

        menuFrame.setVisible(true);
        menuFrame.setResizable( false );

    }
    public void setButtonsStyle (JButton button){
        button.setFont(fontStyle);
        button.setBackground(Color.darkGray);
        button.setForeground(Color.white);
        button.setPreferredSize(new Dimension(200,60));
        button.addActionListener(this);
        button.setEnabled(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==back) {
            menuFrame.dispose();
            new ChaptersPage();
        }
        if(e.getSource()==next){
            a.gridy=4;
            if(saveSubchapter( new Subchapter( inputNewSubchapter.getText(),selectedChapterId))){
                successful= new JLabel("New Subchapter Successfully Added");
                successful.setForeground( Color.green );
            }
            else{
                successful= new JLabel("Unsuccessful.../ID already exists...");
                successful.setForeground( Color.red );
            }
            successful.setFont( fontStyle );
            menuPanel.add(successful,a);
            menuFrame.setVisible( true );
            menuFrame.setResizable( false );
        }
    }
    private static final String SAVE_SUBCHAPTER_API_URL = "http://dagere.comiles.eu:8094/subchapters/create";
    public static boolean saveSubchapter(Subchapter subchapter) {
        boolean success = false;
        try {
            URL url = new URL( SAVE_SUBCHAPTER_API_URL );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            Gson gson = new Gson();
            String json = gson.toJson(subchapter);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(json);
            out.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                success = true;
            }
        } catch (Exception e) {
            System.out.println("Error while saving subchapter: " + e.getMessage());
        }
        return success;
    }
}
