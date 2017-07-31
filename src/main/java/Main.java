import javax.swing.*;
import java.util.List;


public class Main {

    public static void main(String[]args){


        JFrame frame = new JFrame("Converter");
        frame.setContentPane(new Form().getPanelMain());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }

}
