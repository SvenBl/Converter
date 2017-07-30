import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Form {
    private JButton metaButton;
    private JPanel panelMain;
    private JButton convertButton;
    private JLabel inputFile;
    private JLabel outputPath;
    private JTextField inputPathField;
    private JTextField outputPathField;
    private JButton inputPathButton;
    private JButton outputPathButton;
    private JLabel authorLabel;
    private JLabel titleLabel;
    private JLabel dateLabel;
    private JTextField authorField;
    private JTextField titleField;
    private JTextField dateField;
    private JButton setTodayButton;

    private Converter converter;
    private List<String> latexDoc;
    private Header tHead;

    public Form() {
        metaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // TODO: 30.07.2017 remove below
                inputPathField.setText("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\lls_cts_v4\\cts_lit.tex");
                outputPathField.setText("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\lls_cts_v4");


                //set both
                try{
                    converter = new Converter(inputPathField.getText());
                    latexDoc = converter.readFile(inputPathField.getText());
                    tHead = new Header(latexDoc);


                    authorField.setText(tHead.getAuthor());
                    titleField.setText(tHead.getTitle());
                    dateField.setText(tHead.getDate());

                } catch (Exception el){
                    JOptionPane.showMessageDialog(null,
                            "The input file could not be found!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    el.printStackTrace();
                    return;
                }



            }
        });
        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Header
                if(authorField.getText().equals("") || titleField.getText().equals("") || dateField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null,
                            "The file could not be created! Set metadata first!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (tHead == null){
                    JOptionPane.showMessageDialog(null,
                            "The file could not be created! Crawl Metadata from a valid file first!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                tHead.setAuthor(authorField.getText());
                tHead.setTitle(titleField.getText());
                tHead.setDate(dateField.getText());


                tHead.createTEI();
                List<String> teiHeader = tHead.getTeiHeader();


                //Text
                Text tText = new Text(latexDoc);
                List<String> teiText = tText.getTeiText();



                StringBuffer sb = new StringBuffer();
                for(String line : teiHeader){
                    sb.append(line + "\n");
                }
                for(String line : teiText){
                    sb.append(line + "\n");
                }

                String author = tHead.getAuthor();
                author = author.replaceAll("\\s", "-").replaceAll( "[.]" , "-");
                String date = tHead.getDate();
                date = date.replaceAll("\\s", "-").replaceAll( "[.]" , "-");
                String title = tHead.getTitle();
                title = title.replaceAll("\\s", "-").replaceAll( "[.]" , "-");


                String filePath = "";
                try{
                    filePath = (outputPathField.getText())+ "\\"
                            + author + "\\"
                            + date + "\\"
                            + title + ".xml";
                } catch(NullPointerException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "The file could not be created! Output path has to be set!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                BufferedWriter bwr = null;
                try {
                    new File((outputPathField.getText())+ "\\"
                            + author + "\\"
                            + date).mkdirs();
                    bwr = new BufferedWriter(new FileWriter(new File(filePath)));
                    bwr.write(sb.toString());
                    bwr.flush();
                    bwr.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "The File could not be created", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                // create XMLReader for parsing
                XMLReader xmlReader = null;
                FileReader reader = null;
                try {
                    xmlReader = XMLReaderFactory.createXMLReader();
                    reader = new FileReader(filePath);
                    InputSource inputSource = new InputSource(reader);
                    // DTD could be added here
                    // inputSource.setSystemId("X:\\test.dtd");
                    xmlReader.parse(inputSource);
                    JOptionPane.showMessageDialog(null,
                            "The file has been succesfully converted in a valid .xml file!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SAXException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "The file has been converted but the .xml file is not valid!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (FileNotFoundException e1) {
                    System.out.println("parser file not found");
                    e1.printStackTrace();
                } catch (IOException e1) {
                    System.out.println("parser io exception");
                    e1.printStackTrace();
                }
            }
        });
        inputPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Tex Files", "tex");
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(filter);
                int clicked = fc.showOpenDialog(panelMain);

                //on approve
                if(clicked == JFileChooser.APPROVE_OPTION)
                {
                    inputPathField.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });
        outputPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int clicked = fc.showOpenDialog(panelMain);

                //on approve
                if(clicked == JFileChooser.APPROVE_OPTION)
                {
                    outputPathField.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });
        setTodayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                dateField.setText(sdfDate.format(now));
            }
        });
    }

    public static void main(String[]args){
        JFrame frame = new JFrame("Converter");
        frame.setContentPane(new Form().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}
