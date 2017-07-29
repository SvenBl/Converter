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

/**
 * Created by disas on 27.07.2017.
 */
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

    private LatexReader reader;
    private Header tHead;

    public Form() {
        metaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //to remove
                inputPathField.setText("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\lls_cts_v4\\cts_lit.tex");
                outputPathField.setText("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\lls_cts_v4");

                reader = new LatexReader(inputPathField.getText());

                //set both
                reader = new LatexReader("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\lls_cts_v4\\cts_lit.tex");
                //reader = new LatexReader("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\CLARIN_CTS.tex");

                //Read the LatexFiles
                List<String> latexHeader = reader.getLatexHeader();
                tHead = new Header(latexHeader);


                authorField.setText(tHead.getAuthor());
                titleField.setText(tHead.getTitle());
                dateField.setText(tHead.getDate());





            }
        });
        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Header
                tHead.setAuthor(authorField.getText());
                tHead.setTitle(titleField.getText());
                tHead.setDate(dateField.getText());
                tHead.createTEI();
                List<String> teiHeader = tHead.getTeiHeader();


                //Text
                List<String> latexText = reader.getLatexText();
                Text tText = new Text(latexText);
                List<String> teiText = tText.getTeiText();

                int i = 0;

                for(String s : teiHeader)
                {
                    System.out.println(i + " " + s);
                    i++;
                }
                i = 0;
                for(String s : teiText)
                {
                    System.out.println(i + " " + s);
                    i++;
                }

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

                String filePath = (outputPathField.getText())+ "\\"
                        + author + "\\"
                        + date + "\\"
                        + title + ".xml";

                BufferedWriter bwr = null;
                try {
                    System.out.println(new File((outputPathField.getText())+ "\\"
                            + author + "\\"
                            + date).mkdirs());
                    bwr = new BufferedWriter(new FileWriter(new File(filePath)));
                    bwr.write(sb.toString());
                    bwr.flush();
                    bwr.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                // XMLReader erzeugen
                XMLReader xmlReader = null;
                FileReader reader = null;
                try {
                    xmlReader = XMLReaderFactory.createXMLReader();
                    reader = new FileReader(filePath);
                    InputSource inputSource = new InputSource(reader);
                    // DTD kann optional übergeben werden
                    // inputSource.setSystemId("X:\\personen.dtd");
                    xmlReader.parse(inputSource);
                    JOptionPane.showMessageDialog(null,
                            "The file has been succesfully converted in a valid .xml file!");
                } catch (SAXException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "The file has been converted but the .xml file is not valid!");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
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
