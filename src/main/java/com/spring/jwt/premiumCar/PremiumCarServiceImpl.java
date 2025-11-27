package com.spring.jwt.premiumCar;

import com.spring.jwt.dto.FilterDto;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.premiumCar.PremiumCarPendingBooking.PremiumCarPendingBooking;
import com.spring.jwt.premiumCar.PremiumCarPendingBooking.PremiumCarPendingBookingRepository;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.PhotoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PremiumCarServiceImpl implements PremiumCarService {

    @Autowired
    private PremiumCarRepository premiumCarRepository;

    @Autowired
    private PremiumCarPendingBookingRepository bookingRepository;

    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private PhotoRepo photoRepo;


    private PremiumCarDto mapToDto(PremiumCar car) {
        PremiumCarDto dto = new PremiumCarDto();
        dto.setPremiumCarId(car.getPremiumCarId());
        dto.setAirbag(car.getAirbag());
        dto.setABS(car.getABS());
        dto.setButtonStart(car.getButtonStart());
        dto.setSunroof(car.getSunroof());
        dto.setChildSafetyLocks(car.getChildSafetyLocks());
        dto.setAcFeature(car.getAcFeature());
        dto.setMusicFeature(car.getMusicFeature());
        dto.setArea(car.getArea());
        dto.setVariant(car.getVariant());
        dto.setBrand(car.getBrand());
        dto.setCarInsurance(car.getCarInsurance());
        dto.setCarInsuranceDate(car.getCarInsuranceDate());
        dto.setCarInsuranceType(car.getCarInsuranceType());
        dto.setPendingApproval(car.isPendingApproval());
        dto.setCity(car.getCity());
        dto.setColor(car.getColor());
        dto.setDescription(car.getDescription());
        dto.setFuelType(car.getFuelType());
        dto.setKmDriven(car.getKmDriven());
        dto.setModel(car.getModel());
        dto.setOwnerSerial(car.getOwnerSerial());
        dto.setPowerWindowFeature(car.getPowerWindowFeature());
        dto.setPrice(car.getPrice());
        dto.setRearParkingCameraFeature(car.getRearParkingCameraFeature());
        dto.setRegistration(car.getRegistration());
        dto.setTitle(car.getTitle());
        dto.setTransmission(car.getTransmission());
        dto.setYear(car.getYear());
        dto.setDate(car.getDate());
        dto.setDealerId(car.getDealerId());
        dto.setCarPhotoId(car.getCarPhotoId());
        dto.setMainCarId(car.getMainCarId());


        // ✅ Map pending booking IDs
        if (car.getPendingBookings() != null && !car.getPendingBookings().isEmpty()) {
            Set<Long> bookingIds = car.getPendingBookings().stream()
                    .map(PremiumCarPendingBooking::getPremiumCarPendingBookingId) // replace `getId` with actual ID method
                    .collect(Collectors.toSet());
            dto.setPremiumCarPendingBookingId(bookingIds);
        }

        return dto;
    }


    private PremiumCar mapToEntity(PremiumCarDto dto) {
        PremiumCar car = new PremiumCar();
        car.setPremiumCarId(dto.getPremiumCarId());
        car.setAirbag(dto.getAirbag());
        car.setABS(dto.getABS());
        car.setButtonStart(dto.getButtonStart());
        car.setSunroof(dto.getSunroof());
        car.setChildSafetyLocks(dto.getChildSafetyLocks());
        car.setAcFeature(dto.getAcFeature());
        car.setMusicFeature(dto.getMusicFeature());
        car.setArea(dto.getArea());
        car.setVariant(dto.getVariant());
        car.setBrand(dto.getBrand());
        car.setCarInsurance(dto.getCarInsurance());
        car.setCarInsuranceDate(dto.getCarInsuranceDate());
        car.setCarInsuranceType(dto.getCarInsuranceType());
        car.setPendingApproval(dto.isPendingApproval());
        car.setCity(dto.getCity());
        car.setColor(dto.getColor());
        car.setDescription(dto.getDescription());
        car.setFuelType(dto.getFuelType());
        car.setKmDriven(dto.getKmDriven());
        car.setModel(dto.getModel());
        car.setOwnerSerial(dto.getOwnerSerial());
        car.setPowerWindowFeature(dto.getPowerWindowFeature());
        car.setPrice(dto.getPrice());
        car.setRearParkingCameraFeature(dto.getRearParkingCameraFeature());
        car.setRegistration(dto.getRegistration());
        car.setTitle(dto.getTitle());
        car.setTransmission(dto.getTransmission());
        car.setYear(dto.getYear());
        car.setDate(dto.getDate());
        car.setDealerId(dto.getDealerId());
        car.setCarPhotoId(dto.getCarPhotoId());
        car.setMainCarId(dto.getMainCarId());


        // ✅ Set pendingBookings using booking IDs
        if (dto.getPremiumCarPendingBookingId() != null && !dto.getPremiumCarPendingBookingId().isEmpty()) {
            Set<PremiumCarPendingBooking> bookings = dto.getPremiumCarPendingBookingId().stream()
                    .map(id -> bookingRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id)))
                    .collect(Collectors.toSet());
            car.setPendingBookings(bookings);
        }

        return car;
    }


    private static final String MAIN_CAR_ID_FORMAT = "%02d%02d%06d";

    public String generateMainCarId() {
        LocalDate now = LocalDate.now();
        int year = now.getYear() % 100;
        int month = now.getMonthValue();
        long nextSequenceNumber = getNextSequenceNumber();
        return String.format(MAIN_CAR_ID_FORMAT, year, month, nextSequenceNumber);
    }

    private long getNextSequenceNumber() {
        Optional<Long> lastId = premiumCarRepository.findMaxId();
        return lastId.map(id -> id + 1).orElse(1L);
    }

    @Override
    public PremiumCarDto createPremiumCar(PremiumCarDto premiumCarDto) {
        // 1. Check Dealer exists
        Dealer dealer = dealerRepository.findById(premiumCarDto.getDealerId())
                .orElseThrow(() -> new ResourceNotFoundException("Dealer not found for ID: " + premiumCarDto.getDealerId()));

        // 2. Map DTO to Entity
        PremiumCar premiumCar = mapToEntity(premiumCarDto);

        // 3. Generate mainCarId
        String mainCarId = generateMainCarId();
        premiumCar.setMainCarId(mainCarId);
        premiumCar.setCarStatus(Status.ACTIVE);


        // 5. Save to DB
        premiumCar = premiumCarRepository.save(premiumCar);

        // 6. Return mapped DTO
        return mapToDto(premiumCar);
    }



    @Override
    public PremiumCarDto getPremiumCarById(int id) {
        PremiumCar car = premiumCarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PremiumCar not found"));
        return mapToDto(car);
    }

    @Override
    public List<PremiumCarDto> getAllPremiumCars() {
        return premiumCarRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PremiumCarDto updatePremiumCar(int id, PremiumCarDto dto) {
        PremiumCar existing = premiumCarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PremiumCar not found for ID: " + id));

        // Only update fields if they're non-null (i.e., PATCH behavior)
        if (dto.getBrand() != null) existing.setBrand(dto.getBrand());
        if (dto.getModel() != null) existing.setModel(dto.getModel());
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getAirbag() != null) existing.setAirbag(dto.getAirbag());
        if (dto.getABS() != null) existing.setABS(dto.getABS());
        if (dto.getButtonStart() != null) existing.setButtonStart(dto.getButtonStart());
        if (dto.getSunroof() != null) existing.setSunroof(dto.getSunroof());
        if (dto.getChildSafetyLocks() != null) existing.setChildSafetyLocks(dto.getChildSafetyLocks());
        if (dto.getAcFeature() != null) existing.setAcFeature(dto.getAcFeature());
        if (dto.getMusicFeature() != null) existing.setMusicFeature(dto.getMusicFeature());
        if (dto.getArea() != null) existing.setArea(dto.getArea());
        if (dto.getVariant() != null) existing.setVariant(dto.getVariant());
        if (dto.getCarInsurance() != null) existing.setCarInsurance(dto.getCarInsurance());
        if (dto.getCarInsuranceDate() != null) existing.setCarInsuranceDate(dto.getCarInsuranceDate());
        if (dto.getCarInsuranceType() != null) existing.setCarInsuranceType(dto.getCarInsuranceType());
        if (dto.getCity() != null) existing.setCity(dto.getCity());
        if (dto.getColor() != null) existing.setColor(dto.getColor());
        if (dto.getFuelType() != null) existing.setFuelType(dto.getFuelType());
        if (dto.getKmDriven() != null) existing.setKmDriven(dto.getKmDriven());
        if (dto.getOwnerSerial() != null) existing.setOwnerSerial(dto.getOwnerSerial());
        if (dto.getPowerWindowFeature() != null) existing.setPowerWindowFeature(dto.getPowerWindowFeature());
        if (dto.getRearParkingCameraFeature() != null) existing.setRearParkingCameraFeature(dto.getRearParkingCameraFeature());
        if (dto.getRegistration() != null) existing.setRegistration(dto.getRegistration());
        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getTransmission() != null) existing.setTransmission(dto.getTransmission());
        if (dto.getYear() != null) existing.setYear(dto.getYear());
        if (dto.getDate() != null) existing.setDate(dto.getDate());
        if (dto.getDealerId() != null) existing.setDealerId(dto.getDealerId());


        // Boolean primitive field — check if DTO wants to override it
        existing.setPendingApproval(dto.isPendingApproval()); // optional: wrap in logic if needed

        // Save and return
        return mapToDto(premiumCarRepository.save(existing));
    }


    @Override
    public void deletePremiumCar(int id) {
        PremiumCar car = premiumCarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("car not found"));
        premiumCarRepository.delete(car);
    }

    @Override
    public List<PremiumCarDto> getCarsByDealerPremiumCars(Integer dealerId) {
        List<PremiumCar> cars = premiumCarRepository.findByDealerId(dealerId);

        if (cars == null || cars.isEmpty()) {
            throw new ResourceNotFoundException("No premium cars found for dealer with ID: " + dealerId);
        }

        return cars.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @Override
    public PremiumCarDto getPremiumCarByMainId(String mainCarId) {
        PremiumCar car = premiumCarRepository.findByMainCarId(mainCarId)
                .orElseThrow(() -> new ResourceNotFoundException("Premium car not found with main ID: " + mainCarId));

        return mapToDto(car);
    }

    @Override
    public List<PremiumCarDto> searchByFilterPremiumCar(FilterDto filterDto, int pageNo) {
        return null;
    }

    @Override
    public List<PremiumCarDto> getDetails(int dealerId, Status carStatus) {
        return null;
    }

    @Override
    public long getTotalCars() {
        return 0;
    }

    @Override
    public int getCarCountByStatusAndDealer(Status carStatus, int dealerId) {
        return 0;
    }


}
