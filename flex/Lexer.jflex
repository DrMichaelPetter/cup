package java_cup;
import java_cup.runtime.Symbol;
import java.lang.Error;
import java.io.InputStreamReader;

%%

%class Lexer
%implements sym
%public
%unicode
%line
%column
%cup
%{
    public Lexer(){
	this(new InputStreamReader(System.in));
    }
    private StringBuffer sb;
    private int csline,cscolumn;
    public Symbol symbol(int code){
//	System.out.println("code:"+code+" "+yytext());
	return new Symbol(code,yyline+1,yycolumn+1);
    }
    public Symbol symbol(int code, String lexem){
//	System.out.println("code:"+code+", lexem :"+lexem);
	return new Symbol(code, yyline+1, yycolumn + 1, lexem);
    }
    protected void emit_warning(String message){
	ErrorManager.getManager().emit_warning("Scanner at " + (yyline+1) + "(" + (yycolumn+1) + "): " + message);
    }
    protected void emit_error(String message){
	ErrorManager.getManager().emit_error("Scanner at " + (yyline+1) + "(" + (yycolumn+1) +  "): " + message);
    }
%}

Newline = \r | \n | \r\n
Whitespace = [ \t\f] | {Newline}

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" {CommentContent} \*+ "/"
EndOfLineComment = "//" [^\r\n]* {Newline}
CommentContent = ( [^*] | \*+[^*/] )*

ident = ([:jletter:] | "_" ) ([:jletterdigit:] | [:jletter:] | "_" )*


%eofval{
    return new Symbol(sym.EOF);
%eofval}

%state CODESEG

%%  

<YYINITIAL> {

  {Whitespace}  {                                      }
  "?"           { return symbol(QUESTION);             }
  ";"           { return symbol(SEMI);                 }
  ","           { return symbol(COMMA);                }
  "*"           { return symbol(STAR);                 }
  "."           { return symbol(DOT);                  }
  "|"           { return symbol(BAR);                  }
  "["           { return symbol(LBRACK);               }
  "]"           { return symbol(RBRACK);               }
  ":"           { return symbol(COLON);                }
  "::="         { return symbol(COLON_COLON_EQUALS);   }
  "%prec"       { return symbol(PERCENT_PREC);         }
  ">"           { return symbol(GT);                   }
  "<"           { return symbol(LT);                   }
  {Comment}     {                                      }
  "{:"          { sb = new StringBuffer(); csline=yyline+1; cscolumn=yycolumn+1; yybegin(CODESEG);    }
  "package"     { return symbol(PACKAGE);              } 
  "import"      { return symbol(IMPORT);	       }
  "code"        { return symbol(CODE);		       }
  "action"      { return symbol(ACTION);	       }
  "parser"      { return symbol(PARSER);	       }
  "terminal"    { return symbol(TERMINAL);	       }
  "non"         { return symbol(NON);		       }
  "nonterminal" { return symbol(NONTERMINAL);          }
  "init"        { return symbol(INIT);		       }
  "scan"        { return symbol(SCAN);		       }
  "with"        { return symbol(WITH);		       }
  "start"       { return symbol(START);		       }
  "precedence"  { return symbol(PRECEDENCE);	       }
  "left"        { return symbol(LEFT);		       }
  "right"       { return symbol(RIGHT);		       }
  "nonassoc"    { return symbol(NONASSOC);             }
  "extends"     { return symbol(EXTENDS);              }
  "super"       { return symbol(SUPER);                }
  {ident}       { return symbol(ID,yytext());          }
  
}

<CODESEG> {
  ":}"         { yybegin(YYINITIAL); return new Symbol(CODE_STRING,csline, cscolumn, sb.toString()); }
  .|\n            { sb.append(yytext()); }
}

// error fallback
.|\n          { emit_warning("Unrecognized character '" +yytext()+"' -- ignored"); }
