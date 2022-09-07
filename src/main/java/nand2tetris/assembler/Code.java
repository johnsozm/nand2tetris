package nand2tetris.assembler;

/**
 * Class containing static methods for translating assembly mnemonics to machine code.
 */
public class Code {
    /**
     * Translates a destination mnemonic to machine code.
     *
     * @param _dest The destination mnemonic to translate
     * @return The corresponding machine code segment, as a string containing 1 or 0 characters.
     */
    public static String dest(String _dest) {
        String ret = "";
        if (_dest.contains("A")) {
            ret += "1";
        }
        else {
            ret += "0";
        }
        if (_dest.contains("D")) {
            ret += "1";
        }
        else {
            ret += "0";
        }
        if (_dest.contains("M")) {
            ret += "1";
        }
        else {
            ret += "0";
        }
        return ret;
    }

    /**
     * Translates a comparison mnemonic to machine code.
     *
     * @param _comp The comparison mnemonic to translate
     * @return The corresponding machine code segment, as a string containing 1 or 0 characters.
     */
    public static String comp(String _comp) {
        switch (_comp) {
            case "0":
                return "0101010";
            case "1":
                return "0111111";
            case "-1":
                return "0111010";
            case "D":
                return "0001100";
            case "A":
                return "0110000";
            case "M":
                return "1110000";
            case "!D":
                return "0001101";
            case "!A":
                return "0110001";
            case "!M":
                return "1110001";
            case "-D":
                return "0001111";
            case "-A":
                return "0110011";
            case "-M":
                return "1110011";
            case "D+1":
            case "1+D":
                return "0011111";
            case "A+1":
            case "1+A":
                return "0110111";
            case "M+1":
            case "1+M":
                return "1110111";
            case "D-1":
                return "0001110";
            case "A-1":
                return "0110010";
            case "M-1":
                return "1110010";
            case "D+A":
            case "A+D":
                return "0000010";
            case "D+M":
            case "M+D":
                return "1000010";
            case "D-A":
                return "0010011";
            case "D-M":
                return "1010011";
            case "A-D":
                return "0000111";
            case "M-D":
                return "1000111";
            case "D&A":
            case "A&D":
                return "0000000";
            case "D&M":
            case "M&D":
                return "1000000";
            case "D|A":
            case "A|D":
                return "0010101";
            case "D|M":
            case "M|D":
                return "1010101";
            default:
                return null;
        }
    }

    /**
     * Translates a jump mnemonic to machine code.
     *
     * @param _jump The jump mnemonic to translate
     * @return The corresponding machine code segment, as a string containing 1 or 0 characters.
     */
    public static String jump(String _jump) {
        switch (_jump) {
            case "JGT":
                return "001";
            case "JEQ":
                return "010";
            case "JGE":
                return "011";
            case "JLT":
                return "100";
            case "JNE":
                return "101";
            case "JLE":
                return "110";
            case "JMP":
                return "111";
            default:
                return "000";
        }
    }
}
