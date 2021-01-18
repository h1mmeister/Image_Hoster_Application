package ImageHoster.repository;

import ImageHoster.model.Comments;
import ImageHoster.model.Image;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentRepository {

    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory entityManager;

    public void addComments(Comments comments){
        EntityManager entity = entityManager.createEntityManager();
        EntityTransaction transaction = entity.getTransaction();

        try{
            transaction.begin();
            entity.persist(comments);
            transaction.commit();
        }catch(Exception exception){
            transaction.rollback();
        }
    }

    public List<Comments> retrieveComments(Image image){
        List<Comments> comments = new ArrayList<>();
        EntityManager em = entityManager.createEntityManager();
        TypedQuery<Comments> typedQuery = null;
        try{
            typedQuery = (TypedQuery<Comments>) em.createQuery("SELECT c from Comments c where c.image =:image", Comments.class);
            typedQuery.setParameter("image",image);
            comments = typedQuery.getResultList();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return comments;
    }
}
