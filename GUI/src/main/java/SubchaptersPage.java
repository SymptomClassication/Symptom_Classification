import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubchaptersPage implements ActionListener {
    public JFrame menuFrame = new JFrame();

    public JPanel panel = new JPanel();
    public JPanel menuPanel = new JPanel(new GridBagLayout());

    public JLabel chapter;

    public JButton add = new JButton("Add");
    public JButton remove = new JButton("Remove");
    public JButton back = new JButton("Back");


    public Font fontStyle =new Font("Monospaced Bold Italic",Font.BOLD,25);
    public GridBagConstraints a = new GridBagConstraints();

    public String selectedSubchapter;

    public SubchaptersPage(String selectedChapter ){
        menuFrame.setTitle( "Symptom Classifier" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setBounds( 100, 200, 1200, 600 );
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.insets = new Insets( 30,30,15,30);

        a.gridy=1;
        chapter= new JLabel(selectedChapter);
        chapter.setFont( fontStyle );
        menuPanel.add( chapter,a );

        a.gridy=2;
        String[] choices = { "SUBCHAPTERS","CHOICE 1","CHOICE 2", "CHOICE 3","CHOICE 4","CHOICE 5"};
        JComboBox<String> cb = new JComboBox<String>(choices);
        selectedSubchapter=(String)cb.getSelectedItem();
        cb.setFont( fontStyle );
        cb.setPreferredSize( new Dimension(500,50) );
        cb.setVisible( true );
        menuPanel.add( cb,a);

        a.gridy=3;
        setButtonsStyle(add);
        menuPanel.add(add,a);

        a.gridy=4;
        setButtonsStyle( remove );
        menuPanel.add( remove,a );

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
        if(e.getSource()==remove){
            menuFrame.dispose();
            //..
        }
        if(e.getSource()==back){
            menuFrame.dispose();
            new ChaptersPage();
        }
    }
}
