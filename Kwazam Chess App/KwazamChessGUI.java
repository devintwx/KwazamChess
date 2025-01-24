import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * KwazamChessGUI class - GUI implementation for the Kwazam Chess game.
 * This class manages the main GUI setup, including menus, panels, and game flow.
 */
public class KwazamChessGUI extends JFrame {

    // GUI panels
    public static final JPanel panelInTheCenter = new JPanel(new GridLayout(8, 7));
    private static final JPanel bottomPanel = new JPanel(new FlowLayout());
    private static final JPanel topPanel = new JPanel(new BorderLayout());

    // Menu components
    private static final JMenuBar mainMenuBar = new JMenuBar();
    private static final JMenu mainMenu = new JMenu("☰ Menu");
    private static final JMenuItem saveMenu = new JMenuItem("📥 Save"); 
    private static final JMenuItem loadMenu = new JMenuItem("📂 Load"); 
    private static final JMenuItem resetMenu = new JMenuItem("🔄 Reset"); 
    private static final JMenuItem helpMenu = new JMenuItem("❓ Help"); 
    private static final JFileChooser fileChooser = new JFileChooser(); 

    // Game-related components
    static final KwazamChess game = new KwazamChess();
    private static final KwazamChessBoard chessboard = game.chessboard;
    private static final JLabel message = new JLabel("Game start! Team Blue first.");
    private static final JLabel moveCounterLabel = new JLabel("Moves: 0"); // Move counter

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
        String windowInfo = "Exit Kwazam Chess?";
        int result = JOptionPane.showConfirmDialog(panelInTheCenter, windowInfo, windowName, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }


    /**
     * Displays the welcome menu before starting the game.
     */
public void showWelcomeMenu() {
    JFrame welcomeScreen = new JFrame("♙Kwazam Chess");
    welcomeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    welcomeScreen.setSize(400, 300);
    welcomeScreen.setLayout(new BorderLayout());

    // Set the background color of the main menu window
    welcomeScreen.getContentPane().setBackground(new Color(242, 227, 211));

    // Title Panel
    JPanel titlePanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for precise centering
    titlePanel.setBackground(new Color(242, 227, 211)); 

    JLabel titleLabel = new JLabel("♙Kwazam Chess", JLabel.CENTER);
    titleLabel.setFont(new Font("Serif", Font.BOLD, 30)); 
    titleLabel.setForeground(new Color(181, 153, 132)); 

    // Add the title label to the title panel with constraints to center it
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.CENTER; // Center the label
    titlePanel.add(titleLabel, gbc);

    // Button Panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(new Color(242, 227, 211)); 
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical alignment

    // Create buttons with custom colors and same width as the title
    JButton startButton = createCustomButton("Start");
    JButton creditButton = createCustomButton("Credit");
    JButton exitButton = createCustomButton("Exit");

    // Calculate the width of the title label
    int titleWidth = titleLabel.getPreferredSize().width;

    // Set preferred size for buttons to match the title width
    Dimension buttonSize = new Dimension(titleWidth, 30); // Same width as the title, height is 30
    startButton.setPreferredSize(buttonSize);
    startButton.setMaximumSize(buttonSize); 
    creditButton.setPreferredSize(buttonSize);
    creditButton.setMaximumSize(buttonSize); 
    exitButton.setPreferredSize(buttonSize);
    exitButton.setMaximumSize(buttonSize);

    // Center the buttons horizontally
    startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    creditButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Add buttons to the panel with vertical spacing
    buttonPanel.add(Box.createVerticalStrut(10)); // Add some vertical spacing
    buttonPanel.add(startButton);
    buttonPanel.add(Box.createVerticalStrut(10)); // Add some vertical spacing
    buttonPanel.add(creditButton);
    buttonPanel.add(Box.createVerticalStrut(10)); // Add some vertical spacing
    buttonPanel.add(exitButton);
    buttonPanel.add(Box.createVerticalStrut(20)); // Add extra bottom margin below the Exit button

    // Add ActionListeners
    startButton.addActionListener(e -> {
        welcomeScreen.dispose(); // Close the welcome screen
        new ChessApp(); // Start the game by creating an instance of ChessApp
    });

    creditButton.addActionListener(e -> showCredits());

    exitButton.addActionListener(e -> System.exit(0));

    // Add panels to frame
    welcomeScreen.add(titlePanel, BorderLayout.CENTER); // Place title panel in the center
    welcomeScreen.add(buttonPanel, BorderLayout.SOUTH); // Place button panel at the bottom
    welcomeScreen.setLocationRelativeTo(null); // Center the frame on the screen.
    welcomeScreen.setVisible(true);
}

/**
     * Displays the credits in a new window.
     */
    private void showCredits() {
        JFrame creditFrame = new JFrame("Credits");
        creditFrame.setSize(400, 200);
        creditFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        creditFrame.setLayout(new BorderLayout());

        JTextArea creditText = new JTextArea();
        creditText.setText("♙Kwazam Chess\nDevelop by TT9L Group C\n\n1. 1211112069 Tang Wei Xiong\n2. 1211108003 Joey Tan Rou Yi\n3. 1211108404 Low Wan Jin\n4. 1211107904 Yeong Zi Yan");
        creditText.setFont(new Font("Serif", Font.PLAIN, 14));
        creditText.setEditable(false);
        creditText.setBackground(new Color(242, 227, 211));
        creditText.setForeground(new Color(0, 0, 0));

        creditFrame.add(creditText, BorderLayout.CENTER);
        creditFrame.setLocationRelativeTo(null); // Center the frame on the screen.
        creditFrame.setVisible(true);
    }

/**
 * Helper method to create a button with custom colors.
 *
 * @param text The text to display on the button.
 * @return A JButton with custom background and text colors.
 */
private JButton createCustomButton(String text) {
    JButton button = new JButton(text);
    button.setBackground(new Color(181, 153, 132)); 
    button.setForeground(new Color(242, 227, 211)); 
    button.setFocusPainted(false); 
    button.setFont(new Font("Serif", Font.BOLD, 14)); 
    return button;
}

    /**
     * Sets up the menu and layout for the main game GUI.
     */
    public void setupMenu() {
        mainMenu.add(saveMenu);
        mainMenu.add(loadMenu);
        mainMenu.add(resetMenu);
        mainMenu.add(helpMenu);
        mainMenuBar.add(mainMenu);

        // Add the move counter to the top-right corner
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.add(moveCounterLabel);

        // Add the menu bar and move counter to the top panel
        topPanel.add(mainMenuBar, BorderLayout.WEST);
        topPanel.add(topRightPanel, BorderLayout.EAST);

        bottomPanel.add(message);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(panelInTheCenter, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        helpMenu.addActionListener(e -> showHelpImage());
    }

    /**
 * Sets a new message in the bottom panel and changes the background color based on the team's turn.
 *
 * @param word the message to display.
 * @param teamColor the color of the team whose turn it is ("B" for Blue, "R" for Red).
 */
public void setMessage(String word, String teamColor) {
    message.setText(word); // Update the message text

    // Change the background color of the bottom panel based on the team's turn
    if (teamColor.equals("B")) {
        bottomPanel.setBackground(Color.BLUE); // Set background to blue for Team Blue
        message.setForeground(Color.WHITE); // Set text color to white for better visibility
    } else if (teamColor.equals("R")) {
        bottomPanel.setBackground(Color.RED); // Set background to red for Team Red
        message.setForeground(Color.WHITE); // Set text color to white for better visibility
    } else {
        bottomPanel.setBackground(Color.WHITE); // Default background color
        message.setForeground(Color.BLACK); // Default text color
    }

    bottomPanel.repaint(); // Refresh the bottom panel to apply the color change
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
     * Displays the help image in a new window.
     */
    private void showHelpImage() {
    try {
        // Load the image from the Assets folder
        BufferedImage helpImage = ImageIO.read(new File("Assets/help.jpg"));
        
        // Create a JFrame to display the image
        JFrame helpFrame = new JFrame("Help");
        
        // Set the size of the window to match the image dimensions
        helpFrame.setSize(helpImage.getWidth(), helpImage.getHeight());
        
        // Ensure the window cannot be resized
        helpFrame.setResizable(false);
        
        // Close the window when the user clicks the close button
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Add the image to a JLabel and center it in the window
        JLabel imageLabel = new JLabel(new ImageIcon(helpImage));
        helpFrame.add(imageLabel);
        
        // Center the help window relative to the main window
        helpFrame.setLocationRelativeTo(this);
        
        // Make the window visible
        helpFrame.setVisible(true);
    } catch (IOException e) {
        // Display an error message if the image fails to load
        JOptionPane.showMessageDialog(this, "Failed to load help image!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


/**
     * Updates the move counter in the top-right corner.
     *
     * @param moveCount the current move count.
     */
    public void updateMoveCounter(int moveCount) {
        moveCounterLabel.setText("Moves: " + moveCount); // Update the move counter label
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
