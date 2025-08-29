package Frontend;

import javax.swing.*;
import java.awt.*;

public class LabelCreator {
    public static JLabel createJLabel(String text, String font, int fontSize, int fontType,  int hAlignment, Color color){
        JLabel label = new JLabel(text, hAlignment);
        label.setForeground(color);
        label.setFont(new Font(font, fontType, fontSize));

        return label;
    }
}
