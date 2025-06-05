import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class RPGItemCreator extends JFrame {
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextArea loreArea;
    private JComboBox<String> rarityCombo;
    private JTextArea itemDisplay;
    private JButton createButton;
    private JTextField filePathInput;
    private JLabel numberItems;

    //Color Pallete Created using AI
    private final Color DARK_PURPLE = new Color(40, 10, 60);
    private final Color MAGIC_PURPLE = new Color(120, 50, 220);
    private final Color GOLD = new Color(255, 215, 0);
    private final Color CRYSTAL_BLUE = new Color(100, 200, 255);
    private final Color PARCHMENT = new Color(250, 240, 220);
    private final Color SHADOW_BLACK = new Color(20, 10, 30);

    public final Color[] RARITY_COLORS = {
            new Color(180, 180, 180),    // Common (Silver)
            new Color(100, 255, 100),   // Uncommon (Green)
            new Color(100, 150, 255),    // Rare (Blue)
            new Color(200, 80, 255),     // Epic (Purple)
            new Color(255, 200, 50)      // Legendary (Gold)
    };

    public RPGItemCreator() {
        setTitle("✨ Enchanted Item Forge ✨");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(970, 1000);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                //Constructs the program's background to be the the purple color that was already determined
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, SHADOW_BLACK, getWidth(), getHeight(), DARK_PURPLE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        //Using border layout to be able to clamp certain buttons and text fields on one line to different sides
        // of the window in order to look semi aesthetic. (Adds the main panel to the frame
        mainPanel.setLayout(new BorderLayout(20, 20));
        add(mainPanel);

        //Creates the title panel, and centers it
        JLabel titleLabel = new JLabel("Enchanted Item Forge", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Goudy Old Style", Font.BOLD, 36));
        titleLabel.setForeground(GOLD);
        //Creating an empty border to create a little bit of extra spacing to not look so cramped
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        //Adds the title to the main panel and centers it to stay at top, its a title it should be like that
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Creates the input panel which is going to handle all of the input fields/text fields which are
        //needed to give the item name, if you don't want it to be randomly henerated and the story and description of the item too
        JPanel inputPanel = new JPanel();
        //The background color which was created earlier needs to be seen, so the panel should be transparent
        inputPanel.setOpaque(false);
        //Grid layout to order things in asingle column, 0 rows here gives the program freedom to calculate
        //the amount of needed rows so later more can be added
        inputPanel.setLayout(new GridLayout(0, 1, 15, 15));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                //Creates the yellow-ish border to separate different parts from the program. It looks more aesthetic
                new LineBorder(new Color(200, 180, 150), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        //           Uses lambda to not add eventListeners later its faster
        JPanel namePanel = createLabeledField("Item Name:", nameField = new JTextField(), "Randomize", e -> randomizeName());
        inputPanel.add(namePanel);

        // 📖 Description Field
        JPanel descPanel = createLabeledField("Description:", descriptionField = new JTextField(), null, null);
        inputPanel.add(descPanel);

        JPanel rarityPanel = new JPanel(new BorderLayout(10, 10));
        rarityPanel.setOpaque(false);

        JLabel rarityLabel = new JLabel("Rarity:");
        rarityLabel.setForeground(CRYSTAL_BLUE);

        rarityPanel.add(rarityLabel, BorderLayout.WEST);

        //Creates the drop-down menu for the rarity.
        rarityCombo = new JComboBox<>(new String[]{"Common", "Uncommon", "Rare", "Epic", "Legendary"});
        rarityCombo.setRenderer(new RarityComboBoxRenderer());
        rarityCombo.setBackground(PARCHMENT);
        rarityCombo.setFont(new Font("Book Antiqua", Font.PLAIN, 34));
        rarityPanel.add(rarityCombo, BorderLayout.CENTER);

        //Randomizes the combobox with the rarirty
        JButton randomRarityBtn = createMagicButton("Randomize");
        //ActionEvent e is given what function to call
        randomRarityBtn.addActionListener(e -> randomizeRarity());
        rarityPanel.add(randomRarityBtn, BorderLayout.EAST);
        inputPanel.add(rarityPanel);

        // Lore
        JPanel lorePanel = new JPanel(new BorderLayout());
        lorePanel.setOpaque(false);

        JLabel loreLabel = new JLabel("Lore (Optional):");
        loreLabel.setForeground(CRYSTAL_BLUE);
        lorePanel.add(loreLabel, BorderLayout.NORTH);

        // The area from where the lore of the item is going to be added
        loreArea = new JTextArea(5, 20);
        loreArea.setBackground(PARCHMENT);
        loreArea.setForeground(SHADOW_BLACK);
        loreArea.setFont(new Font("Book Antiqua", Font.ITALIC, 14));
        loreArea.setLineWrap(true);
        loreArea.setWrapStyleWord(true);
        JScrollPane loreScroll = new JScrollPane(loreArea);
        loreScroll.setOpaque(false);
        loreScroll.getViewport().setOpaque(false);
        loreScroll.setBorder(BorderFactory.createLineBorder(new Color(180, 160, 140), 1));
        lorePanel.add(loreScroll, BorderLayout.CENTER);
        inputPanel.add(lorePanel);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Item Display, it is going to have the buttons as well as the item preview window
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setOpaque(false);
        displayPanel.setPreferredSize(new Dimension(getWidth(), 250));
        //Creates a border around the preview to make it look prettier
        displayPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 140), 2, true),
                "Item Preview",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION,
                new Font("Goudy Old Style", Font.BOLD, 16),
                GOLD
        ));

        //the preview window with its information
        itemDisplay = new JTextArea();
        itemDisplay.setEditable(false);
        itemDisplay.setBackground(DARK_PURPLE);
        itemDisplay.setForeground(Color.WHITE);
        itemDisplay.setFont(new Font("Monospaced", Font.PLAIN, 14));
        itemDisplay.setBorder(new EmptyBorder(30, 15, 15, 15));

        //Scroll for the preview window
        JScrollPane displayScroll = new JScrollPane(itemDisplay);
        displayScroll.setOpaque(false);
        displayScroll.getViewport().setOpaque(false);
        displayPanel.add(displayScroll, BorderLayout.CENTER);


        // Creates the button that will create the items and save them to the arrayList in Main class
        createButton = createMagicButton("Forge Item");
        createButton.addActionListener(e -> createItem());
        createButton.setFont(new Font("Goudy Old Style", Font.BOLD, 18));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);


        filePathInput = new JTextField("Path");
        filePathInput.setPreferredSize(new Dimension(200, 30));
        JButton ReadFile = createMagicButton("Read");
        JButton WriteFile = createMagicButton("Write");
        JButton displayItemsButton = createMagicButton("Display");
        JButton clearButton = createMagicButton("Clear");
        JButton removeItems = createMagicButton("Destroy");
        WriteFile.addActionListener(e -> writeFile(filePathInput.getText()));
        displayItemsButton.addActionListener(e -> displayItems());
        clearButton.addActionListener(e -> clearFields());
        ReadFile.addActionListener(e -> readFile(filePathInput.getText()));
        removeItems.addActionListener(e -> removeItems());

        numberItems = new JLabel("Count: ");
        numberItems.setForeground(CRYSTAL_BLUE);



        buttonPanel.add(numberItems);
        buttonPanel.add(filePathInput);
        buttonPanel.add(ReadFile);
        buttonPanel.add(createButton);
        buttonPanel.add(WriteFile);
        buttonPanel.add(displayItemsButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(removeItems);

        //Adds the bottom part of the ui with all of the buttons for controls
        displayPanel.add(buttonPanel, BorderLayout.SOUTH);

        //Adds the created panel to the main displayed thing
        mainPanel.add(displayPanel, BorderLayout.SOUTH);

        // Update preview when fields change
        nameField.getDocument().addDocumentListener(new SimpleDocumentListener(this::updatePreview));
        descriptionField.getDocument().addDocumentListener(new SimpleDocumentListener(this::updatePreview));
        loreArea.getDocument().addDocumentListener(new SimpleDocumentListener(this::updatePreview));
        rarityCombo.addActionListener(e -> updatePreview());
    }

    // Creates a labeled field with optional button
    // I again am using this function as a template for easier creation and more clean code
    private JPanel createLabeledField(String labelText, JTextField field, String buttonText, ActionListener buttonAction) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(CRYSTAL_BLUE);
        //centers it to the left part of the screen
        panel.add(label, BorderLayout.WEST);

        field.setBackground(PARCHMENT);
        field.setForeground(SHADOW_BLACK);
        field.setFont(new Font("Book Antiqua", Font.BOLD, 24));
        //creates a border for a more aesthetic look
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 140), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(field, BorderLayout.CENTER);

        //Connection with the createMagicButton function that cretes the button
        if (buttonText != null) {
            JButton button = createMagicButton(buttonText);
            button.addActionListener(buttonAction);
            panel.add(button, BorderLayout.EAST);
        }

        return panel;
    }

    // Creates the button that tints blue when hovered on\
    // I use this function as a template for all the buttons
    private JButton createMagicButton(String text) {
        //Creates the button and stores it so its values can be changed
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        //Transparent contents
        button.setContentAreaFilled(false);

        //creates a border for the button that is going to change color
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAGIC_PURPLE, 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Book Antiqua", Font.BOLD, 14));

        // Hover effect, changes the color of the button to indicate that the cursor is getting the hitbox
        button.addMouseListener(new MouseAdapter() {
            @Override
            //An event when the cursor touches the hitbox
            public void mouseEntered(MouseEvent e) {
                button.setForeground(CRYSTAL_BLUE);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(CRYSTAL_BLUE, 2),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)
                ));
            }
            //Changes colors back to normal
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(MAGIC_PURPLE, 2),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)
                ));
            }
        });

        return button;
    }

    // Calls the main class which contains the database with the names, then it creates a name using the methiod
    private void randomizeName() {
        nameField.setText(Main.randomizeName());
    }

    // Chooses a random rarity and sends the combo box to display it
    private void randomizeRarity() {
        Random rand = new Random();
        rarityCombo.setSelectedIndex(rand.nextInt(rarityCombo.getItemCount()));
    }

    //Clears all the items from the arrayList in the main method
    private void removeItems(){
        Main.items.clear();
        updatePreview();
        itemDisplay.setText("Destroyed all items successfully");
    }

    // Handles the item preview and updates all necesssary Ui things when infromation is changed
    private void updatePreview() {
        itemDisplay.removeAll();
        itemDisplay.setBackground(DARK_PURPLE);

        String name = nameField.getText();
        String description = descriptionField.getText();
        String lore = loreArea.getText();
        String rarity = (String) rarityCombo.getSelectedItem();

        if(rarity.equals("Common")) rarityCombo.setForeground(RARITY_COLORS[0]);
        if(rarity.equals("Uncommon")) rarityCombo.setForeground(RARITY_COLORS[1]);
        if(rarity.equals("Rare")) rarityCombo.setForeground(RARITY_COLORS[2]);
        if(rarity.equals("Epic")) rarityCombo.setForeground(RARITY_COLORS[3]);
        if(rarity.equals("Legendary")) rarityCombo.setForeground(RARITY_COLORS[4]);
        rarityCombo.setBackground(PARCHMENT);

        //How the item's sheet would look like
        String preview = "✨ " + name + " ✨\n" +
                "⚔️ Rarity: " + rarity + "\n" +
                "📖 Description: " + description + "\n";

        if (!lore.isEmpty()) {
            preview += "\n📜 Lore:\n" + lore;
        }
        itemDisplay.revalidate();
        itemDisplay.setText(preview);

        numberItems.setText("Count: " + Main.items.size());
    }

    // Creates an item using the specified name, description, rarity and lore returns if no info is added
    private void createItem() {
        if(nameField.getText().isEmpty()){
            itemDisplay.setText("Invalid Entry. Please state the name of the file");
            return;
        }


        Main.addItem(new Item(nameField.getText(), descriptionField.getText(), (String) rarityCombo.getSelectedItem(), loreArea.getText()));

        // Simulates the animation of when the item is forged
        createButton.setText("✨ Item Forged! ✨");
        Timer timer = new Timer(1500, e -> {
            createButton.setText("Forge Item");
        });
        timer.setRepeats(false);
        timer.start();
        updatePreview();
    }

    //Clears all input fields and text areas and returns the rarity to common
    private void clearFields(){
        nameField.setText("");
        descriptionField.setText("");
        rarityCombo.setSelectedIndex(0);
        loreArea.setText("");

        itemDisplay.removeAll();
        itemDisplay.setBackground(DARK_PURPLE);
    }

    //Calls the main method function that writes the text file with all of the items.
    private void writeFile(String path){
        displayItems();
        itemDisplay.setText(Main.writeFile(path));
    }

    //Calls the main method to read the file specified in the path input field
    private void readFile(String path){
        itemDisplay.setText(Main.readFile(path));
        numberItems.setText("Count: " + Main.items.size());
    }

    //Displays the items in the file preview section
    private void displayItems(){
        itemDisplay.removeAll();
        itemDisplay.setBackground(DARK_PURPLE);
        itemDisplay.setText(Main.DisplayItems());
    }

    private class RarityComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (index >= 0 && index < RARITY_COLORS.length) {
                label.setForeground(RARITY_COLORS[index]);
            }
            label.setFont(new Font("Book Antiqua", Font.BOLD, 14));
            return label;
        }
    }

}

// Simple document listener for field changes
class SimpleDocumentListener implements javax.swing.event.DocumentListener {
    private final Runnable callback;


    public SimpleDocumentListener(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        callback.run();
    }

    @Override
    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        callback.run();
    }

    @Override
    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        callback.run();
    }
}
