package io.github.orionlibs.ecommerce.store;

import io.github.orionlibs.ecommerce.store.api.SaveStoreRequest;
import io.github.orionlibs.ecommerce.store.model.StoreModel;
import io.github.orionlibs.ecommerce.store.model.StoresDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreService
{
    @Autowired private StoresDAO dao;


    @Transactional
    public StoreModel save(SaveStoreRequest request)
    {
        StoreModel model = new StoreModel();
        model.setName(request.getStoreName());
        return save(model);
    }


    @Transactional
    public StoreModel save(StoreModel model)
    {
        return dao.saveAndFlush(model);
    }


    @Transactional
    public void deleteAll()
    {
        dao.deleteAll();
    }
}
