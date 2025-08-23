package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

public class CommentTypeModelBuilder
{
    private final CommentTypeModel model = new CommentTypeModel();


    private CommentTypeModel getModel()
    {
        return this.model;
    }


    public static CommentTypeModelBuilder aModel()
    {
        return new CommentTypeModelBuilder();
    }


    public CommentTypeModel build()
    {
        return getModel();
    }


    public CommentTypeModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public CommentTypeModelBuilder withName(String name)
    {
        getModel().setName(name);
        return this;
    }


    public CommentTypeModelBuilder withDomain(DomainModel domain)
    {
        getModel().setDomain(domain);
        return this;
    }


    public CommentTypeModelBuilder withMetaType(ComposedTypeModel metaType)
    {
        getModel().setMetaType(metaType);
        return this;
    }
}
