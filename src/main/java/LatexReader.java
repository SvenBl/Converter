import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by disas on 18.04.2017.
 */

public class LatexReader {

    private List<String> file;
    private List<String> latexHeader;
    private List<String> latexText;

    public LatexReader(String filepath) {
        file = readFile(filepath);
        splitFile(file);

    }

    //Split File into Header and Text
    private void splitFile(List<String> file)
    {
        Pattern p = Pattern.compile("^\\\\begin\\{document\\}");   // the pattern to search for
        Matcher m;
        latexHeader = new ArrayList<String>();
        latexText = new ArrayList<String>();
        boolean inHeader = true;

        for (String s : file) {
            m = p.matcher(s);
            if(inHeader) {
                if (m.find()) {
                    inHeader = false;
                    latexText.add(s);

                } else {
                    latexHeader.add(s);
                }
            }
            else{
                latexText.add(s);
            }
        }
    }

    //Create List for File
    private static List<String> readFile(String filename)
    {
        List<String> records = new ArrayList<String>();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null)
            {
                records.add(line);
            }
            reader.close();
            return records;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getLatexHeader() {
        return latexHeader;
    }

    public List<String> getLatexText() {
        return latexText;
    }
}
