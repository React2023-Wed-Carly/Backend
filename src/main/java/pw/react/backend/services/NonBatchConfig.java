package pw.react.backend.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import pw.react.backend.dao.CarRepository;
import pw.react.backend.dao.UserRepository;

@Profile("!batch")
public class NonBatchConfig {


    @Bean
    public CarService carService(CarRepository carRepository)
    {
        return new CarMainService(carRepository);
    }
    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserMainService(userRepository, passwordEncoder);
    }

}
