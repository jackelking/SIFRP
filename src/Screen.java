import com.sun.imageio.plugins.jpeg.JPEG;
import com.sun.javaws.exceptions.ExitException;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

/**
 * Created by arredon on 10/27/2016.
 */
public class Screen extends JFrame {
    private int xMax;     //x dimension of frame
    private int yMax;      //y dimension of frame
    private int halfX;
    private Point labelPos;             //Position of screen label (i.e. "main menu", "account menu", etc.)
    private Dimension buttonSize;       //size of buttons
    private Color background;           //color of background

    private JLabel screenName;

    private Container c;

    private HouseList baseHouseList;

    public Screen(String title, int xMax, int yMax) {
        super(title);
        this.xMax = xMax;
        this.yMax = yMax;
        setSize(xMax, yMax);
        halfX = xMax/2 - 100;

        buttonSize = new Dimension(200,30);
        labelPos = new Point(halfX, 20);
        background = new Color(170,170,120);

        //Screen name
        screenName = new JLabel();
        screenName.setSize(buttonSize);
        screenName.setLocation(labelPos);

        c = null;
        setLayout(null);

        initializeHouseList();

        mainMenu();
        //houseMenu();
    }

    private void calculateSize() {
        setSize(xMax = getWidth(), yMax = getHeight());
        halfX = xMax/2 - 100;
    }

    private void mainMenu() {
        repaint();
        getContentPane().removeAll();

        screenName.setName("Main Menu");
        //House menu button
        JButton house = new JButton("House Menu");
        house.setSize(buttonSize);
        house.setLocation(halfX, 250);
        //Character Menu Screen
        JButton character = new JButton("Character Menu");
        character.setSize(buttonSize);
        character.setLocation(halfX, 290);
        //exit program
        JButton exit = new JButton("Exit");
        exit.setSize(buttonSize);
        exit.setLocation(halfX, yMax - 150);

        c = getContentPane();

        c.setBackground(background);
        c.add(house);
        c.add(character);
        c.add(exit);
        c.add(screenName);

        character.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                characterMenu();
            }
        });

        house.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                houseMenu();
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        /*
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                calculateSize();
                mainMenu();
            }
        });
        */
    }

    /*
    Needs
        * House information     - done
        * House Stats           - done
        * steward               - done
        * heir(s)               - done
        * residents             -
        * historical events     - done
        * holdings              - done
        * Banner house(s)       - done
 */
    private void viewHouse(HouseNode houseNode) {
        repaint();
        getContentPane().removeAll();

        //Screen name
        screenName.setName("House Menu");
        //Back button
        JButton back = new JButton("Back");
        back.setSize(buttonSize);
        back.setLocation(20,10);

        //next button
        JButton next = new JButton("Next");
        next.setSize(buttonSize);
        next.setLocation(xMax - 400, yMax - 100);
        //previous button
        JButton previous = new JButton("Previous");
        previous.setSize(buttonSize);
        previous.setLocation(xMax - 700, yMax - 100);

        Dimension labelSize = new Dimension(100, 20);
        int panelHeight = yMax - 200;

        //HouseNode current = houseList.getCurrent();

        //historical events
        JLabel historyLabel = new JLabel("History");
        historyLabel.setSize(labelSize);
        historyLabel.setLocation(20, 210);
        JTextArea historyArea = new JTextArea(houseNode.printHistory());
        historyArea.setSize(200, 200);
        historyArea.setEditable(false);
        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setSize(200, 110);
        historyScroll.setLocation(20, 230);

        //return to Liege House
        JButton liegeHouse = new JButton("Liege House");
        liegeHouse.setSize(buttonSize);
        liegeHouse.setLocation(20, 10);

        //Displays Lord/Lady of the house
        JLabel stewardLabel = new JLabel("Steward");
        stewardLabel.setLocation(20, 150);
        stewardLabel.setSize(labelSize);
        JButton stewardButton = new JButton();
        if (houseNode.getSteward() != null) {
            stewardButton.setText(houseNode.getSteward().getName());
        }
        stewardButton.setSize(buttonSize);
        stewardButton.setLocation(20, 170);

        //displays house information
        JLabel houseArea = new JLabel("House Information");
        houseArea.setSize(200, 20);
        houseArea.setLocation(20,20);

        JTextArea houseInfo = new JTextArea(houseNode.printHouseInfo());
        houseInfo.setLocation(20,40);
        houseInfo.setSize(200, 100);
        houseInfo.setEditable(false);

        //stats
        JLabel statsArea = new JLabel("House Stats");
        statsArea.setSize(200,20);
        statsArea.setLocation(250, 20);
        //Defense, Influence, Lands, Law, Population, Power, Wealth
        int row = 10;
        JLabel defenseLabel = new JLabel("Defense " + houseNode.getDefense());
        defenseLabel.setLocation(0, row);
        defenseLabel.setSize(labelSize);

        JTextArea temp = new JTextArea(houseNode.printDefenseHoldings());
        temp.setEditable(false);
        temp.getPreferredSize();

        JScrollPane defenseHoldings = new JScrollPane(temp);
        defenseHoldings.setLocation(100, row);
        defenseHoldings.setSize(450, 100);
        row += 120;

        //Influence / Heirs
        JLabel heirsLabel = new JLabel("Influence " + houseNode.getInfluence());
        heirsLabel.setLocation(0, row);
        heirsLabel.setSize(labelSize);

        temp = new JTextArea(houseNode.printHeirs());
        temp.getPreferredSize();
        temp.setEditable(false);

        JButton heirsButton = new JButton("Heirs");
        heirsButton.setSize(buttonSize);
        heirsButton.setLocation(350, row);

        JScrollPane heirs = new JScrollPane(temp);
        heirs.setLocation(100, row);
        heirs.setSize(240, 75);
        row += 95;

        //Lands
        JLabel landsLabel = new JLabel("Lands " + houseNode.getLands());
        landsLabel.setLocation(0, row);
        landsLabel.setSize(labelSize);

        temp = new JTextArea(houseNode.printLandHoldings());
        temp.getPreferredSize();
        temp.setEditable(false);

        JScrollPane landsScroll = new JScrollPane(temp);
        landsScroll.setLocation(100, row);
        landsScroll.setSize(450, 100);
        row += 120;
        //Law
        JLabel lawLabel = new JLabel("Law " + houseNode.getLaw());
        lawLabel.setLocation(0, row);
        lawLabel.setSize(labelSize);

        temp = new JTextArea(houseNode.printLawHoldings());
        temp.getPreferredSize();
        temp.setEditable(false);

        JScrollPane lawScroll = new JScrollPane(temp);
        lawScroll.setLocation(100, row);
        lawScroll.setSize(450, 20);
        row += 40;

        //population
        JLabel populationLabel = new JLabel("Population " + houseNode.getPopulation());
        populationLabel.setLocation(0, row);
        populationLabel.setSize(labelSize);

        temp = new JTextArea(houseNode.printPopulationHoldings());
        temp.getPreferredSize();
        temp.setEditable(false);

        JScrollPane populationScroll = new JScrollPane(temp);
        populationScroll.setLocation(100, row);
        populationScroll.setSize(450, 20);
        row += 40;
        //power
        JLabel powerLabel = new JLabel("Power " + houseNode.getPower());
        powerLabel.setLocation(0, row);
        powerLabel.setSize(labelSize);

        //display each Banner house as a button that links to a new page for that house
        temp = new JTextArea(houseNode.printBannerNames());
        temp.getPreferredSize();
        temp.setEditable(false);

        JButton bannerButton = new JButton("Banners");
        bannerButton.setSize(buttonSize);
        bannerButton.setLocation(350, row);

        JScrollPane powerScroll = new JScrollPane(temp);
        powerScroll.setLocation(100, row);
        powerScroll.setSize(240, 40);
        row += 60;
        //wealth
        JLabel wealthLabel = new JLabel("Wealth " + houseNode.getWealth());
        wealthLabel.setLocation(0, row);
        wealthLabel.setSize(labelSize);

        temp = new JTextArea(houseNode.printWealthHoldings());
        temp.getPreferredSize();
        temp.setEditable(false);

        JScrollPane wealthScroll = new JScrollPane(temp);
        wealthScroll.setLocation(100, row);
        wealthScroll.setSize(450, 100);


        JPanel statsPanel = new JPanel(null);
        statsPanel.setPreferredSize(new Dimension(400, 600));

        statsPanel.add(bannerButton);
        statsPanel.add(wealthLabel);
        statsPanel.add(wealthScroll);
        statsPanel.add(powerLabel);
        statsPanel.add(powerScroll);
        statsPanel.add(populationLabel);
        statsPanel.add(populationScroll);
        statsPanel.add(lawLabel);
        statsPanel.add(lawScroll);
        statsPanel.add(landsLabel);
        statsPanel.add(landsScroll);
        statsPanel.add(heirsButton);
        statsPanel.add(heirsLabel);
        statsPanel.add(heirs);
        statsPanel.add(defenseLabel);
        statsPanel.add(defenseHoldings);

        JScrollPane statsScroll = new JScrollPane(statsPanel);
        statsScroll.setSize(new Dimension(600,panelHeight - 100));
        statsScroll.setLocation(250,40);
        statsScroll.setWheelScrollingEnabled(true);
        statsScroll.getVerticalScrollBar().setUnitIncrement(16);

        //
        JPanel mainPanel = new JPanel(null);

        mainPanel.add(stewardButton);
        mainPanel.add(stewardLabel);
        mainPanel.add(historyScroll);
        mainPanel.add(historyLabel);
        mainPanel.add(statsArea);
        mainPanel.add(houseInfo);
        mainPanel.add(houseArea);
        mainPanel.add(statsScroll);

        statsScroll.revalidate();

        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setViewportView(mainPanel);
        mainScroll.setLocation(50, 50);
        mainScroll.setSize(xMax - 100, panelHeight );
        mainScroll.setWheelScrollingEnabled(true);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);

        c = getContentPane();

        if (houseNode.hasLiegeHouse())
            c.add(liegeHouse);
        else
            c.add(back);

        c.setBackground(background);
        c.add(screenName);
        c.add(mainScroll);
        c.add(next);
        c.add(previous);

        mainScroll.revalidate();
        repaint();

        heirsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CharacterNode characterNode = houseNode.getHeirs().getCurrent();
                characterSheet(characterNode, houseNode);
            }
        });

        stewardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CharacterNode characterNode = new CharacterNode(houseNode.getSteward());
                characterSheet(characterNode, houseNode);
            }
        });

        liegeHouse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHouse(baseHouseList.getCurrent());
            }
        });

        bannerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (houseNode.hasBanners())
                    viewHouse(houseNode.getBanners().getCurrent());
            }
        });

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (houseNode.getNext() != null)
                    viewHouse(houseNode.getNext());
            }
        });

        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (houseNode.getPrev() != null)
                    viewHouse(houseNode.getPrev());
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu();
            }
        });

        /*
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                calculateSize();
                houseMenu(houseNode);
            }
        });
        */
    }

    private void houseMenu() {
        repaint();
        getContentPane().removeAll();

        JButton back = new JButton("Back");
        back.setSize(buttonSize);
        back.setLocation(20,10);

        //create new house
        JButton createHouse = new JButton("Create New House");
        createHouse.setSize(buttonSize);
        createHouse.setLocation(halfX, 200);
        //view sample
        JButton viewSample = new JButton("View Sample");
        viewSample.setLocation(halfX, 250);
        viewSample.setSize(buttonSize);
        //choose from list


        c = getContentPane();

        c.add(createHouse);
        c.add(viewSample);
        c.add(back);


        createHouse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouse();
            }
        });

        viewSample.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHouse(baseHouseList.getCurrent());
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu();
            }
        });
    }

    private void initializeHouseList() {
        HouseNode temp;
        baseHouseList = new HouseList();
        for (int i = 0; i < 10; ++i) {
            temp = new HouseNode();
            temp.generateRandHouse();
            baseHouseList.insert(temp);
        }
    }

    /*  In character menu should be able to
            1. Create new Character
            2. Create list of random characters
            3. Display list of characters
            4. Edit Character
            5. Delete Character
     */
    private void characterMenu() {
        repaint();
        getContentPane().removeAll();

        JButton back = new JButton("Back");
        back.setSize(buttonSize);
        back.setLocation(20,10);

        JButton newCharacter = new JButton("New Character");
        newCharacter.setSize(buttonSize);
        newCharacter.setLocation(halfX, 150);

        JButton randCharacters = new JButton("Random Characters");
        randCharacters.setSize(buttonSize);
        randCharacters.setLocation(halfX, 190);

        JButton editCharacter = new JButton("Edit Character");
        editCharacter.setSize(buttonSize);
        editCharacter.setLocation(halfX, 230);

        JButton deleteCharacter = new JButton("Delete Character");
        deleteCharacter.setSize(buttonSize);
        deleteCharacter.setLocation(halfX, 270);

        JButton characterSheet = new JButton("Character Sheet");
        characterSheet.setSize(buttonSize);
        characterSheet.setLocation(halfX, 310);


        c = getContentPane();
        c.setBackground(background);
        c.add(back);
        c.add(newCharacter);
        c.add(randCharacters);
        c.add(editCharacter);
        c.add(deleteCharacter);
        c.add(characterSheet);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu();
            }
        });

        newCharacter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                characterCreation();
            }
        });

        characterSheet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CharacterNode character = new CharacterNode();
                character.generateCharacter();

                Armor test = new Armor();
                test.randArmor();
                character.giveArmor(test);

                Weapon weapon = new Weapon();
                weapon.randWeapon();
                character.giveWeapon(weapon);
                weapon.randWeapon();
                character.giveWeapon(weapon);
                weapon.randWeapon();
                character.giveWeapon(weapon);

                characterSheet(character, null);
            }
        });
    }

    /*
        Character Creation
            1. Choose House
                a. from list
                b. Create
                c. Random
            2. Concept
                Pick or roll age.
                pick or roll status (up to house max)
                //determine role
                pick or roll background
                pick or roll goal
                pick or roll motivation
                pick or roll at least one virtue
                pick or roll at least one vice
            3. Assign Abilities
                determine starting experience from age
                allocate experience between abilities
            4. Assign specialties
                determine starting experience from age
                allocate experience between specialties
            5. Destiny Points and Benefits
                determine Destiny points from age
                invest Destiny points into benefits up to maximum
                    allowed by age
            6. Drawbacks
                Determine required drawbacks by age
                select drawbacks that match concept (vice)
            7. Starting Possessions
                Roll Status test for starting coin
                Spend at least half your starting coin on possessions
            8. Derived stats
                calculate derived stats
     */
    private void characterCreation() {
        chooseHouse();
    }

    private void createCharacter(House house) {

    }

    private void chooseHouse() {
        repaint();
        getContentPane().removeAll();

        JButton back = new JButton("Back");
        back.setSize(buttonSize);
        back.setLocation(20,10);


        //JList
        DefaultListModel listModel = new DefaultListModel();
        JList list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setSize(200, 200);
        scrollPane.setLocation(halfX - 100, yMax/2 - 100);
        //Choose/create house
        baseHouseList.resetCurrent();
        for (int i = 0; i < baseHouseList.getNum(); ++i) {
            listModel.addElement(baseHouseList.getCurrent());
            baseHouseList.moveNext();
        }

        //Create House
        JButton createHouse = new JButton("Create House");
        createHouse.setSize(buttonSize);
        createHouse.setLocation(halfX + 150, yMax/2 -100);



        c = getContentPane();

        c.add(back);
        c.add(createHouse);
        c.add(scrollPane);

        createHouse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouse();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                characterMenu();
            }
        });

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                }
            }
        };
        list.addMouseListener(mouseListener);
    }

    /*
        create House
            1. Realm
            2. roll resources
            3. History
                age
                events
            4. Holdings
            5. Motto and Arms // will do later
            6. The HouseHold
                PCs
                Lord/Lady
                Heirs
                Retainers, Servants, Household Knights
     */
    private void createHouse() {
        repaint();
        getContentPane().removeAll();

        int col1 = halfX-200, col2 = halfX + 50;
        int rowStart = yMax/2 - 240;
        int row = rowStart;

        JButton back = new JButton("Back");
        back.setLocation(20,10);
        back.setSize(buttonSize);

        JLabel label = new JLabel("Choose Realm");
        label.setLocation(halfX, 40);
        label.setSize(200,30);


        JButton king = new JButton("King's Landing");
        king.setSize(buttonSize);
        king.setLocation(col1, row);

        JButton dragon = new JButton("Dragonstone");
        dragon.setSize(buttonSize);
        dragon.setLocation(col1, row += 40);

        JButton north = new JButton("The North");
        north.setSize(buttonSize);
        north.setLocation(col1, row += 40);

        JButton iron = new JButton("The Iron Islands");
        iron.setSize(buttonSize);
        iron.setLocation(col1, row += 40);

        JButton river = new JButton("The Riverlands");
        river.setSize(buttonSize);
        river.setLocation(col1, row);

        JButton mountain = new JButton("The Mountains of the Moon");
        mountain.setSize(buttonSize);
        mountain.setLocation(col2, row = rowStart);

        JButton west = new JButton("The Westerlands");
        west.setSize(buttonSize);
        west.setLocation(col2, row += 40);

        JButton reach = new JButton("The Reach");
        reach.setSize(buttonSize);
        reach.setLocation(col2, row += 40);

        JButton storm = new JButton("The Stormlands");
        storm.setSize(buttonSize);
        storm.setLocation(col2, row += 40);

        JButton dorne = new JButton("Dorne");
        dorne.setSize(buttonSize);
        dorne.setLocation(col2, row += 40);


        JButton rollRealm = new JButton("Roll for Realm");
        rollRealm.setSize(buttonSize);
        rollRealm.setLocation( (col1 + col2)/2, row+= 60 );


        c = getContentPane();
        c.add(back);
        c.add(rollRealm);
        c.add(label);
        c.add(king);
        c.add(dragon);
        c.add(north);
        c.add(iron);
        c.add(river);
        c.add(mountain);
        c.add(west);
        c.add(reach);
        c.add(storm);
        c.add(dorne);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseHouse();
            }
        });

        dorne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                House house = new House();
                house.setRealm("Dorne");
                houseRollStats(house);
            }
        });

        storm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                House house = new House();
                house.setRealm("The Stormlands");
                houseRollStats(house);
            }
        });

        reach.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                House house = new House();
                house.setRealm("The Reach");
                houseRollStats(house);
            }
        });

        king.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                House house = new House();
                house.setRealm("King's Landing");
                houseRollStats(house);
            }
        });

        dragon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                House house = new House();
                house.setRealm("Dragonstone");
                houseRollStats(house);
            }
        });

        north.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                House house = new House();
                house.setRealm("The North");
                houseRollStats(house);
            }
        });

        iron.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                House house = new House();
                house.setRealm("The Iron Islands");
                houseRollStats(house);
            }
        });

        river.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                House house = new House();
                house.setRealm("The Riverlands");
                houseRollStats(house);
            }
        });

        mountain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                House house = new House();
                house.setRealm("Mountains of the Moon");
                houseRollStats(house);
            }
        });

        west.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                House house = new House();
                house.setRealm("The Westerlands");
                houseRollStats(house);
            }
        });

        rollRealm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data data = new Data();
                House house = new House();
                house.setRealm(data.randRealm());
                houseRollStats(house);
            }
        });
    }

    private void houseRollStats(House house) {
        repaint();
        getContentPane().removeAll();

        JButton back = new JButton("Back");
        back.setSize(buttonSize);
        back.setLocation(20,10);

        //verify that user wants this realm, then either proceed or return to chooseHouse
        String realm = house.printRealm();
        Data data = new Data();

        //column of stat names, column of roll results, column of realm modifier, column of total
        int colNames = 20, colRoll = 120, colRealm = 220, colTotal = 320;
        Dimension fieldSize = new Dimension(80, 40);
        int rowStart = 100;
        int row = rowStart;
        int rowInc = 50;

        //colors commented out in case of future use
        JTextArea statName = new JTextArea("Stat Name");
        //statName.setBackground(new Color(40,120,120));
        statName.setSize(fieldSize);
        statName.setLocation(colNames, row);
        JTextArea rollResult = new JTextArea("Roll\nResult");
        //rollResult.setBackground(new Color(40,120,120));
        rollResult.setLocation(colRoll, row);
        rollResult.setSize(fieldSize);
        JTextArea realmModifier = new JTextArea("Realm\nModifier");
        //realmModifier.setBackground(new Color(40,120,120));
        realmModifier.setSize(fieldSize);
        realmModifier.setLocation(colRealm, row);
        JTextArea totalCol = new JTextArea("Total");
        //totalCol.setBackground(new Color(40,120,120));
        totalCol.setSize(fieldSize);
        totalCol.setLocation(colTotal, row);
        row += rowInc;

        //defense
        JTextArea defenseName = new JTextArea("Defense");
        //defenseName.setBackground(new Color(40,120,120));
        defenseName.setSize(fieldSize);
        defenseName.setLocation(colNames, row);
        JTextArea defenseResult = new JTextArea(house.getDefense() + "");
        //defenseResult.setBackground(new Color(40,120,120));
        defenseResult.setLocation(colRoll, row);
        defenseResult.setSize(fieldSize);
        JTextArea defenseModifier = new JTextArea(data.getRealmDefenseModifier(realm) + "");
        //defenseModifier.setBackground(new Color(40,120,120));
        defenseModifier.setSize(fieldSize);
        defenseModifier.setLocation(colRealm, row);
        JTextArea defenseTotal = new JTextArea("" + (house.getDefense() + data.getRealmDefenseModifier(realm)));
        //defenseCol.setBackground(new Color(40,120,120));
        defenseTotal.setSize(fieldSize);
        defenseTotal.setLocation(colTotal, row);
        row += rowInc;

        //influence
        JTextArea influenceName = new JTextArea("influence");
        //influenceName.setBackground(new Color(40,120,120));
        influenceName.setSize(fieldSize);
        influenceName.setLocation(colNames, row);
        JTextArea influenceResult = new JTextArea(house.getInfluence() + "");
        //influenceResult.setBackground(new Color(40,120,120));
        influenceResult.setLocation(colRoll, row);
        influenceResult.setSize(fieldSize);
        JTextArea influenceModifier = new JTextArea(data.getRealmInfluenceModifier(realm) + "");
        //influenceModifier.setBackground(new Color(40,120,120));
        influenceModifier.setSize(fieldSize);
        influenceModifier.setLocation(colRealm, row);
        JTextArea influenceTotal = new JTextArea("" + (house.getInfluence() + data.getRealmInfluenceModifier(realm)));
        //influenceCol.setBackground(new Color(40,120,120));
        influenceTotal.setSize(fieldSize);
        influenceTotal.setLocation(colTotal, row);
        row += rowInc;

        //Lands
        JTextArea landsName = new JTextArea("Lands");
        //landsName.setBackground(new Color(40,120,120));
        landsName.setSize(fieldSize);
        landsName.setLocation(colNames, row);
        JTextArea landsResult = new JTextArea(house.getLands() + "");
        //landsResult.setBackground(new Color(40,120,120));
        landsResult.setLocation(colRoll, row);
        landsResult.setSize(fieldSize);
        JTextArea landsModifier = new JTextArea(data.getRealmLandsModifier(realm) + "");
        //landsModifier.setBackground(new Color(40,120,120));
        landsModifier.setSize(fieldSize);
        landsModifier.setLocation(colRealm, row);
        JTextArea landsTotal = new JTextArea("" + (house.getLands() + data.getRealmLandsModifier(realm)));
        //landsCol.setBackground(new Color(40,120,120));
        landsTotal.setSize(fieldSize);
        landsTotal.setLocation(colTotal, row);
        row += rowInc;
        //Law
        JTextArea lawName = new JTextArea("Law");
        //lawName.setBackground(new Color(40,120,120));
        lawName.setSize(fieldSize);
        lawName.setLocation(colNames, row);
        JTextArea lawResult = new JTextArea(house.getLaw() + "");
        //lawResult.setBackground(new Color(40,120,120));
        lawResult.setLocation(colRoll, row);
        lawResult.setSize(fieldSize);
        JTextArea lawModifier = new JTextArea(data.getRealmLawModifier(realm) + "");
        //lawModifier.setBackground(new Color(40,120,120));
        lawModifier.setSize(fieldSize);
        lawModifier.setLocation(colRealm, row);
        JTextArea lawTotal = new JTextArea("" + (house.getLaw() + data.getRealmLawModifier(realm)));
        //lawCol.setBackground(new Color(40,120,120));
        lawTotal.setSize(fieldSize);
        lawTotal.setLocation(colTotal, row);
        row += rowInc;
        //Population
        JTextArea populationName = new JTextArea("Population");
        //populationName.setBackground(new Color(40,120,120));
        populationName.setSize(fieldSize);
        populationName.setLocation(colNames, row);
        JTextArea populationResult = new JTextArea(house.getPopulation() + "");
        //populationResult.setBackground(new Color(40,120,120));
        populationResult.setLocation(colRoll, row);
        populationResult.setSize(fieldSize);
        JTextArea populationModifier = new JTextArea(data.getRealmPopulationModifier(realm) + "");
        //populationModifier.setBackground(new Color(40,120,120));
        populationModifier.setSize(fieldSize);
        populationModifier.setLocation(colRealm, row);
        JTextArea populationTotal = new JTextArea("" + (house.getPopulation() + data.getRealmPopulationModifier(realm)));
        //populationCol.setBackground(new Color(40,120,120));
        populationTotal.setSize(fieldSize);
        populationTotal.setLocation(colTotal, row);
        row += rowInc;
        //Power
        JTextArea powerName = new JTextArea("Power");
        //powerName.setBackground(new Color(40,120,120));
        powerName.setSize(fieldSize);
        powerName.setLocation(colNames, row);
        JTextArea powerResult = new JTextArea(house.getPower() + "");
        //powerResult.setBackground(new Color(40,120,120));
        powerResult.setLocation(colRoll, row);
        powerResult.setSize(fieldSize);
        JTextArea powerModifier = new JTextArea(data.getRealmPowerModifier(realm) + "");
        //powerModifier.setBackground(new Color(40,120,120));
        powerModifier.setSize(fieldSize);
        powerModifier.setLocation(colRealm, row);
        JTextArea powerTotal = new JTextArea("" + (house.getPower() + data.getRealmPowerModifier(realm)));
        //powerCol.setBackground(new Color(40,120,120));
        powerTotal.setSize(fieldSize);
        powerTotal.setLocation(colTotal, row);
        row += rowInc;
        //Wealth
        JTextArea wealthName = new JTextArea("Wealth");
        //wealthName.setBackground(new Color(40,120,120));
        wealthName.setSize(fieldSize);
        wealthName.setLocation(colNames, row);
        JTextArea wealthResult = new JTextArea(house.getWealth() + "");
        //wealthResult.setBackground(new Color(40,120,120));
        wealthResult.setLocation(colRoll, row);
        wealthResult.setSize(fieldSize);
        JTextArea wealthModifier = new JTextArea(data.getRealmWealthModifier(realm) + "");
        //wealthModifier.setBackground(new Color(40,120,120));
        wealthModifier.setSize(fieldSize);
        wealthModifier.setLocation(colRealm, row);
        JTextArea wealthTotal = new JTextArea("" + (house.getWealth() + data.getRealmWealthModifier(realm)));
        //wealthCol.setBackground(new Color(40,120,120));
        wealthTotal.setSize(fieldSize);
        wealthTotal.setLocation(colTotal, row);
        row += rowInc;

        JButton rollDice = new JButton("Roll Dice (7D6 per stat)");
        rollDice.setSize(buttonSize);
        rollDice.setLocation(500, 100);

        JButton createHistory = new JButton("Create History");
        createHistory.setSize(buttonSize);
        createHistory.setLocation(500, 100);

        c.add(back);
        if (house.getDefense() == 0)
            c.add(rollDice);
        else
            c.add(createHistory);

        c.add(statName);
        c.add(rollResult);
        c.add(realmModifier);
        c.add(totalCol);
        c.add(defenseName);
        c.add(defenseModifier);
        c.add(defenseResult);
        c.add(defenseTotal);
        c.add(influenceName);
        c.add(influenceModifier);
        c.add(influenceResult);
        c.add(influenceTotal);
        c.add(landsName);
        c.add(landsModifier);
        c.add(landsResult);
        c.add(landsTotal);
        c.add(lawName);
        c.add(lawModifier);
        c.add(lawResult);
        c.add(lawTotal);
        c.add(populationName);
        c.add(populationModifier);
        c.add(populationResult);
        c.add(populationTotal);
        c.add(powerName);
        c.add(powerModifier);
        c.add(powerResult);
        c.add(powerTotal);
        c.add(wealthName);
        c.add(wealthModifier);
        c.add(wealthResult);
        c.add(wealthTotal);

        createHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouseHistory(house);
            }
        });

        rollDice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                house.randStats();
                houseRollStats(house);
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouse();
            }
        });
    }

    private void createHouseHistory(House house) {
        createHouseHistory(house, false);
    }

    private void createHouseHistory(House house, boolean done) {
        repaint();
        getContentPane().removeAll();

        JButton back = new JButton("Back");
        back.setSize(buttonSize);
        back.setLocation(20,10);

        Data data = new Data();
        //age
        JButton rollAge = new JButton("Roll 1D6 for Age");
        rollAge.setSize(buttonSize);
        rollAge.setLocation(20, 100);

        //events
        JButton rollHistory = new JButton("Generate History");
        rollHistory.setSize(buttonSize);
        rollHistory.setLocation(20,100);
        //Text area for House history
        JTextArea historyArea = new JTextArea(house.printHistory());
        historyArea.setEditable(false);
        historyArea.getPreferredSize();
        //Text field for age of house
        JTextField houseAge = new JTextField("House Age: " + data.getAge(house.getAge()));
        houseAge.setEditable(false);
        houseAge.setSize(buttonSize);
        houseAge.setLocation(300, 100);
        //Scroll panel for house history text
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setLocation(300, 140);
        scrollPane.setSize(200, 150);

        //Move to next phase of house creation
        JButton next = new JButton("Holdings");
        next.setSize(buttonSize);
        next.setLocation(20,100);


        c = getContentPane();

        if (!done && house.getAge() == 0)
            c.add(rollAge);
        if (!done && house.getAge() != 0 && house.printHistory() != null)
            c.add(rollHistory);
        if (done)
            c.add(next);

        c.add(houseAge);
        c.add(scrollPane);
        c.add(back);

        scrollPane.revalidate();

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouseHoldings(house);
            }
        });

        rollHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                house.generateHistory();
                scrollPane.revalidate();
                createHouseHistory(house, true);
            }
        });

        rollAge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dice die = new Dice();
                house.setAge(die.roll());
                createHouseHistory(house, false);
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouse();
            }
        });
    }

    private void createHouseHoldings(House house) {
        repaint();
        getContentPane().removeAll();

        //next button
        JButton back = new JButton("Choose Realm");
        back.setSize(buttonSize);
        back.setLocation(20, 20);

        //column of stat names, column of roll results, column of realm modifier, column of total
        int colNames = 20, colRoll = 120, colRealm = 220, colTotal = 320, colButton = 420;
        Dimension fieldSize = new Dimension(80, 40);
        int rowStart = 100;
        int row = rowStart;
        int rowInc = 50;

        //colors commented out in case of future use
        JLabel statName = new JLabel("Stat Name");
        //statName.setEditable(false);
        //statName.setBackground(new Color(40,120,120));
        //statName.setEditable(false);
        statName.setSize(fieldSize);
        statName.setLocation(colNames, row);
        JLabel total = new JLabel("Total");
        //total.setEditable(false);
        //rollResult.setBackground(new Color(40,120,120));
        total.setLocation(colRoll, row);
        total.setSize(fieldSize);
        JLabel invested = new JLabel("Invested");
        //invested.setEditable(false);
        //realmModifier.setBackground(new Color(40,120,120));
        invested.setSize(fieldSize);
        invested.setLocation(colRealm, row);
        JLabel remaining = new JLabel("Remaining");
        //remaining.setEditable(false);
        //totalCol.setBackground(new Color(40,120,120));
        remaining.setSize(fieldSize);
        remaining.setLocation(colTotal, row);
        row += 30;

        JTextField defenseStat = new JTextField("Defense");
        defenseStat.setEditable(false);
        //defenseStat.setBackground(new Color(40,120,120));
        defenseStat.setSize(fieldSize);
        defenseStat.setLocation(colNames, row);
        JTextField defenseTotal = new JTextField(house.getDefense() + "");
        defenseTotal.setEditable(false);
        //defenseTotal.setBackground(new Color(40,120,120));
        defenseTotal.setLocation(colRoll, row);
        defenseTotal.setSize(fieldSize);
        JTextField defenseInvested = new JTextField(house.getDefenseInvested() + "");
        defenseInvested.setEditable(false);
        //defenseInvested.setBackground(new Color(40,120,120));
        defenseInvested.setSize(fieldSize);
        defenseInvested.setLocation(colRealm, row);
        JTextField defenseRemaining = new JTextField(house.getDefenseRemaining() + "");
        defenseRemaining.setEditable(false);
        //defenseRemaining.setBackground(new Color(40,120,120));
        defenseRemaining.setSize(fieldSize);
        defenseRemaining.setLocation(colTotal, row);
        //button
        JButton defenseButton = new JButton("Purchase Holdings");
        defenseButton.setSize(buttonSize);
        defenseButton.setLocation(colButton, row);
        row += rowInc;

        JTextField influenceStat = new JTextField("Influence");
        influenceStat.setEditable(false);
        //influenceStat.setBackground(new Color(40,120,120));
        influenceStat.setSize(fieldSize);
        influenceStat.setLocation(colNames, row);
        JTextField influenceTotal = new JTextField(house.getInfluence() + "");
        influenceTotal.setEditable(false);
        //influenceTotal.setBackground(new Color(40,120,120));
        influenceTotal.setLocation(colRoll, row);
        influenceTotal.setSize(fieldSize);
        JTextField influenceInvested = new JTextField(house.getInfluenceInvested() + "");
        influenceInvested.setEditable(false);
        //influenceInvested.setBackground(new Color(40,120,120));
        influenceInvested.setSize(fieldSize);
        influenceInvested.setLocation(colRealm, row);
        JTextField influenceRemaining = new JTextField(house.getInfluenceRemaining() + "");
        influenceRemaining.setEditable(false);
        //influenceRemaining.setBackground(new Color(40,120,120));
        influenceRemaining.setSize(fieldSize);
        influenceRemaining.setLocation(colTotal, row);
        //button
        JButton influenceButton = new JButton("Purchase Holdings");
        influenceButton.setSize(buttonSize);
        influenceButton.setLocation(colButton, row);
        row += rowInc;

        JTextField landsStat = new JTextField("Lands");
        landsStat.setEditable(false);
        //landsStat.setBackground(new Color(40,120,120));
        landsStat.setSize(fieldSize);
        landsStat.setLocation(colNames, row);
        JTextField landsTotal = new JTextField(house.getLands() + "");
        landsTotal.setEditable(false);
        //landsTotal.setBackground(new Color(40,120,120));
        landsTotal.setLocation(colRoll, row);
        landsTotal.setSize(fieldSize);
        JTextField landsInvested = new JTextField(house.getLandsInvested() + "");
        landsInvested.setEditable(false);
        //landsInvested.setBackground(new Color(40,120,120));
        landsInvested.setSize(fieldSize);
        landsInvested.setLocation(colRealm, row);
        JTextField landsRemaining = new JTextField(house.getLandsRemaining() + "");
        landsRemaining.setEditable(false);
        //landsRemaining.setBackground(new Color(40,120,120));
        landsRemaining.setSize(fieldSize);
        landsRemaining.setLocation(colTotal, row);
        //button
        JButton landsButton = new JButton("Purchase Holdings");
        landsButton.setSize(buttonSize);
        landsButton.setLocation(colButton, row);
        row += rowInc;

        JTextField lawStat = new JTextField("Law");
        lawStat.setEditable(false);
        //lawStat.setBackground(new Color(40,120,120));
        lawStat.setSize(fieldSize);
        lawStat.setLocation(colNames, row);
        JTextField lawTotal = new JTextField(house.getLaw() + "");
        lawTotal.setEditable(false);
        //lawTotal.setBackground(new Color(40,120,120));
        lawTotal.setLocation(colRoll, row);
        lawTotal.setSize(fieldSize);

        Data data = new Data();
        JTextField lawArea = new JTextField(house.printLawHoldings());
        lawArea.setSize(fieldSize.width*2 + 20, fieldSize.height);
        lawArea.setLocation(colRealm, row);
        lawArea.setEditable(false);
        row += rowInc;

        JTextField populationStat = new JTextField("Population");
        populationStat.setEditable(false);
        //populationStat.setBackground(new Color(40,120,120));
        populationStat.setSize(fieldSize);
        populationStat.setLocation(colNames, row);
        JTextField populationTotal = new JTextField(house.getPopulation() + "");
        populationTotal.setEditable(false);
        //populationTotal.setBackground(new Color(40,120,120));
        populationTotal.setLocation(colRoll, row);
        populationTotal.setSize(fieldSize);

        JTextField populationArea = new JTextField(house.printPopulationHoldings());
        populationArea.setEditable(false);
        populationArea.setSize(fieldSize.width*2 + 20, fieldSize.height);
        populationArea.setLocation(colRealm, row);
        populationArea.setEditable(false);
        row += rowInc;

        JTextField powerStat = new JTextField("Power");
        powerStat.setEditable(false);
        //powerStat.setBackground(new Color(40,120,120));
        powerStat.setSize(fieldSize);
        powerStat.setLocation(colNames, row);
        JTextField powerTotal = new JTextField(house.getPower() + "");
        powerTotal.setEditable(false);
        //powerTotal.setBackground(new Color(40,120,120));
        powerTotal.setLocation(colRoll, row);
        powerTotal.setSize(fieldSize);
        JTextField powerInvested = new JTextField(house.getPowerInvested() + "");
        powerInvested.setEditable(false);
        //powerInvested.setBackground(new Color(40,120,120));
        powerInvested.setSize(fieldSize);
        powerInvested.setLocation(colRealm, row);
        JTextField powerRemaining = new JTextField(house.getPowerRemaining() + "");
        powerRemaining.setEditable(false);
        //powerRemaining.setBackground(new Color(40,120,120));
        powerRemaining.setSize(fieldSize);
        powerRemaining.setLocation(colTotal, row);
        //button
        JButton powerButton = new JButton("Purchase Holdings");
        powerButton.setSize(buttonSize);
        powerButton.setLocation(colButton, row);
        row += rowInc;

        JTextField wealthStat = new JTextField("Wealth");
        wealthStat.setEditable(false);
        //wealthStat.setBackground(new Color(40,120,120));
        wealthStat.setSize(fieldSize);
        wealthStat.setLocation(colNames, row);
        JTextField wealthTotal = new JTextField(house.getWealth() + "");
        wealthTotal.setEditable(false);
        //wealthTotal.setBackground(new Color(40,120,120));
        wealthTotal.setLocation(colRoll, row);
        wealthTotal.setSize(fieldSize);
        JTextField wealthInvested = new JTextField(house.getWealthInvested() + "");
        wealthInvested.setEditable(false);
        //wealthInvested.setBackground(new Color(40,120,120));
        wealthInvested.setSize(fieldSize);
        wealthInvested.setLocation(colRealm, row);
        JTextField wealthRemaining = new JTextField(house.getWealthRemaining() + "");
        wealthRemaining.setEditable(false);
        //wealthRemaining.setBackground(new Color(40,120,120));
        wealthRemaining.setSize(fieldSize);
        wealthRemaining.setLocation(colTotal, row);
        //button
        JButton wealthButton = new JButton("Purchase Holdings");
        wealthButton.setSize(buttonSize);
        wealthButton.setLocation(colButton, row);
        row += rowInc;


        c = getContentPane();

        c.add(back);
        c.add(statName);
        c.add(remaining);
        c.add(total);
        c.add(invested);
        c.add(defenseInvested);
        c.add(defenseRemaining);
        c.add(defenseStat);
        c.add(defenseTotal);
        c.add(defenseButton);
        c.add(influenceInvested);
        c.add(influenceRemaining);
        c.add(influenceStat);
        c.add(influenceTotal);
        c.add(influenceButton);
        c.add(landsInvested);
        c.add(landsRemaining);
        c.add(landsStat);
        c.add(landsTotal);
        c.add(landsButton);
        c.add(lawArea);
        c.add(lawStat);
        c.add(lawTotal);
        c.add(populationArea);
        c.add(populationStat);
        c.add(populationTotal);
        c.add(powerInvested);
        c.add(powerRemaining);
        c.add(powerStat);
        c.add(powerTotal);
        c.add(powerButton);
        c.add(wealthInvested);
        c.add(wealthRemaining);
        c.add(wealthStat);
        c.add(wealthTotal);
        c.add(wealthButton);


        defenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseDefenseHoldings(house);
            }
        });

        influenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseInfluenceHoldings(house);
            }
        });

        landsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseLandsHoldings(house);
            }
        });

        powerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchasePowerHoldings(house);
            }
        });

        wealthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseWealthHoldings(house);
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouse();
            }
        });
    }

    private void purchaseDefenseHoldings(House house) {
        repaint();
        getContentPane().removeAll();

        //back button
        JButton back = new JButton("Purchase Holdings");
        back.setSize(buttonSize);
        back.setLocation(20, 20);


        c.add(back);


        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouseHoldings(house);
            }
        });

    }

    private void purchaseInfluenceHoldings(House house) {
        repaint();
        getContentPane().removeAll();

        //back button
        JButton back = new JButton("Purchase Holdings");
        back.setSize(buttonSize);
        back.setLocation(20, 20);


        c.add(back);


        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouseHoldings(house);
            }
        });
    }

    private void purchaseLandsHoldings(House house) {
        repaint();
        getContentPane().removeAll();

        //back button
        JButton back = new JButton("Purchase Holdings");
        back.setSize(buttonSize);
        back.setLocation(20, 20);


        c.add(back);


        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouseHoldings(house);
            }
        });
    }

    private void purchasePowerHoldings(House house) {
        repaint();
        getContentPane().removeAll();

        //back button
        JButton back = new JButton("Purchase Holdings");
        back.setSize(buttonSize);
        back.setLocation(20, 20);


        c.add(back);


        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouseHoldings(house);
            }
        });
    }

    private void purchaseWealthHoldings(House house) {
        repaint();
        getContentPane().removeAll();

        //back button
        JButton back = new JButton("Purchase Holdings");
        back.setSize(buttonSize);
        back.setLocation(20, 20);


        c.add(back);


        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHouseHoldings(house);
            }
        });

    }

    private void characterSheet(CharacterNode character, HouseNode houseNode) {
        repaint();
        getContentPane().removeAll();

        //next button
        JButton next = new JButton("Next");
        next.setSize(buttonSize);
        next.setLocation(xMax - 400, yMax - 100);
        //previous button
        JButton previous = new JButton("Previous");
        previous.setSize(buttonSize);
        previous.setLocation(xMax - 700, yMax - 100);

        Dimension specialtySize = new Dimension(250, 30);
        Dimension fieldSize = new Dimension(75, 30);
        //column and row locations
        int col1 = 20;  //column one for rating
        int col2 = 100;  //column for ability name
        int col3 = 175; //column for specialties
        int row = 100;  //y coordinate
        int rowInc = 40; //row increment value

        //Back button
        JButton back = new JButton("Back");
        back.setSize(buttonSize);
        back.setLocation(20,10);

        //Bio stuff
        //name
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setLocation(250, 10);
        nameLabel.setSize(buttonSize);
        JTextField charName = new JTextField(character.getName());
        charName.setSize(300, 30);
        charName.setLocation(300, 10);
        //age
        JLabel ageLabel = new JLabel("Age");
        ageLabel.setLocation(250, 50);
        ageLabel.setSize(fieldSize);
        JTextField charAge = new JTextField(character.getAge());
        charAge.setSize(fieldSize);
        charAge.setLocation(300, 50);
        //gender
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setLocation(400, 50);
        genderLabel.setSize(fieldSize);
        JTextField charGender = new JTextField(character.getGender());
        charGender.setSize(fieldSize);
        charGender.setLocation(450, 50);
        //house
        JLabel houseLabel = new JLabel("House");
        houseLabel.setSize(buttonSize);
        houseLabel.setLocation(550, 50);
        JTextField charHouse = new JTextField(character.getHouseName());
        charHouse.setSize(fieldSize);
        charHouse.setLocation(600, 50);
        //Stats
        //column labels
        JLabel rating = new JLabel("Rating");
        rating.setSize(fieldSize);
        rating.setLocation(col1, row);
        JLabel ability = new JLabel("Ability");
        ability.setSize(fieldSize);
        ability.setLocation(col2, row);
        JLabel specialties = new JLabel("Specialties");
        specialties.setSize(fieldSize);
        specialties.setLocation(col3, row);
        row += rowInc;
        //agility
        JLabel agility = new JLabel("Agility");
        agility.setSize(fieldSize);
        agility.setLocation(col2, row);
        JTextField agilityRating = new JTextField(character.printAgility());
        agilityRating.setEditable(false);
        agilityRating.setSize(fieldSize);
        agilityRating.setLocation(col1, row);
        JTextField agilitySpecialties = new JTextField(character.printAgilitySpecialties());
        agilitySpecialties.setEditable(false);
        agilitySpecialties.setSize(specialtySize);
        agilitySpecialties.setLocation(col3, row);
        row += rowInc;
        //animal handling
        JLabel animalHandling = new JLabel("<html>Animal<br>Handling</html>");
        animalHandling.setSize(fieldSize);
        animalHandling.setLocation(col2, row);
        JTextField animalHandlingRating = new JTextField(character.printAnimalHandling());
        animalHandlingRating.setEditable(false);
        animalHandlingRating.setSize(fieldSize);
        animalHandlingRating.setLocation(col1, row);
        JTextField animalHandlingSpecialties = new JTextField(character.printAnimalHandlingSpecialties());
        animalHandlingSpecialties.setEditable(false);
        animalHandlingSpecialties.setSize(specialtySize);
        animalHandlingSpecialties.setLocation(col3, row);
        row += rowInc;
        //Athletics
        JLabel athletics = new JLabel("Athletics");
        athletics.setSize(fieldSize);
        athletics.setLocation(col2, row);
        JTextField athleticsRating = new JTextField(character.printAthletics());
        athleticsRating.setEditable(false);
        athleticsRating.setSize(fieldSize);
        athleticsRating.setLocation(col1, row);
        JTextField athleticsSpecialties = new JTextField(character.printAthleticsSpecialties());
        athleticsSpecialties.setEditable(false);
        athleticsSpecialties.setSize(specialtySize);
        athleticsSpecialties.setLocation(col3, row);
        row += rowInc;
        //Awareness
        JLabel awareness = new JLabel("Awareness");
        awareness.setSize(fieldSize);
        awareness.setLocation(col2, row);
        JTextField awarenessRating = new JTextField(character.printAwareness());
        awarenessRating.setEditable(false);
        awarenessRating.setSize(fieldSize);
        awarenessRating.setLocation(col1, row);
        JTextField awarenessSpecialties = new JTextField(character.printAwarenessSpecialties());
        awarenessSpecialties.setEditable(false);
        awarenessSpecialties.setSize(specialtySize);
        awarenessSpecialties.setLocation(col3, row);
        row += rowInc;
        //Cunning
        JLabel cunning = new JLabel("Cunning");
        cunning.setSize(fieldSize);
        cunning.setLocation(col2, row);
        JTextField cunningRating = new JTextField(character.printCunning());
        cunningRating.setEditable(false);
        cunningRating.setSize(fieldSize);
        cunningRating.setLocation(col1, row);
        JTextField cunningSpecialties = new JTextField(character.printCunningSpecialties());
        cunningSpecialties.setEditable(false);
        cunningSpecialties.setSize(specialtySize);
        cunningSpecialties.setLocation(col3, row);
        row += rowInc;
        //Deception
        JLabel deception = new JLabel("Deception");
        deception.setSize(fieldSize);
        deception.setLocation(col2, row);
        JTextField deceptionRating = new JTextField(character.printDeception());
        deceptionRating.setEditable(false);
        deceptionRating.setSize(fieldSize);
        deceptionRating.setLocation(col1, row);
        JTextField deceptionSpecialties = new JTextField(character.printDeceptionSpecialties());
        deceptionSpecialties.setEditable(false);
        deceptionSpecialties.setSize(specialtySize);
        deceptionSpecialties.setLocation(col3, row);
        row += rowInc;
        //Endurance
        JLabel endurance = new JLabel("Endurance");
        endurance.setSize(fieldSize);
        endurance.setLocation(col2, row);
        JTextField enduranceRating = new JTextField(character.printEndurance());
        enduranceRating.setEditable(false);
        enduranceRating.setSize(fieldSize);
        enduranceRating.setLocation(col1, row);
        JTextField enduranceSpecialties = new JTextField(character.printEnduranceSpecialties());
        enduranceSpecialties.setEditable(false);
        enduranceSpecialties.setSize(specialtySize);
        enduranceSpecialties.setLocation(col3, row);
        row += rowInc;
        //Fighting
        JLabel fighting = new JLabel("Fighting");
        fighting.setSize(fieldSize);
        fighting.setLocation(col2, row);
        JTextField fightingRating = new JTextField(character.printFighting());
        fightingRating.setEditable(false);
        fightingRating.setSize(fieldSize);
        fightingRating.setLocation(col1, row);
        JTextField fightingSpecialties = new JTextField(character.printFightingSpecialties());
        fightingSpecialties.setEditable(false);
        fightingSpecialties.setSize(specialtySize);
        fightingSpecialties.setLocation(col3, row);
        row += rowInc;
        //Healing
        JLabel healing = new JLabel("Healing");
        healing.setSize(fieldSize);
        healing.setLocation(col2, row);
        JTextField healingRating = new JTextField(character.printHealing());
        healingRating.setEditable(false);
        healingRating.setSize(fieldSize);
        healingRating.setLocation(col1, row);
        JTextField healingSpecialties = new JTextField(character.printHealingSpecialties());
        healingSpecialties.setEditable(false);
        healingSpecialties.setSize(specialtySize);
        healingSpecialties.setLocation(col3, row);
        row += rowInc;
        //Language
        JLabel language = new JLabel("Language");
        language.setSize(fieldSize);
        language.setLocation(col2, row);
        JTextField languageRating = new JTextField(character.printLanguage());
        languageRating.setEditable(false);
        languageRating.setSize(fieldSize);
        languageRating.setLocation(col1, row);
        JTextField languageSpecialties = new JTextField(character.printLanguageSpecialties());
        languageSpecialties.setEditable(false);
        languageSpecialties.setSize(180 + (specialtySize.width * 2), specialtySize.height);
        languageSpecialties.setLocation(col3, row);
        row += rowInc;
        //new columns
        int col12 = col1 + 430;
        int col22 = col2 + 430;
        int col32 = col3 + 430;
        row = 100;
        //second column labels
        JLabel rating2 = new JLabel("Rating");
        rating2.setSize(fieldSize);
        rating2.setLocation(col12, row);
        JLabel ability2 = new JLabel("Ability");
        ability2.setSize(fieldSize);
        ability2.setLocation(col22, row);
        JLabel specialties2 = new JLabel("Specialties");
        specialties2.setSize(fieldSize);
        specialties2.setLocation(col32, row);
        row += rowInc;
        //Knowledge
        JLabel knowledge = new JLabel("Knowledge");
        knowledge.setSize(fieldSize);
        knowledge.setLocation(col22, row);
        JTextField knowledgeRating = new JTextField(character.printKnowledge());
        knowledgeRating.setEditable(false);
        knowledgeRating.setSize(fieldSize);
        knowledgeRating.setLocation(col12, row);
        JTextField knowledgeSpecialties = new JTextField(character.printKnowledgeSpecialties());
        knowledgeSpecialties.setEditable(false);
        knowledgeSpecialties.setSize(specialtySize);
        knowledgeSpecialties.setLocation(col32, row);
        row += rowInc;
        //Marksmanship
        JLabel marksmanship = new JLabel("<html>Marks-<br>manship</html>");
        marksmanship.setSize(fieldSize);
        marksmanship.setLocation(col22, row);
        JTextField marksmanshipRating = new JTextField(character.printMarksmanship());
        marksmanshipRating.setEditable(false);
        marksmanshipRating.setSize(fieldSize);
        marksmanshipRating.setLocation(col12, row);
        JTextField marksmanshipSpecialties = new JTextField(character.printMarksmanshipSpecialties());
        marksmanshipSpecialties.setEditable(false);
        marksmanshipSpecialties.setSize(specialtySize);
        marksmanshipSpecialties.setLocation(col32, row);
        row += rowInc;
        //Persuasion
        JLabel persuasion = new JLabel("Persuasion");
        persuasion.setSize(fieldSize);
        persuasion.setLocation(col22, row);
        JTextField persuasionRating = new JTextField(character.printPersuasion());
        persuasionRating.setEditable(false);
        persuasionRating.setSize(fieldSize);
        persuasionRating.setLocation(col12, row);
        JTextField persuasionSpecialties = new JTextField(character.printPersuasionSpecialties());
        persuasionSpecialties.setEditable(false);
        persuasionSpecialties.setSize(specialtySize);
        persuasionSpecialties.setLocation(col32, row);
        row += rowInc;
        //Status
        JLabel status = new JLabel("Status");
        status.setSize(fieldSize);
        status.setLocation(col22, row);
        JTextField statusRating = new JTextField(character.printStatus());
        statusRating.setEditable(false);
        statusRating.setSize(fieldSize);
        statusRating.setLocation(col12, row);
        JTextField statusSpecialties = new JTextField(character.printStatusSpecialties());
        statusSpecialties.setEditable(false);
        statusSpecialties.setSize(specialtySize);
        statusSpecialties.setLocation(col32, row);
        row += rowInc;
        //Stealth
        JLabel stealth = new JLabel("Stealth");
        stealth.setSize(fieldSize);
        stealth.setLocation(col22, row);
        JTextField stealthRating = new JTextField(character.printStealth());
        stealthRating.setEditable(false);
        stealthRating.setSize(fieldSize);
        stealthRating.setLocation(col12, row);
        JTextField stealthSpecialties = new JTextField(character.printStealthSpecialties());
        stealthSpecialties.setEditable(false);
        stealthSpecialties.setSize(specialtySize);
        stealthSpecialties.setLocation(col32, row);
        row += rowInc;
        //Survival
        JLabel survival = new JLabel("Survival");
        survival.setSize(fieldSize);
        survival.setLocation(col22, row);
        JTextField survivalRating = new JTextField(character.printSurvival());
        survivalRating.setEditable(false);
        survivalRating.setSize(fieldSize);
        survivalRating.setLocation(col12, row);
        JTextField survivalSpecialties = new JTextField(character.printSurvivalSpecialties());
        survivalSpecialties.setEditable(false);
        survivalSpecialties.setSize(specialtySize);
        survivalSpecialties.setLocation(col32, row);
        row += rowInc;
        //Thievery
        JLabel thievery = new JLabel("Thievery");
        thievery.setSize(fieldSize);
        thievery.setLocation(col22, row);
        JTextField thieveryRating = new JTextField(character.printThievery());
        thieveryRating.setEditable(false);
        thieveryRating.setSize(fieldSize);
        thieveryRating.setLocation(col12, row);
        JTextField thieverySpecialties = new JTextField(character.printThieverySpecialties());
        thieverySpecialties.setEditable(false);
        thieverySpecialties.setSize(specialtySize);
        thieverySpecialties.setLocation(col32, row);
        row += rowInc;
        //Warfare
        JLabel warfare = new JLabel("Warfare");
        warfare.setSize(fieldSize);
        warfare.setLocation(col22, row);
        JTextField warfareRating = new JTextField(character.printWarfare());
        warfareRating.setEditable(false);
        warfareRating.setSize(fieldSize);
        warfareRating.setLocation(col12, row);
        JTextField warfareSpecialties = new JTextField(character.printWarfareSpecialties());
        warfareSpecialties.setEditable(false);
        warfareSpecialties.setSize(specialtySize);
        warfareSpecialties.setLocation(col32, row);
        row += rowInc;
        //Will
        JLabel will = new JLabel("Will");
        will.setSize(fieldSize);
        will.setLocation(col22, row);
        JTextField willRating = new JTextField(character.printWill());
        willRating.setEditable(false);
        willRating.setSize(fieldSize);
        willRating.setLocation(col12, row);
        JTextField willSpecialties = new JTextField(character.printWillSpecialties());
        willSpecialties.setEditable(false);
        willSpecialties.setSize(specialtySize);
        willSpecialties.setLocation(col32, row);
        //row += rowInc;

        //Qualities
        JLabel qualitiesLabel = new JLabel("Qualities");
        qualitiesLabel.setSize(fieldSize);
        qualitiesLabel.setLocation(col1, row += 80);
        JTextArea qualities = new JTextArea();
        qualities.setEditable(false);
        qualities.setSize(400, 90);
        qualities.setLocation(col1, row + 30);

        //Destiny points
        //will do later

        //Intrigue
        JLabel intrigueLabel = new JLabel("Intrigue");
        intrigueLabel.setSize(fieldSize);
        intrigueLabel.setLocation(col12, row);
        JLabel intrigueDefenseLabel = new JLabel("Intrigue Defense");
        intrigueDefenseLabel.setSize(buttonSize);
        intrigueDefenseLabel.setLocation(col12, row + 20);
        JTextField intrigueValue = new JTextField(character.printIntrigueDefense());
        intrigueValue.setEditable(false);
        intrigueValue.setLocation(col12, row + 45);
        intrigueValue.setSize(buttonSize);
        JLabel intrigueExplanation = new JLabel("Awareness + Cunning + Status");
        intrigueExplanation.setSize(buttonSize);
        intrigueExplanation.setLocation(col12 + 15, row + 75);
        JLabel composureLabel = new JLabel("Composure");
        composureLabel.setLocation(col12, row + 100);
        composureLabel.setSize(buttonSize);
        JTextField composureValue = new JTextField(character.printComposure());
        composureValue.setEditable(false);
        composureValue.setSize(buttonSize);
        composureValue.setLocation(col12, row + 130);
        JLabel composureExplanation = new JLabel("Will Ranks x 3");
        composureExplanation.setLocation(col12 + 45, row + 155);
        composureExplanation.setSize(buttonSize);

        //Combat
        JLabel combatLabel = new JLabel("Combat");
        combatLabel.setSize(fieldSize);
        combatLabel.setLocation(col32 + 50, row);
        JLabel combatDefenseLabel = new JLabel("Combat Defense");
        combatDefenseLabel.setSize(buttonSize);
        combatDefenseLabel.setLocation(col32 + 50, row + 20);
        JTextField combatValue = new JTextField(character.printCombatDefense());
        combatValue.setEditable(false);
        combatValue.setLocation(col32 + 50, row + 45);
        combatValue.setSize(buttonSize);
        JLabel combatExplanation = new JLabel("<html>Agility+Athletics+Awareness<br>" +
                "Defensive Bonus-Armor Penalty</html>");
        combatExplanation.setSize(buttonSize);
        combatExplanation.setLocation(col32 + 50 + 15, row + 75);
        JLabel healthLabel = new JLabel("Health");
        healthLabel.setLocation(col32 + 50, row + 100);
        healthLabel.setSize(buttonSize);
        JTextField healthValue = new JTextField(character.printHealth());
        healthValue.setEditable(false);
        healthValue.setSize(buttonSize);
        healthValue.setLocation(col32 + 50, row + 130);
        JLabel healthExplanation = new JLabel("Endurance Ranks x 3");
        healthExplanation.setLocation(col32 + 50 + 25, row + 155);
        healthExplanation.setSize(buttonSize);

        //Weapons, Armor, Damage
        //Weapons columns
        int weaponNameCol = col1;
        int weaponDiceCol = col1 + 160;
        int weaponDamageCol = weaponDiceCol + fieldSize.width + 10;
        row += 160;
        JLabel weaponSection = new JLabel("Weapons");
        weaponSection.setSize(buttonSize);
        weaponSection.setLocation(col3, row);
        //
        JLabel attack = new JLabel("Attack");
        attack.setLocation(weaponNameCol, row + 40);
        attack.setSize(150, 30);
        //
        JLabel testDice = new JLabel("Test Dice");
        testDice.setLocation(weaponDiceCol, row + 40);
        testDice.setSize(specialtySize);
        //
        JLabel weaponDamage = new JLabel("Weapon Damage");
        weaponDamage.setSize(specialtySize);
        weaponDamage.setLocation(weaponDamageCol, row + 40);
        //Weapon1
        JTextField weapon1Name = new JTextField(character.printWeapon1Name());
        weapon1Name.setLocation(weaponNameCol, row + 70);
        weapon1Name.setSize(150, 30);
        weapon1Name.setEditable(false);
        JTextField weapon1Dice = new JTextField(character.printWeapon1AttackDice());
        weapon1Dice.setLocation(weaponDiceCol, row + 70);
        weapon1Dice.setSize(fieldSize);
        weapon1Dice.setEditable(false);
        JTextField weapon1Damage = new JTextField(character.printWeapon1Damage()
                + "   " + character.printWeapon1Description());
        weapon1Damage.setLocation(weaponDamageCol, row + 70);
        weapon1Damage.setSize(specialtySize.width + 50, specialtySize.height);
        weapon1Damage.setEditable(false);
        //
        row += 40;
        //Off-Hand weapon
        JTextField offHandName = new JTextField(character.printOffHandName());
        offHandName.setLocation(weaponNameCol, row + 70);
        offHandName.setSize(150, 30);
        offHandName.setEditable(false);
        JTextField offHandDice = new JTextField(character.printOffHandAttackDice());
        offHandDice.setLocation(weaponDiceCol, row + 70);
        offHandDice.setSize(fieldSize);
        offHandDice.setEditable(false);
        JTextField offHandDamage = new JTextField(character.printOffHandDamage()
                + "   " + character.printOffHandDescription());
        offHandDamage.setLocation(weaponDamageCol, row + 70);
        offHandDamage.setSize(specialtySize.width + 50, specialtySize.height);
        offHandDamage.setEditable(false);
        //
        row += 40;
        //weapon2
        JTextField weapon2Name = new JTextField(character.printWeapon2Name());
        weapon2Name.setLocation(weaponNameCol, row + 70);
        weapon2Name.setSize(150, 30);
        weapon2Name.setEditable(false);
        JTextField weapon2Dice = new JTextField(character.printWeapon2AttackDice());
        weapon2Dice.setLocation(weaponDiceCol, row + 70);
        weapon2Dice.setSize(fieldSize);
        weapon2Dice.setEditable(false);
        JTextField weapon2Damage = new JTextField(character.printWeapon2Damage()
                + "   " + character.printWeapon2Description());
        weapon2Damage.setLocation(weaponDamageCol, row + 70);
        weapon2Damage.setSize(specialtySize.width + 50, specialtySize.height);
        weapon2Damage.setEditable(false);
        //
        row += 40;
        //weapon3
        JTextField weapon3Name = new JTextField(character.printWeapon3Name());
        weapon3Name.setLocation(weaponNameCol, row + 70);
        weapon3Name.setSize(150, 30);
        weapon3Name.setEditable(false);
        JTextField weapon3Dice = new JTextField(character.printWeapon3AttackDice());
        weapon3Dice.setLocation(weaponDiceCol, row + 70);
        weapon3Dice.setSize(fieldSize);
        weapon3Dice.setEditable(false);
        JTextField weapon3Damage = new JTextField(character.printWeapon3Damage()
                + "   " + character.printWeapon2Description());
        weapon3Damage.setLocation(weaponDamageCol, row + 70);
        weapon3Damage.setSize(specialtySize.width + 50, specialtySize.height);
        weapon3Damage.setEditable(false);
        row -= 80;

        //Armor
        Armor armor = character.getArmor();

        JLabel armorLabel = new JLabel("Armor");
        armorLabel.setLocation(col32, row);
        armorLabel.setSize(buttonSize);
        JTextField armorName = new JTextField();
        if (armor != null)
            armorName.setText(armor.getName());
        armorName.setLocation(col32,row + 30);
        armorName.setSize(buttonSize);
        armorName.setEditable(false);
        //armor rating
        JLabel armorRatingLabel = new JLabel("Armor Rating");
        armorRatingLabel.setLocation(col32, row +70);
        armorRatingLabel.setSize(buttonSize);

        JTextField armorRating = new JTextField();
        if (armor != null)
            armorRating.setText(armor.getArmorRating() + "");
        armorRating.setLocation(col32 + 100, row +70);
        armorRating.setSize(fieldSize);
        armorRating.setEditable(false);
        //armor penalty
        JLabel armorPenaltyLabel = new JLabel("Armor Penalty");
        armorPenaltyLabel.setLocation(col32, row +110);
        armorPenaltyLabel.setSize(buttonSize);

        JTextField armorPenalty = new JTextField();
        if (armor != null)
            armorPenalty.setText(armor.getArmorPenalty() + "");
        armorPenalty.setLocation(col32 + 100, row +110);
        armorPenalty.setSize(fieldSize);
        armorPenalty.setEditable(false);


        //panel for containing things
        JPanel sheet = new JPanel(null);
        sheet.setPreferredSize(new Dimension(xMax-100, yMax));



        c = getContentPane();

        sheet.add(armorPenaltyLabel);
        sheet.add(armorRatingLabel);
        sheet.add(armorName);
        sheet.add(armorPenalty);
        sheet.add(armorRating);
        sheet.add(armorLabel);
        sheet.add(weapon1Name);
        sheet.add(weapon1Dice);
        sheet.add(weapon1Damage);
        sheet.add(weapon2Name);
        sheet.add(weapon2Dice);
        sheet.add(weapon2Damage);
        sheet.add(weapon3Name);
        sheet.add(weapon3Dice);
        sheet.add(weapon3Damage);
        sheet.add(offHandName);
        sheet.add(offHandDice);
        sheet.add(offHandDamage);
        sheet.add(weaponSection);
        sheet.add(attack);
        sheet.add(testDice);
        sheet.add(weaponDamage);

        sheet.add(back);
        sheet.add(charName);
        sheet.add(nameLabel);
        sheet.add(charAge);
        sheet.add(ageLabel);
        sheet.add(charGender);
        sheet.add(genderLabel);
        sheet.add(charHouse);
        sheet.add(houseLabel);
        //qualities, destiny, intrigue, combat
        sheet.add(qualitiesLabel);
        sheet.add(qualities);
        //combat
        sheet.add(combatLabel);
        sheet.add(combatDefenseLabel);
        sheet.add(combatValue);
        sheet.add(combatExplanation);
        sheet.add(healthLabel);
        sheet.add(healthValue);
        sheet.add(healthExplanation);
        //intrigue
        sheet.add(intrigueDefenseLabel);
        sheet.add(intrigueLabel);
        sheet.add(intrigueValue);
        sheet.add(intrigueExplanation);
        sheet.add(composureLabel);
        sheet.add(composureValue);
        sheet.add(composureExplanation);
        //Abilities section
        sheet.add(rating);
        sheet.add(rating2);
        sheet.add(ability);
        sheet.add(ability2);
        sheet.add(specialties);
        sheet.add(specialties2);
        sheet.add(agility);
        sheet.add(agilityRating);
        sheet.add(agilitySpecialties);
        sheet.add(animalHandling);
        sheet.add(animalHandlingRating);
        sheet.add(animalHandlingSpecialties);
        sheet.add(athletics);
        sheet.add(athleticsRating);
        sheet.add(athleticsSpecialties);
        sheet.add(awareness);
        sheet.add(awarenessRating);
        sheet.add(awarenessSpecialties);
        sheet.add(cunning);
        sheet.add(cunningRating);
        sheet.add(cunningSpecialties);
        sheet.add(deception);
        sheet.add(deceptionRating);
        sheet.add(deceptionSpecialties);
        sheet.add(endurance);
        sheet.add(enduranceRating);
        sheet.add(enduranceSpecialties);
        sheet.add(fighting);
        sheet.add(fightingRating);
        sheet.add(fightingSpecialties);
        sheet.add(healing);
        sheet.add(healingRating);
        sheet.add(healingSpecialties);
        sheet.add(language);
        sheet.add(languageRating);
        sheet.add(languageSpecialties);
        sheet.add(knowledge);
        sheet.add(knowledgeRating);
        sheet.add(knowledgeSpecialties);
        sheet.add(marksmanship);
        sheet.add(marksmanshipRating);
        sheet.add(marksmanshipSpecialties);
        sheet.add(persuasion);
        sheet.add(persuasionRating);
        sheet.add(persuasionSpecialties);
        sheet.add(status);
        sheet.add(statusRating);
        sheet.add(statusSpecialties);
        sheet.add(stealth);
        sheet.add(stealthRating);
        sheet.add(stealthSpecialties);
        sheet.add(survival);
        sheet.add(survivalRating);
        sheet.add(survivalSpecialties);
        sheet.add(thievery);
        sheet.add(thieveryRating);
        sheet.add(thieverySpecialties);
        sheet.add(warfare);
        sheet.add(warfareRating);
        sheet.add(warfareSpecialties);
        sheet.add(will);
        sheet.add(willRating);
        sheet.add(willSpecialties);


        //contains the panel for scrolling
        JScrollPane scrollPane = new JScrollPane(sheet);
        scrollPane.setViewportView(sheet);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.setSize(xMax-50, yMax - 200);
        scrollPane.setLocation(20, 60);
        scrollPane.setWheelScrollingEnabled(true);
        //scrollPane.setPreferredSize(new Dimension(xMax - 100, yMax-100));

        c.add(back);
        c.add(next);
        c.add(previous);
        c.add(scrollPane);

        scrollPane.revalidate();
        repaint();

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (character.getNext() != null)
                    characterSheet(character.getNext(), houseNode);
            }
        });

        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (character.getPrev() != null)
                    characterSheet(character.getPrev(), houseNode);
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (houseNode == null)
                    characterMenu();
                else
                    viewHouse(houseNode);
            }
        });

        /*
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                calculateSize();
                characterSheet(character, houseNode);
            }
        });
        */
    }

    //print text blocks on multiple lines
    private void drawString(Graphics g, String text, int x, int y) {
        for (String line: text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }
}
