package com.org.simplelab.database.services.restservice;

import com.org.simplelab.database.entities.sql.files.ImageFile;
import com.org.simplelab.database.repositories.sql.ImageFileRepository;
import com.org.simplelab.database.services.SQLService;
import com.org.simplelab.exception.EntityDBModificationException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Transactional
@Component
@Getter
public class ImageFileDB extends SQLService<ImageFile> {

    @Autowired
    private ImageFileRepository repository;

    public ImageFile insertFromMultipartFile(MultipartFile file) throws EntityDBModificationException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            ImageFile imageFile = new ImageFile();
            imageFile.setFileName(fileName);
            imageFile.setFileType(file.getContentType());
            imageFile.setData(file.getBytes());
            return super.insert(imageFile);
        } catch (IOException e) {
            throw new EntityDBModificationException(e.getMessage());
        }
    }

}
