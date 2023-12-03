package com.ffhs.referencease.beans;

//import ch.ffhs.bude4u.utils.PBKDF2Hash;
import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.entities.UserAccount;
import com.ffhs.referencease.exceptionhandling.PositionNotFoundException;
import com.ffhs.referencease.services.UserAccountService;
import com.ffhs.referencease.services.interfaces.IUserAccountService;
import com.ffhs.referencease.utils.PBKDF2Hash;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@SessionScoped
@Named
@Getter
@Setter
public class AuthenticationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final transient IUserAccountService userAccountService;

    private transient UserAccountDTO userAccountDTO;

    private transient HttpSession session = null;

    @Setter(AccessLevel.PRIVATE)
    private boolean authenticated = false;
    private String email = null;
    private String password = null;
    private String firstName = null;
    private String lastName = null;
    private String updatePassword = null;



    @Inject
    public AuthenticationBean(IUserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    public HttpSession getSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        session = request.getSession();

        return session;
    }

    public String login() throws PositionNotFoundException {
        String emailInput = getEmail();
        String hashedPasswordInput = PBKDF2Hash.createHash(getPassword());
        session = getSession();

//        Optional<UserAccount> userAccountAccess = userAccountService.getUserByEmail(emailInput);
//
//        if (userAccountAccess.isEmpty()) return "/resources/components/sites/login.xhtml?error=true";

        UserAccountDTO userAccountDTO = userAccountService.getUserByEmail(emailInput);

//        String hashedSavedPw = userAccountAccess.get().getPassword();
//        String hashedSavedPw = userAccountDTO.getPassword();
//        boolean pwMatch = userAccountService.passwordMatches(emailInput, hashedPasswordInput);


        if (userAccountService.passwordMatches(emailInput, hashedPasswordInput)) {
            session.setAttribute("authenticated", true);
            session.setAttribute("email", userAccountDTO.getEmail());
            session.setAttribute("userId", userAccountDTO.getUserId());
//            session.setAttribute("selectedTheme", userAccount.get().getSelectedTheme());
            this.authenticated = true;
            setUserAccountDTO(userAccountDTO);
            // Navigate to landing page
            return "/resources/components/sites/home.xhtml";
        }
        this.authenticated = false;
        setUserAccountDTO(null);

        return "/resources/components/sites/login.xhtml?error=true";
    }


    public String logout() {
        userAccountDTO = null;
        this.authenticated = false;
        session.setAttribute("userId", null);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.invalidateSession();
        // Navigate back to main-page
        return "/resources/components/sites/login.xhtml";
    }


    public boolean isAuthenticated() {
        try {
            this.authenticated = (boolean) getSession().getAttribute("authenticated");
        } catch (Exception e) {
            this.authenticated = false;
        }

        return authenticated;
    }

//    public String updateProfile() {
//        try {
//
//            Optional<UserAccount> updatedUser = userService.getUserById((UUID) session.getAttribute("userId"));
//            if (updatedUser.isEmpty()) return "/resources/components/sites/login.xhtml?error=true";
//
//            if (getUpdatePassword() != null && !getUpdatePassword().isEmpty()) {
//                updatedUser.get().setPassword(getUpdatePassword());
//            }
//
////            String theme = updatedUser.get().getSelectedTheme();
////            session.setAttribute("selectedTheme", theme);
//
//            // Check if username has been updated
//            if (updatedUser.get().getEmail().equals(session.getAttribute("email"))) {
//                userService.updateUser(updatedUser.get());
//            } else {
//                // Check if username has already been set.
//                Optional<UserAccount> hasUser = userService.getUserByEmail(updatedUser.get().getEmail());
//                // Update failed -> Navigate to...
//                if (hasUser.isPresent()) return "/resources/components/sites/login.xhtml?error=true";
//                userService.updateUser(updatedUser.get());
//            }
//
//            return "/index.xhtml";
//        } catch (Exception ex) {
//            return "ERROR: " + ex.getMessage();
//        }
//    }
}