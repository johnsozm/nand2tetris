package nand2tetris.compiler;

/**
 * Custom exception for syntax errors detected during compile.
 */
public class SyntaxException extends Exception {

    /**
     * Overloaded constructor - calls default exception constructor.
     *
     * @param errorMessage The error message to throw.
     */
    public SyntaxException(String errorMessage) {
        super(errorMessage);
    }
}
