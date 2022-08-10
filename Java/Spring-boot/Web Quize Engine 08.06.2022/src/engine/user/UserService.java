package engine.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    private BCryptPasswordEncoder encoder;

    private UserRepository repository;

    @Autowired
    UserService(@Lazy BCryptPasswordEncoder encoder, UserRepository repository) {
        this.encoder = encoder;
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found!"));
    }

    boolean save(User user) {
        if (!repository.existsByEmail(user.getEmail())) {
            user.setPassword(encoder.encode(user.getPassword()));
            repository.save(user);
            return true;
        } else {
            return false;
        }
    }
}
