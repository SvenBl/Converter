import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by disas on 18.04.2017.
 */

public class Header {

    private String title = "";
    private String author = "";

    private String publisher = "";
    private String pubPlace = "";
    private String license = "";

    private String date = "";
    private String language = "";

    private List<String> teiHeader;



    public Header(List<String> latexHeader){
        teiHeader = new ArrayList<String>();
        initializeIntel(latexHeader);

        createTEI();

    }


    //Todo set right regex and fill with group
    public void initializeIntel(List<String> latexHeader)
    {
        Pattern titlePattern = Pattern.compile("^\\\\title\\{(.+?)\\}");
        Pattern authorPattern = Pattern.compile("^\\\\author\\{.*\\}");
        Pattern publisherPattern = Pattern.compile("^\\\\title\\{.*\\}");
        Pattern pubPlacePattern = Pattern.compile("^\\\\title\\{.*\\}");
        Pattern licensePattern = Pattern.compile("^\\\\title\\{.*\\}");
        Pattern datePattern = Pattern.compile("^\\\\date\\{(.+?)\\}");
        Pattern languagePattern = Pattern.compile("^\\\\setdefaultlanguage\\{(.+?)\\}");

        Matcher m;
        for(String s : latexHeader){
            m = titlePattern.matcher(s);
            if(m.find()){
                title = m.group(1);
            }
            m = authorPattern.matcher(s);
            if(m.find()){
                author = "foo";
            }
            m = publisherPattern.matcher(s);
            if(m.find()){
                publisher = "foo";
            }
            m = pubPlacePattern.matcher(s);
            if(m.find()){
                pubPlace = "foo";
            }
            m = licensePattern.matcher(s);
            if(m.find()){
                license = "foo";
            }
            m = datePattern.matcher(s);
            if(m.find()){
                date = m.group(1);
                if(date.equals("\\today")){

                    //Todo set correct date
                    date = "Heute";
                }
            }
            m = languagePattern.matcher(s);
            if(m.find()){
                language = m.group(1);
            }

        }
    }

    public List<String> getTeiHeader() {
        return teiHeader;
    }


    public void createTEI(){
        teiHeader.add("<TEI xmlns=\"http://www.tei-c.org/ns/1.0\">");
        teiHeader.add("\t<teiHeader type=\"text\">");
        teiHeader.add("\t\t<fileDesc>");

        teiHeader.add("\t\t\t<titleStmt>");
        teiHeader.add("\t\t\t\t<title>" + title + "</title>");
        teiHeader.add("\t\t\t\t<author>" + author + "</author>");
        teiHeader.add("\t\t\t</titleStmt>");

        teiHeader.add("\t\t\t<publicationStmt>");
        teiHeader.add("\t\t\t\t<publisher>" + publisher + "</publisher>");
        teiHeader.add("\t\t\t\t<pubPlace>" + pubPlace + "</pubPlace>");
        teiHeader.add("\t\t\t\t<license>" + license + "</license>");
        teiHeader.add("\t\t\t</publicationStmt>");

        teiHeader.add("\t\t\t<sourceDesc>");
        teiHeader.add("\t\t\t\t<date>" + date + "</date>");
        teiHeader.add("\t\t\t</sourceDesc>");

        teiHeader.add("\t\t</fileDesc>");
        teiHeader.add("\t\t<profileDesc>");

        teiHeader.add("\t\t\t<langUsage>");
        teiHeader.add("\t\t\t\t<language>" + language + "</language>");
        teiHeader.add("\t\t\t</langUsage>");

        teiHeader.add("\t\t</profileDesc>");
        teiHeader.add("\t</teiHeader>");
    }



}
