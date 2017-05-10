package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by root on 4.5.2017.
 */
public class Agency {
    private JPanel agencyPanel;
    private JTabbedPane tabbedPane1;
    private JButton editMissionButton;
    private JButton deleteMissionButton;
    private JButton createMissionButton;
    private JButton editAgentButton;
    private JButton deleteAgentButton;
    private JButton createAgentButton;
    private JList missionNameList;
    private JList agentNameList;
    private JList missionGoalList;
    private JList missionLocationList;
    private JList missionDescriptionList;
    private JList agentBirthdayList;
    private JList agentSecLevelList;
    private JLabel agentNameLabel;
    private JLabel agentBirthdayLabel;
    private JLabel agentSecLevelLabel;
    private JLabel missionNameLabel;
    private JLabel missionGoalLabel;
    private JLabel missionLocationLabel;
    private JLabel missionDescriptionLabel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Titulek okna");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(800,600));
                frame.pack();
                frame.setVisible(true);
                frame.setJMenuBar(createMenu());
            }
        });
    }

    private static JMenuBar createMenu() {
        //hlavní úroveň menu
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);
        JMenuItem exitItem = new JMenuItem();
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        return menubar;
    }
}
