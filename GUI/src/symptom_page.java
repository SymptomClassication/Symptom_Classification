import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class symptom_page implements ActionListener {
    PythonInterpreter interpreter = new PythonInterpreter();

    public JFrame menuFrame = new JFrame();

    public JPanel panel = new JPanel();
    public JPanel menuPanel = new JPanel(new GridBagLayout());

    public JLabel label = new JLabel("Symptoms :");
    public JLabel chapter = new JLabel("Chapter :");
    public JLabel subchapter = new JLabel("Subchapter :");
    public JLabel databaseRetrieved=new JLabel("");
    public JLabel successful = new JLabel("");

    public JTextField input = new JTextField( );

    public JButton back = new JButton("Back");
    public JButton next = new JButton("Next");

    public Font fontStyle=new Font("Monospaced Bold Italic",Font.BOLD,25);
    public GridBagConstraints a = new GridBagConstraints();

    public symptom_page() {

        menuFrame.setTitle( "MY APP" );
        menuFrame.setBackground( Color.darkGray );
        menuFrame.setBounds( 100, 200, 1200, 600 );

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
        @Override
    public void actionPerformed(ActionEvent e) 
    {
            if(e.getSource()==back) {
                menuFrame.dispose();
                new menu_page();
            }
            if(e.getSource()==next){

                interpreter.execfile("../classifyInput/test.py");
                String test = "result = pipeline("+"'"+input.getText()+"'"+")";
                interpreter.exec(test);
                PyObject result = interpreter.get("result");
                a.gridy=3;
                chapter.setFont( fontStyle );
                menuPanel.add(chapter,a);

                menuPanel.remove( databaseRetrieved );
                menuPanel.remove( successful );
                databaseRetrieved = new JLabel(result.__getitem__(0).toString());
                if(databaseRetrieved.getText().equals("")){
                    successful= new JLabel("Chapter Not Found");
                    successful.setForeground( Color.red );
                }
                else{
                    successful=new JLabel("Chapter Found");
                    successful.setForeground( Color.green );
                }
                databaseRetrieved.setFont( fontStyle );
                menuPanel.add(databaseRetrieved,a);

                a.gridy=4;
                subchapter.setFont( fontStyle );
                menuPanel.add(subchapter,a);

                a.gridy=5;
                successful.setFont( fontStyle );
                menuPanel.add( successful,a );

                menuFrame.setVisible( true );
                menuFrame.setResizable( false );

            }
    }

}
