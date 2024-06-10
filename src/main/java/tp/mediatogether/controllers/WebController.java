package tp.mediatogether.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tp.mediatogether.models.FileDB;
import tp.mediatogether.models.FileNoData;
import tp.mediatogether.models.RegistrationForm;
import tp.mediatogether.models.User;
import tp.mediatogether.services.StorageService;
import tp.mediatogether.services.UserService;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("")
public class WebController {

    private final UserService userService;
    private final StorageService storageService;


    public WebController(UserService userService, StorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
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
            return "redirect:/";
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

    @GetMapping("/upload")
    public String upload(Model model) {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(MultipartFile file, RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            storageService.store(file, username);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Failed to upload " + file.getOriginalFilename() + "! " + e.getMessage());
            return "redirect:/upload";
        }
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/upload";
    }

    @GetMapping("/profile")
    public String profile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) userService.loadUserByUsername(authentication.getName());
        model.addAttribute("user", user);

        List<FileNoData> files = storageService.getAllFilesByUploader(user.getUsername());
        model.addAttribute("songs", files);

        return "profile";
    }

    @GetMapping("/room")
    public String room(Model model) {
        List<FileNoData> files = storageService.getAllFiles();
        model.addAttribute("songs", files);
        return "room";
    }

    @PostMapping("/room")
    public String roomPost(String room_name, HttpSession session) {
        if (room_name == null || room_name.isEmpty()) {
            session.removeAttribute("room_name");
            return "redirect:/room";
        }
        session.setAttribute("room_name", room_name);
        return "redirect:/room";
    }

    @GetMapping("/play/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        if (storageService.getFile(id) == null) {
            return ResponseEntity.notFound().build();
        }
        FileDB file = storageService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

    @Transactional
    @PostMapping("/delete/{id}")
    public String deleteFile(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        storageService.deleteFile(id);
        redirectAttributes.addFlashAttribute("message", "File deleted successfully!");
        return "redirect:/profile";
    }

}
