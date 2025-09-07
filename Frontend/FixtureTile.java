package Frontend;

import java.awt.event.*;

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;

public class FixtureTile extends JPanel {
    private final Color EASY_COLOR = new Color(97, 201, 102);
    private final Color MEDIUM_COLOR = new Color(214, 214, 214);
    private final Color HARD_COLOR = new Color(227, 113, 113);

    private Fixture fixture;
    private boolean home;

    public FixtureTile(Fixture fixture, boolean home){
        this.fixture = fixture;
        this.home = home;

        setup();
    }

    private void setup(){
        JLabel opponentLabel = LabelCreator.createJLabel(fixture.getFixtureString(home), "SansSerif", 10, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
        Color difficultyColor;
        Team opponent;

        if(home) opponent = fixture.getAwayTeam();
        else opponent = fixture.getHomeTeam();

        if(opponent.getLeaguePosition() <= 6) difficultyColor = HARD_COLOR;
        else if(opponent.getLeaguePosition() <= 16) difficultyColor = MEDIUM_COLOR;
        else difficultyColor = EASY_COLOR;

        this.setBackground(difficultyColor);
        this.add(opponentLabel);
    }
}
