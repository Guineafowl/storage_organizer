package olcia.products.controller;

import olcia.products.persistence.Photo;
import olcia.products.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping(path = "/api")
public class PhotoController {

    private final String uploadPath;
    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoController(@Value("${photo.upload.path}") String uploadPath, PhotoRepository photoRepository) {
        this.uploadPath = uploadPath;
        this.photoRepository = photoRepository;
    }

    @Transactional
    @PostMapping(path = "/photo")
    public long uploadPhoto(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File uploadFile = Paths.get(uploadPath, multipartFile.getOriginalFilename()).toFile();
        multipartFile.transferTo(uploadFile);
        return photoRepository.save(Photo.builder().path(multipartFile.getOriginalFilename()).build()).getId();
    }

}
