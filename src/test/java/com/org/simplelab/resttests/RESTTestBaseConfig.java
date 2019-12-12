package com.org.simplelab.resttests;

import com.org.simplelab.SpringMockMVCTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public abstract class RESTTestBaseConfig extends SpringMockMVCTestConfig {

    @Autowired
    protected MockMvc mvc;

    abstract void loadRestRequest();

}
