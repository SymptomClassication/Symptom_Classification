import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
public class SymptomPage implements ActionListener {

    public JFrame menuFrame = new JFrame();

    private JPanel panel = new JPanel();
    private JPanel menuPanel = new JPanel(new GridBagLayout());

    private JLabel label = new JLabel("Symptoms :");
    private JLabel classification = new JLabel("Classification :");
    //public JLabel chapter = new JLabel("Chapter :");
    //public JLabel subchapter = new JLabel("Subchapter :");
    private static JLabel databaseRetrieved=new JLabel("");
    private JLabel successful = new JLabel("");

    private JTextField input = new JTextField( );

    private JButton back = new JButton("Back");
    private JButton next = new JButton("Next");

    private Font fontStyle=new Font("Monospaced Bold Italic",Font.BOLD,25);
    private GridBagConstraints a = new GridBagConstraints();
    private static String CLASSIFY_SYMPTOM_API_URL = "http://dagere.comiles.eu:8090/classifiedSymptoms/classifySymptom/";
    private static String[] myInput;
    public SymptomPage() {
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setBounds( 100, 200, 1200, 600 );
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);
        //set the general distance between components

        a.gridy=2;
        label.setFont( fontStyle );
        menuPanel.add(label,a);

        a.gridy=2;
        input.setPreferredSize( new Dimension(600,30) );
        menuPanel.add( input,a );

        a.gridy=6;
        setButtonsStyle( back );
        menuPanel.add( back,a);

        a.gridy=6;
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
    //TESTING STUFF START
    public static String getInput()
    {
        return myInput.toString();
    }
    public static String getChapters()
    {
        return databaseRetrieved.getText();
    }
    //TESTING STUFF END
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==back) {
            menuFrame.dispose();
            new MenuPage();
        }
        if(e.getSource()==next){
            a.gridy=3;
            String unknown = "[{\"result\":\"0\"}]";
            System.out.println(unknown);
            classification.setFont( fontStyle );
            menuPanel.add(classification,a);
            menuPanel.remove( databaseRetrieved );
            menuPanel.remove( successful );
            //databaseRetrieved = new JLabel(result.__getitem__(0).toString());
            //might have to change .equals("") --> .equals("unknown")
            try 
            {
                myInput = input.getText().split(" ");
                ClassifiedDataPage.setInput(input.getText());
                for(String a : myInput)
                {
                    CLASSIFY_SYMPTOM_API_URL += a +"%20";
                }
                databaseRetrieved.setText(classifySymptom());
                System.out.println(databaseRetrieved.getText());
                if(!databaseRetrieved.getText().equals(unknown))
                {
                    ClassifiedDataPage.setChapter(databaseRetrieved.getText());
                    successful=new JLabel("Chapter Found");
                    successful.setForeground( Color.green );
                    databaseRetrieved.setFont( fontStyle );
                    menuPanel.add(databaseRetrieved,a);
                }
                if(databaseRetrieved.getText().equals(unknown))
                {
                    successful = new JLabel("Chapter Not Found", SwingConstants.CENTER);
                    successful.setForeground(Color.red);
                }
            } 
            catch (IOException e1) 
            {
                e1.printStackTrace();
            }

            CLASSIFY_SYMPTOM_API_URL = "http://dagere.comiles.eu:8090/classifiedSymptoms/classifySymptom/";

            a.gridy=5;
            successful.setFont( fontStyle );
            menuPanel.add( successful,a );

            menuFrame.setVisible( true );
            menuFrame.setResizable( true );
        }
    }
    public static String classifySymptom() throws IOException
    {
        StringBuilder result = new StringBuilder();
        URL url = new URL(CLASSIFY_SYMPTOM_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        for (String line; (line = reader.readLine()) != null;)
        {
            result.append(line);
        }
        //System.out.println(url.toString());
        //System.out.println(result.toString());
        return result.toString();
    }
}
