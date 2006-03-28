package java_cup.runtime;

/**
 * Creates the Symbols interface, which CUP uses as default
 *
 * @version last updated 27-03-2006
 * @author Michael Petter
 */

/* *************************************************
  Interface SymbolFactory

  interface for creating new symbols  
 ***************************************************/
public interface SymbolFactory {
    // Factory methods
    public Symbol newSymbol(String name, int id, Symbol left, Symbol right, Object value);
    public Symbol newSymbol(String name, int id, Symbol left, Symbol right);
    public Symbol newSymbol(String name, int id);
    public Symbol startSymbol(String name, int id, int state);
}
