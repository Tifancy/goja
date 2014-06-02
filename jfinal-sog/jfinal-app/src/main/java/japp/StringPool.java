/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package japp;

/**
 * Pool of <code>String</code> constants to prevent repeating of
 * hard-coded <code>String</code> literals in the code.
 * Due to fact that these are <code>public static final</code>
 * they will be inlined by java compiler and
 * reference to this class will be dropped.
 * There is <b>no</b> performance gain of using this pool.
 * Read: http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#3.10.5
 * <ul>
 * <li>Literal strings within the same class in the same package represent references to the same <code>String</code> object.</li>
 * <li>Literal strings within different classes in the same package represent references to the same <code>String</code> object.</li>
 * <li>Literal strings within different classes in different packages likewise represent references to the same <code>String</code> object.</li>
 * <li>Strings computed by constant expressions are computed at compile time and then treated as if they were literals.</li>
 * <li>Strings computed by concatenation at run time are newly created and therefore distinct.</li>
 * </ul>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-31 2:08
 * @since JDK 1.6
 */
public interface StringPool {
    String JFINAL_APP        = "jfinal-app";
    String AMPERSAND         = "&";
    String AND               = "and";
    String AT                = "@";
    String ASTERISK          = "*";
    String BACK_SLASH        = "\\";
    String COLON             = ":";
    String COMMA             = ",";
    String DASH              = "-";
    String DOLLAR            = "$";
    String DOT               = ".";
    String DOTDOT            = "..";
    String DOT_CLASS         = ".class";
    String DOT_JAVA          = ".java";
    String EMPTY             = "";
    String EQUALS            = "=";
    String FALSE             = "false";
    String SLASH             = "/";
    String HASH              = "#";
    String HAT               = "^";
    String LEFT_BRACE        = "{";
    String LEFT_BRACKET      = "(";
    String LEFT_CHEV         = "<";
    String NEWLINE           = "\n";
    String N                 = "n";
    String NO                = "no";
    String NULL              = "null";
    String OFF               = "off";
    String ON                = "on";
    String PERCENT           = "%";
    String PIPE              = "|";
    String PLUS              = "+";
    String QUESTION_MARK     = "?";
    String EXCLAMATION_MARK  = "!";
    String QUOTE             = "\"";
    String RETURN            = "\r";
    String TAB               = "\t";
    String RIGHT_BRACE       = "}";
    String RIGHT_BRACKET     = ")";
    String RIGHT_CHEV        = ">";
    String SEMICOLON         = ";";
    String SINGLE_QUOTE      = "'";
    String SPACE             = " ";
    String LEFT_SQ_BRACKET   = "[";
    String RIGHT_SQ_BRACKET  = "]";
    String TRUE              = "true";
    String UNDERSCORE        = "_";
    String UTF_8             = "UTF-8";
    String US_ASCII          = "US-ASCII";
    String ISO_8859_1        = "ISO-8859-1";
    String Y                 = "y";
    String YES               = "yes";
    String ONE               = "1";
    String ZERO              = "0";
    String DOLLAR_LEFT_BRACE = "${";
    String CRLF              = "\r\n";

    String HTML_NBSP  = "&nbsp;";
    String HTML_AMP   = "&amp;";
    String HTML_QUOTE = "&quot;";
    String HTML_LT    = "&lt;";
    String HTML_GT    = "&gt;";

    String LOCAL_HOST = "127.0.0.1";

    String PK_COLUMN = "id";
}
