package com.ffhs.referencease.beans;

import com.ffhs.referencease.dto.UserAccountDTO;
import com.ffhs.referencease.exceptionhandling.PositionNotFoundException;
import com.ffhs.referencease.services.interfaces.IUserAccountService;
import com.ffhs.referencease.utils.FrontendMessages;
import com.ffhs.referencease.utils.PWHash;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Managed Bean Klasse zur Authentifizierung und Verwaltung von Benutzersitzungen. Diese Klasse
 * bietet Funktionen für das Login und Logout von Benutzern sowie zur Überprüfung des
 * Authentifizierungsstatus. Sie nutzt `IUserAccountService` für die Geschäftslogik im Zusammenhang
 * mit Benutzerkonten und interagiert mit dem JSF-Frontend, um Authentifizierungsprozesse zu
 * verwalten.
 */
@Named
@Getter
@Setter
@SessionScoped
public class AuthenticationBean implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LogManager.getLogger(AuthenticationBean.class);

  private final transient IUserAccountService userAccountService;
  private UserAccountDTO userAccountDTO;
  private transient HttpSession session = null;
  private boolean authenticated = false;

  @Email(message = "Bitte geben Sie eine gültige Email-Adresse ein. (@Email Validation)")
  private String email = null;
  private String password = null;
  private String firstName = null;
  private String lastName = null;
  private String updatePassword = null;

  /**
   * Konstruktor, der eine Instanz von `IUserAccountService` injiziert.
   *
   * @param userAccountService Der Service für Benutzerkonten.
   */
  @Inject
  public AuthenticationBean(IUserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }

  /**
   * Initialisiert die Bean nach der Konstruktion.
   */
  @PostConstruct
  public void init() {
    this.userAccountDTO = new UserAccountDTO();
  }

  /**
   * Ermittelt die aktuelle Benutzersitzung.
   *
   * @return Die aktuelle HttpSession.
   */
  public HttpSession getSession() {
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
    session = request.getSession();

    return session;
  }

  /**
   * Führt den Login-Prozess mit dem gegebenen UserAccountDTO durch.
   *
   * @param userAccountDTO Das DTO des Benutzers.
   * @return Ein String, der den Navigationspfad nach dem Login bestimmt.
   * @throws PositionNotFoundException Wird ausgelöst, wenn keine Position für den Benutzer gefunden wird.
   */
  public String loginWithValidRegistrationUserDTOValidator(UserAccountDTO userAccountDTO)
      throws PositionNotFoundException {
    return login();
  }

  /**
   * Führt den Login-Prozess durch.
   *
   * @return Ein String, der den Navigationspfad nach dem Login bestimmt.
   * @throws PositionNotFoundException Wird ausgelöst, wenn keine Position für den Benutzer gefunden wird.
   */
  public String login() throws PositionNotFoundException {
    String emailInput = getEmail();
    String hashedPasswordInput = PWHash.createHash(getPassword());
    session = getSession();
    String message = "";

    try {
      userAccountDTO = userAccountService.getUserByEmail(emailInput);
    } catch (PositionNotFoundException e) {
      message = "Benutzer [" + emailInput + "] nicht gefunden.";
      FrontendMessages.sendErrorMessageToFrontend(null, "Fehler", message);
      LOGGER.error(message);
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
      message = "Benutzer [" + emailInput + "] erfolgreich angemeldet.";
      LOGGER.info(message);
      FrontendMessages.sendInfoMessageToFrontend(null, "Info", message);
      // Navigate to landing page
      return "/resources/components/sites/secured/home.xhtml?faces-redirect=true";
    }
    this.authenticated = false;
    setUserAccountDTO(null);
    message = "Benutzer [" + emailInput + "]konnte nicht angemeldet werden.";
    LOGGER.warn(message);
    FrontendMessages.sendErrorMessageToFrontend(null, "Fehler", message);

    return "/resources/components/sites/login.xhtml?error=true";
  }

  /**
   * Führt den Logout-Prozess durch.
   *
   * @return Ein String, der den Navigationspfad nach dem Logout bestimmt.
   */
  public String logout() {
    String message;
    userAccountDTO = null;
    this.authenticated = false;
    session.setAttribute("userId", null);
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext externalContext = facesContext.getExternalContext();
    externalContext.invalidateSession();
    // Setzen einer Erfolgsmeldung im Flash Scope
    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    message = "Benutzer erfolgreich abgemeldet.";
    FrontendMessages.sendInfoMessageToFrontend(null, "Info", message);

    // Navigate back to main-page
    return "/resources/components/sites/login.xhtml?faces-redirect=true";
  }

  /**
   * Überprüft, ob der aktuelle Benutzer authentifiziert ist.
   *
   * @return true, wenn der Benutzer authentifiziert ist, sonst false.
   */
  public boolean isAuthenticated() {
    try {
      this.authenticated = (boolean) getSession().getAttribute("authenticated");
    } catch (Exception e) {
      this.authenticated = false;
    }
    return authenticated;
  }
}