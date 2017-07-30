import javax.swing.*;
import java.util.List;


public class Main {

    public static void main(String[]args){

        //LatexReader reader = new LatexReader("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\CLARIN_CTS.tex");
        Converter reader = new Converter("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\lls_cts_v4\\cts_lit.tex");
        Header header = new Header(reader.readFile("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\lls_cts_v4\\cts_lit.tex"));

        Text text = new Text(reader.readFile("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\lls_cts_v4\\cts_lit.tex"));
        List<String> test = text.getTeiText();
        int i = 1;
        for(String s : test){
            System.out.println(i + " " + s);
            i++;
        }

        JFrame frame = new JFrame("Converter");
        frame.setContentPane(new Form().getPanelMain());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }

}
