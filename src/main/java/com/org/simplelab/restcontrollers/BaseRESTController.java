package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.BaseController;
import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.services.DBService;
import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.BaseTable;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import com.org.simplelab.database.validators.InvalidFieldException;
import com.org.simplelab.database.validators.Validator;
import org.bouncycastle.cert.ocsp.Req;
import org.codehaus.jackson.map.Serializers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Contains generic implementations of common REST endpoints.
 * @param <T> - The Entity which each endpoint modifies.
 * @Author Jacob Thomas
 */
@Component
public abstract class BaseRESTController<T extends BaseTable> extends BaseController {

    @Autowired
    HttpSession session;

    /**
     * Adds an entity to the application database.
     * @param validator - Validator object which contains all fields which need to be copied to the Entity object
     * @param db - The DB that manages the entity to be created
     * @return - success: true on successful creation
     */
    protected Map addEntity(Validator<T> validator, DBService<T> db){
        RequestResponse response = new RequestResponse();
        try{
            validator.validate();
        } catch (InvalidFieldException e){
            response.setSuccess(false);
            response.setError(e.getMessage());
            return response.map();
        }
        T created = validator.build();

        //set the creator if its a UserCreated entity
        if (UserCreated.class.isInstance(created)){
            long user_id = getUserIdFromSession(session);
            UserCreated created_assign = (UserCreated)created;
            User u = userDB.findById(user_id);
            created_assign.setCreator(u);
        }
        try{
            db.insert(created);
        } catch (DBService.EntityInsertionException e){
            response.setSuccess(false);
            response.setError(e.getMessage());
            return response.map();
        }
        response.setSuccess(true);
        return response.map();
    }

    protected T getEntityById(long id, DBService<T> db){
        return db.findById(id);
    }

    protected Map updateEntity(long idToUpdate, Object DTO, DBService<T> db){
        RequestResponse response = new RequestResponse();
        ModelMapper mm = DBUtils.getMapper();
        T found = db.findById(idToUpdate);
        if (found == null){
            response.setError("Failed to update: Entity not found.");
            return response.map();
        }
        //if its a validator, validate before copying.
        if (Validator.class.isInstance(DTO)){
            Validator<T> DTOValidator = (Validator<T>)DTO;
            try{
                DTOValidator.validate();
            } catch (InvalidFieldException e){
                response.setSuccess(false);
                response.setError(e.getMessage());
                return response.map();
            }
        }
        T toUpdate = db.findById(idToUpdate);
        mm.map(DTO, toUpdate);
        if (db.update(toUpdate)){
            response.setSuccess(true);
            return response.map();
        } else {
            response.setSuccess(false);
            response.setError("Error while updating entity");
            return response.map();
        }
    }

    protected Map deleteEntity(long idToDelete, DBService<T> db){
        RequestResponse response = new RequestResponse();
        if (db.deleteById(idToDelete)){
            response.setSuccess(true);
            return response.map();
        } else {
            response.setSuccess(false);
            return response.map();
        }
    }

    protected <U extends BaseTable>
    Map addEntitiesToEntityList(DBService.EntitySetManager<U,T> set,
                                List<U> toAdd, DBService<T> db) {
        RequestResponse response = new RequestResponse();
        if (set == null){
            response.setError(DBService.EntitySetManager.NOT_FOUND_STRING);
            return response.map();
        }
        response.setSuccess(true);
        for (U entity: toAdd){
            try{
                set.insert(entity);
            } catch (DBService.EntitySetManager.EntitySetModificationException e){
                response.setError(e.getMessage());
                response.setSuccess(false);
            }
        }
        T toSave = set.getFullEntity();
        db.update(toSave);
        return response.map();
    }

    protected  <U extends BaseTable>
    Map removeEntitiesFromEntityList(DBService.EntitySetManager<U, T> set,
                                     List<U> toRemove, DBService<T> db){
        RequestResponse response = new RequestResponse();
        if (set == null){
            response.setError(DBService.EntitySetManager.NOT_FOUND_STRING);
            return response.map();
        }
        response.setSuccess(true);
        for (U entity: toRemove){
            try{
                set.delete(entity);
            } catch (DBService.EntitySetManager.EntitySetModificationException e){
                response.setError(e.getMessage());
                response.setSuccess(false);
            }
        }
        T toSave = set.getFullEntity();
        db.update(toSave);
        return response.map();
    }

}
