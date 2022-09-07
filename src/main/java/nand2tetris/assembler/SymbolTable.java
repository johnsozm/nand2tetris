package nand2tetris.assembler;

/**
 * Class which maintains the symbol table for an assembly file.
 */
public class SymbolTable {

    /**
     * Creates a new empty symbol table.
     */
    public SymbolTable() {

    }

    /**
     * Adds an entry to the symbol table.
     *
     * @param _symbol The symbol to be added.
     * @param _address The address to assign to this symbol.
     */
    public void addEntry(String _symbol, int _address) {

    }

    /**
     * Checks whether the table contains the given symbol.
     *
     * @param _symbol The symbol to look for.
     * @return True if the table contains this symbol, false if not.
     */
    public boolean contains(String _symbol) {
        return false;
    }

    /**
     * Gets the address corresponding to the given symbol.
     *
     * @param _symbol The symbol to look up.
     * @return The address assigned to this symbol/
     */
    public int getAddress(String _symbol) {
       return 0;
    }
}
