import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI extends JFrame{

    public void startGame() {
    menuSetup();
    setVisible(true);
    }

    public static void main(String[] args) {
    // Display the welcome menu
    SwingUtilities.invokeLater(() -> {
        GUI gui = new GUI();
        gui.showWelcomeMenu();
    });
}

    // Display welcome menu before starting the game
    public void showWelcomeMenu() {
        JFrame welcomeFrame = new JFrame("♙Kwazam Chess");
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setSize(400, 300);
        welcomeFrame.setLayout(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("♙Kwazam Chess", JLabel.CENTER);
        JLabel subtitleLabel = new JLabel("Developed by TT9L Group C", JLabel.CENTER);
        
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        subtitleLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        JButton startButton = new JButton("Start");
        JButton loadButton = new JButton("Load");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(startButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(exitButton);

        // Add ActionListeners
        startButton.addActionListener(e -> {
        welcomeFrame.dispose(); // Close welcome menu
        startGame(); // Launch the chess game GUI
        });


        loadButton.addActionListener(e -> {
            welcomeFrame.dispose(); // Close welcome menu
            try {
                game.load(); // Load the game
                menuSetup();
                setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(welcomeFrame, "Failed to load game data!");
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Add panels to frame
        welcomeFrame.add(titlePanel, BorderLayout.NORTH);
        welcomeFrame.add(buttonPanel, BorderLayout.CENTER);
        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setVisible(true);
    }
    public static JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public static JPanel middlePanel = new JPanel(new GridLayout(8,7));
    public static JPanel bottomPanel = new JPanel(new FlowLayout());
    
    public static JMenuBar menuBar = new JMenuBar();
    public static JMenu menu = new JMenu("☰ Menu");
    public static JMenuItem menuItemSave = new JMenuItem("📥 Save Game");
    public static JMenuItem menuItemLoad = new JMenuItem("📂 Load Game");
    public static JMenuItem menuItemRestart = new JMenuItem("🔄 Restart");
    public static JFileChooser fileChooser = new JFileChooser();
    
    private static KwazamChess game = new KwazamChess();
    private static ChessBoard chessboard = game.chessboard;
    private static JLabel message = new JLabel("Game start! Team Blue first.");

    
    // Default constructor. Set title and size of window and add WindowListener to GUI
    GUI(){
        
        super("Kwazam Chess");
        showWelcomeMenu();

        setSize(550,700); 
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener( new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            { 
                String windowName = "Exit Application";
                String windowInfo = "Do you want to exit Kwazam Chess?";
                int result = JOptionPane.showConfirmDialog(middlePanel, windowInfo, windowName, JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                    setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });

        middlePanel.setBackground(Color.white);
        
    }

    // Set new message in bottomPanel 
    public void setMessage(String word){
        message.setText(word);
    }

    // Add ActionListener to restart item 
    public void menuItemRestartListener(ActionListener e){
        menuItemRestart.addActionListener(e);
    }

    // Add ActionListener to save item 
    public void menuItemSaveListener(ActionListener e){
        menuItemSave.addActionListener(e);
    }

    // Add ActionListener to load item 
    public void menuItemLoadListener(ActionListener e){
        menuItemLoad.addActionListener(e);
    }
    
    // Setup menu 
    public void menuSetup(){
        menu.add(menuItemSave);
        menu.add(menuItemLoad);
        menu.add(menuItemRestart);
        menuBar.add(menu);    
        topPanel.add(menuBar);
        bottomPanel.add(message);
        
        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(middlePanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        
    }
    
    // Add icons to chessboard 
    public void iconSetup(){
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