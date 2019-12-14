package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.entities.sql.files.ImageFile;
import com.org.simplelab.database.services.ImageFileDB;
import com.org.simplelab.database.validators.ImageFileValidator;
import com.org.simplelab.restcontrollers.rro.RRO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ImageFileRESTController.BASE_MAPPING)
@Getter
public class ImageFileRESTController extends BaseRESTController<ImageFile> {

    @Autowired
    private ImageFileDB db;

    public static final String BASE_MAPPING = "/image/rest";
    public static final String IMAGE_ID_MAPPING = "{image_id}";

    /**
     * FileDTO: {
     *     fileName: String -- name of file
     *     fileType: String -- extension of file
     *     data: byte[] -- raw file data
     * }
     */
    @PostMapping("/upload")
    public RRO uploadImage(@RequestBody ImageFileValidator fileDTO) throws Exception{
        return super.addEntity(fileDTO);
    }

    @GetMapping(IMAGE_ID_MAPPING)
    public RRO getImage(@PathVariable("image_id") long image_id){
        return super.getEntityById(image_id);
    }

}
