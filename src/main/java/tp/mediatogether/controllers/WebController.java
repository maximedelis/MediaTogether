package tp.mediatogether.controllers;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tp.mediatogether.models.RegistrationForm;
import tp.mediatogether.models.User;
import tp.mediatogether.services.UserService;


@Controller
@RequestMapping("")
public class WebController {

    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("authenticated", true);
            return "index";
        }
        model.addAttribute("authenticated", false);
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("authenticated", true);
            return "index";
        }
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid RegistrationForm registrationForm, BindingResult bindingResult, Model model) {

        if (userService.checkExists(registrationForm.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "An account already exists for this username.");
            return "register";
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.saveUser(new User(registrationForm.getUsername(), registrationForm.getPassword()));
        model.addAttribute("registered", true);
        return "register";
    }


}
