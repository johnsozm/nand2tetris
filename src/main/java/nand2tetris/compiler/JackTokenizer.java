package nand2tetris.compiler;

import java.io.File;

/**
 * Tokenizer class for Jack syntax analysis.
 */
public class JackTokenizer {
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
    public JackTokenizer(File _input) {

    }

    /**
     * Checks if the tokenizer has finished reading the input file.
     *
     * @return True if there are more tokens to read, false if not.
     */
    public boolean hasMoreTokens() {
        return false;
    }

    /**
     * Advances to and parses the next token.
     */
    public void advance() {

    }

    /**
     * Gets the type of the current token.
     *
     * @return A TokenType enum representing the token's type.
     */
    public TokenType tokenType() {
        return null;
    }

    /**
     * Gets the keyword referenced by the current token. Should only be called when tokenType() is KEYWORD.
     *
     * @return A KeyWord enum representing the keyword.
     */
    public KeyWord keyWord() {
        return null;
    }

    /**
     * Gets the symbol referenced by the current token. Should only be called when tokenType() is SYMBOL.
     *
     * @return The symbol, as a character.
     */
    public char symbol() {
        return ' ';
    }

    /**
     * Gets the identifier referenced by the current token. Should only be called when tokenType() is IDENTIFIER.
     *
     * @return The identifier, as a string.
     */
    public String identifier() {
        return "";
    }

    /**
     * Gets the literal integer referenced by the current token. Should only be called when tokenType() is INT_CONST.
     *
     * @return The literal integer.
     */
    public int intVal() {
        return 0;
    }

    /**
     * Gets the literal string referenced by the current token. Should only be called when tokenType() is STRING_CONST.
     *
     * @return The literal, as a String.
     */
    public String stringVal() {
        return "";
    }
}
