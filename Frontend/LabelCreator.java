package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class LabelCreator {
    public static JLabel createJLabel(String text, String font, int fontSize, int fontType,  int hAlignment, Color color){
        JLabel label = new JLabel(text, hAlignment);
        label.setForeground(color);
        label.setFont(new Font(font, fontType, fontSize));

        return label;
    }

    public static JLabel getIconLabel(String logoPath, int width, int height) {
        ImageIcon icon = new ImageIcon(logoPath);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(image));

        return label;
    }

    public static ImageIcon getImageIcon(String logoPath, int width, int height) {
        ImageIcon icon = new ImageIcon(logoPath);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(image);
    }
}

