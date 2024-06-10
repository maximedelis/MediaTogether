package tp.mediatogether.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tp.mediatogether.repositories.UserRepository;
import tp.mediatogether.models.User;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        init();
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (checkExists(username)) {
            return userRepository.findByUsername(username);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }


    public boolean checkExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public void saveUser(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
    }

    public void init() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("12345678"));
        userRepository.save(user);
    }

}
