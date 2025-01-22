import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * KwazamChessGUI class - GUI implementation for the Kwazam Chess game.
 * This class manages the main GUI setup, including menus, panels, and game flow.
 */
public class KwazamChessGUI extends JFrame {

    // GUI panels
    public static final JPanel panelInTheCenter = new JPanel(new GridLayout(8, 7));
    private static final JPanel bottomPanel = new JPanel(new FlowLayout());
    private static final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    // Menu components
    private static final JMenuBar mainMenuBar = new JMenuBar();
    private static final JMenu mainMenu = new JMenu("☰ Menu");
    private static final JMenuItem saveMenu = new JMenuItem("📥 Save Current Game");
    private static final JMenuItem loadMenu = new JMenuItem("📂 Load The Game");
    private static final JMenuItem resetMenu = new JMenuItem("🔄 Reset the Game");
    private static final JFileChooser fileChooser = new JFileChooser();

    // Game-related components
    static final KwazamChess game = new KwazamChess();
    private static final KwazamChessBoard chessboard = game.chessboard;
    private static final JLabel message = new JLabel("Game start! Team Blue first.");

    /**
     * Main entry point of the application. Displays the welcome menu.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KwazamChessGUI gui = new KwazamChessGUI();
            gui.showWelcomeMenu();
        });
    }

    /**
     * Default constructor. Initializes the main window and sets up listeners.
     * Sets up a confirmation dialog for exiting the application.
     */
    public KwazamChessGUI() {
        super("Kwazam Chess");
        setSize(550, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        panelInTheCenter.setBackground(Color.WHITE); // Set the middle panel background color to white.
    }

    /**
     * Confirms exit with a dialog.
     */
    private void confirmExit() {
        String windowName = "Exit the Application";
        String windowInfo = "Do you really want to exit Kwazam Chess?";
        int result = JOptionPane.showConfirmDialog(panelInTheCenter, windowInfo, windowName, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    /**
     * Starts the Kwazam Chess game by setting up the menu and making the window visible.
     */
    public void startTheGame() {
        setupMenu();
        setVisible(true);
    }

    /**
     * Displays the welcome menu before starting the game.
     */
    public void showWelcomeMenu() {
        JFrame welcomeScreen = new JFrame("♙Kwazam Chess");
        welcomeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeScreen.setSize(400, 300);
        welcomeScreen.setLayout(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("♙Kwazam Chess", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30)); // Set the font and size for the title.
        titlePanel.add(titleLabel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton startButton = new JButton("Start a new game");
        JButton loadButton = new JButton("Load game");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(startButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(exitButton);

        // Add ActionListeners
        startButton.addActionListener(e -> {
            welcomeScreen.dispose();
            startTheGame();
        });

        loadButton.addActionListener(e -> {
            welcomeScreen.dispose();
            try {
                game.load();
                setupMenu();
                setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(welcomeScreen, "Failed to load game data!");
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Add panels to frame
        welcomeScreen.add(titlePanel, BorderLayout.NORTH);
        welcomeScreen.add(buttonPanel, BorderLayout.CENTER);
        welcomeScreen.setLocationRelativeTo(null); // Center the frame on the screen.
        welcomeScreen.setVisible(true);
    }

    /**
     * Sets up the menu and layout for the main game GUI.
     */
    public void setupMenu() {
        mainMenu.add(saveMenu);
        mainMenu.add(loadMenu);
        mainMenu.add(resetMenu);
        mainMenuBar.add(mainMenu);
        topPanel.add(mainMenuBar);
        bottomPanel.add(message);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(panelInTheCenter, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets a new message in the bottom panel.
     *
     * @param word the message to display.
     */
    public void setMessage(String word) {
        message.setText(word);
    }

    /**
     * Adds an ActionListener to the restart menu item.
     *
     * @param e the ActionListener to add.
     */
    public void addRestartMenuListener(ActionListener e) {
        resetMenu.addActionListener(e);
    }

    /**
     * Adds an ActionListener to the save menu item.
     *
     * @param e the ActionListener to add.
     */
    public void addSaveMenuListener(ActionListener e) {
        saveMenu.addActionListener(e);
    }

    /**
     * Adds an ActionListener to the load menu item.
     *
     * @param e the ActionListener to add.
     */
    public void addLoadMenuListener(ActionListener e) {
        loadMenu.addActionListener(e);
    }

    /**
     * Sets up icons for the chessboar`d.
     */
    public void setupIcons() {
        chessboard.addRedIcon("Assets/TorR.png");
        chessboard.addRedIcon("Assets/XorR.png");
        chessboard.addRedIcon("Assets/BizR.png");
        chessboard.addRedIcon("Assets/SauR.png");
        chessboard.addRedIcon("Assets/RamR.png");

        chessboard.addBlueIcon("Assets/TorB.png");
        chessboard.addBlueIcon("Assets/XorB.png");
        chessboard.addBlueIcon("Assets/BizB.png");
        chessboard.addBlueIcon("Assets/SauB.png");
        chessboard.addBlueIcon("Assets/RamB.png");
    }
}