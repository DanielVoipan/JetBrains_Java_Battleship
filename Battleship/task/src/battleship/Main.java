package battleship;


import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

enum ships {
    AircraftCarrier(5, "Aircraft Carrier", 1), Battleship(4, "Battleship", 2), Submarine(3, "Submarine", 3), Cruiser(3, "Cruiser", 4), Destroyer(2, "Destroyer", 5);

    private int nrOfCells;
    private final String name;
    private int position;

    ships(int nrOfCells, String name, int position) {
        this.nrOfCells = nrOfCells;
        this.name = name;
        this.position = position;
    }

    int getNrOfCells() {
        return nrOfCells;
    }

    String getName() {
        return name;
    }

    int getPosition() {
        return position;
    }
}

enum letter {
    A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7), I(8), J(9);

    private int num;

    letter(int num) {
        this.num = num;
    }
    int getNum() {
        return num;
    }
}

class Field {
    protected char[][] field;
    protected char[][] tempField;

    protected String[] shipShot;

    protected String[] shipList;

    protected int defeated;

    public Field() {
        int columnNumber = 10;
        int rowsNumber = 10;
        this.field = new char[rowsNumber][columnNumber];
        this.tempField = new char[rowsNumber][columnNumber];
        this.shipShot = new String[rowsNumber * 10];
        this.shipList = new String[5];
        this.defeated = 0;
    }
    public char[][] getField() {
        return field;
    }
    public char[][] getTempField() {
        return tempField;
    }
    public String[] getShipShot() {
        return shipShot;
    }
    public String[] getShipList() {
        return shipList;
    }

    public int getDefeated() {
        return defeated;
    }

    public void setDefeated(int defeated) {
        this.defeated = defeated;
    }

    public void setShipList(String[] shipList) {
        this.shipList = shipList;
    }

    public void setShipShot(String[] shipShot) {
        this.shipShot = shipShot;
    }

    public void setField(char[][] field) {
        this.field = field;
    }
    public void setTempField(char[][] field) {
        this.tempField = field;
    }
}

public class Main {

    private final static int rowsNumber = 10;
    private final static int columnNumber = 10;

    private final static int nrOfShips = 5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String fPos = null;
        String lPos = null;
        boolean error = false;
        Field player1Field = new Field();
        Field player2Field = new Field();
        // generating the fields
        generateField(player1Field, 0, 0);
        generateField(player2Field, 0, 0);
        int players = 2;
        int counterPlayers = 0;
        Field mainField = player1Field;
        while (counterPlayers < players) {
            int counterShips = 1;
            if (counterPlayers == 1) {
                System.out.println("Player 2, place your ships on the game field");
            } else {
                System.out.println("Player 1, place your ships on the game field");
            }
            System.out.println();
            generateField(mainField, 1, 0);
            while (counterShips <= nrOfShips) {
                ships currentShip = currentShip(counterShips);
                String shipName = currentShip.getName();
                int shipCells = currentShip.getNrOfCells();
                if (!error) {
                    System.out.printf("Enter the coordinates of the %s (%d cells):\n", shipName, shipCells);
                }
                fPos = scanner.next();
                lPos = scanner.next();
                error = false;
                try {
                    verifyArguments(fPos, lPos);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    error = true;
                    continue;
                }
                try {
                    verifyShipPosition(fPos, lPos, shipCells, shipName, mainField);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                    error = true;
                    continue;
                }
                // showing the field
                generateField(mainField, 1, 0);
                counterShips++;
            }
            if (counterPlayers == 0) {
                player1Field = mainField;
                mainField = player2Field;
            } else {
                player2Field = mainField;
            }
            System.out.println();
            System.out.println("Press Enter and pass the move to another player");
            Scanner scan = new Scanner(System.in);
            scan.nextLine();
            counterPlayers++;
        }
        startGame(player1Field, player2Field, false, 0);
    }

    static void startGame(Field player1Field, Field player2Field, boolean firstStart, int pos) {
        boolean endGame = false;
        Field mainPlayer;
        Field secondPlayer;
        if (!firstStart) {
            System.out.println("The game starts!");
            generateField(player1Field, 0, 1);
            generateField(player2Field, 0, 1);
        }
        if (pos == 0) {
            mainPlayer = player1Field;
            secondPlayer = player2Field;
        } else {
            mainPlayer = player2Field;
            secondPlayer = player1Field;
        }
            generateField(secondPlayer, 1, 1);
            System.out.println("----------------------");
            generateField(mainPlayer, 1, 0);
            System.out.println();

            if (pos == 0) {
                System.out.println("Player 1, it's your turn:");
            } else {
                System.out.println("Player 2, it's your turn:");
            }
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String get = scanner.next();
                try {
                verifyShotPosition(get);
                } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
                }
                secondPlayer = takeAShot(get, secondPlayer);
                if (pos == 0) {
                    player2Field = secondPlayer;
                } else {
                    player1Field = secondPlayer;
                }
                if (!checkIfMoreShipToShot(secondPlayer)) {
                endGame = true;
                break;
                }
                break;
            }
            if (endGame) {
                return;
            }
            if (++pos > 1) {
                pos = 0;
            }

            System.out.println("Press Enter and pass the move to another player");
            Scanner scan = new Scanner(System.in);
        scan.nextLine();
            startGame(player1Field, player2Field, true, pos);
    }

    // check if there are more ships to shoot
    static boolean checkIfMoreShipToShot(Field f) {
        char[][] field = f.getField();
        boolean gameOn = false;
        for (int i = 0; i < rowsNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                if (field[i][j] == 'O') {
                    gameOn = true;
                    break;
                }
            }
        }
        return gameOn;
    }

    // take a shot method
    static Field takeAShot(String get, Field f) {
        char[][] field = f.getField();
        char[][] tempField = f.getTempField();
        boolean shot = false;
        char let = get.charAt(0);
        int num;
        int letterPos = 0;
        if (get.matches("[A-Za-z][0-9]{2}")) {
            num = Integer.parseInt(get.substring(1,3));
        } else {
            num = Integer.parseInt(String.valueOf(get.charAt(1)));
        }
        for (letter l : letter.values()) {
            if (l.toString().equalsIgnoreCase(String.valueOf(let))) {
                letterPos = l.getNum();
            }
        }
        if (field[letterPos][num - 1] == '~') {
            field[letterPos][num - 1] = 'M';
            tempField[letterPos][num - 1] = 'M';
        } else if (field[letterPos][num - 1] == 'O') {
            shot = true;
            field[letterPos][num - 1] = 'X';
            tempField[letterPos][num - 1] = 'X';
            addShot(f,letterPos + ":" + (num - 1));
        }
        f.setField(field);
        f.setTempField(tempField);
        if (shot) {
            boolean isSank = isShipSunk(f);
            if (isSank) {
                boolean defeated = isDefeated(f);
                if (defeated) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                } else {
                    System.out.println("You sank a ship!");
                }
            } else {
                System.out.println("You hit a ship!");
            }
        } else {
            System.out.println("You missed!");
        }
        return f;
    }

    // check if defeated
    static boolean isDefeated(Field f) {
        boolean defeated = false;
        int counter = f.getDefeated();
        counter++;
        if (counter < nrOfShips) {
            f.setDefeated(counter);
        } else {
            defeated = true;
        }
        return defeated;
    }

    // return current ship based on the order set in enum
    static ships currentShip(int num) {
        ships returnShip = null;
        for (ships s :ships.values()) {
            if (s.getPosition() == num) {
                returnShip = s;
                break;
            }
        }
        return returnShip;
    }

    // verify if valid letter and position
    static void verifyArguments(String fPos, String lPos) {
        if (!fPos.matches("[A-Ja-j]([1-9]|10)")) {
            throw new IllegalArgumentException("Error! Invalid position: \""+ fPos + "\"! Try again:");
        }
        if (!lPos.matches("[A-Ja-j]([1-9]|10)")) {
            throw new IllegalArgumentException("Error! Invalid position \""+ lPos + "\"! Try again:");
        }
    }

    // verify shot position
    static void verifyShotPosition(String fPos) {
        if (!fPos.matches("[A-Ja-j]([1-9]|10)")) {
            throw new IllegalArgumentException("Error! You entered the wrong coordinates! Try again:");
        }
    }

    // verify position of the battleships
    static void verifyShipPosition(String fPos, String lPos, int shipCells, String shipName, Field f) {
        char[][] field = f.getField();
        char fLetter = fPos.charAt(0);
        char lLetter = lPos.charAt(0);
        int fNum;
        int lNum;
        if (fPos.matches("[A-Za-z][0-9]{2}")) {
            fNum = Integer.parseInt(fPos.substring(1,3));
        } else {
            fNum = Integer.parseInt(String.valueOf(fPos.charAt(1)));
        }
        if (lPos.matches("[A-Za-z][0-9]{2}")) {
            lNum = Integer.parseInt(lPos.substring(1,3));
        } else {
            lNum = Integer.parseInt(String.valueOf(lPos.charAt(1)));
        }
        int fLetterPos = 0;
        int lLetterPos = 0;
        int dif = 0;
        // temporary array to work.
        char[][] tempField = Arrays.copyOf(field, field.length);
        // get letter position
        for (letter l : letter.values()) {
            if (l.toString().equalsIgnoreCase(String.valueOf(fLetter))) {
                fLetterPos = l.getNum();
            }
            if (l.toString().equalsIgnoreCase(String.valueOf(lLetter))) {
                lLetterPos = l.getNum();
            }
        }
        if (fLetter == lLetter) {
            dif = fNum > lNum ? fNum - lNum : lNum - fNum;
            if (dif + 1 != shipCells) {
                throw new RuntimeException("Error! Wrong length of the " + shipName + "! Try again:");
            }
        } else {
            for (int i = Math.min(fLetterPos,lLetterPos); i <= Math.max(lLetterPos, fLetterPos); i++) {
                dif++;
            }
            if (dif != shipCells) {
                throw new RuntimeException("Error! Wrong length of the " + shipName + "! Try again:");
            }
        }
        if (!String.valueOf(fLetter).equalsIgnoreCase(String.valueOf(lLetter)) && fNum != lNum) {
            throw new RuntimeException("Error! Wrong ship location! Try again:");
        }
        int occupied = 0;
        int type = 0;
        StringBuilder added = new StringBuilder();
        // check if on different lines
        if (fLetterPos != lLetterPos) {
            type = 1;
            for (int i = Math.min(fLetterPos,lLetterPos); i <= Math.max(fLetterPos, lLetterPos); i++) {
                try {
                    if (field[i][fNum - 1] == 'O') {
                        occupied++;
                        continue;
                    }
                    tempField[i][fNum - 1] = 'O';
                    // save positions where we set out ship
                    added.append(i).append(":").append(fNum - 1).append(" ");
                } catch (ArrayIndexOutOfBoundsException ignored) {

                }
            }
        } else {
            type = 2;
            // check if on same line
            for (int i = 0; i < columnNumber; i++) {
                if (i >= (Math.min(fNum, lNum) - 1) && i < Math.max(fNum, lNum)) {
                    try {
                        if (field[fLetterPos][i] == 'O') {
                            occupied++;
                        }
                        tempField[fLetterPos][i] = 'O';
                        // save positions where we set out ship
                        added.append(fLetterPos).append(":").append(i).append(" ");
                    } catch (ArrayIndexOutOfBoundsException ignored) {

                    }
                }
            }
        }

        // check if what we added is near another ship
        String[] addedSplit = added.toString().split(" ");
        String[] start = addedSplit[0].split(":");
        String[] end = addedSplit[addedSplit.length - 1].split(":");

        // check if is more then 1 space on line or collumn
        int startIntcolumn = Integer.parseInt(start[1]);
        int endIntcolumn = Integer.parseInt(end[1]);
        int startIntLine = Integer.parseInt(start[0]);
        int endIntLine = Integer.parseInt(end[0]);

        try {
            if (tempField[startIntLine][startIntcolumn - 1] == 'O') {
            occupied++;
            } else if (tempField[endIntLine][endIntcolumn + 1] == 'O') {
            occupied++;
            }
            if (tempField[startIntLine - 1][startIntcolumn] == 'O') {
            occupied++;
            } else if (tempField[endIntLine + 1][endIntcolumn] == 'O') {
            occupied++;
            }

            for (int i = 0; i < addedSplit.length; i++) {
                String[] coord = addedSplit[i].split(":");
                int line = Integer.parseInt(coord[0]);
                int col = Integer.parseInt(coord[1]);
                if (type == 2 && (tempField[line + 1][col] == 'O' || tempField[line - 1][col] == 'O')) {
                    occupied++;
                } else if (type == 1 && (tempField[line][col + 1] == 'O' || tempField[line][col - 1] == 'O')) {
                    occupied++;
                }
            }

        } catch (ArrayIndexOutOfBoundsException ignored) {

        }
        if (occupied > 0) {
            throw new RuntimeException("Error! You placed it too close to another one. Try again:");
        } else {
            f.setField(tempField);
            addShip(f, added.toString());
        }
    }

    // generate OR show field and temporary enemy field for battleship game
    static void generateField(Field f, int type, int temp) {
        char[][] field;
        if (temp == 0) {
            field = f.getField();
        } else {
            field = f.getTempField();
        }
        int fLetter = 65;
        int fnum = 1;
        for (int k = 0; k < 10; k++) {
            // only show field
            if (type == 1) {
                if (fnum == 10) {
                    System.out.println(fnum);
                } else if (fnum == 1) {
                    System.out.printf("\s\s" + fnum + " ");
                } else {
                    System.out.print(fnum + " ");
                }
            }
            fnum++;
        }
        for (int i = 0; i < rowsNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                if (j == 0) {
                    // only show field
                    if (type == 1) {
                        System.out.print((char) fLetter + " ");
                    }
                }
                // generating empty field
                if (type == 0) {
                    field[i][j] = '~';
                } else {
                    System.out.print(field[i][j] + " ");
                }
                fnum++;
            }
            // only show field
            if (type == 1) {
                System.out.println();
            }
            fLetter++;
        }
        // setting values for field, it can be the official or temporary
        if (type == 0) {
            if (temp == 0) {
                f.setField(field);
            } else {
                f.setTempField(field);
            }
        }
    }

    // take a shot to the ship and save it to list if succeeded
    public static void addShot(Field f, String shot) {
        String[] shots = f.getShipShot();
        int lastShot = 0;
        boolean firstShot = false;
        for (int i = 0; i < shots.length; i++) {
            if (!Objects.equals(shots[i], null)) {
                lastShot = i;
                firstShot = true;
            }
        }
        if (lastShot == 0 && !firstShot) {
            shots[0] = shot;
        } else {
            shots[lastShot + 1] = shot;
        }
        f.setShipShot(shots);
    }

    // add ship to field ship list
    public static void addShip(Field f, String shipPos) {
        String[] ships = f.getShipList();
        int lastShip = 0;
        boolean firstShip = false;
        for (int i = 0; i < ships.length; i++) {
            if (!Objects.equals(ships[i], null)) {
                lastShip = i;
                firstShip = true;
            }
        }
        if (lastShip == 0 && !firstShip) {
            ships[0] = shipPos;
        } else {
            ships[lastShip + 1] = shipPos;
        }
        f.setShipList(ships);
    }

    // check if ship is sunk
    public static boolean isShipSunk(Field f) {
        boolean isSunk = false;
        String[] shots = f.getShipShot();
        String[] shipList = f.getShipList();
        int nrOfCells = 0;
        int nrOfHitCells = 0;
        for (int i = 0; i < shipList.length; i++) {
            if (!Objects.equals(shipList[i], null)) {
                String[] str1 = shipList[i].split(" ");
                nrOfCells = 0;
                nrOfHitCells = 0;
                for (int j = 0; j < str1.length ; j++) {
                    for (int k = 0; k < shots.length; k++) {
                        if (!Objects.equals(shots[k], null)) {
                            if (str1[j].equals(shots[k])) {
                                nrOfHitCells++;
                            }
                        }
                    }
                    nrOfCells++;
                }
            }
            if (nrOfCells == nrOfHitCells) {
                isSunk = true;
                break;
            }
        }
        return isSunk;
    }
}
