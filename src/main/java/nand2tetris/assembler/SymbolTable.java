package nand2tetris.assembler;

import java.util.HashMap;

/**
 * Class which maintains the symbol table for an assembly file.
 */
public class SymbolTable {
    /**Storage for symbol mappings*/
    private HashMap<String, Integer> map;

    /**
     * Creates a new symbol table populated with default symbols.
     */
    public SymbolTable() {
        map = new HashMap<>();
        map.put("SP", 0);
        map.put("LCL", 1);
        map.put("ARG", 2);
        map.put("THIS", 3);
        map.put("THAT", 4);
        map.put("SCREEN", 16384);
        map.put("KBD", 24576);
        for (int i = 0; i < 16; i++) {
            map.put("R" + i, i);
        }
    }

    /**
     * Adds an entry to the symbol table.
     *
     * @param _symbol The symbol to be added.
     * @param _address The address to assign to this symbol.
     */
    public void addEntry(String _symbol, int _address) {
        map.put(_symbol, _address);
    }

    /**
     * Checks whether the table contains the given symbol.
     *
     * @param _symbol The symbol to look for.
     * @return True if the table contains this symbol, false if not.
     */
    public boolean contains(String _symbol) {
        return map.containsKey(_symbol);
    }

    /**
     * Gets the address corresponding to the given symbol.
     *
     * @param _symbol The symbol to look up.
     * @return The address assigned to this symbol, or -1 if it is not in use.
     */
    public int getAddress(String _symbol) {
        if (map.containsKey(_symbol)) {
            return map.get(_symbol);
        }
        else {
            return -1;
        }
    }
}
