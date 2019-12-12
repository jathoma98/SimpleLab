package com.org.simplelab;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@Transactional
public abstract class SpringMockMVCTestConfig extends SpringTestConfig {

}
