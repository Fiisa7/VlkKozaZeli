package core;

import javax.swing.JOptionPane;

enum Side {
    LEFT,
    RIGHT,
}

class Game {

    // Pro usnadnění čtení kódu
    public final int WOLF = 0;
    public final int GOAT = 1;
    public final int CABBAGE = 2;
    public final int NONE = 3;

    public boolean play;

    public Shore left;
    public Shore right;
    // Strana na kterém břehu je převozník
    public Side ferryman;

    public void loop() {
        int repeat;
        do {
            start();
            do {
                int input = getInput(ferryman == Side.LEFT ? right : left);
                evaluate(input);
                ferryman = ferryman == Side.LEFT ? Side.RIGHT : Side.LEFT;
                play = check();
            } while (play);

            String[] options = new String[]{"Ano", "Ne"};
            repeat = JOptionPane.showOptionDialog(null, "Hrát znovu?", "VKZ", 0, 3, null, options, true);
        } while (repeat == 0);
    }

    public void start() {
        play = true;
        ferryman = Side.LEFT;
        left = new Shore("Levý břeh", true, true, true);
        right = new Shore("Pravý břeh", false, false, false);
    }

    public int getInput(Shore toShore) {
        String[] options = new String[]{
            !toShore.wolf ? "Vlk" : "",
            !toShore.goat ? "Koza" : "",
            !toShore.cabbage ? "Zelí" : "",
            "Nic"
        };
        int input = JOptionPane.showOptionDialog(null, "Převést na " + toShore.displayName + "\n\n" + toString(), "VKZ", 0, 3, null, options, true);
        return input;
    }

    // Převoz mezi břehy
    public void evaluate(int input) {
        Shore[] shores = new Shore[]{left, right};

        int currentShore = ferryman == Side.LEFT ? 0 : 1;
        int oppositeShore = ferryman == Side.LEFT ? 1 : 0;

        switch (input) {
            case WOLF:
                if (!shores[currentShore].wolf) {
                    break;
                }
                shores[currentShore].wolf = false;
                shores[oppositeShore].wolf = true;
                break;
            case GOAT:
                if (!shores[currentShore].goat) {
                    break;
                }
                shores[currentShore].goat = false;
                shores[oppositeShore].goat = true;
                break;
            case CABBAGE:
                if (!shores[currentShore].cabbage) {
                    break;
                }
                shores[currentShore].cabbage = false;
                shores[oppositeShore].cabbage = true;
                break;
        }
    }

    // Kontrola vlka a kozy, kozy a zelí, výhry; Hodnotu pro ukončení hry
    public boolean check() {
        // Výhra
        if (right.wolf && right.goat && right.cabbage) {
            JOptionPane.showMessageDialog(null, "Vyhrál si!", "VKZ", 3);
            return false;
        }

        // Převozník na pravo
        if (ferryman == Side.RIGHT) {
            if (left.wolf && left.goat) {
                JOptionPane.showMessageDialog(null, "Vlk sežral kozu!", "VKZ", 0);
                return false;
            }
            if (left.goat && left.cabbage) {
                JOptionPane.showMessageDialog(null, "Koza sežrala zelí!", "VKZ", 0);
                return false;
            }

            return true;
        }

        // Převozník na levo
        if (right.wolf && right.goat) {
            JOptionPane.showMessageDialog(null, "Vlk sežral kozu!", "VKZ", 0);
            return false;
        }
        if (right.goat && right.cabbage) {
            JOptionPane.showMessageDialog(null, "Koza sežrala zelí!", "VKZ", 0);
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        String s = "";
        s += left.toString();
        s += right.toString();
        return s;
    }

    // Třída pro sledování vlka kozy a zelí na břehu
    class Shore {

        public String displayName;
        public boolean wolf;
        public boolean goat;
        public boolean cabbage;

        public Shore(String displayName, boolean wolf, boolean goat, boolean cabbage) {
            this.displayName = displayName;
            this.wolf = wolf;
            this.goat = goat;
            this.cabbage = cabbage;
        }

        // Levý břeh: Vlk; Koza; Zelí;
        @Override
        public String toString() {
            String s = "";
            s += displayName + ":";
            s += wolf ? " Vlk;" : "";
            s += goat ? " Koza;" : "";
            s += cabbage ? " Zelí;" : "";
            s += "\n";
            return s;
        }
    }
}
