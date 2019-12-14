package com.org.simplelab.restrequest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.org.simplelab.resttests.RESTTests.session_atr;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RESTRequest {

    private MockMvc mvc;
    private String baseURL;
    private boolean enablePrintouts;

    public RESTRequest(MockMvc mvc,String baseURL){
        this.baseURL = baseURL;
        this.mvc = mvc;
        this.enablePrintouts = true;
    }

    public RESTRequest(MockMvc mvc, String baseURL, boolean enable){
       this(mvc, baseURL);
       this.enablePrintouts = enable;
    }

    @FunctionalInterface
    interface MVCMethodType{
        MockHttpServletRequestBuilder sendRequest(String url);
    }

    public enum RequestType{
        GET(MockMvcRequestBuilders::get),
        POST(MockMvcRequestBuilders::post),
        DELETE(MockMvcRequestBuilders::delete),
        PATCH(MockMvcRequestBuilders::patch);

        private final MVCMethodType method;
        RequestType(MVCMethodType method){
            this.method = method;
        }
    }


    public RESTRequestResultActionWrapper send(RequestType type, String endpointURL) throws Exception{
        String fullUrl = baseURL + endpointURL;
        MVCMethodType requestMethod = type.method;
        ResultActions baseRequest;

        baseRequest =  mvc.perform(requestMethod.sendRequest(fullUrl)
                                                .sessionAttrs(session_atr));
        return new RESTRequestResultActionWrapper(baseRequest, this.enablePrintouts);
    }

    public RESTRequestResultActionWrapper sendData(RequestType type, String endpointURL, String body) throws Exception{
        String fullUrl = baseURL + endpointURL;
        MVCMethodType requestMethod = type.method;
        ResultActions baseRequest;
        baseRequest = mvc.perform(requestMethod.sendRequest(fullUrl)
                                                .sessionAttrs(session_atr)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(body));
        return new RESTRequestResultActionWrapper(baseRequest, this.enablePrintouts);
    }

    public class RESTRequestResultActionWrapper{
        private ResultActions ra;
        private boolean enable;

        public RESTRequestResultActionWrapper(ResultActions ra, boolean printOut) throws Exception{
            this.ra = ra;
            this.ra = init();
            this.enable = printOut;
        }

        private ResultActions init() throws Exception{
            return ra.andExpect(status().isOk());
        }

        public RESTRequestResultActionWrapper andExpectSuccess(Boolean status) throws Exception{
            String bool = status.toString();
            String content = "{\"success\":" + bool + "}";
            ra = ra.andExpect(content().json(content));
            if (enable){
                ra = ra.andDo(print());
            }
            return this;
        }

        public RESTRequestResultActionWrapper andExpectError(String errormsg) throws Exception{
            String content = "{\"success\":false, \"msg\":\"" + errormsg  + "\"}";
            ra = ra.andExpect(content().json(content));
            if (enable){
                ra = ra.andDo(print());
            }
            return this;
        }

        public RESTRequestResultActionWrapper andExpectData(String jsonData) throws Exception{
            String content = "{\"success\":true, \"data\":" + jsonData + "}";
            ra = ra.andExpect(content().json(content));
            if (enable){
                ra = ra.andDo(print());
            }
            return this;
        }

    }

}
