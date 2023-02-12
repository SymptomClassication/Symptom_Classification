import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddPage implements ActionListener {
    public JFrame menuFrame = new JFrame();

    public JPanel panel = new JPanel();
    public JPanel menuPanel = new JPanel(new GridBagLayout());

    public JLabel newChapter = new JLabel("New Chapter Name :");
    public JLabel newChapterId = new JLabel("New Chapter ID :");
    public JLabel successful = new JLabel("");

    public JTextField inputNewChapter = new JTextField( );
    public JTextField inputNewChapterId = new JTextField( );

    public JButton back = new JButton("Back");
    public JButton next = new JButton("Next");

    public Font fontStyle=new Font("Monospaced Bold Italic",Font.BOLD,25);
    public GridBagConstraints a = new GridBagConstraints();

    public AddPage(){
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setBounds( 100, 200, 1200, 600 );
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);
        //set the general distance between components

//        a.gridy=2;
//        newChapterId.setFont( fontStyle );
//        menuPanel.add( newChapterId,a);

//        a.gridy=2;
//        inputNewChapterId.setPreferredSize( new Dimension(600,30) );
//        menuPanel.add( inputNewChapterId,a );

        a.gridy=3;
        newChapter.setFont( fontStyle );
        menuPanel.add( newChapter,a);

        a.gridy=3;
        inputNewChapter.setPreferredSize( new Dimension(600,30) );
        menuPanel.add( inputNewChapter,a );

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
            System.out.println(saveChapter( new Chapter(inputNewChapter.getText())));
            if(saveChapter( new Chapter(inputNewChapter.getText()))){
                successful= new JLabel("New Chapter Successfully Added");
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
    private static final String SAVE_CHAPTER_API_URL = "http://dagere.comiles.eu:8090/api/v1/chapters/createChapter";
    public static boolean saveChapter(Chapter chapter) {
        boolean success = false;
        try {
            URL url = new URL(SAVE_CHAPTER_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            Gson gson = new Gson();
            String json = gson.toJson(chapter);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(json);
            out.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                success = true;
            }
        } catch (Exception e) {
            System.out.println("Error while saving chapter: " + e.getMessage());
        }
        return success;
    }
}
