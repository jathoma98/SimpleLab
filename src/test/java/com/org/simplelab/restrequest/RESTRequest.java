package com.org.simplelab.restrequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.org.simplelab.RESTTests.session_atr;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Component
public class RESTRequest {
    public static final String NO_BODY = "no_body";

    public class RESTRequestResultActionWrapper{
        private ResultActions ra;
        public RESTRequestResultActionWrapper(ResultActions ra){
            this.ra = ra;
        }

    }

    @Autowired
    private MockMvc mvc;

    private String baseURL;

    public RESTRequest(String baseURL){
        this.baseURL = baseURL;
    }

    public enum RequestType{
        GET,
        POST,
        DELETE,
        PATCH
    }




    public RESTRequestResultActionWrapper send(RequestType type, String endpointURL) throws Exception{
        String fullUrl = baseURL + endpointURL;
        ResultActions baseRequest = null;
        switch (type){
            case GET:
                baseRequest = mvc.perform(get(fullUrl)
                                          .sessionAttrs(session_atr));
                break;
            case POST:
                baseRequest = mvc.perform(post(fullUrl)
                                          .sessionAttrs(session_atr));
                break;
            case PATCH:
                baseRequest = mvc.perform(patch(fullUrl)
                                         .sessionAttrs(session_atr));
                break;
            case DELETE:
                baseRequest = mvc.perform(delete(fullUrl)
                                          .sessionAttrs(session_atr));
                break;
            default:
                throw new Exception("BAD TEST");
        }
        return new RESTRequestResultActionWrapper(baseRequest);
    }

    public RESTRequestResultActionWrapper sendData(RequestType type, String endpointURL, String body) throws Exception{
        return null;
    }

}
