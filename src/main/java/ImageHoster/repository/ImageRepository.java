package ImageHoster.repository;

import ImageHoster.model.Comments;
import ImageHoster.model.Image;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;


@Repository
public class ImageRepository {


    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory emf;

    public Image uploadImage(Image newImage) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(newImage);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return newImage;
    }

    public List<Image> getAllImages() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> query = em.createQuery("SELECT i from Image i", Image.class);
        List<Image> resultList = query.getResultList();

        return resultList;
    }

    public Image getImageByTitle(String title,int id) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> typedQuery = null;
        try {
            //typedQuery = (TypedQuery<Image>) em.createQuery("SELECT i from Image i where i.title =:title , Image.class)setParameter("title",title);
            typedQuery = (TypedQuery<Image>) em.createQuery("SELECT i from Image i where i.title =:title and i.id =:id", Image.class);

            typedQuery.setParameter("title",title);
            typedQuery.setParameter("id",id);

            return typedQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public Image getImage(Integer imageId) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Image> typedQuery = null;
        Image image = null;
        try {
            typedQuery = em.createQuery("SELECT i from Image i where i.id =:imageId", Image.class).setParameter("imageId", imageId);
            image = typedQuery.getSingleResult();

        }catch(Exception ex){
            System.out.println("Exception: in getImage() "+ex.toString());
        }finally{
            typedQuery = null;
            em.close();
        }
        return image;
    }

    public void updateImage(Image updatedImage) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.merge(updatedImage);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }


    public void deleteImage(Integer imageId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Image image = em.find(Image.class, imageId);
            em.remove(image);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

}
