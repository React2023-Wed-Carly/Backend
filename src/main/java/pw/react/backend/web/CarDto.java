package pw.react.backend.web;
import pw.react.backend.models.Car;
public record CarDto(Long id, String brand, String model, Long mileage, Integer year,
                     Long ownerId, Long dailyPrice, String photo, String description,
                     Double latitude, Double longitude, Integer seatNumber) {
    public static CarDto valueFrom(Car c){
        return new CarDto(c.getId(), c.getBrand(), c.getModel(), c.getMileage(), c.getYear(),
                c.getOwnerId(), c.getDailyPrice(), c.getPhoto(), c.getDescription(),
                c.getLatitude(), c.getLongitude(), c.getSeatNumber());
    }
    public static Car ConvertToCar(CarDto cd)
    {
        Car car=new Car();
        car.setId(cd.id);
        car.setBrand(cd.brand);
        car.setModel(cd.model);
        car.setMileage(cd.mileage);
        car.setOwnerId(cd.ownerId);
        car.setYear(cd.year);
        car.setDailyPrice(cd.dailyPrice);
        car.setPhoto(cd.photo);
        car.setDescription(cd.description);
        car.setLatitude(cd.latitude);
        car.setLongitude(cd.longitude);
        car.setSeatNumber(cd.seatNumber);
        return car;
    }
}
