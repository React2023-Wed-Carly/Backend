package pw.react.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import pw.react.backend.dao.UserRepository;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.User;

import java.util.*;

public class UserMainService implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserMainService.class);

    protected final UserRepository userRepository;
    protected final PasswordEncoder passwordEncoder;

    public UserMainService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> FindByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Collection<User> GetAll() {
        return userRepository.findAll();
    }

    @Override
    public User validateAndSave(User user) {
        if (isValidUser(user)) {
            log.info("User is valid");
            Optional<User> dbUser = userRepository.findByUsername(user.getUsername());
            if (dbUser.isPresent()) {
                log.info("User already exists. Updating it.");
                //user.setId(dbUser.get().getId());
              //  user.setPassword(passwordEncoder.encode(user.getPassword()));
                return null;
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user = userRepository.save(user);
            log.info("User was saved.");
        }
        return user;
    }
    @Override
    public User saveEdited(User user)
    {
        userRepository.save(user);
        return user;
    }
    private boolean isValidUser(User user) {
        if (user != null) {
            if (isValid(user.getUsername())) {
                log.error("Empty username.");
                throw new UserValidationException("Empty username.");
            }
            if (isValid(user.getPassword())) {
                log.error("Empty user password.");
                throw new UserValidationException("Empty user password.");
            }
            if (isValid(user.getEmail())) {
                log.error("UEmpty email.");
                throw new UserValidationException("Empty email.");
            }
            return true;
        }
        log.error("User is null.");
        throw new UserValidationException("User is null.");
    }

    private boolean isValid(String value) {
        return value == null || value.isBlank();
    }

    @Override
    public User updatePassword(User user, String password) {
        if (isValidUser(user)) {
            if (passwordEncoder != null) {
                log.debug("Encoding password.");
                user.setPassword(passwordEncoder.encode(password));
            } else {
                log.debug("Password in plain text.");
                user.setPassword(password);
            }
            user = userRepository.save(user);
        }
        return user;
    }

    @Override
    public Collection<User> GetAllNonAdmin(int page,long id,String username) {
        Pageable pageable= PageRequest.of(page,20);
        return userRepository.findByIdAndIsAdminOrUsernameLikeAndIsAdmin
                (Long.valueOf(id),Boolean.FALSE,username,Boolean.FALSE,pageable);
    }

    @Override
    public Collection<User> batchSave(Collection<User> users) {
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                isValidUser(user);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return userRepository.saveAll(users);
        } else {
            log.warn("User collection is empty or null.");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
