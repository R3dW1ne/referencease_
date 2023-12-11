package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.exceptionhandling.PositionNotFoundException;
import com.ffhs.referencease.services.interfaces.IUserAccountService;
import com.ffhs.referencease.utils.PBKDF2Hash;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SessionScoped
@Named
@Getter
@Setter
public class AuthenticationBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject
  private Logger LOGGER = LoggerFactory.getLogger(AuthenticationBean.class);

  private final transient IUserAccountService userAccountService;

  private transient UserAccountDTO userAccountDTO;

  private transient HttpSession session = null;


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
    try {
      userAccountDTO = userAccountService.getUserByEmail(emailInput);
    } catch (PositionNotFoundException e) {
      LOGGER.error("User with email " + emailInput + " not found.");
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Keinen Benutzer unter diese Email-Adresse gefunden! \n Bitte registrieren Sie sich zuerst."));
      return "/resources/components/sites/login.xhtml";
    }

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
//            LOGGER.info("User " + userAccountDTO.getEmail() + " logged in.");
      // Setzen einer Erfolgsmeldung im Flash Scope
      FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Sie sind angemeldet.", null));

      // Navigate to landing page
      return "/resources/components/sites/secured/employeeList.xhtml?faces-redirect=true";
    }
    this.authenticated = false;
    setUserAccountDTO(null);
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
            "Login aufgrund eines falschen Passworts fehlgeschlagen!"));

    return "/resources/components/sites/login.xhtml?error=true";
  }


  public String logout() {
    userAccountDTO = null;
    this.authenticated = false;
    session.setAttribute("userId", null);
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext externalContext = facesContext.getExternalContext();
    externalContext.invalidateSession();
    // Setzen einer Erfolgsmeldung im Flash Scope
    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich abgemeldet",
            "Wir wünschen Ihnen einen sonnigen Tag!"));

    // Navigate back to main-page
    return "/resources/components/sites/login.xhtml?faces-redirect=true";
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