import java.util.Arrays;

public final class HzExchange {

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private HzExchange() {
    }

    /**
     * Commonly used A4=440Hz tuning.
     */
    public static final double STANDARD_A4_TUNING = 440.0;
    /**
     * Distance that A0 to A4.
     */
    private static final int DISTANCE_A4 = 48;
    private static final int OCTAVE_CHANGE_ADJUST = -9;

    public static final Character[] SHARPS = { '#', '♯', '＃', '井', '嬰' };
    public static final Character[] FLATS = { 'b', '♭', '変' };
    public static final Character[] NATURALS = { '♮' };
    public static final Character[] DOUBLE_SHARPS = { 'x', 'X' };
    // not exist-> public static final char[] DOUBLE_FLATS = {};

    /**
     * <p>
     * Interpret notation(note name) and convert to Hz.
     * </p>
     * 
     * Examples:
     * 
     * <pre>
     * notationToHz("A4", 440.0);
     * // 440.0
     * notationToHz("D5", 432.0);
     * // 576.6508170014548
     * notationToHz("F#3", 452.893);
     * // 190.41805009675025
     * notationToHz("Gbb6", 440);
     * // 1396.9129257320155
     * notationToHz("B♯3", 440); // Synonymous With "C4"
     * // 261.6255653005986
     * notationToHz("C♭4", 440); // Synonymous With "B3"
     * // 246.94165062806206
     * notationToHz("Ex2", 440);
     * // 92.4986056779086
     * </pre>
     * 
     * @param notation {@code String} consisting of <b>notes in alphabet</b>
     *                 and <b>accidental</b> and <b>octave</b>.
     *                 <p>
     *                 Allowed notations are {@code "C4"} {@code "Eb-2"}
     *                 {@code "B#7"} {@code "F♭0"} {@code "G♮3"} etc.
     *                 </p>
     * @param tuning   set Hz for the A4 pitch.
     * @return Hz value
     * @throws IllegalArgumentException when <b>notation</b> format is wrong, or
     *                                  <b>tuning</b> value is negative.
     */
    public static double notationToHz(String notation, double tuning) throws IllegalArgumentException {
        if (notation.length() < 2) {
            throw new IllegalArgumentException("Invalid input length. (It's too short!)");
        }
        if (tuning < 0) {
            throw new IllegalArgumentException("Tuning is never be negative.");
        }

        // Uppercase A ~ G
        String alphabetNote;
        // If positive then sharp, negative then flat, 0 then natural.
        int accidental = 0;
        // 4 is standard. see DISTANCE_A4
        int octave;

        char[] noteArray = notation.toCharArray();
        int idx = 0;

        noteArray[idx] = String.valueOf(noteArray[idx]).toUpperCase().charAt(0);
        if (!Notation.contains(String.valueOf(noteArray[idx]))/* !('A' <= noteArray[idx] && noteArray[idx] <= 'G') */) {
            throw new IllegalArgumentException();
        }
        alphabetNote = String.valueOf(noteArray[idx]);

        idx++;

        while (true) {
            if (contains(noteArray[idx], SHARPS)) {
                accidental++;
                idx++;
                continue;
            }
            if (contains(noteArray[idx], FLATS)) {
                accidental--;
                idx++;
                continue;
            }
            if (contains(noteArray[idx], DOUBLE_SHARPS)) {
                accidental += 2;
                idx++;
                continue;
            }
            if (contains(noteArray[idx], NATURALS)) {
                idx++;
                continue;
            }
            break;
        }

        try {
            octave = Integer.parseInt(String.valueOf(Arrays.copyOfRange(noteArray, idx, noteArray.length)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        /*
         * System.out.print(alphabetNote + ", ");
         * System.out.print(accidental + ", ");
         * System.out.println(octave);
         */

        // tuning * 2 ^ ({distance from A4} / 12)
        double hz = tuning * Math.pow(2,
                (Notation.valueOf(alphabetNote).getRelativeDistance() + accidental + (octave * 12 - DISTANCE_A4)
                        + OCTAVE_CHANGE_ADJUST)
                        / 12.0);
        return hz;
    }

    /**
     * <p>
     * Interpret notation(note name) and convert to Hz.
     * </p>
     * <p>
     * Tuning is set to A4=440Hz.
     * </p>
     * 
     * Examples:
     * 
     * <pre>
     * notationToHz("A4");
     * // 440.0
     * notationToHz("D5");
     * // 587.3295358348151
     * notationToHz("F#3");
     * // 184.9972113558172
     * notationToHz("Gbb6");
     * // 1396.9129257320155
     * notationToHz("B♯3"); // Synonymous With "C4"
     * // 261.6255653005986
     * notationToHz("C♭4"); // Synonymous With "B3"
     * // 246.94165062806206
     * notationToHz("Ex2");
     * // 92.4986056779086
     * </pre>
     * 
     * @param notation {@code String} consisting of <b>notes in alphabet</b>
     *                 and <b>accidental</b> and <b>octave</b>.
     *                 <p>
     *                 Allowed notations are {@code "C4"} {@code "Eb-2"}
     *                 {@code "B#7"} {@code "F♭0"} {@code "G♮3"} etc.
     *                 </p>
     * @return Hz value
     * @throws IllegalArgumentException when <b>notation</b> format is wrong.
     */
    public static double notationToHz(String notation) throws IllegalArgumentException {
        return notationToHz(notation, STANDARD_A4_TUNING);
    }

    private static <T> boolean contains(T element, T[] array) {
        for (T elem : array) {
            if (element.equals(elem)) {
                return true;
            }
        }
        for (int i = 0; i < array.length; i++) {
            if (element.equals(array[i])) {
                return true;
            }
        }
        return false;
    }
}

enum Notation {
    A(9),
    B(11),
    C(0),
    D(2),
    E(4),
    F(5),
    G(7);

    private int relativeDistance;

    private Notation(int relativeDistance) {
        this.relativeDistance = relativeDistance;
    }

    /**
     * <p>
     * get a relative distance between C.
     * </p>
     * 
     * @return relativeDistance
     */
    public int getRelativeDistance() {
        return relativeDistance;
    }

    /**
     * <p>
     * Search all of const and find a same const name.
     * </p>
     * <p>
     * Please note that lowercase are not contain.
     * </p>
     * 
     * @param note that single character {@code String}
     * @return
     *         <p>
     *         If <b>note</b> was contain then {@code true} else {@code false}
     *         </p>
     */
    public static boolean contains(String note) {
        for (Notation notation : Notation.values()) {
            if (notation.toString().equals(note)) {
                return true;
            }
        }
        return false;
    }
}
