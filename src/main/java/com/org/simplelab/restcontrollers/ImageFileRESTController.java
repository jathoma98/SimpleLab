package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.entities.sql.files.ImageFile;
import com.org.simplelab.database.services.ImageFileDB;
import com.org.simplelab.restcontrollers.rro.RRO;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(ImageFileRESTController.BASE_MAPPING)
@Getter
public class ImageFileRESTController extends BaseRESTController<ImageFile> {

    @Autowired
    private ImageFileDB db;

    public static final String BASE_MAPPING = "/image/rest";

    @PostMapping("/upload")
    public RRO uploadImage(MultipartHttpServletRequest request) throws Exception{
        MultipartFile file = request.getFile("image");
//        System.out.println("Name: " + file.getName());

        StringWriter writer = new StringWriter();
        InputStream content = file.getInputStream();
        IOUtils.copy(content, writer, StandardCharsets.UTF_8);
        RRO rro = new RRO();
        rro.setSuccess(true);
        rro.setMsg(" CONTENT: " + writer.toString());
        return rro;
    }

}
