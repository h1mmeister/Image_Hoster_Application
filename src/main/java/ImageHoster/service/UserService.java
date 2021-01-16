package ImageHoster.service;

import ImageHoster.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public void registerUser(User newUser) {
        return;
    }

    //Since we do not have any user in the database, therefore the user with username 'upgrad' and password 'password' is hard-coded
    //This method returns true if the username is 'upgrad' and password is 'password'
    public boolean login(User user) {
        if (user.getUsername().equals("upgrad") && user.getPassword().equals("password"))
            return true;
        else
            return false;
    }

}
