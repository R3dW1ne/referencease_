package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.exceptionhandling.PositionNotFoundException;
import com.ffhs.referencease.services.interfaces.IAuthenticationService;
import com.ffhs.referencease.services.interfaces.IUserAccountService;
import com.ffhs.referencease.utils.PBKDF2Hash;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;


@Named
@Getter
@Setter
@SessionScoped
public class AuthenticationBean implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final transient IUserAccountService userAccountService;

  private final transient IAuthenticationService authenticationService;

  private UserAccountDTO userAccountDTO;

  private transient HttpSession session = null;


  private boolean authenticated = false;
  @Email(message = "Bitte geben Sie eine gültige Email-Adresse ein. (@Email Validation)")
  private String email = null;
  private String password = null;
  private String firstName = null;
  private String lastName = null;
  private String updatePassword = null;


  @Inject
  public AuthenticationBean(IUserAccountService userAccountService,
      IAuthenticationService authenticationService) {
    this.userAccountService = userAccountService;
    this.authenticationService = authenticationService;
  }

  @PostConstruct
  public void init() {
    this.userAccountDTO = new UserAccountDTO();
  }

  public HttpSession getSession() {
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
    session = request.getSession();

    return session;
  }


  public String loginWithValidRegistrationUserDTOValidator(UserAccountDTO userAccountDTO)
      throws PositionNotFoundException {
    return login();
  }

  public String login() throws PositionNotFoundException {
    String emailInput = getEmail();
    String hashedPasswordInput = PBKDF2Hash.createHash(getPassword());
    session = getSession();

    try {
      userAccountDTO = userAccountService.getUserByEmail(emailInput);
    } catch (PositionNotFoundException e) {
      FacesContext.getCurrentInstance().addMessage(null,
                                                   new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                    "Fehler",
                                                                    "Keinen Benutzer unter diese Email-Adresse gefunden! \n Bitte registrieren Sie sich zuerst."));
      return "/resources/components/sites/login.xhtml";
    }

    UserAccountDTO storedUserAccountDTO = userAccountService.getUserByEmail(emailInput);

    if (userAccountService.passwordMatches(emailInput, hashedPasswordInput)) {
      session.setAttribute("authenticated", true);
      session.setAttribute("email", storedUserAccountDTO.getEmail());
      session.setAttribute("userId", storedUserAccountDTO.getUserId());
      this.authenticated = true;
      setUserAccountDTO(storedUserAccountDTO);

      // Setzen einer Erfolgsmeldung im Flash Scope
      FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
      FacesContext.getCurrentInstance().addMessage(null,
                                                   new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                                    "Sie sind angemeldet.", null));

      // Navigate to landing page
      return "/resources/components/sites/secured/home.xhtml?faces-redirect=true";
    }
    this.authenticated = false;
    setUserAccountDTO(null);
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                        "Fehler",
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
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                                        "Erfolgreich abgemeldet",
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

}