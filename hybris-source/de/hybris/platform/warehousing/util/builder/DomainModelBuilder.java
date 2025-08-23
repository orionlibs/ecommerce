package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.DomainModel;
import java.util.Collection;

public class DomainModelBuilder
{
    private final DomainModel model = new DomainModel();


    private DomainModel getModel()
    {
        return this.model;
    }


    public static DomainModelBuilder aModel()
    {
        return new DomainModelBuilder();
    }


    public DomainModel build()
    {
        return getModel();
    }


    public DomainModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public DomainModelBuilder withName(String name)
    {
        getModel().setName(name);
        return this;
    }


    public DomainModelBuilder withCommentTypes(Collection<CommentTypeModel> commentTypes)
    {
        getModel().setCommentTypes(commentTypes);
        return this;
    }
}
