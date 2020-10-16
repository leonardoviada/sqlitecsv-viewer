import org.apache.commons.io.FilenameUtils;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;


public class IOWindow extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                new IOWindow();
            }
        });
    }

    private IOWindow() {
        setTitle("");
        setLocationRelativeTo(null);
        setSize(800, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {

        /* NORTH */
        JMenuBar pnlMenu = new JMenuBar();
        JMenu mnuMenu = new JMenu("File");
        mnuMenu.setMnemonic('F');
        JMenuItem mniOpen = new JMenuItem("Open...");
        mniOpen.setMnemonic('O');
        mniOpen.setIcon(UIManager.getIcon("FileView.directoryIcon"));

        mnuMenu.add(mniOpen);
        pnlMenu.add(mnuMenu);
        this.add(pnlMenu, BorderLayout.NORTH);

        /* CENTER */
        final MyTable tblData = new MyTable();
        JScrollPane pnlData = new JScrollPane(tblData);
        this.add(pnlData, BorderLayout.CENTER);

        /* LISTENERS */
        mniOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if( fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;
                File f = fileChooser.getSelectedFile();
                System.out.println("\n---\nAPRO " + f);

                String ext = FilenameUtils.getExtension(f.toString());
                System.out.println("ESTENSIONE " + ext);

                switch(ext) {
                    case "csv":
                        System.out.println("CSV");
                        try {
                            tblData.loadFromCSV(f);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    case "db":
                    case "sqlite3":
                        System.out.println("SQLITE");
                        try {
                            tblData.loadFromSQL(f);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        break;

                }
            }
        });
    }
}
