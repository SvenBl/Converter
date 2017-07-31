import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        this.teiHeader = new ArrayList<String>();
        initializeIntel(latexHeader);
    }


    private void initializeIntel(List<String> latexHeader)
    {
        //Patterns with
        Pattern titlePattern = Pattern.compile(".*(\\\\title)");
        Pattern authorPattern = Pattern.compile(".*(\\\\author)");
        Pattern datePattern = Pattern.compile("^\\\\date\\{(.*)\\}");
        Pattern yearDatePattern = Pattern.compile("[12][0-9]{3}");
        Pattern languagePattern = Pattern.compile("^\\\\setdefaultlanguage\\{(.*)\\}");
        Pattern innerBracketPattern = Pattern.compile("\\{([^\\{\\}]*)\\}");

        Matcher m;
        for(String s : latexHeader){
            m = titlePattern.matcher(s);
            if(m.find()){
                if(m.group(1)!=null){
                    m = innerBracketPattern.matcher(s);
                    if (m.find()) {
                        this.title = m.group(1);
                    }
                }
            }
            m = authorPattern.matcher(s);
            if(m.find()){
                if(m.group(1)!=null){
                    m = innerBracketPattern.matcher(s);
                    if (m.find()) {
                        this.author = m.group(1);
                        while (m.find()){
                            this.author += "," + m.group(1);
                        }
                    }
                }
            }
            m = datePattern.matcher(s);
            if(m.find()){
                this.date = m.group(1);
                if(date.equals("\\today") || date.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                    String date = sdf.format(new Date());
                    this.date = date;
                }
                m = yearDatePattern.matcher(s);
                if(m.find()){
                    this.date = m.group(1);
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
        teiHeader.clear();
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

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
