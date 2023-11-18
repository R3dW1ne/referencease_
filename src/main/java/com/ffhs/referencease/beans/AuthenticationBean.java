package com.ffhs.referencease.beans;

import ch.ffhs.bude4u.utils.PBKDF2Hash;
import com.ffhs.referencease.entities.UserAccount;
import com.ffhs.referencease.entityservices.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@SessionScoped
@Named
@Getter
@Setter
public class AuthenticationBean implements Serializable {


    @Inject
    private UserService userService;

    private HttpSession session = null;

    @Setter(AccessLevel.PRIVATE)
    private boolean authenticated = false;
    private String email = null;
    private String password = null;
    private String firstName = null;
    private String lastName = null;
    private String updatePassword = null;

    UserAccount userAccount;

//    public String register() {
//        String firstName = getFirstName();
//        String lastName = getLastName();
//        String email = getEmail();
//        String password = getPassword();
//
//        // Check if user with specific username is not in db.
//        Optional<UserAccount> userAccount = userService.getUserByEmail(email);
//        if (userAccount.isPresent()) {
//            // This means user is already registered, navigate back to login page.
//            return "/views/login.xhtml";
//        }
//        // Create new user
//        try {
//            UserAccount newUser = new UserAccount(email, password);
//            userService.createUser(newUser);
//            // New user created, navigate back to login page.
//            return "/views/login.xhtml";
//        } catch (Exception ex) {
//            return "ERROR: " + ex.getMessage();
//        }
//    }

    public HttpSession getSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        session = request.getSession();

        return session;
    }

    public String login() {
        String emailInput = getEmail();
        String passwordInput = getPassword();
        session = getSession();

        Optional<UserAccount> userAccount = userService.getUserByEmail(emailInput);

        if (userAccount.isEmpty()) return "/views/loginFailed.xhtml";

        String hashedPw = userAccount.get().getPassword();
        boolean pwMatch = userService.checkPassword(passwordInput, hashedPw);
//        boolean pwMatch = PBKDF2Hash.CheckPassword(hashedPw, passwordInput);

        if (pwMatch) {
            session.setAttribute("authenticated", true);
            session.setAttribute("email", emailInput);
            session.setAttribute("userId", userAccount.get().getUserId());
//            session.setAttribute("selectedTheme", userAccount.get().getSelectedTheme());
            this.authenticated = true;
            setUserAccount(userAccount.get());
            // Navigate to landing page
            return "/index.xhtml";
        }
        this.authenticated = false;
        setUserAccount(null);

        return "/views/loginFailed.xhtml";
    }


    public String logout() {
        userAccount = null;
        this.authenticated = false;
        session.setAttribute("userId", null);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.invalidateSession();
        // Navigate back to main-page
        return "/index.xhtml?faces-redirect=true";
    }


    public boolean isAuthenticated() {
        try {
            this.authenticated = (boolean) getSession().getAttribute("authenticated");
        } catch (Exception e) {
            this.authenticated = false;
        }

        return authenticated;
    }

    public String updateProfile() {
        try {

            Optional<UserAccount> updatedUser = userService.getUserById((Long) session.getAttribute("userId"));
            if (updatedUser.isEmpty()) return "/views/loginFailed.xhtml";

            if (getUpdatePassword() != null && !getUpdatePassword().isEmpty()) {
                updatedUser.get().setPassword(getUpdatePassword());
            }

//            String theme = updatedUser.get().getSelectedTheme();
//            session.setAttribute("selectedTheme", theme);

            // Check if username has been updated
            if (updatedUser.get().getEmail().equals(session.getAttribute("email"))) {
                userService.updateUser(updatedUser.get());
            } else {
                // Check if username has already been set.
                Optional<UserAccount> hasUser = userService.getUserByEmail(updatedUser.get().getEmail());
                // Update failed -> Navigate to...
                if (hasUser.isPresent()) return "/views/loginFailed.xhtml";
                userService.updateUser(updatedUser.get());
            }

            return "/index.xhtml";
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }
}