package ImageHoster.controller;

import ImageHoster.model.Image;
import ImageHoster.model.Tag;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import ImageHoster.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;


    @RequestMapping("images")
    public String getUserImages(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "images";
    }


    @RequestMapping("/images/{id}/{title}")
    public String showImage(@PathVariable("id") int id, @PathVariable("title") String title, Model model) {
        Image image = imageService.getImageByTitle(title,id);
        model.addAttribute("image", image);
        model.addAttribute("tags", image.getTags());
        model.addAttribute("comments",commentService.retrieveComments(image));
        return "images/image";
    }


    @RequestMapping("/images/upload")
    public String newImage() {
        return "images/upload";
    }


    @RequestMapping(value = "/images/upload", method = RequestMethod.POST)
    public String createImage(@RequestParam("file") MultipartFile file, @RequestParam("tags") String tags, Image newImage, HttpSession session) throws IOException {

        User user = (User) session.getAttribute("loggeduser");
        newImage.setUser(user);
        String uploadedImageData = convertUploadedFileToBase64(file);
        newImage.setImageFile(uploadedImageData);

        List<Tag> imageTags = findOrCreateTags(tags);
        newImage.setTags(imageTags);
        newImage.setDate(new Date());
        imageService.uploadImage(newImage);
        return "redirect:/images";
    }


    @RequestMapping(value = "/editImage")
    public String editImage(@RequestParam("imageId") Integer imageId, Model model,HttpSession session) {
        Image image = imageService.getImage(imageId);

        String errorPage = "", result = "";
        User user = (User) session.getAttribute("loggeduser");
        Integer imageUserID = imageService.getImage(imageId).getUser().getId();
        Integer loggedUserID = user.getId();
        if (imageUserID != loggedUserID){
            errorPage = imageId+"/"+image.getTitle();
            model.addAttribute("editError","Only the owner of the image can edit the image");
            return "redirect:/images/"+errorPage+"?"+"editError";
        }
        else {
            String tags = convertTagsToString(image.getTags());
            model.addAttribute("image", image);
            model.addAttribute("tags", tags);
        }

        return "images/edit";
    }


    @RequestMapping(value = "/editImage", method = RequestMethod.PUT)
    public String editImageSubmit(@RequestParam("file") MultipartFile file, @RequestParam("imageId") Integer imageId, @RequestParam("tags") String tags, Image updatedImage, HttpSession session) throws IOException {

        Image image = imageService.getImage(imageId);
        String updatedImageData = convertUploadedFileToBase64(file);
        List<Tag> imageTags = findOrCreateTags(tags);

        User user = (User) session.getAttribute("loggeduser");

        if (updatedImageData.isEmpty())
            updatedImage.setImageFile(image.getImageFile());
        else {
            updatedImage.setImageFile(updatedImageData);
        }

        updatedImage.setId(imageId);
        updatedImage.setUser(user);
        updatedImage.setTags(imageTags);
        updatedImage.setDate(new Date());

        imageService.updateImage(updatedImage);

        return "redirect:/images/"+imageId+"/"+updatedImage.getTitle();
    }


    @RequestMapping(value = "/deleteImage", method = RequestMethod.DELETE)
    public String deleteImageSubmit(@RequestParam(name = "imageId") Integer imageId, HttpSession session,Model model) {
        String result = "",errorPage = "";
        Image image = imageService.getImage(imageId);
        User user = (User) session.getAttribute("loggeduser");
        Integer imageUserID = imageService.getImage(imageId).getUser().getId();
        Integer loggedUserID = user.getId();

        if (imageUserID == loggedUserID){
            imageService.deleteImage(imageId);
            result = "redirect:/images";
        }
        else{
            errorPage = imageId+"/"+image.getTitle();
            model.addAttribute("deleteError","Only the owner of the image can delete the image");
            result = "redirect:images/"+errorPage+"?"+"deleteError";
        }

        return result;
    }


    private String convertUploadedFileToBase64(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }


    private List<Tag> findOrCreateTags(String tagNames) {
        StringTokenizer st = new StringTokenizer(tagNames, ",");
        List<Tag> tags = new ArrayList<Tag>();

        while (st.hasMoreTokens()) {
            String tagName = st.nextToken().trim();
            Tag tag = tagService.getTagByName(tagName);

            if (tag == null) {
                Tag newTag = new Tag(tagName);
                tag = tagService.createTag(newTag);
            }
            tags.add(tag);
        }
        return tags;
    }


    private String convertTagsToString(List<Tag> tags) {
        StringBuilder tagString = new StringBuilder();

        for (int i = 0; i <= tags.size() - 2; i++) {
            tagString.append(tags.get(i).getName()).append(",");
        }
        if(tags.size()<=0) return "";
        else {
            Tag lastTag = tags.get(tags.size() - 1);
            tagString.append(lastTag.getName());
        }
        return tagString.toString();
    }


}
