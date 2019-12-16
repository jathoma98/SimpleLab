package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.BaseController;
import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import com.org.simplelab.database.entities.sql.BaseTable;
import com.org.simplelab.database.entities.sql.User;
import com.org.simplelab.database.services.SQLService;
import com.org.simplelab.database.validators.Validator;
import com.org.simplelab.exception.EntityDBModificationException;
import com.org.simplelab.exception.EntitySetModificationException;
import com.org.simplelab.exception.InvalidFieldException;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 * Contains generic implementations of common REST endpoints.
 * @param <T> - The Entity which each endpoint modifies.
 * @Author Jacob Thomas
 */
@Component
abstract class BaseRESTController<T extends BaseTable> extends BaseController {

    @Autowired
    HttpSession session;

    //must be implemented by all RESTControllers
    public abstract SQLService<T> getDb();

    /**
     * Adds an entity to the application database.
     * @param validator - Validator object which contains all fields which need to be copied to the Entity object
     * @return - success: true on successful creation
     */
    protected RRO<String> addEntity(Validator<T> validator){
        RRO<String> rro = new RRO();
        try{
            validator.validate();
        } catch (InvalidFieldException e){
            rro.setSuccess(false);
            rro.setMsg(e.getMessage());
            rro.setAction(RRO.ACTION_TYPE.PRINT_MSG.name());
            return rro;
        }
        T created = validator.build();
        T inserted = created;

        //set the creator if its a UserCreated entity
        if (UserCreated.class.isInstance(created)){
            long user_id = getUserIdFromSession();
            UserCreated created_assign = (UserCreated)created;
            User u = userDB.findById(user_id);
            created_assign.setCreator(u);
        }
        try{
            inserted = getDb().insert(created);
        } catch (EntityDBModificationException e){
            rro.setSuccess(false);
            rro.setMsg(e.getMessage());
            rro.setAction(RRO.ACTION_TYPE.PRINT_MSG.name());
            return rro;
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        rro.setData("" + inserted.getId());
        return rro;
    }

    @Transactional
    protected RRO<T> getEntityById(long id){
        T obj = getDb().findById(id);
        RRO<T> rro = new RRO<T>();
        if (obj == null){
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.PRINT_MSG.name());
            rro.setMsg("Not found!");
            return rro;
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        rro.setData(obj);
        return rro;
    }

    protected RRO<String> updateEntity(long idToUpdate, DTO dto) {
        RRO<String> rro = new RRO();
        ModelMapper mm = DBUtils.getMapper();
        T found = getDb().findById(idToUpdate);
        if (found == null) {
            return RRO.sendErrorMessage(RRO.MSG.ENTITY_UPDATE_ENTITY_NO_FOUND.getMsg());
        }
        //if its a validator, validate before copying.
        if (Validator.class.isInstance(dto)) {
            Validator<T> DTOValidator = (Validator<T>) dto;
            try {
                DTOValidator.validate();
            } catch (InvalidFieldException e) {
                return RRO.sendErrorMessage(e.getMessage());
            }
        }
        T toUpdate = getDb().findById(idToUpdate);
        mm.map(dto, toUpdate);
        try{
            getDb().update(toUpdate);
        } catch (EntityDBModificationException e){
            return RRO.sendErrorMessage(e.getMessage());
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        return rro;
    }

    protected RRO<String> deleteEntity(long idToDelete){
        RRO<String> rro = new RRO();
        if (getDb().deleteById(idToDelete)){
            rro.setSuccess(true);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        } else {
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
    }

    protected <U extends BaseTable>
    RRO<String> addEntitiesToEntityList(SQLService.EntitySetManager<U, T> set,
                                        Collection<U> toAdd) {
        RRO<String> rro = new RRO();
        if (set == null){
            return RRO.sendErrorMessage(SQLService.EntitySetManager.NOT_FOUND_STRING);
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());

        for (U entity: toAdd){
            if (UserCreated.class.isInstance(entity)){
                User creator = userDB.findById(getUserIdFromSession());
                ((UserCreated)entity).setCreator(creator);
            }
            try{
                set.insert(entity);
            } catch (EntitySetModificationException e){
                rro = RRO.sendErrorMessage(e.getMessage());
            }
        }
        T toSave = set.getFullEntity();
        return rro;
    }

    protected  <U extends BaseTable>
    RRO<String> removeEntitiesFromEntityList(SQLService.EntitySetManager<U, T> set,
                                             Collection<U> toRemove){
        RRO<String> rro = new RRO();
        if (set == null){
            rro.setMsg(SQLService.EntitySetManager.NOT_FOUND_STRING);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            rro.setSuccess(false);
            return rro;
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        for (U entity: toRemove){
            try{
                set.delete(entity);
            } catch (EntitySetModificationException e){
                rro.setMsg(e.getMessage());
                rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
                rro.setSuccess(false);
            }
        }
        T toSave = set.getFullEntity();
        return rro;
    }

}
