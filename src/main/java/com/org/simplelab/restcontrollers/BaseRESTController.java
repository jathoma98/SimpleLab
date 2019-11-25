package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.BaseController;
import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.DBService;
import com.org.simplelab.database.entities.BaseTable;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.entities.interfaces.UserCreated;
import com.org.simplelab.database.validators.InvalidFieldException;
import com.org.simplelab.database.validators.Validator;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class BaseRESTController<T extends BaseTable> extends BaseController {

    protected Map addEntity(Validator<T> validator, HttpSession session, DBService<T> db){
        RequestResponse response = new RequestResponse();
        long user_id = getUserIdFromSession(session);
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
            UserCreated created_assign = (UserCreated)created;
            User u = userDB.findUserById(user_id);
            created_assign.setCreator(u);
        }
        if (db.insert(created)){
            response.setSuccess(true);
            return response.map();
        } else {
            response.setSuccess(false);
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
