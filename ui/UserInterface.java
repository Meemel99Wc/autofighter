package autofighter.ui;

import autofighter.AutoFighter;
import autofighter.FightProfile;
import autofighter.ui.util.CombatTarget;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.osrsbots.orb.api.interactables.world.locations.Banks;
import com.osrsbots.orb.api.util.TaskManager;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Locale;

@SuppressWarnings({"unused", "unsafe", "unchecked"})
public class UserInterface extends JPanel {

    public final TaskManager timer = new TaskManager();

    public WorldPoint location = null;

    public final DefaultListModel<String> lootListModel = new DefaultListModel<>();

    public final DefaultListModel<String> bankListModel = new DefaultListModel<>();

    public final DefaultListModel<String> prayerListModel = new DefaultListModel<>();

    public final DefaultListModel<CombatTarget> targetListModel = new DefaultListModel<>();

    public final DefaultComboBoxModel<FightProfile> profileChoiceModel = new DefaultComboBoxModel<>();

    public boolean setTargets;

    public final ButtonGroup bg = new ButtonGroup();

    JPanel root;
    JTabbedPane tabs;
    JButton setLocBtn;
    JSlider combatZone;
    JButton saveProfileBtn;
    JTextField newProfileName;
    JPanel loot;
    JComboBox<String> buryBones;
    JCheckBox useBankBtn;
    JComboBox<Banks.Location> bankChoice;
    JCheckBox bankNoFoodBtn;
    JButton startBtn;
    JCheckBox lootInCmb;
    JButton addLootBtn;
    JList<String> lootList;
    JTextField lootItemName;
    JButton removeLootBtn;
    JLabel lootLbl;
    JTextField bankItemName;
    JButton addBankBtn;
    JList<String> bankList;
    JCheckBox bankInvFullBtn;
    JLabel bankOrLbl;
    JButton removeBankBtn;
    JLabel bankLbl;
    JButton loadProfileBtn;
    JComboBox<FightProfile> profileChoice;
    JButton setTargetsBtn;
    JButton clearTargetsBtn;
    JList<CombatTarget> targetsList;
    JLabel combatZoneLbl;
    JLabel combatLocLbl;
    public JLabel combatTargetLbl;
    JPanel startPanel;
    JPanel combatPanel;
    private JPanel bankPanel;
    JPanel statPanel;
    public JLabel kills;
    public JLabel targetsStolen;
    public JLabel foodConsumed;
    public JLabel itemsLooted;
    private JLabel itemsLootedLbl;
    private JLabel potionSips;
    public JLabel bankTrips;
    private JLabel breaksTaken;
    public JLabel statusLbl;
    private JLabel totalBreakTime;
    public JLabel levelsGained;
    public JLabel bonesBuried;
    private JLabel worldHops;
    private JPanel boostPanel;
    JCheckBox tooManyPlayersBtn;
    JCheckBox usePotionsBtn;
    JButton addPrayerBtn;
    JButton removePrayerBtn;
    JTextField prayerNameTxt;
    JLabel boostLbl;
    JList<String> prayerList;
    private JPanel worldPanel;
    JSlider tooManyPlayersSlider;
    JCheckBox stolenTargetsBtn;
    JSlider stolenTargetsSlider;
    JCheckBox noTargetBtn;
    JSlider noTargetSlider;
    JCheckBox periodicBtn;
    JSlider periodicSlider;

    final AutoFighter script;

    public UserInterface(final AutoFighter script) {
        this.script = script;
        setLayout(new BorderLayout());

        setCursors();

        LootPanel.init(this);
        BankPanel.init(this);
        BoostPanel.init(this);
        CombatPanel.init(this);
        WorldPanel.init(this);
        StartPanel.init(this);

        add(root, BorderLayout.CENTER);
    }

    private void setCursors() {
        final Cursor c = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

        // TABS
        tabs.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int tabIndex = tabs.indexAtLocation(e.getX(), e.getY());
                if (tabIndex >= 0) {
                    tabs.setCursor(c);
                } else {
                    tabs.setCursor(Cursor.getDefaultCursor());
                }
            }
        });


        // LOOT
        buryBones.setCursor(c);
        lootInCmb.setCursor(c);
        addLootBtn.setCursor(c);
        removeLootBtn.setCursor(c);

        // BANK
        addBankBtn.setCursor(c);
        removeBankBtn.setCursor(c);
        useBankBtn.setCursor(c);
        bankNoFoodBtn.setCursor(c);
        bankInvFullBtn.setCursor(c);


        // BOOST
        usePotionsBtn.setCursor(c);
        addPrayerBtn.setCursor(c);
        removePrayerBtn.setCursor(c);

        // COMBAT
        setLocBtn.setCursor(c);
        setTargetsBtn.setCursor(c);
        clearTargetsBtn.setCursor(c);

        // WORLD
        tooManyPlayersBtn.setCursor(c);
        stolenTargetsBtn.setCursor(c);
        noTargetBtn.setCursor(c);
        periodicBtn.setCursor(c);

        // START
        profileChoice.setCursor(c);
        saveProfileBtn.setCursor(c);
        loadProfileBtn.setCursor(c);
        startBtn.setCursor(c);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /** Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new BorderLayout(0, 0));
        tabs = new JTabbedPane();
        tabs.setName("stats");
        tabs.setTabPlacement(3);
        root.add(tabs, BorderLayout.CENTER);
        bankPanel = new JPanel();
        bankPanel.setLayout(new GridLayoutManager(7, 2, new Insets(10, 0, 10, 0), -1, 10));
        tabs.addTab("Bank", bankPanel);
        bankLbl = new JLabel();
        Font bankLblFont = this.$$$getFont$$$("Arial", Font.BOLD, 16, bankLbl.getFont());
        if (bankLblFont != null) bankLbl.setFont(bankLblFont);
        bankLbl.setForeground(new Color(-1769418));
        bankLbl.setText("0 Items To Withdraw");
        bankPanel.add(bankLbl, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        bankPanel.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        removeBankBtn = new JButton();
        removeBankBtn.setBackground(new Color(-13947600));
        removeBankBtn.setEnabled(true);
        removeBankBtn.setForeground(new Color(-2104859));
        removeBankBtn.setText("Remove Item");
        panel1.add(removeBankBtn, BorderLayout.SOUTH);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setFocusable(false);
        scrollPane1.setHorizontalScrollBarPolicy(31);
        scrollPane1.setVerticalScrollBarPolicy(20);
        panel1.add(scrollPane1, BorderLayout.CENTER);
        bankList = new JList();
        scrollPane1.setViewportView(bankList);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        bankPanel.add(panel2, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        useBankBtn = new JCheckBox();
        useBankBtn.setText("Use Bank");
        panel2.add(useBankBtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bankChoice = new JComboBox();
        panel2.add(bankChoice, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(125, -1), new Dimension(125, -1), new Dimension(125, -1), 0, false));
        bankOrLbl = new JLabel();
        bankOrLbl.setEnabled(false);
        Font bankOrLblFont = this.$$$getFont$$$("Arial", Font.BOLD, 16, bankOrLbl.getFont());
        if (bankOrLblFont != null) bankOrLbl.setFont(bankOrLblFont);
        bankOrLbl.setText("or");
        bankPanel.add(bankOrLbl, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bankInvFullBtn = new JCheckBox();
        bankInvFullBtn.setEnabled(false);
        Font bankInvFullBtnFont = this.$$$getFont$$$("Arial", -1, -1, bankInvFullBtn.getFont());
        if (bankInvFullBtnFont != null) bankInvFullBtn.setFont(bankInvFullBtnFont);
        bankInvFullBtn.setText("Inventory is full");
        bankPanel.add(bankInvFullBtn, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bankNoFoodBtn = new JCheckBox();
        bankNoFoodBtn.setEnabled(false);
        Font bankNoFoodBtnFont = this.$$$getFont$$$("Arial", -1, -1, bankNoFoodBtn.getFont());
        if (bankNoFoodBtnFont != null) bankNoFoodBtn.setFont(bankNoFoodBtnFont);
        bankNoFoodBtn.setSelected(false);
        bankNoFoodBtn.setText("Player is out of food");
        bankPanel.add(bankNoFoodBtn, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        bankPanel.add(panel3, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bankItemName = new JTextField();
        bankItemName.setText("Amount:Name");
        bankItemName.setToolTipText("Amount:Name");
        panel3.add(bankItemName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), new Dimension(100, -1), 0, false));
        addBankBtn = new JButton();
        addBankBtn.setText("Add Item");
        panel3.add(addBankBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), new Dimension(100, -1), 0, false));
        boostPanel = new JPanel();
        boostPanel.setLayout(new GridLayoutManager(5, 1, new Insets(10, 0, 10, 0), -1, 10));
        tabs.addTab("Boost", boostPanel);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        boostPanel.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        removePrayerBtn = new JButton();
        removePrayerBtn.setBackground(new Color(-13947600));
        removePrayerBtn.setEnabled(true);
        removePrayerBtn.setForeground(new Color(-2104859));
        removePrayerBtn.setText("Remove Prayer");
        panel4.add(removePrayerBtn, BorderLayout.SOUTH);
        final JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setFocusable(false);
        scrollPane2.setHorizontalScrollBarPolicy(31);
        scrollPane2.setVerticalScrollBarPolicy(20);
        panel4.add(scrollPane2, BorderLayout.CENTER);
        prayerList = new JList();
        scrollPane2.setViewportView(prayerList);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        boostPanel.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prayerNameTxt = new JTextField();
        prayerNameTxt.setText("Name");
        prayerNameTxt.setToolTipText("Amount:Name");
        panel5.add(prayerNameTxt, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, new Dimension(100, -1), new Dimension(100, -1), new Dimension(100, -1), 0, false));
        addPrayerBtn = new JButton();
        addPrayerBtn.setText("Add Prayer");
        panel5.add(addPrayerBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, new Dimension(100, -1), new Dimension(100, -1), new Dimension(100, -1), 0, false));
        boostLbl = new JLabel();
        Font boostLblFont = this.$$$getFont$$$("Arial", Font.BOLD, 16, boostLbl.getFont());
        if (boostLblFont != null) boostLbl.setFont(boostLblFont);
        boostLbl.setForeground(new Color(-1));
        boostLbl.setText("Using 0 Prayers");
        boostPanel.add(boostLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        boostPanel.add(panel6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-1));
        label1.setText("Use Potions");
        panel6.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usePotionsBtn = new JCheckBox();
        Font usePotionsBtnFont = this.$$$getFont$$$("Arial", Font.ITALIC, 16, usePotionsBtn.getFont());
        if (usePotionsBtnFont != null) usePotionsBtn.setFont(usePotionsBtnFont);
        usePotionsBtn.setForeground(new Color(-1769418));
        usePotionsBtn.setText("Disabled");
        panel6.add(usePotionsBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Arial", Font.ITALIC, 16, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-1));
        label2.setHorizontalAlignment(0);
        label2.setHorizontalTextPosition(0);
        label2.setText("Add desired potion(s) to Bank list");
        label2.setVerticalAlignment(0);
        label2.setVerticalTextPosition(0);
        boostPanel.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        combatPanel = new JPanel();
        combatPanel.setLayout(new GridLayoutManager(7, 1, new Insets(10, 0, 10, 0), -1, 10));
        tabs.addTab("Combat", combatPanel);
        combatZone = new JSlider();
        combatZone.setMajorTickSpacing(5);
        combatZone.setMaximum(50);
        combatZone.setMinimum(5);
        combatZone.setMinorTickSpacing(1);
        combatZone.setPaintLabels(true);
        combatZone.setPaintTicks(true);
        combatZone.setSnapToTicks(true);
        combatZone.setValue(15);
        combatPanel.add(combatZone, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        combatZoneLbl = new JLabel();
        Font combatZoneLblFont = this.$$$getFont$$$("Arial", Font.BOLD, 16, combatZoneLbl.getFont());
        if (combatZoneLblFont != null) combatZoneLbl.setFont(combatZoneLblFont);
        combatZoneLbl.setForeground(new Color(-1));
        combatZoneLbl.setText("Combat Zone - 15 Tiles");
        combatPanel.add(combatZoneLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        combatTargetLbl = new JLabel();
        Font combatTargetLblFont = this.$$$getFont$$$("Arial", Font.BOLD, 16, combatTargetLbl.getFont());
        if (combatTargetLblFont != null) combatTargetLbl.setFont(combatTargetLblFont);
        combatTargetLbl.setForeground(new Color(-1769418));
        combatTargetLbl.setText("No Targets");
        combatPanel.add(combatTargetLbl, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        combatLocLbl = new JLabel();
        Font combatLocLblFont = this.$$$getFont$$$("Arial", Font.BOLD, 16, combatLocLbl.getFont());
        if (combatLocLblFont != null) combatLocLbl.setFont(combatLocLblFont);
        combatLocLbl.setForeground(new Color(-1769418));
        combatLocLbl.setText("No Location");
        combatPanel.add(combatLocLbl, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setLocBtn = new JButton();
        setLocBtn.setForeground(new Color(-1));
        setLocBtn.setText("Set Location");
        combatPanel.add(setLocBtn, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new BorderLayout(0, 0));
        combatPanel.add(panel7, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane3 = new JScrollPane();
        scrollPane3.setFocusable(false);
        scrollPane3.setHorizontalScrollBarPolicy(31);
        scrollPane3.setVerticalScrollBarPolicy(20);
        panel7.add(scrollPane3, BorderLayout.CENTER);
        targetsList = new JList();
        targetsList.setFocusable(false);
        scrollPane3.setViewportView(targetsList);
        clearTargetsBtn = new JButton();
        clearTargetsBtn.setBackground(new Color(-13947600));
        clearTargetsBtn.setEnabled(true);
        clearTargetsBtn.setForeground(new Color(-2104859));
        clearTargetsBtn.setText("Clear Targets");
        panel7.add(clearTargetsBtn, BorderLayout.SOUTH);
        setTargetsBtn = new JButton();
        setTargetsBtn.setForeground(new Color(-1));
        setTargetsBtn.setText("Set Target(s)");
        combatPanel.add(setTargetsBtn, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loot = new JPanel();
        loot.setLayout(new GridLayoutManager(6, 2, new Insets(10, 0, 25, 0), -1, 10));
        tabs.addTab("Loot", loot);
        lootLbl = new JLabel();
        Font lootLblFont = this.$$$getFont$$$("Arial", Font.BOLD, 16, lootLbl.getFont());
        if (lootLblFont != null) lootLbl.setFont(lootLblFont);
        lootLbl.setForeground(new Color(-1769418));
        lootLbl.setText("0 Lootables");
        loot.add(lootLbl, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        loot.add(panel8, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lootItemName = new JTextField();
        lootItemName.setText("Name");
        lootItemName.setToolTipText("Item Name");
        panel8.add(lootItemName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), new Dimension(100, -1), 0, false));
        addLootBtn = new JButton();
        addLootBtn.setText("Add Item");
        panel8.add(addLootBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), new Dimension(100, -1), 0, false));
        lootInCmb = new JCheckBox();
        lootInCmb.setText("Loot In Combat");
        loot.add(lootInCmb, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new BorderLayout(0, 0));
        loot.add(panel9, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        removeLootBtn = new JButton();
        removeLootBtn.setBackground(new Color(-13947600));
        removeLootBtn.setEnabled(true);
        removeLootBtn.setForeground(new Color(-2104859));
        removeLootBtn.setText("Remove Item");
        panel9.add(removeLootBtn, BorderLayout.SOUTH);
        final JScrollPane scrollPane4 = new JScrollPane();
        scrollPane4.setHorizontalScrollBarPolicy(31);
        scrollPane4.setVerticalScrollBarPolicy(20);
        panel9.add(scrollPane4, BorderLayout.CENTER);
        lootList = new JList();
        lootList.setFocusable(false);
        scrollPane4.setViewportView(lootList);
        buryBones = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("DISABLED");
        defaultComboBoxModel1.addElement("Full Inventory");
        defaultComboBoxModel1.addElement("Before Combat");
        defaultComboBoxModel1.addElement("During Combat");
        defaultComboBoxModel1.addElement("After Combat");
        defaultComboBoxModel1.addElement("RANDOM");
        buryBones.setModel(defaultComboBoxModel1);
        loot.add(buryBones, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Bury Bones");
        loot.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        worldPanel = new JPanel();
        worldPanel.setLayout(new GridLayoutManager(9, 1, new Insets(10, 0, 10, 0), -1, 10));
        tabs.addTab("World Hop", worldPanel);
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setForeground(new Color(-1));
        label4.setText("World Hop");
        worldPanel.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tooManyPlayersSlider = new JSlider();
        tooManyPlayersSlider.setMajorTickSpacing(2);
        tooManyPlayersSlider.setMaximum(15);
        tooManyPlayersSlider.setMinimum(1);
        tooManyPlayersSlider.setMinorTickSpacing(1);
        tooManyPlayersSlider.setPaintLabels(false);
        tooManyPlayersSlider.setPaintTicks(false);
        tooManyPlayersSlider.setPaintTrack(true);
        tooManyPlayersSlider.setSnapToTicks(true);
        tooManyPlayersSlider.setToolTipText("Nearby Players");
        tooManyPlayersSlider.setValue(5);
        worldPanel.add(tooManyPlayersSlider, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tooManyPlayersBtn = new JCheckBox();
        tooManyPlayersBtn.setText("If 5 or more players are nearby");
        worldPanel.add(tooManyPlayersBtn, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stolenTargetsSlider = new JSlider();
        stolenTargetsSlider.setMajorTickSpacing(5);
        stolenTargetsSlider.setMaximum(25);
        stolenTargetsSlider.setMinimum(1);
        stolenTargetsSlider.setMinorTickSpacing(1);
        stolenTargetsSlider.setPaintLabels(false);
        stolenTargetsSlider.setPaintTicks(false);
        stolenTargetsSlider.setPaintTrack(true);
        stolenTargetsSlider.setSnapToTicks(true);
        stolenTargetsSlider.setToolTipText("Stolen Targets");
        stolenTargetsSlider.setValue(5);
        worldPanel.add(stolenTargetsSlider, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stolenTargetsBtn = new JCheckBox();
        stolenTargetsBtn.setText("More than 10 targets are stolen");
        worldPanel.add(stolenTargetsBtn, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        noTargetSlider = new JSlider();
        noTargetSlider.setMajorTickSpacing(60);
        noTargetSlider.setMaximum(300);
        noTargetSlider.setMinimum(30);
        noTargetSlider.setMinorTickSpacing(5);
        noTargetSlider.setPaintLabels(false);
        noTargetSlider.setPaintTicks(false);
        noTargetSlider.setPaintTrack(true);
        noTargetSlider.setSnapToTicks(true);
        noTargetSlider.setToolTipText("Time");
        noTargetSlider.setValue(120);
        worldPanel.add(noTargetSlider, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        noTargetBtn = new JCheckBox();
        noTargetBtn.setText("Unable to find a target after 60s");
        worldPanel.add(noTargetBtn, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        periodicBtn = new JCheckBox();
        periodicBtn.setText("After about 60 minutes");
        worldPanel.add(periodicBtn, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        periodicSlider = new JSlider();
        periodicSlider.setMajorTickSpacing(60);
        periodicSlider.setMaximum(300);
        periodicSlider.setMinimum(30);
        periodicSlider.setMinorTickSpacing(5);
        periodicSlider.setPaintLabels(false);
        periodicSlider.setPaintTicks(false);
        periodicSlider.setPaintTrack(true);
        periodicSlider.setSnapToTicks(true);
        periodicSlider.setToolTipText("Time");
        periodicSlider.setValue(120);
        worldPanel.add(periodicSlider, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startPanel = new JPanel();
        startPanel.setLayout(new GridLayoutManager(5, 1, new Insets(10, 0, 10, 0), -1, 10));
        startPanel.setForeground(new Color(-15080192));
        tabs.addTab("Start", startPanel);
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Ready? Set.");
        startPanel.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        startPanel.add(panel10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel10.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "New Profile", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        newProfileName = new JTextField();
        newProfileName.setHorizontalAlignment(0);
        panel10.add(newProfileName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveProfileBtn = new JButton();
        saveProfileBtn.setText("Save Profile");
        panel10.add(saveProfileBtn, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        startPanel.add(panel11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Saved Profiles", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        profileChoice = new JComboBox();
        profileChoice.setForeground(new Color(-1));
        panel11.add(profileChoice, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadProfileBtn = new JButton();
        loadProfileBtn.setText("Load Profile");
        panel11.add(loadProfileBtn, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startBtn = new JButton();
        startBtn.setText("Lets Gooo!");
        startPanel.add(startBtn, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statPanel = new JPanel();
        statPanel.setLayout(new BorderLayout(0, 0));
        statPanel.setEnabled(false);
        statPanel.setVisible(false);
        tabs.addTab("Stats", statPanel);
        tabs.setEnabledAt(6, false);
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new BorderLayout(0, 0));
        statPanel.add(panel12, BorderLayout.CENTER);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(12, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel12.add(panel13, BorderLayout.CENTER);
        panel13.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Statistics", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel14, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        kills = new JLabel();
        Font killsFont = this.$$$getFont$$$("Arial", -1, 16, kills.getFont());
        if (killsFont != null) kills.setFont(killsFont);
        kills.setText("0 (0 per/Hr)");
        panel14.add(kills, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Kills:");
        panel14.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel15, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        targetsStolen = new JLabel();
        Font targetsStolenFont = this.$$$getFont$$$("Arial", -1, 16, targetsStolen.getFont());
        if (targetsStolenFont != null) targetsStolen.setFont(targetsStolenFont);
        targetsStolen.setText("0");
        panel15.add(targetsStolen, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("Targets Stolen:");
        panel15.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel16, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        foodConsumed = new JLabel();
        Font foodConsumedFont = this.$$$getFont$$$("Arial", -1, 16, foodConsumed.getFont());
        if (foodConsumedFont != null) foodConsumed.setFont(foodConsumedFont);
        foodConsumed.setText("0");
        panel16.add(foodConsumed, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setText("Food Consumed:");
        panel16.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel17, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        itemsLooted = new JLabel();
        Font itemsLootedFont = this.$$$getFont$$$("Arial", -1, 16, itemsLooted.getFont());
        if (itemsLootedFont != null) itemsLooted.setFont(itemsLootedFont);
        itemsLooted.setText("0 (0k)");
        panel17.add(itemsLooted, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        itemsLootedLbl = new JLabel();
        Font itemsLootedLblFont = this.$$$getFont$$$("Arial", Font.BOLD, 16, itemsLootedLbl.getFont());
        if (itemsLootedLblFont != null) itemsLootedLbl.setFont(itemsLootedLblFont);
        itemsLootedLbl.setText("Items Looted:");
        panel17.add(itemsLootedLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel18 = new JPanel();
        panel18.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel18, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        potionSips = new JLabel();
        Font potionSipsFont = this.$$$getFont$$$("Arial", -1, 16, potionSips.getFont());
        if (potionSipsFont != null) potionSips.setFont(potionSipsFont);
        potionSips.setText("0");
        panel18.add(potionSips, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setText("Potion Sips:");
        panel18.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel19 = new JPanel();
        panel19.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel19, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bankTrips = new JLabel();
        Font bankTripsFont = this.$$$getFont$$$("Arial", -1, 16, bankTrips.getFont());
        if (bankTripsFont != null) bankTrips.setFont(bankTripsFont);
        bankTrips.setText("0");
        panel19.add(bankTrips, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("Bank Trips:");
        panel19.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel20 = new JPanel();
        panel20.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel20, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        breaksTaken = new JLabel();
        Font breaksTakenFont = this.$$$getFont$$$("Arial", -1, 16, breaksTaken.getFont());
        if (breaksTakenFont != null) breaksTaken.setFont(breaksTakenFont);
        breaksTaken.setText("0");
        panel20.add(breaksTaken, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setText("Breaks Taken:");
        panel20.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel21 = new JPanel();
        panel21.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel21, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        statusLbl = new JLabel();
        Font statusLblFont = this.$$$getFont$$$("Arial", Font.BOLD, 14, statusLbl.getFont());
        if (statusLblFont != null) statusLbl.setFont(statusLblFont);
        statusLbl.setForeground(new Color(-6376867));
        statusLbl.setHorizontalAlignment(0);
        statusLbl.setHorizontalTextPosition(0);
        statusLbl.setText("AutoFighter by ORB");
        panel21.add(statusLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel22 = new JPanel();
        panel22.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel22, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        totalBreakTime = new JLabel();
        Font totalBreakTimeFont = this.$$$getFont$$$("Arial", -1, 16, totalBreakTime.getFont());
        if (totalBreakTimeFont != null) totalBreakTime.setFont(totalBreakTimeFont);
        totalBreakTime.setText("0");
        panel22.add(totalBreakTime, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setText("Total Break Time:");
        panel22.add(label12, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel23 = new JPanel();
        panel23.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel23, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        levelsGained = new JLabel();
        Font levelsGainedFont = this.$$$getFont$$$("Arial", -1, 16, levelsGained.getFont());
        if (levelsGainedFont != null) levelsGained.setFont(levelsGainedFont);
        levelsGained.setText("0");
        panel23.add(levelsGained, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        Font label13Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label13.getFont());
        if (label13Font != null) label13.setFont(label13Font);
        label13.setText("Levels Gained");
        panel23.add(label13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel24 = new JPanel();
        panel24.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel24, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bonesBuried = new JLabel();
        Font bonesBuriedFont = this.$$$getFont$$$("Arial", -1, 16, bonesBuried.getFont());
        if (bonesBuriedFont != null) bonesBuried.setFont(bonesBuriedFont);
        bonesBuried.setText("0");
        panel24.add(bonesBuried, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label14.getFont());
        if (label14Font != null) label14.setFont(label14Font);
        label14.setText("Bones Buried:");
        panel24.add(label14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel25 = new JPanel();
        panel25.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel13.add(panel25, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        worldHops = new JLabel();
        Font worldHopsFont = this.$$$getFont$$$("Arial", -1, 16, worldHops.getFont());
        if (worldHopsFont != null) worldHops.setFont(worldHopsFont);
        worldHops.setText("0");
        panel25.add(worldHops, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        Font label15Font = this.$$$getFont$$$("Arial", Font.BOLD, 16, label15.getFont());
        if (label15Font != null) label15.setFont(label15Font);
        label15.setText("World Hops:");
        panel25.add(label15, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /** @noinspection ALL */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /** @noinspection ALL */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

    public int getCombatZoneDistance() {
        return combatZone.getValue();
    }

    public ArrayList<CombatTarget> getCombatTargets() {
        final ArrayList<CombatTarget> list = new ArrayList<>();
        final int size = targetListModel.getSize();

        for (int i = 0; i < size; i++) {
            list.add(targetListModel.getElementAt(i));
        }

        return list;
    }

    public ArrayList<String> getModelList(final DefaultListModel<String> model) {
        final ArrayList<String> list = new ArrayList<>();
        final int size = model.getSize();

        for (int i = 0; i < size; i++) {
            list.add(model.getElementAt(i));
        }

        return list;
    }

    public boolean useBank() {
        return useBankBtn.isSelected();

    }

    public boolean bankOnFullInv() {
        return bankInvFullBtn.isSelected();
    }

    public @Nullable WorldPoint getBank() {
        final Object o = bankChoice.getSelectedItem();
        if (o instanceof Banks.Location) {
            return ((Banks.Location) o).worldArea.toWorldPoint();
        }

        return null;


    }

    public ArrayList<String> getBankItems() {
        return getModelList(bankListModel);
    }

    public boolean lootItems() {
        return !lootListModel.isEmpty();
    }

    public boolean lootInCombat() {
        return lootInCmb.isSelected();
    }

    public ArrayList<String> getLootItems() {
        return getModelList(lootListModel);
    }

    public int buryBones() {
        return buryBones.getSelectedIndex();
    }

    public WorldArea getCombatZone() {
        final int d = getCombatZoneDistance();
        final int i = d / 2;

        return new WorldArea(
                location.getX() - i,
                location.getY() - i,
                d,
                d,
                location.getPlane()
        );
    }

    public boolean usePotions() {
        return usePotionsBtn.isSelected();
    }
}
