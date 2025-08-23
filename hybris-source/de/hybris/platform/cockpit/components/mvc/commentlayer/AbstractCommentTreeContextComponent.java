package de.hybris.platform.cockpit.components.mvc.commentlayer;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.CommentMainAreaBrowserComponent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.impl.ContextAreaCommentTreeModel;
import java.util.List;

public abstract class AbstractCommentTreeContextComponent extends CommentMainAreaBrowserComponent
{
    public AbstractCommentTreeContextComponent(ContextAreaCommentTreeModel model, AbstractContentBrowser contentBrowser)
    {
        super((AdvancedBrowserModel)model, contentBrowser);
    }


    public boolean initialize()
    {
        getModel().setComments(getComments());
        if(!this.initialized)
        {
            this.initialized = super.initialize();
        }
        return this.initialized;
    }


    public ContextAreaCommentTreeModel getModel()
    {
        return (ContextAreaCommentTreeModel)super.getModel();
    }


    protected abstract List<TypedObject> getComments();
}
