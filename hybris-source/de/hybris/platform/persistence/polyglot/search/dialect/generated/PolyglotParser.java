package de.hybris.platform.persistence.polyglot.search.dialect.generated;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ATNSimulator;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

public class PolyglotParser extends Parser
{
    protected static final DFA[] _decisionToDFA;

    static
    {
        RuntimeMetaData.checkVersion("4.7.1", "4.7.1");
    }

    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1;
    public static final int T__1 = 2;
    public static final int T__2 = 3;
    public static final int T__3 = 4;
    public static final int T__4 = 5;
    public static final int T__5 = 6;
    public static final int T__6 = 7;
    public static final int T__7 = 8;
    public static final int ORDER_BY = 9;
    public static final int GET = 10;
    public static final int WHERE = 11;
    public static final int CMP_OPERATOR = 12;
    public static final int NULL_OPERATOR = 13;
    public static final int AND_OPERATOR = 14;
    public static final int OR_OPERATOR = 15;
    public static final int ORDER_DIRECTION = 16;
    public static final int IDENTIFIER = 17;
    public static final int WS = 18;
    public static final int RULE_query = 0;
    public static final int RULE_where_clause = 1;
    public static final int RULE_type_key = 2;
    public static final int RULE_expr_or = 3;
    public static final int RULE_expr_and = 4;
    public static final int RULE_expr_atom = 5;
    public static final int RULE_attribute_key = 6;
    public static final int RULE_order_key = 7;
    public static final int RULE_order_by = 8;
    public static final String[] ruleNames = new String[] {"query", "where_clause", "type_key", "expr_or", "expr_and", "expr_atom", "attribute_key", "order_key", "order_by"};
    private static final String[] _LITERAL_NAMES = new String[] {null, "'{'", "'}'", "'?'", "'('", "')'", "'['", "']'", "','"};
    private static final String[] _SYMBOLIC_NAMES = new String[] {
                    null, null, null, null, null, null, null, null, null, "ORDER_BY",
                    "GET", "WHERE", "CMP_OPERATOR", "NULL_OPERATOR", "AND_OPERATOR", "OR_OPERATOR", "ORDER_DIRECTION", "IDENTIFIER", "WS"};
    public static final Vocabulary VOCABULARY = (Vocabulary)new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
    @Deprecated(since = "ages", forRemoval = true)
    public static final String[] tokenNames = new String[_SYMBOLIC_NAMES.length];
    public static final String _serializedATN = "\003悋Ꜫ脳맭䅼㯧瞆奤\003\024Z\004\002\t\002\004\003\t\003\004\004\t\004\004\005\t\005\004\006\t\006\004\007\t\007\004\b\t\b\004\t\t\t\004\n\t\n\003\002\003\002\003\002\003\002\005\002\031\n\002\003\002\003\002\003\003\003\003\003\003\005\003 \n\003\003\004\003\004\003\004\003\004\003\005\003\005\003\005\007\005)\n\005\f\005\016\005,\013\005\003\006\003\006\003\006\007\0061\n\006\f\006\016\0064\013\006\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\005\007B\n\007\003\b\003\b\003\b\003\b\003\b\005\bI\n\b\003\b\003\b\003\t\003\t\005\tO\n\t\003\n\003\n\003\n\003\n\007\nU\n\n\f\n\016\nX\013\n\003\n\002\002\013\002\004\006\b\n\f\016\020\022\002\002\002Y\002\024\003\002\002\002\004\037\003\002\002\002\006!\003\002\002\002\b%\003\002\002\002\n-\003\002\002\002\fA\003\002\002\002\016C\003\002\002\002\020L\003\002\002\002\022P\003\002\002\002\024\025\007\f\002\002\025\026\005\006\004\002\026\030\005\004\003\002\027\031\005\022\n\002\030\027\003\002\002\002\030\031\003\002\002\002\031\032\003\002\002\002\032\033\007\002\002\003\033\003\003\002\002\002\034\035\007\r\002\002\035 \005\b\005\002\036 \003\002\002\002\037\034\003\002\002\002\037\036\003\002\002\002 \005\003\002\002\002!\"\007\003\002\002\"#\007\023\002\002#$\007\004\002\002$\007\003\002\002\002%*\005\n\006\002&'\007\021\002\002')\005\n\006\002(&\003\002\002\002),\003\002\002\002*(\003\002\002\002*+\003\002\002\002+\t\003\002\002\002,*\003\002\002\002-2\005\f\007\002./\007\020\002\002/1\005\f\007\0020.\003\002\002\00214\003\002\002\00220\003\002\002\00223\003\002\002\0023\013\003\002\002\00242\003\002\002\00256\005\016\b\00267\007\016\002\00278\007\005\002\00289\007\023\002\0029B\003\002\002\002:;\005\016\b\002;<\007\017\002\002<B\003\002\002\002=>\007\006\002\002>?\005\b\005\002?@\007\007\002\002@B\003\002\002\002A5\003\002\002\002A:\003\002\002\002A=\003\002\002\002B\r\003\002\002\002CD\007\003\002\002DH\007\023\002\002EF\007\b\002\002FG\007\023\002\002GI\007\t\002\002HE\003\002\002\002HI\003\002\002\002IJ\003\002\002\002JK\007\004\002\002K\017\003\002\002\002LN\005\016\b\002MO\007\022\002\002NM\003\002\002\002NO\003\002\002\002O\021\003\002\002\002PQ\007\013\002\002QV\005\020\t\002RS\007\n\002\002SU\005\020\t\002TR\003\002\002\002UX\003\002\002\002VT\003\002\002\002VW\003\002\002\002W\023\003\002\002\002XV\003\002\002\002\n\030\037*2AHNV";

    static
    {
        int i;
        for(i = 0; i < tokenNames.length; i++)
        {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if(tokenNames[i] == null)
            {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }
            if(tokenNames[i] == null)
            {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Deprecated(since = "ages", forRemoval = true)
    public String[] getTokenNames()
    {
        return tokenNames;
    }


    public Vocabulary getVocabulary()
    {
        return VOCABULARY;
    }


    public String getGrammarFileName()
    {
        return "Polyglot.g4";
    }


    public String[] getRuleNames()
    {
        return ruleNames;
    }


    public String getSerializedATN()
    {
        return "\003悋Ꜫ脳맭䅼㯧瞆奤\003\024Z\004\002\t\002\004\003\t\003\004\004\t\004\004\005\t\005\004\006\t\006\004\007\t\007\004\b\t\b\004\t\t\t\004\n\t\n\003\002\003\002\003\002\003\002\005\002\031\n\002\003\002\003\002\003\003\003\003\003\003\005\003 \n\003\003\004\003\004\003\004\003\004\003\005\003\005\003\005\007\005)\n\005\f\005\016\005,\013\005\003\006\003\006\003\006\007\0061\n\006\f\006\016\0064\013\006\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\005\007B\n\007\003\b\003\b\003\b\003\b\003\b\005\bI\n\b\003\b\003\b\003\t\003\t\005\tO\n\t\003\n\003\n\003\n\003\n\007\nU\n\n\f\n\016\nX\013\n\003\n\002\002\013\002\004\006\b\n\f\016\020\022\002\002\002Y\002\024\003\002\002\002\004\037\003\002\002\002\006!\003\002\002\002\b%\003\002\002\002\n-\003\002\002\002\fA\003\002\002\002\016C\003\002\002\002\020L\003\002\002\002\022P\003\002\002\002\024\025\007\f\002\002\025\026\005\006\004\002\026\030\005\004\003\002\027\031\005\022\n\002\030\027\003\002\002\002\030\031\003\002\002\002\031\032\003\002\002\002\032\033\007\002\002\003\033\003\003\002\002\002\034\035\007\r\002\002\035 \005\b\005\002\036 \003\002\002\002\037\034\003\002\002\002\037\036\003\002\002\002 \005\003\002\002\002!\"\007\003\002\002\"#\007\023\002\002#$\007\004\002\002$\007\003\002\002\002%*\005\n\006\002&'\007\021\002\002')\005\n\006\002(&\003\002\002\002),\003\002\002\002*(\003\002\002\002*+\003\002\002\002+\t\003\002\002\002,*\003\002\002\002-2\005\f\007\002./\007\020\002\002/1\005\f\007\0020.\003\002\002\00214\003\002\002\00220\003\002\002\00223\003\002\002\0023\013\003\002\002\00242\003\002\002\00256\005\016\b\00267\007\016\002\00278\007\005\002\00289\007\023\002\0029B\003\002\002\002:;\005\016\b\002;<\007\017\002\002<B\003\002\002\002=>\007\006\002\002>?\005\b\005\002?@\007\007\002\002@B\003\002\002\002A5\003\002\002\002A:\003\002\002\002A=\003\002\002\002B\r\003\002\002\002CD\007\003\002\002DH\007\023\002\002EF\007\b\002\002FG\007\023\002\002GI\007\t\002\002HE\003\002\002\002HI\003\002\002\002IJ\003\002\002\002JK\007\004\002\002K\017\003\002\002\002LN\005\016\b\002MO\007\022\002\002NM\003\002\002\002NO\003\002\002\002O\021\003\002\002\002PQ\007\013\002\002QV\005\020\t\002RS\007\n\002\002SU\005\020\t\002TR\003\002\002\002UX\003\002\002\002VT\003\002\002\002VW\003\002\002\002W\023\003\002\002\002XV\003\002\002\002\n\030\037*2AHNV";
    }


    public ATN getATN()
    {
        return _ATN;
    }


    public PolyglotParser(TokenStream input)
    {
        super(input);
        this._interp = (ATNSimulator)new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }


    public final QueryContext query() throws RecognitionException
    {
        QueryContext _localctx = new QueryContext(this._ctx, getState());
        enterRule((ParserRuleContext)_localctx, 0, 0);
        try
        {
            enterOuterAlt((ParserRuleContext)_localctx, 1);
            setState(18);
            match(10);
            setState(19);
            type_key();
            setState(20);
            where_clause();
            setState(22);
            this._errHandler.sync(this);
            int _la = this._input.LA(1);
            if(_la == 9)
            {
                setState(21);
                order_by();
            }
            setState(24);
            match(-1);
        }
        catch(RecognitionException re)
        {
            _localctx.exception = re;
            this._errHandler.reportError(this, re);
            this._errHandler.recover(this, re);
        }
        finally
        {
            exitRule();
        }
        return _localctx;
    }


    public final Where_clauseContext where_clause() throws RecognitionException
    {
        Where_clauseContext _localctx = new Where_clauseContext(this._ctx, getState());
        enterRule((ParserRuleContext)_localctx, 2, 1);
        try
        {
            enterOuterAlt((ParserRuleContext)_localctx, 1);
            setState(29);
            this._errHandler.sync(this);
            switch(this._input.LA(1))
            {
                case 11:
                    setState(26);
                    match(11);
                    setState(27);
                    expr_or();
                    break;
                case -1:
                case 9:
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch(RecognitionException re)
        {
            _localctx.exception = re;
            this._errHandler.reportError(this, re);
            this._errHandler.recover(this, re);
        }
        finally
        {
            exitRule();
        }
        return _localctx;
    }


    public final Type_keyContext type_key() throws RecognitionException
    {
        Type_keyContext _localctx = new Type_keyContext(this._ctx, getState());
        enterRule((ParserRuleContext)_localctx, 4, 2);
        try
        {
            enterOuterAlt((ParserRuleContext)_localctx, 1);
            setState(31);
            match(1);
            setState(32);
            _localctx.type = match(17);
            setState(33);
            match(2);
        }
        catch(RecognitionException re)
        {
            _localctx.exception = re;
            this._errHandler.reportError(this, re);
            this._errHandler.recover(this, re);
        }
        finally
        {
            exitRule();
        }
        return _localctx;
    }


    public final Expr_orContext expr_or() throws RecognitionException
    {
        Expr_orContext _localctx = new Expr_orContext(this._ctx, getState());
        enterRule((ParserRuleContext)_localctx, 6, 3);
        try
        {
            enterOuterAlt((ParserRuleContext)_localctx, 1);
            setState(35);
            expr_and();
            setState(40);
            this._errHandler.sync(this);
            int _la = this._input.LA(1);
            while(_la == 15)
            {
                setState(36);
                _localctx.operator = match(15);
                setState(37);
                expr_and();
                setState(42);
                this._errHandler.sync(this);
                _la = this._input.LA(1);
            }
        }
        catch(RecognitionException re)
        {
            _localctx.exception = re;
            this._errHandler.reportError(this, re);
            this._errHandler.recover(this, re);
        }
        finally
        {
            exitRule();
        }
        return _localctx;
    }


    public final Expr_andContext expr_and() throws RecognitionException
    {
        Expr_andContext _localctx = new Expr_andContext(this._ctx, getState());
        enterRule((ParserRuleContext)_localctx, 8, 4);
        try
        {
            enterOuterAlt((ParserRuleContext)_localctx, 1);
            setState(43);
            expr_atom();
            setState(48);
            this._errHandler.sync(this);
            int _la = this._input.LA(1);
            while(_la == 14)
            {
                setState(44);
                _localctx.operator = match(14);
                setState(45);
                expr_atom();
                setState(50);
                this._errHandler.sync(this);
                _la = this._input.LA(1);
            }
        }
        catch(RecognitionException re)
        {
            _localctx.exception = re;
            this._errHandler.reportError(this, re);
            this._errHandler.recover(this, re);
        }
        finally
        {
            exitRule();
        }
        return _localctx;
    }


    public final Expr_atomContext expr_atom() throws RecognitionException
    {
        Expr_atomContext _localctx = new Expr_atomContext(this._ctx, getState());
        enterRule((ParserRuleContext)_localctx, 10, 5);
        try
        {
            setState(63);
            this._errHandler.sync(this);
            switch(((ParserATNSimulator)getInterpreter()).adaptivePredict(this._input, 4, this._ctx))
            {
                case 1:
                    enterOuterAlt((ParserRuleContext)_localctx, 1);
                    setState(51);
                    attribute_key();
                    setState(52);
                    _localctx.operator = match(12);
                    setState(53);
                    match(3);
                    setState(54);
                    _localctx.param = match(17);
                    break;
                case 2:
                    enterOuterAlt((ParserRuleContext)_localctx, 2);
                    setState(56);
                    attribute_key();
                    setState(57);
                    _localctx.null_check = match(13);
                    break;
                case 3:
                    enterOuterAlt((ParserRuleContext)_localctx, 3);
                    setState(59);
                    match(4);
                    setState(60);
                    expr_or();
                    setState(61);
                    match(5);
                    break;
            }
        }
        catch(RecognitionException re)
        {
            _localctx.exception = re;
            this._errHandler.reportError(this, re);
            this._errHandler.recover(this, re);
        }
        finally
        {
            exitRule();
        }
        return _localctx;
    }


    public final Attribute_keyContext attribute_key() throws RecognitionException
    {
        Attribute_keyContext _localctx = new Attribute_keyContext(this._ctx, getState());
        enterRule((ParserRuleContext)_localctx, 12, 6);
        try
        {
            enterOuterAlt((ParserRuleContext)_localctx, 1);
            setState(65);
            match(1);
            setState(66);
            _localctx.qualifier = match(17);
            setState(70);
            this._errHandler.sync(this);
            int _la = this._input.LA(1);
            if(_la == 6)
            {
                setState(67);
                match(6);
                setState(68);
                _localctx.lang = match(17);
                setState(69);
                match(7);
            }
            setState(72);
            match(2);
        }
        catch(RecognitionException re)
        {
            _localctx.exception = re;
            this._errHandler.reportError(this, re);
            this._errHandler.recover(this, re);
        }
        finally
        {
            exitRule();
        }
        return _localctx;
    }


    public final Order_keyContext order_key() throws RecognitionException
    {
        Order_keyContext _localctx = new Order_keyContext(this._ctx, getState());
        enterRule((ParserRuleContext)_localctx, 14, 7);
        try
        {
            enterOuterAlt((ParserRuleContext)_localctx, 1);
            setState(74);
            _localctx.key = attribute_key();
            setState(76);
            this._errHandler.sync(this);
            int _la = this._input.LA(1);
            if(_la == 16)
            {
                setState(75);
                _localctx.direction = match(16);
            }
        }
        catch(RecognitionException re)
        {
            _localctx.exception = re;
            this._errHandler.reportError(this, re);
            this._errHandler.recover(this, re);
        }
        finally
        {
            exitRule();
        }
        return _localctx;
    }


    public final Order_byContext order_by() throws RecognitionException
    {
        Order_byContext _localctx = new Order_byContext(this._ctx, getState());
        enterRule((ParserRuleContext)_localctx, 16, 8);
        try
        {
            enterOuterAlt((ParserRuleContext)_localctx, 1);
            setState(78);
            match(9);
            setState(79);
            order_key();
            setState(84);
            this._errHandler.sync(this);
            int _la = this._input.LA(1);
            while(_la == 8)
            {
                setState(80);
                match(8);
                setState(81);
                order_key();
                setState(86);
                this._errHandler.sync(this);
                _la = this._input.LA(1);
            }
        }
        catch(RecognitionException re)
        {
            _localctx.exception = re;
            this._errHandler.reportError(this, re);
            this._errHandler.recover(this, re);
        }
        finally
        {
            exitRule();
        }
        return _localctx;
    }


    public static final ATN _ATN = (new ATNDeserializer())
                    .deserialize("\003悋Ꜫ脳맭䅼㯧瞆奤\003\024Z\004\002\t\002\004\003\t\003\004\004\t\004\004\005\t\005\004\006\t\006\004\007\t\007\004\b\t\b\004\t\t\t\004\n\t\n\003\002\003\002\003\002\003\002\005\002\031\n\002\003\002\003\002\003\003\003\003\003\003\005\003 \n\003\003\004\003\004\003\004\003\004\003\005\003\005\003\005\007\005)\n\005\f\005\016\005,\013\005\003\006\003\006\003\006\007\0061\n\006\f\006\016\0064\013\006\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\003\007\005\007B\n\007\003\b\003\b\003\b\003\b\003\b\005\bI\n\b\003\b\003\b\003\t\003\t\005\tO\n\t\003\n\003\n\003\n\003\n\007\nU\n\n\f\n\016\nX\013\n\003\n\002\002\013\002\004\006\b\n\f\016\020\022\002\002\002Y\002\024\003\002\002\002\004\037\003\002\002\002\006!\003\002\002\002\b%\003\002\002\002\n-\003\002\002\002\fA\003\002\002\002\016C\003\002\002\002\020L\003\002\002\002\022P\003\002\002\002\024\025\007\f\002\002\025\026\005\006\004\002\026\030\005\004\003\002\027\031\005\022\n\002\030\027\003\002\002\002\030\031\003\002\002\002\031\032\003\002\002\002\032\033\007\002\002\003\033\003\003\002\002\002\034\035\007\r\002\002\035 \005\b\005\002\036 \003\002\002\002\037\034\003\002\002\002\037\036\003\002\002\002 \005\003\002\002\002!\"\007\003\002\002\"#\007\023\002\002#$\007\004\002\002$\007\003\002\002\002%*\005\n\006\002&'\007\021\002\002')\005\n\006\002(&\003\002\002\002),\003\002\002\002*(\003\002\002\002*+\003\002\002\002+\t\003\002\002\002,*\003\002\002\002-2\005\f\007\002./\007\020\002\002/1\005\f\007\0020.\003\002\002\00214\003\002\002\00220\003\002\002\00223\003\002\002\0023\013\003\002\002\00242\003\002\002\00256\005\016\b\00267\007\016\002\00278\007\005\002\00289\007\023\002\0029B\003\002\002\002:;\005\016\b\002;<\007\017\002\002<B\003\002\002\002=>\007\006\002\002>?\005\b\005\002?@\007\007\002\002@B\003\002\002\002A5\003\002\002\002A:\003\002\002\002A=\003\002\002\002B\r\003\002\002\002CD\007\003\002\002DH\007\023\002\002EF\007\b\002\002FG\007\023\002\002GI\007\t\002\002HE\003\002\002\002HI\003\002\002\002IJ\003\002\002\002JK\007\004\002\002K\017\003\002\002\002LN\005\016\b\002MO\007\022\002\002NM\003\002\002\002NO\003\002\002\002O\021\003\002\002\002PQ\007\013\002\002QV\005\020\t\002RS\007\n\002\002SU\005\020\t\002TR\003\002\002\002UX\003\002\002\002VT\003\002\002\002VW\003\002\002\002W\023\003\002\002\002XV\003\002\002\002\n\030\037*2AHNV".toCharArray());

    static
    {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for(i = 0; i < _ATN.getNumberOfDecisions(); i++)
        {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
