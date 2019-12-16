package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.files.ImageFile;
import com.org.simplelab.database.services.ImageFileDB;
import com.org.simplelab.exception.EntityDBModificationException;
import com.org.simplelab.restcontrollers.rro.RRO;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
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
    public static final String IMG_TO_EQUIPMENT_MAPPING = "/{equipment_id}";

    private static final String FILENAME = "image";

    //test method, dont actually use
    @PostMapping("/upload")
    public RRO uploadImage(MultipartHttpServletRequest request) throws Exception{
        
        MultipartFile file = request.getFile(FILENAME);
//        System.out.println("Name: " + file.getName());

        StringWriter writer = new StringWriter();
        InputStream content = file.getInputStream();
        IOUtils.copy(content, writer, StandardCharsets.UTF_8);
        RRO rro = new RRO();
        rro.setSuccess(true);
        rro.setMsg(" CONTENT: " + writer.toString());
        return rro;
    }

    @PostMapping(IMG_TO_EQUIPMENT_MAPPING)
    public RRO addImgToEquipment(@PathVariable("equipment_id") long equipment_id,
                                 MultipartHttpServletRequest request){
        RRO rro = new RRO();
        MultipartFile file = request.getFile(FILENAME);
        if (file == null){
            return RRO.sendErrorMessage("No file found with name: '" + FILENAME + "' . File must be sent with this filename.");
        }
        Equipment eq = equipmentDB.findById(equipment_id);
        if (eq == null){
            return RRO.sendErrorMessage("Equipment not found.");
        }
        try{
            ImageFile img = imageDB.insertFromMultipartFile(file);
            eq.setImg(img);
            equipmentDB.update(eq);
        } catch (EntityDBModificationException e){
            return RRO.sendErrorMessage(e.getMessage());
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        return rro;
    }

    @GetMapping(IMG_TO_EQUIPMENT_MAPPING)
    public void showImage(@PathVariable("equipment_id") long equipment_id, HttpServletResponse response) throws Exception{
        Equipment e = equipmentDB.findById(equipment_id);
        if (e == null){
            //TODO: return something if no img.
        }
        ImageFile img = e.getImg();
        response.setContentType(img.getFileType());
        response.getOutputStream().write(img.getData());
        response.getOutputStream().close();
    }

}
