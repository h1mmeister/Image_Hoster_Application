package ImageHoster.repository;

import ImageHoster.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Repository
public class UserRepository {

    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory emf;

    public void registerUser(User newUser) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(newUser);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }


    public Integer checkPassword(User user){
        Integer passwordFlag = 0;

        Pattern pattern;
        Matcher matcher;

        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{3,}$";

        pattern = Pattern.compile(passwordPattern);
        matcher = pattern.matcher(user.getPassword());

        if(matcher.matches())
            passwordFlag = 1;

        return passwordFlag;
    }

    public User checkUser(String username, String password) {
        try {
            EntityManager em = emf.createEntityManager();
            TypedQuery<User> typedQuery = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
            typedQuery.setParameter("username", username);
            typedQuery.setParameter("password", password);

            return typedQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}