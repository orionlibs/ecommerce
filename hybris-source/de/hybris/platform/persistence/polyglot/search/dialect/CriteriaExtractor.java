package de.hybris.platform.persistence.polyglot.search.dialect;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.model.TypeId;
import de.hybris.platform.persistence.polyglot.search.criteria.Condition;
import de.hybris.platform.persistence.polyglot.search.criteria.Conditions;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.persistence.polyglot.search.dialect.generated.PolyglotBaseListener;
import de.hybris.platform.persistence.polyglot.search.dialect.generated.PolyglotParser;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;
import org.antlr.v4.runtime.Token;

public class CriteriaExtractor extends PolyglotBaseListener
{
    private Criteria.CriteriaBuilder builder;
    private final Deque<Condition> conditions = new ArrayDeque<>();
    private final TypeNameConverter typeNameConverter;


    public CriteriaExtractor(TypeNameConverter typeNameConverter)
    {
        this.typeNameConverter = typeNameConverter;
    }


    public Criteria.CriteriaBuilder getBuilder()
    {
        return this.builder;
    }


    public void enterType_key(PolyglotParser.Type_keyContext ctx)
    {
        String typeName = ctx.type.getText();
        List<TypeId> subTypes = this.typeNameConverter.getSubTypes(typeName);
        this.builder = Criteria.builder(this.typeNameConverter.getTypeId(typeName), subTypes);
    }


    public void enterOrder_key(PolyglotParser.Order_keyContext ctx)
    {
        SingleAttributeKey key = createKey(ctx.key);
        Criteria.OrderByElement.Direction dir = Criteria.OrderByElement.Direction.defaultDirection();
        if(ctx.direction != null)
        {
            dir = Criteria.OrderByElement.Direction.fromString(ctx.direction.getText());
        }
        this.builder.withOrderByAttribute(key, dir);
    }


    public void exitQuery(PolyglotParser.QueryContext ctx)
    {
        Preconditions.checkState((this.conditions.size() < 2), "Expected at most one condition but was: %s", this.conditions.size());
        if(!this.conditions.isEmpty())
        {
            this.builder.withCondition(this.conditions.getFirst());
        }
    }


    public void enterExpr_atom(PolyglotParser.Expr_atomContext ctx)
    {
        if(ctx.operator != null)
        {
            Conditions.ComparisonCondition.CmpOperator operator = Conditions.ComparisonCondition.CmpOperator.fromString(ctx.operator.getText());
            SingleAttributeKey key = createKey(ctx.attribute_key());
            this.conditions.push(Conditions.cmp(key, operator, ctx.param.getText()));
        }
        else if(ctx.null_check != null)
        {
            Conditions.ComparisonCondition.CmpOperator operator = ctx.null_check.getText().toLowerCase(LocaleHelper.getPersistenceLocale()).contains("not") ? Conditions.ComparisonCondition.CmpOperator.NOT_EQUAL : Conditions.ComparisonCondition.CmpOperator.EQUAL;
            this.conditions.push(Conditions.cmp(createKey(ctx.attribute_key()), operator, null));
        }
    }


    public void exitExpr_and(PolyglotParser.Expr_andContext ctx)
    {
        if(ctx.getChildCount() < 2)
        {
            return;
        }
        toCompositeCondition(Conditions::and, 1 + ctx.getChildCount() / 2);
    }


    public void exitExpr_or(PolyglotParser.Expr_orContext ctx)
    {
        if(ctx.getChildCount() < 2)
        {
            return;
        }
        toCompositeCondition(Conditions::or, 1 + ctx.getChildCount() / 2);
    }


    private SingleAttributeKey createKey(PolyglotParser.Attribute_keyContext attributeKey)
    {
        Token langToken = attributeKey.lang;
        if(langToken != null)
        {
            return PolyglotPersistence.getLocalizedKey(attributeKey.qualifier.getText(), langToken.getText());
        }
        return PolyglotPersistence.getNonlocalizedKey(attributeKey.qualifier.getText());
    }


    private void toCompositeCondition(Function<List<Condition>, Conditions.CompositeCondition> creator, int numberOfConditionsToPop)
    {
        Condition[] children = new Condition[numberOfConditionsToPop];
        for(int i = numberOfConditionsToPop - 1; i >= 0; i--)
        {
            children[i] = this.conditions.pop();
        }
        this.conditions.push((Condition)creator.apply(Arrays.asList(children)));
    }
}
