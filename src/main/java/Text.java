import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by disas on 20.04.2017.
 */
public class Text {
    private List<String> teiText;

    public Text(List<String> latexText) {
            teiText = new ArrayList<String>();
            createTEI(latexText);

    }

    private void createTEI(List<String> latexText){

        //Start Tags
        teiText.add("\t<text>");
        teiText.add("\t\t<body>");

        Pattern commentPattern = Pattern.compile("^\\%");
        Pattern sectionPattern = Pattern.compile("^\\\\section\\{(.+?)\\}");
        Pattern subsectionPattern = Pattern.compile("^\\\\subsection\\{(.+?)\\}");
        Pattern sentencePattern = Pattern.compile("^[^\\\\]");
        Pattern itemPattern = Pattern.compile("^\\\\item(.+?)");

        Matcher m;

        int section = 1;
        int subsection = 0;
        int sentence = 1;


        for(String s : latexText){
            m = commentPattern.matcher(s);
            if(m.find() || s.equals("")){
            }
            else{

                //sections (+subsection)
                m = sectionPattern.matcher(s);
                if(m.find()){
                    if(section > 1){
                        teiText.add("\t\t\t\t</div2>");
                        teiText.add("\t\t\t</div1>");
                    }
                    subsection =0;
                    teiText.add("\t\t\t<div1 type = \"section\" n=" + section + ">");
                    teiText.add("\t\t\t\t<div2 type = \"subsection\" n=" + (subsection+1) + ">");
                    section++;
                    sentence = 1;

                }

                //subsection
                m = subsectionPattern.matcher(s);
                if(m.find()){
                    if(subsection==0){
                        subsection++;
                    }
                    else if(subsection>=1){
                        teiText.add("\t\t\t\t</div2>");
                        teiText.add("\t\t\t\t<div2 type = \"subsection\" n=" + (subsection+1) + ">");
                        subsection++;

                    }
                }

                //sentences
                m = sentencePattern.matcher(s);
                if(m.find()){
                    teiText.add("\t\t\t\t\t<div3 type = \"sentence\" n=" + sentence + ">" + s + "</div3>");
                    sentence++;
                }
                m = itemPattern.matcher(s);
                if(m.find()){
                    teiText.add("\t\t\t\t\t<div3 type = \"sentence\" n=" + m.group(1) + ">" + s + "</div3>");
                }

                //teiText.add(s);
            }
        }

        //End Tags
        teiText.add("\t\t\t\t</div2>");
        teiText.add("\t\t\t</div1>");
        teiText.add("\t\t</body>");
        teiText.add("\t</text>");
        teiText.add("</TEI>");
    }

    public List<String> getTeiText() {
        return teiText;
    }
}
