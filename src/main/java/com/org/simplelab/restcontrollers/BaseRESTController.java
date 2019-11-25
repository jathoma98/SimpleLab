package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.BaseController;
import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.BaseTable;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import com.org.simplelab.database.services.DBService;
import com.org.simplelab.database.validators.InvalidFieldException;
import com.org.simplelab.database.validators.Validator;
import com.org.simplelab.restcontrollers.rro.RRO;
import com.org.simplelab.restcontrollers.rro.RRO_ACTION_TYPE;
import com.org.simplelab.restcontrollers.rro.RRO_MSG;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    protected RRO<String> addEntity(Validator<T> validator, DBService<T> db){
        RRO<String> rro = new RRO();
        try{
            validator.validate();
        } catch (InvalidFieldException e){
            rro.setSuccess(false);
            rro.setMsg(e.getMessage());
            rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
            return rro;
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
            rro.setSuccess(true);
            rro.setMsg(e.getMessage());
            rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
            return rro;
        }
        rro.setSuccess(true);
        rro.setAction(RRO_ACTION_TYPE.NOTHING.name());
        return rro;
    }

    protected T getEntityById(long id, DBService<T> db){
        return db.findById(id);
    }

    protected RRO<String> updateEntity(long idToUpdate, Object DTO, DBService<T> db){
        RRO<String> rro = new RRO();
        ModelMapper mm = DBUtils.getMapper();
        T found = db.findById(idToUpdate);
        if (found == null){
            rro.setSuccess(false);
            rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
            rro.setMsg(RRO_MSG.ENTITY_UPDATE_ENTITY_NO_FOUND.getMsg());
            return rro;
        }
        //if its a validator, validate before copying.
        if (Validator.class.isInstance(DTO)){
            Validator<T> DTOValidator = (Validator<T>)DTO;
            try{
                DTOValidator.validate();
            } catch (InvalidFieldException e){
                rro.setSuccess(false);
                rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
                rro.setMsg(e.getMessage());
                return rro;
            }
        }
        T toUpdate = db.findById(idToUpdate);
        mm.map(DTO, toUpdate);
        if (db.update(toUpdate)){
            rro.setSuccess(true);
            rro.setAction(RRO_ACTION_TYPE.NOTHING.name());
            return rro;
        } else {
            rro.setSuccess(false);
            rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
            rro.setMsg(RRO_MSG.ENTITY_UPDATE_ERROR.getMsg());
            return rro;
        }
    }

    protected RRO<String> deleteEntity(long idToDelete, DBService<T> db){
        RRO<String> rro = new RRO();
        if (db.deleteById(idToDelete)){
            rro.setSuccess(true);
            rro.setAction(RRO_ACTION_TYPE.NOTHING.name());
            return rro;
        } else {
            rro.setSuccess(false);
            rro.setAction(RRO_ACTION_TYPE.NOTHING.name());
            return rro;
        }
    }

    protected <U extends BaseTable>
    RRO<String> addEntitiesToEntityList(DBService.EntitySetManager<U,T> set,
                                List<U> toAdd, DBService<T> db) {
        RRO<String> rro = new RRO();
        if (set == null){
            rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
            rro.setMsg(DBService.EntitySetManager.NOT_FOUND_STRING);
            rro.setSuccess(false);
            return rro;
        }

        rro.setSuccess(true);
        rro.setAction(RRO_ACTION_TYPE.NOTHING.name());

        for (U entity: toAdd){
            try{
                set.insert(entity);
            } catch (DBService.EntitySetManager.EntitySetModificationException e){
                rro.setMsg(e.getMessage());
                rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
                rro.setSuccess(false);
            }
        }
        T toSave = set.getFullEntity();
        db.update(toSave);
        return rro;
    }

    protected  <U extends BaseTable>
    RRO<String> removeEntitiesFromEntityList(DBService.EntitySetManager<U, T> set,
                                     List<U> toRemove, DBService<T> db){
        RRO<String> rro = new RRO();
        if (set == null){
            rro.setMsg(DBService.EntitySetManager.NOT_FOUND_STRING);
            rro.setAction(RRO_ACTION_TYPE.NOTHING.name());
            rro.setSuccess(false);
            return rro;
        }
        rro.setSuccess(true);
        rro.setAction(RRO_ACTION_TYPE.NOTHING.name());
        for (U entity: toRemove){
            try{
                set.delete(entity);
            } catch (DBService.EntitySetManager.EntitySetModificationException e){
                rro.setMsg(e.getMessage());
                rro.setAction(RRO_ACTION_TYPE.NOTHING.name());
                rro.setSuccess(false);
            }
        }
        T toSave = set.getFullEntity();
        db.update(toSave);
        return rro;
    }

}
