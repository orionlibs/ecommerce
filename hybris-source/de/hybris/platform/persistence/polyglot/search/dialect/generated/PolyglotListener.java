package de.hybris.platform.persistence.polyglot.search.dialect.generated;

import org.antlr.v4.runtime.tree.ParseTreeListener;

public interface PolyglotListener extends ParseTreeListener
{
    void enterQuery(PolyglotParser.QueryContext paramQueryContext);


    void exitQuery(PolyglotParser.QueryContext paramQueryContext);


    void enterWhere_clause(PolyglotParser.Where_clauseContext paramWhere_clauseContext);


    void exitWhere_clause(PolyglotParser.Where_clauseContext paramWhere_clauseContext);


    void enterType_key(PolyglotParser.Type_keyContext paramType_keyContext);


    void exitType_key(PolyglotParser.Type_keyContext paramType_keyContext);


    void enterExpr_or(PolyglotParser.Expr_orContext paramExpr_orContext);


    void exitExpr_or(PolyglotParser.Expr_orContext paramExpr_orContext);


    void enterExpr_and(PolyglotParser.Expr_andContext paramExpr_andContext);


    void exitExpr_and(PolyglotParser.Expr_andContext paramExpr_andContext);


    void enterExpr_atom(PolyglotParser.Expr_atomContext paramExpr_atomContext);


    void exitExpr_atom(PolyglotParser.Expr_atomContext paramExpr_atomContext);


    void enterAttribute_key(PolyglotParser.Attribute_keyContext paramAttribute_keyContext);


    void exitAttribute_key(PolyglotParser.Attribute_keyContext paramAttribute_keyContext);


    void enterOrder_key(PolyglotParser.Order_keyContext paramOrder_keyContext);


    void exitOrder_key(PolyglotParser.Order_keyContext paramOrder_keyContext);


    void enterOrder_by(PolyglotParser.Order_byContext paramOrder_byContext);


    void exitOrder_by(PolyglotParser.Order_byContext paramOrder_byContext);
}
