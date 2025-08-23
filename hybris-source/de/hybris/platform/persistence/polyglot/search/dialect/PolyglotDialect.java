package de.hybris.platform.persistence.polyglot.search.dialect;

import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.persistence.polyglot.search.dialect.generated.PolyglotLexer;
import de.hybris.platform.persistence.polyglot.search.dialect.generated.PolyglotParser;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class PolyglotDialect
{
    public static Criteria.CriteriaBuilder prepareCriteriaBuilder(String queryString, CriteriaExtractor.TypeNameConverter typeNameConverter)
    {
        PolyglotLexer lexer = new PolyglotLexer((CharStream)CharStreams.fromString(queryString));
        PolyglotParser parser = new PolyglotParser((TokenStream)new CommonTokenStream((TokenSource)lexer));
        parser.removeErrorListeners();
        lexer.removeErrorListeners();
        ANTLRErrorListener errorThrowingListener = createErrorThrowingListener(queryString);
        parser.addErrorListener(errorThrowingListener);
        lexer.addErrorListener(errorThrowingListener);
        CriteriaExtractor extractor = new CriteriaExtractor(typeNameConverter);
        ParseTreeWalker.DEFAULT.walk((ParseTreeListener)extractor, (ParseTree)parser.query());
        return extractor.getBuilder();
    }


    private static ANTLRErrorListener createErrorThrowingListener(String queryString)
    {
        return (ANTLRErrorListener)new Object(queryString);
    }
}
