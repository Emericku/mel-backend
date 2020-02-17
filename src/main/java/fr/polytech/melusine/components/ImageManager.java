package fr.polytech.melusine.components;

import fr.polytech.melusine.configurations.PathProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
@EnableConfigurationProperties({PathProperties.class})
public class ImageManager {

    public static final String IMAGES = "/images/";
    private final PathProperties pathProperties;

    public ImageManager(PathProperties pathProperties) {
        this.pathProperties = pathProperties;
    }

    /**
     * Upload an image.
     *
     * @param image the image file
     * @return a string
     */
    public String uploadImage(MultipartFile image) {
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        Path path = Paths.get(pathProperties.getBase() + fileName);
        try {
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(IMAGES)
                .path(fileName)
                .toUriString();
    }

}
