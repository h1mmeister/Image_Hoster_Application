package ImageHoster.service;

import ImageHoster.model.Image;
import ImageHoster.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;


    public List<Image> getAllImages() {
        return imageRepository.getAllImages();
    }


    public void uploadImage(Image image) {
        imageRepository.uploadImage(image);
    }


    public Image getImageByTitle(String title, int id) {
        return imageRepository.getImageByTitle(title, id);
    }


    public Image getImage(Integer imageId) {
        return imageRepository.getImage(imageId);
    }


    public void updateImage(Image updatedImage) {
        imageRepository.updateImage(updatedImage);
    }


    public void deleteImage(Integer imageId) {
        imageRepository.deleteImage(imageId);
    }

}
