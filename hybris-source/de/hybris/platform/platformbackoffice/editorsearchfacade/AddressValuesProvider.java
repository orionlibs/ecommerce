package de.hybris.platform.platformbackoffice.editorsearchfacade;

import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorSearchFacade;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "1905", forRemoval = true)
public class AddressValuesProvider implements ReferenceEditorSearchFacade<AddressModel>
{
    private static final String OWNER = "owner";
    private static final int PAGE_SIZE = 5;
    private ModelService modelService;


    public Pageable<AddressModel> search(SearchQueryData searchQueryData)
    {
        if(searchQueryData instanceof AdvancedSearchQueryData)
        {
            Objects.requireNonNull(UserModel.class);
            Objects.requireNonNull(UserModel.class);
            Optional<UserModel> foundUser = ((AdvancedSearchQueryData)searchQueryData).getConditions(true).stream().filter(condition -> "owner".equals(condition.getDescriptor().getAttributeName())).map(SearchQueryCondition::getValue).filter(UserModel.class::isInstance).map(UserModel.class::cast)
                            .findFirst();
            if(foundUser.isPresent())
            {
                UserModel user = foundUser.get();
                this.modelService.refresh(user);
                return (Pageable<AddressModel>)new PageableList(new ArrayList(user.getAddresses()), 5);
            }
        }
        return (Pageable<AddressModel>)new PageableList(new ArrayList(), 5);
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
