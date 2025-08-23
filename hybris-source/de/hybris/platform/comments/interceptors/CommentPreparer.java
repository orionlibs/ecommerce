package de.hybris.platform.comments.interceptors;

import de.hybris.platform.cockpit.model.CommentMetadataModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Required;

public class CommentPreparer implements PrepareInterceptor
{
    private KeyGenerator keyGenerator;
    private ModelService modelService;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CommentModel)
        {
            CommentModel modelImpl = (CommentModel)model;
            prepareUniqueId(modelImpl);
            deleteUnrelatedMetaData(modelImpl);
        }
    }


    private void prepareUniqueId(CommentModel model)
    {
        if(model.getCode() == null)
        {
            model.setCode((String)this.keyGenerator.generate());
        }
    }


    private void deleteUnrelatedMetaData(CommentModel modelImpl)
    {
        if(!this.modelService.isNew(modelImpl))
        {
            Collection<CommentMetadataModel> commentMetadata = new ArrayList<>(modelImpl.getCommentMetadata());
            Iterator<CommentMetadataModel> iterator = commentMetadata.iterator();
            boolean removed = false;
            while(iterator.hasNext())
            {
                CommentMetadataModel metadata = iterator.next();
                if(metadata.getComment() == null)
                {
                    iterator.remove();
                    this.modelService.remove(metadata);
                    removed = true;
                }
            }
            if(removed)
            {
                modelImpl.setCommentMetadata(commentMetadata);
            }
        }
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
