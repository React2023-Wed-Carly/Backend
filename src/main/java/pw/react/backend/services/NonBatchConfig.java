package pw.react.backend.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import pw.react.backend.dao.*;

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

    @Bean
    public BookingService bookingService(BookingRepository bookingRepository)
    {
        return new BookingMainService(bookingRepository);
    }
    @Bean
    public PaymentService paymentService(PaymentRepository paymentRepository)
    {
        return new PaymentMainService(paymentRepository);
    }
    @Bean
    public FavoriteCarService favoriteCarService(FavoriteCarsRepository favoriteCarsRepository)
    {
        return new FavoriteCarMainService(favoriteCarsRepository);
    }
    @Bean
    public ImageService imageService(CarImageRepository carImageRepository)
    {
        return new CarImageService(carImageRepository);
    }
}
