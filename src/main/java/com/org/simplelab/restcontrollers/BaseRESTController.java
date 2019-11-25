package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.BaseController;
import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.DBService;
import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.BaseTable;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import com.org.simplelab.database.validators.InvalidFieldException;
import com.org.simplelab.database.validators.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Contains generic implementations of common REST endpoints.
 * @param <T> - The Entity which each endpoint modifies.
 */
@Component
public class BaseRESTController<T extends BaseTable> extends BaseController {

    @Autowired
    HttpSession session;

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

}
