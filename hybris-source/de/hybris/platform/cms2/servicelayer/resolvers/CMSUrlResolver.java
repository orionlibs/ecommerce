package de.hybris.platform.cms2.servicelayer.resolvers;

public interface CMSUrlResolver<T extends de.hybris.platform.cms2.model.contents.CMSItemModel>
{
    String resolve(T paramT);
}
