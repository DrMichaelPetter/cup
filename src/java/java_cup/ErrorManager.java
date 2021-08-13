
package java_cup;
import java_cup.runtime.Symbol;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import static java_cup.ErrorManager.ConsoleColor.*;
public class ErrorManager{
    private static ErrorManager errorManager;
    private int errors = 0;
    private int warnings = 0;
    private int fatals = 0;
    public int getFatalCount() { return fatals; }
    public int getErrorCount() { return errors; }
    public int getWarningCount() { return warnings; }
    public static void clear() {
        errorManager = new ErrorManager();
    }
    public static ErrorManager getManager() { return errorManager; }
    private ErrorManager(){
    }

    //TODO: migrate to java.util.logging
    /**
     * Error message format: 
     * ERRORLEVEL at (LINE/COLUMN)@SYMBOL: MESSAGE
     * ERRORLEVEL : MESSAGE
     **/
    public void emit_fatal(String message){
        System.err.println(RED_BOLD+"fatal : "+RESET+message);
        fatals++;
    }
    public void emit_fatal(String message, Symbol sym){
        //System.err.println("Fatal at ("+sym.left+"/"+sym.right+")@"+convSymbol(sym)+" : "+message);
        System.err.println(RED_BOLD+"fatal: "+RESET+message+" @ "+sym);
        fatals++;
    }
    public void emit_warning(String message){
        System.err.println(CYAN+"warning : "+RESET+ message);
        warnings++;	
    }
    public void emit_warning(String message, Symbol sym){
//        System.err.println("Warning at ("+sym.left+"/"+sym.right+")@"+convSymbol(sym)+" : "+message);
        System.err.println(CYAN+"warning: "+RESET+message+" @ "+sym);
        warnings++;
    }
    public void emit_error(String message){
        System.err.println(RED+"error : " +RESET+ message);
        errors++;
    }
    public void emit_error(String message, Symbol sym){
//        System.err.println("Error at ("+sym.left+"/"+sym.right+")@"+convSymbol(sym)+" : "+message);
        System.err.println(RED+"error: "+RESET+message+" @ "+sym);
        errors++;
    }
    private static String convSymbol(Symbol symbol){
        String result = (symbol.value == null)? "" : " (\""+symbol.value.toString()+"\")";
        Field [] fields = sym.class.getFields();
        for (int i = 0; i < fields.length ; i++){
            if (!Modifier.isPublic(fields[i].getModifiers())) continue;
            try {
                if (fields[i].getInt(null) == symbol.sym) return fields[i].getName()+result;
            }catch (Exception ex) {
            }
        }
        return symbol.toString()+result;
    }

    public static enum ConsoleColor {
        //Color end string, color reset
        RESET("\033[0m"),
    
        // Regular Colors. Normal color, no bold, background color etc.
        BLACK("\033[0;30m"),    // BLACK
        RED("\033[0;31m"),      // RED
        GREEN("\033[0;32m"),    // GREEN
        YELLOW("\033[0;33m"),   // YELLOW
        BLUE("\033[0;34m"),     // BLUE
        MAGENTA("\033[0;35m"),  // MAGENTA
        CYAN("\033[0;36m"),     // CYAN
        WHITE("\033[0;37m"),    // WHITE
    
        // Bold
        BLACK_BOLD("\033[1;30m"),   // BLACK
        RED_BOLD("\033[1;31m"),     // RED
        GREEN_BOLD("\033[1;32m"),   // GREEN
        YELLOW_BOLD("\033[1;33m"),  // YELLOW
        BLUE_BOLD("\033[1;34m"),    // BLUE
        MAGENTA_BOLD("\033[1;35m"), // MAGENTA
        CYAN_BOLD("\033[1;36m"),    // CYAN
        WHITE_BOLD("\033[1;37m"),   // WHITE
    
        // Underline
        BLACK_UNDERLINED("\033[4;30m"),     // BLACK
        RED_UNDERLINED("\033[4;31m"),       // RED
        GREEN_UNDERLINED("\033[4;32m"),     // GREEN
        YELLOW_UNDERLINED("\033[4;33m"),    // YELLOW
        BLUE_UNDERLINED("\033[4;34m"),      // BLUE
        MAGENTA_UNDERLINED("\033[4;35m"),   // MAGENTA
        CYAN_UNDERLINED("\033[4;36m"),      // CYAN
        WHITE_UNDERLINED("\033[4;37m"),     // WHITE
    
        // Background
        BLACK_BACKGROUND("\033[40m"),   // BLACK
        RED_BACKGROUND("\033[41m"),     // RED
        GREEN_BACKGROUND("\033[42m"),   // GREEN
        YELLOW_BACKGROUND("\033[43m"),  // YELLOW
        BLUE_BACKGROUND("\033[44m"),    // BLUE
        MAGENTA_BACKGROUND("\033[45m"), // MAGENTA
        CYAN_BACKGROUND("\033[46m"),    // CYAN
        WHITE_BACKGROUND("\033[47m"),   // WHITE
    
        // High Intensity
        BLACK_BRIGHT("\033[0;90m"),     // BLACK
        RED_BRIGHT("\033[0;91m"),       // RED
        GREEN_BRIGHT("\033[0;92m"),     // GREEN
        YELLOW_BRIGHT("\033[0;93m"),    // YELLOW
        BLUE_BRIGHT("\033[0;94m"),      // BLUE
        MAGENTA_BRIGHT("\033[0;95m"),   // MAGENTA
        CYAN_BRIGHT("\033[0;96m"),      // CYAN
        WHITE_BRIGHT("\033[0;97m"),     // WHITE
    
        // Bold High Intensity
        BLACK_BOLD_BRIGHT("\033[1;90m"),    // BLACK
        RED_BOLD_BRIGHT("\033[1;91m"),      // RED
        GREEN_BOLD_BRIGHT("\033[1;92m"),    // GREEN
        YELLOW_BOLD_BRIGHT("\033[1;93m"),   // YELLOW
        BLUE_BOLD_BRIGHT("\033[1;94m"),     // BLUE
        MAGENTA_BOLD_BRIGHT("\033[1;95m"),  // MAGENTA
        CYAN_BOLD_BRIGHT("\033[1;96m"),     // CYAN
        WHITE_BOLD_BRIGHT("\033[1;97m"),    // WHITE
    
        // High Intensity backgrounds
        BLACK_BACKGROUND_BRIGHT("\033[0;100m"),     // BLACK
        RED_BACKGROUND_BRIGHT("\033[0;101m"),       // RED
        GREEN_BACKGROUND_BRIGHT("\033[0;102m"),     // GREEN
        YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),    // YELLOW
        BLUE_BACKGROUND_BRIGHT("\033[0;104m"),      // BLUE
        MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"),   // MAGENTA
        CYAN_BACKGROUND_BRIGHT("\033[0;106m"),      // CYAN
        WHITE_BACKGROUND_BRIGHT("\033[0;107m");     // WHITE
    
        private final String code;
    
        ConsoleColor(String code) {
            this.code = code;
        }
    
        @Override
        public String toString() {
            if (ansi) return code;
            return "";
        }

        static boolean ansi=false;
        static {
            if (System.console() != null && System.getenv().get("TERM") != null) ansi=true;
        }

    }
}
