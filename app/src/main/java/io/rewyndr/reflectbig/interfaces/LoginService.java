package io.rewyndr.reflectbig.interfaces;

/**
 * This interface provides methods to user login and signup
 * Created by Satish on 9/6/2014.
 */
public interface LoginService {

    /**
     * This method logs in the user to the application using email and password
     * @param email
     * @param password
     * @throws Exception
     */
    void logIn(String email, String password) throws Exception;

    /**
     * This method signs up a new user to the application
     * @param name
     * @param email
     * @param password
     * @throws Exception
     */
    void signUp(String name, String email, String password) throws Exception;

    /**
     * This method logs out the current user
     * @throws Exception
     */
    void logOut() throws Exception;
}
