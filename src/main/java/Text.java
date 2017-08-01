import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        Pattern sentencePattern = Pattern.compile("^[^\\\\\\#]");
        Pattern footnotePattern = Pattern.compile("\\\\footnote\\{(.+?)\\}");
        Pattern textitPattern = Pattern.compile("\\\\textit\\{(.+?)\\}");
        Pattern textbfPattern = Pattern.compile("\\\\textibf\\{(.+?)\\}");
        Pattern underlinePattern = Pattern.compile("\\\\underline\\{(.+?)\\}");



        Matcher m;

        int section = 1;
        int subsection = 0;
        int sentence = 1;


        boolean textstart = false;
        for(String s : latexText){
            //to replace from string
            s = s.replaceAll("(\\\\[a-zA-Z]+?)[\\n\\t\\r\\s]", ""); //delete latex commands with no brackets

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
                    teiText.add("\t\t\t<div1 type = \"section\" n=\"" + section + "\">");
                    teiText.add("\t\t\t\t<div2 type = \"subsection\" n=\"" + (subsection+1) + "\">");
                    section++;
                    sentence = 1;
                    textstart = true;

                }

                //subsection
                m = subsectionPattern.matcher(s);
                if(m.find() && textstart){
                    if(subsection==0){
                        subsection++;
                    }
                    else if(subsection>=1){
                        teiText.add("\t\t\t\t</div2>");
                        teiText.add("\t\t\t\t<div2 type = \"subsection\" n=\"" + (subsection+1) + "\">");
                        subsection++;

                    }
                }

                //sentences
                m = sentencePattern.matcher(s);
                if(m.find() && textstart){
                    m = footnotePattern.matcher(s);
                    while(m.find()){
                        String footnote = "( Footnote: " + m.group(1) + ")";
                        s = s.replaceFirst("\\\\footnote\\{(.+?)\\}", footnote);
                    }
                    m = textitPattern.matcher(s);
                    while(m.find()){
                        s = s.replaceFirst("\\\\textit\\{(.+?)\\}", m.group(1));
                    }
                    m = textbfPattern.matcher(s);
                    while(m.find()){
                        s = s.replaceFirst("\\\\textbf\\{(.+?)\\}", m.group(1));
                    }
                    m = underlinePattern.matcher(s);
                    while(m.find()){
                        s = s.replaceFirst("\\\\underline\\{(.+?)\\}", m.group(1));
                    }
                    //clean potential errors
                        s = s.replaceAll("\\\\textit\\{", "")
                                .replaceAll("\\\\textbf\\{", "")
                                .replaceAll("\\\\underline\\{", "");

                    s = s.replaceAll("&", "&amp;")
                            .replaceAll("<", "&lt;")
                            .replaceAll(">" , "&gt;")
                            .replaceAll("\"","&quot;")
                            .replaceAll("'", "&apos;");
                    teiText.add("\t\t\t\t\t<div3 type = \"paragraph\" n=\"" + sentence + "\">" + s + "</div3>");
                    sentence++;
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
