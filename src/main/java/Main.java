import java.util.List;

/**
 * Created by disas on 18.04.2017.
 */

public class Main {

    public static void main(String[]args){

        //LatexReader reader = new LatexReader("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\CLARIN_CTS.tex");
        LatexReader reader = new LatexReader("C:\\Users\\disas\\Dropbox\\Uni Leipzig\\Anwendungen der Linguistischen Informatik\\lls_cts_v4\\cts_lit.tex");

        //Read the LatexFiles
        List<String> latexHeader = reader.getLatexHeader();
        List<String> latexText = reader.getLatexText();

        //Create the TEI Components
        Header tHead = new Header(latexHeader);
        List<String> teiHeader = tHead.getTeiHeader();
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

    }

}
