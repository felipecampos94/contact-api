package br.com.contact.api.service;

import br.com.contact.api.entity.User;
import br.com.contact.api.entity.model.request.UserRequest;
import br.com.contact.api.entity.model.response.UserResponse;
import br.com.contact.api.mapper.ModelMapperConfig;
import br.com.contact.api.repository.UserRepository;
import br.com.contact.api.service.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public List<UserResponse> findAll() {
        logger.info("Finding all users!");
        return this.userRepository.findAll()
                .stream()
                .map(user -> ModelMapperConfig.parseObject(user, UserResponse.class))
                .toList();
    }

    public Page<UserResponse> findAllPageable(PageRequest pageable) {
        logger.info("Finding all companies pageable!");
        var userPage = this.userRepository.findAll(pageable);
        return userPage.map(user -> ModelMapperConfig.parseObject(userPage, UserResponse.class));
    }

    public UserResponse findById(Long id) {
        logger.info("Finding one user!");
        var userResponse = this.userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Object Not Found! Id: %s Type %s".formatted(id, User.class.getSimpleName())));

        return ModelMapperConfig.parseObject(userResponse, UserResponse.class);
    }

    public User findByUsername(String username) {
        logger.info("Finding one user by name: %s Type %s!".formatted(username, User.class.getSimpleName()));
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("Object Not Found! Username: %s Type %s"
                        .formatted(username, User.class.getSimpleName())));
    }

    public UserResponse create(UserRequest userRequest) {
        logger.info("Creating one user!");
        userRequest.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
        var userResponse = this.userRepository.save(ModelMapperConfig.parseObject(userRequest, User.class));
        return ModelMapperConfig.parseObject(userResponse, UserResponse.class);
    }

    public UserResponse update(Long id, UserRequest userRequest) {
        logger.info("Updating one user!");
        var userResponse = this.findById(id);
        this.updateData(userResponse, ModelMapperConfig.parseObject(userRequest, UserResponse.class));
        var user = this.userRepository.save(ModelMapperConfig.parseObject(userResponse, User.class));
        return ModelMapperConfig.parseObject(user, UserResponse.class);
    }

    private void updateData(UserResponse response, UserResponse request) {
        response.setFullname(request.getFullname());
        response.setUsername(request.getUsername());
        response.setAccountNonLocked(request.getAccountNonLocked());
        response.setAccountNonExpired(request.getAccountNonExpired());
        response.setCredentialsNonExpired(request.getCredentialsNonExpired());
        response.setEnabled(request.getEnabled());
        response.setPermissions(request.getPermissions());
        response.setAddresses(request.getAddresses());
        response.setContactInfo(request.getContactInfo());
    }

    public void delete(Long id) {
        logger.info("Deleting one user!");
        var userResponse = this.findById(id);
        this.userRepository.deleteById(userResponse.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding one user by name: %s".formatted(username));
        return this.findByUsername(username);
    }
}
