package nand2tetris.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Tokenizer class for Jack syntax analysis.
 */
public class JackTokenizer {
    /**List of tokens in the file*/
    private ArrayList<Token> tokens;
    /**Current token pointer*/
    private int currentToken;

    /**
     * Class holding a single token and its type.
     */
    private class Token {
        /**Token's string value*/
        private String value;
        /**Token's type*/
        private TokenType type;

        /**
         * Construct a new token.
         *
         * @param _value The token's value.
         * @param _type The token's type.
         */
        public Token(String _value, TokenType _type) {
            value = _value;
            type = _type;
        }

        /**
         * Get the token's value.
         *
         * @return The value, as a string.
         */
        public String getValue() {
            return value;
        }

        /**
         * Get the token's type.
         *
         * @return The type, as a TokenType enum.
         */
        public TokenType getType() {
            return type;
        }
    }

    /**
     * Enum containing all token types.
     */
    public enum TokenType {
        /**Jack keyword*/
        KEYWORD,
        /**Arithmetic or syntactic symbol*/
        SYMBOL,
        /**Identifier/name*/
        IDENTIFIER,
        /**Literal integer*/
        INT_CONST,
        /**Literal string*/
        STRING_CONST
    }

    /**
     * Enum containing all valid Jack keywords
     */
    public enum KeyWord {
        /**Class declaration*/
        CLASS,
        /**Object member function*/
        METHOD,
        /**General function*/
        FUNCTION,
        /**Constructor function*/
        CONSTRUCTOR,
        /**Integer type*/
        INT,
        /**Boolean type*/
        BOOLEAN,
        /**Character type*/
        CHAR,
        /**Void type*/
        VOID,
        /**Variable declaration*/
        VAR,
        /**Static variable declaration*/
        STATIC,
        /**Object field declaration*/
        FIELD,
        /**Assignment*/
        LET,
        /**Command*/
        DO,
        /**If statement*/
        IF,
        /**Else statement*/
        ELSE,
        /**While statement*/
        WHILE,
        /**Return statement*/
        RETURN,
        /**Literal true*/
        TRUE,
        /**Literal false*/
        FALSE,
        /**Literal null*/
        NULL,
        /**Current object reference*/
        THIS
    }

    /**
     * Constructs a new tokenizer from the given input file.
     *
     * @param _input A File object pointing to the file to be read.
     */
    public JackTokenizer(File _input) throws FileNotFoundException {
        //Read in file, trimming 1-line comments and leading/trailing whitespace
        Scanner scanner = new Scanner(_input);
        String code = "";
        currentToken = -1;
        tokens = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();

            int index = nextLine.indexOf("//");
            if (index != -1) {
                nextLine = nextLine.substring(0, index);
            }

            code += nextLine.strip() + " ";
        }

        scanner.close();

        //Remove block comments and replace tabs with spaces
        while (true) {
            int beginIndex = code.indexOf("/*");

            if (beginIndex == -1) {
                break;
            }
            else {
                int endIndex = code.indexOf("*/");
                code = code.substring(0, beginIndex) + code.substring(endIndex + 2);
            }
        }
        code = code.replace("\t", " ");

        //Tokenize remaining file
        int index = 0;
        while (index < code.length()) {
            char firstChar = code.charAt(index);
            //Literal string token
            if (firstChar == '"') {
                int beginIndex = index + 1;
                int endIndex = code.indexOf('"', beginIndex);
                tokens.add(new Token(code.substring(beginIndex, endIndex), TokenType.STRING_CONST));
                index = endIndex;
            }
            //Literal integer token
            else if (firstChar >= '0' && firstChar <= '9') {
                //Parse integer until we hit a non-numeric character
                int value = firstChar - '0';
                index++;
                while (true) {
                    char nextChar = code.charAt(index);
                    if (nextChar >= '0' && nextChar <= '9') {
                        value *= 10;
                        value += nextChar - '0';
                    }
                    else {
                        index--;
                        break;
                    }
                }

                //Check for overflow
                if (value > 32767 || value < 0) {
                    throw new IllegalArgumentException("Integer literal " + value + " is out of bounds.");
                }
                else {
                    tokens.add(new Token(Integer.toString(value), TokenType.INT_CONST));
                }
            }
            else if ("{}()[].,;+-*/&|<>=~".indexOf(firstChar) != -1) {
                tokens.add(new Token(Character.toString(firstChar), TokenType.SYMBOL));
            }
            else if (firstChar == ' ') {
                index++;
                continue;
            }
            else {
                int spaceIndex = code.indexOf(" ", index);
                int parenIndex = code.indexOf("(", index);
                int squareIndex = code.indexOf("[", index);
                int endIndex = code.length();
                if (spaceIndex != -1 && spaceIndex < endIndex) {
                    endIndex = spaceIndex;
                }
                if (parenIndex != -1 && parenIndex < endIndex) {
                    endIndex = parenIndex;
                }
                if (squareIndex != -1 && squareIndex < endIndex) {
                    endIndex = squareIndex;
                }
                String word = code.substring(index, endIndex);
                switch (word) {
                    case "class":
                    case "constructor":
                    case "function":
                    case "method":
                    case "field":
                    case "static":
                    case "var":
                    case "int":
                    case "char":
                    case "boolean":
                    case "void":
                    case "true":
                    case "false":
                    case "null":
                    case "this":
                    case "let":
                    case "do":
                    case "if":
                    case "else":
                    case "while":
                    case "return":
                        tokens.add(new Token(word, TokenType.KEYWORD));
                        break;
                    default:
                        tokens.add(new Token(word, TokenType.IDENTIFIER));
                        break;
                }
                index = endIndex;
            }

            index++;
        }
    }

    /**
     * Checks if the tokenizer has finished reading the input file.
     *
     * @return True if there are more tokens to read, false if not.
     */
    public boolean hasMoreTokens() {
        return currentToken < tokens.size() - 1;
    }

    /**
     * Advances to and parses the next token.
     */
    public void advance() {
        currentToken++;
    }

    /**
     * Gets the type of the current token.
     *
     * @return A TokenType enum representing the token's type.
     */
    public TokenType tokenType() {
        return tokens.get(currentToken).getType();
    }

    /**
     * Gets the keyword referenced by the current token. Should only be called when tokenType() is KEYWORD.
     *
     * @return A KeyWord enum representing the keyword.
     */
    public KeyWord keyWord() {
        switch (tokens.get(currentToken).getValue()) {
            case "class":
                return KeyWord.CLASS;
            case "constructor":
                return KeyWord.CONSTRUCTOR;
            case "function":
                return KeyWord.FUNCTION;
            case "method":
                return KeyWord.METHOD;
            case "field":
                return KeyWord.FIELD;
            case "static":
                return KeyWord.STATIC;
            case "var":
                return KeyWord.VAR;
            case "int":
                return KeyWord.INT;
            case "char":
                return KeyWord.CHAR;
            case "boolean":
                return KeyWord.BOOLEAN;
            case "void":
                return KeyWord.VOID;
            case "true":
                return KeyWord.TRUE;
            case "false":
                return KeyWord.FALSE;
            case "null":
                return KeyWord.NULL;
            case "this":
                return KeyWord.THIS;
            case "let":
                return KeyWord.LET;
            case "do":
                return KeyWord.DO;
            case "if":
                return KeyWord.IF;
            case "else":
                return KeyWord.ELSE;
            case "while":
                return KeyWord.WHILE;
            case "return":
                return KeyWord.RETURN;
            default:
                return null;
        }
    }

    /**
     * Gets the symbol referenced by the current token. Should only be called when tokenType() is SYMBOL.
     *
     * @return The symbol, as a character.
     */
    public char symbol() {
        return tokens.get(currentToken).getValue().charAt(0);
    }

    /**
     * Gets the identifier referenced by the current token. Should only be called when tokenType() is IDENTIFIER.
     *
     * @return The identifier, as a string.
     */
    public String identifier() {
        return tokens.get(currentToken).getValue();
    }

    /**
     * Gets the literal integer referenced by the current token. Should only be called when tokenType() is INT_CONST.
     *
     * @return The literal integer.
     */
    public int intVal() {
        return Integer.parseInt(tokens.get(currentToken).getValue());
    }

    /**
     * Gets the literal string referenced by the current token. Should only be called when tokenType() is STRING_CONST.
     *
     * @return The literal, as a String.
     */
    public String stringVal() {
        return tokens.get(currentToken).getValue();
    }
}
