package com.spring.jwt.service;


import com.spring.jwt.Interfaces.ICarRegister;
import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.FilterDto;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.DealerNotFoundException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.PhotoRepo;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarRegisterImp implements ICarRegister {
    @Autowired
    private CarRepo carRepo;

    private static final Logger logger = LoggerFactory.getLogger(CarRegisterImp.class);


    @Autowired
    private DealerRepository dealerRepo;
    @Autowired
    private PhotoRepo photoRepo;




    private static final String MAIN_CAR_ID_FORMAT = "%02d%02d%06d";

    public String generateMainCarId() {
        LocalDate now = LocalDate.now();
        int year = now.getYear() % 100;
        int month = now.getMonthValue();
        long nextSequenceNumber = getNextSequenceNumber();
        return String.format(MAIN_CAR_ID_FORMAT, year, month, nextSequenceNumber);
    }

    private long getNextSequenceNumber() {
        Optional<Long> lastId = carRepo.findMaxId();
        return lastId.map(id -> id + 1).orElse(1L);
    }

    @Override
    public int AddCarDetails(CarDto carDto, String carType) {
        // Find the dealer
        Dealer dealer = dealerRepo.findById(carDto.getDealer_id())
                .orElseThrow(() -> new CarNotFoundException("Dealer Not Found For ID " + carDto.getDealer_id(), HttpStatus.NOT_FOUND));

        // Create a new Car entity from CarDto
        Car car = new Car(carDto);

        // Generate the mainCarId
        String mainCarId = generateMainCarId();
        car.setMainCarId(mainCarId);

        // Set the carType from request parameter
        car.setCarType(carType);
        // Save the car entity

        car = carRepo.save(car);
        return car.getId();
    }


//    @Override
//    public int AddCarDetails(CarDto carDto) {
//        Dealer dealer = dealerRepo.findById(carDto.getDealer_id())
//                .orElseThrow(() -> new CarNotFoundException("Dealer Not Found For ID " + carDto.getDealer_id(), HttpStatus.NOT_FOUND));
//
//        Car car = new Car(carDto);
//        String mainCarId = generateMainCarId();
//        car.setMainCarId(mainCarId);
//        car = carRepo.save(car);
//        return car.getId();
//    }
    /////////////////////////////////////////////////////////////////////
    //
    //  Method Name   :  editCarDetails
    //  Description   :  Used to edit The car Profile
    //  Input         :  editCarDetails
    //  Output        :  String
    //  Date 		  :  27/06/2023
    //  Author 		  :  Geetesh Gajanan Kumbalkar
    //
    /////////////////////////////////////////////////////////////////////

    @Override
    public String editCarDetails(CarDto carDto, int id) {
        System.err.println(carDto.getCarStatus() + "" + id);
        Car car = carRepo.findById(id).orElseThrow(() -> new CarNotFoundException("car not found", HttpStatus.NOT_FOUND));

        if (carDto.getAcFeature() != null) car.setAcFeature(carDto.getAcFeature());
        if (carDto.getMusicFeature() != null) car.setMusicFeature(carDto.getMusicFeature());
        if (carDto.getArea() != null) car.setArea(carDto.getArea());
        if (carDto.getDate() != null) car.setDate(carDto.getDate());
        if (carDto.getBrand() != null) car.setBrand(carDto.getBrand());
        if (carDto.getCarInsurance() != null) car.setCarInsurance(carDto.getCarInsurance());
        if (carDto.getCarStatus() != null) car.setCarStatus(carDto.getCarStatus());
        if (carDto.getCity() != null) car.setCity(carDto.getCity());
        if (carDto.getColor() != null) car.setColor(carDto.getColor());
        if (carDto.getDescription() != null) car.setDescription(carDto.getDescription());
        if (carDto.getFuelType() != null) car.setFuelType(carDto.getFuelType());
        if (carDto.getKmDriven() != 0) car.setKmDriven(carDto.getKmDriven());
        if (carDto.getModel() != null) car.setModel(carDto.getModel());
        if (carDto.getOwnerSerial() != 0) car.setOwnerSerial(carDto.getOwnerSerial());
        if (carDto.getPrice() != 0) car.setPrice(carDto.getPrice());
        if (carDto.getPowerWindowFeature() != null) car.setPowerWindowFeature(carDto.getPowerWindowFeature());
        if (carDto.getRearParkingCameraFeature() != null) car.setRearParkingCameraFeature(carDto.getRearParkingCameraFeature());
        if (carDto.getRegistration() != null) car.setRegistration(carDto.getRegistration());
        if (carDto.getCarInsuranceDate() != null) car.setCarInsuranceDate(carDto.getCarInsuranceDate());
        if (carDto.getTitle() != null) car.setTitle(carDto.getTitle());
        if (carDto.getVariant() != null) car.setVariant(carDto.getVariant());
        if (carDto.getTransmission() != null) car.setTransmission(carDto.getTransmission());
        if (carDto.getCarInsuranceType() != null) car.setCarInsuranceType(carDto.getCarInsuranceType());
        if (carDto.getYear() != 0) car.setYear(carDto.getYear());
        if (carDto.getSunroof() != null) car.setSunroof(carDto.getSunroof());
        if (carDto.getAirbag() != null) car.setAirbag(carDto.getAirbag());

        if (carDto.getABS() != null) car.setABS(carDto.getABS());

        if (carDto.getButtonStart() != null) car.setButtonStart(carDto.getButtonStart());

        if (carDto.getChildSafetyLocks() != null) car.setChildSafetyLocks(carDto.getChildSafetyLocks());

        carRepo.save(car);
        return "Car Updated " + id;
    }


    @Override
    public List<CarDto> getAllCarsWithPages(int pageNo, int pageSize) {

        List<Car> listOfCar = carRepo.getPendingAndActivateCarOrderedByIdDesc();
        if (listOfCar.isEmpty()) {
            throw new CarNotFoundException("Car not found");
        }
        System.out.println("Car IDs from repository:");
        listOfCar.forEach(car -> System.out.println("Car ID: " + car.getId()));


        int totalCars = listOfCar.size();
        int totalPages = (int) Math.ceil((double) totalCars / pageSize);

        if (pageNo < 0 || pageNo >= totalPages) {
            throw new PageNotFoundException("Page not found");
        }

        int pageStart = pageNo * pageSize;
        int pageEnd = Math.min(pageStart + pageSize, totalCars);

        List<CarDto> listOfCarDto = new ArrayList<>();
        for (int i = pageStart; i < pageEnd; i++) {
            Car car = listOfCar.get(i);
            CarDto carDto = new CarDto(car);
            carDto.setCarId(car.getId());
            listOfCarDto.add(carDto);
        }

        System.out.println("CarDto IDs after pagination:");
        listOfCarDto.forEach(carDto -> System.out.println("CarDto ID: " + carDto.getCarId()));

        return listOfCarDto;
    }

    @Override
    public Page<CarDto> getAllCarsWithPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<Status> statuses = Arrays.asList(Status.PENDING, Status.ACTIVE);

        Page<Car> pageOfCars = carRepo.findByCarStatusInOrderByIdDesc(statuses, pageable);
        if (pageOfCars.isEmpty()) {
            throw new CarNotFoundException("Car not found");
        }

        return pageOfCars.map(CarDto::new);
    }

    @Override
    public CarDto findByMainCarId(String mainCarId) {
        Car car = carRepo.findByMainCarId(mainCarId)
                .orElseThrow(() -> new CarNotFoundException("Car not found for MainCarId: " + mainCarId, HttpStatus.NOT_FOUND));

        return new CarDto(car);
    }


    @Override
    public long getTotalCars() {
        return carRepo.countAllByCarStatusNotSold();
    }


    @Override
    public String deleteCar(int carId, int dealerId) {
        Optional<Car> carOptional = carRepo.findById(carId);
        if (carOptional.isPresent()) {
            Car carDetail = carOptional.get();
            int cardealerId = carDetail.getDealerId();
            if (cardealerId == dealerId) {
                Long carDocumentPhotoId = carDetail.getCarPhotoId();
                if (carDocumentPhotoId != null && carDocumentPhotoId != 0) {
                    photoRepo.deleteById(carDocumentPhotoId);
                }
                carRepo.deleteById(carId);
                return "Car details deleted";
            } else {
                throw new RuntimeException("You are not authorized to delete this car");
            }
        } else {
            throw new CarNotFoundException("Car not found", HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public CarDto getCarById(int carId) {
        Optional<Car> car = carRepo.findById(carId);
        return car.map(CarDto::new).orElse(null);
    }

    @Override
    public List<CarDto> searchByFilter(FilterDto filterDto, int pageNo) {
        Specification<Car> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("price"), filterDto.getMinPrice()));
            }
            if (filterDto.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("price"), filterDto.getMaxPrice()));
            }

            if (filterDto.getArea() != null && !filterDto.getArea().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("area"), filterDto.getArea()));
            }
            if (filterDto.getYear() != 0) {
                predicates.add(criteriaBuilder.equal(root.get("year"), filterDto.getYear()));
            }
            if (filterDto.getBrand() != null && !filterDto.getBrand().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("brand"), filterDto.getBrand()));
            }
            if (filterDto.getModel() != null && !filterDto.getModel().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("model"), filterDto.getModel()));
            }
            if (filterDto.getTransmission() != null && !filterDto.getTransmission().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("transmission"), filterDto.getTransmission()));
            }
            if (filterDto.getFuelType() != null && !filterDto.getFuelType().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("fuelType"), filterDto.getFuelType()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(pageNo - 1, 5);

        Page<Car> carPage = carRepo.findAll(spec, pageable);
        if(carPage.isEmpty()){
            throw new PageNotFoundException("Page Not found");
        }
        List<CarDto> listOfCarDto =new ArrayList<>();

        for (int counter=0;counter<carPage.getContent().size();counter++){

            CarDto carDto = new CarDto(carPage.getContent().get(counter));
            carDto.setCarId(carPage.getContent().get(counter).getId());
            listOfCarDto.add(carDto);
        }

        return listOfCarDto;
    }

    @Override
    public CarDto findById(int carId) {
        Optional<Car> car = carRepo.findById(carId);
        if (car.isEmpty()){
            throw new CarNotFoundException("car not found",HttpStatus.NOT_FOUND);
        }
        CarDto carDto = new CarDto(car.get());
        carDto.setCarId(carId);
        return carDto;
    }

    @Override
    public List<String> getAutocompleteSuggestions(String query) {
        List<String> modelSuggestions = carRepo.findByModelContaining(query);
        List<String> brandSuggestions = carRepo.findByBrandContaining(query);
        List<String> citySuggestions = carRepo.findByCityContaining(query);

        Set<String> suggestions = new LinkedHashSet<>();
        suggestions.addAll(modelSuggestions);
        suggestions.addAll(brandSuggestions);
        suggestions.addAll(citySuggestions);

        return new ArrayList<>(suggestions);
    }


    @Override
    public List<CarDto> getDetails(int dealerId, Status carStatus, int pageNo,String carType) {
        if (!dealerExists(dealerId)) {
            throw new DealerNotFoundException("Dealer not found by id");
        }

        List<Car> listOfCar = carRepo.findByDealerIdAndCarStatusAndCarTypeOrderByIdDesc(dealerId, carStatus,carType);

        if (listOfCar.isEmpty()) {
            throw new CarNotFoundException("Car not found", HttpStatus.NOT_FOUND);
        }

        int pageSize = 10;
        int pageStart = pageNo * pageSize;
        int pageEnd = Math.min(pageStart + pageSize, listOfCar.size());

        if (pageStart >= listOfCar.size()) {
            throw new PageNotFoundException("Page not found");
        }

        List<CarDto> listOfCarDto = new ArrayList<>();
        for (int i = pageStart; i < pageEnd; i++) {
            Car car = listOfCar.get(i);
            CarDto carDto = new CarDto(car);
            carDto.setCarId(car.getId());
            listOfCarDto.add(carDto);
        }

        return listOfCarDto;
    }

    private boolean dealerExists(int dealerId) {
        return dealerRepo.existsById(dealerId);
    }

    @Override
    public String editCarDetails(CarDto carDto) {
        return null;
    }

    private CarDto convertToDto(Car car) {
        CarDto carDto = new CarDto();
        carDto.setCarId(car.getId());
        carDto.setDealer_id(car.getDealerId());
        carDto.setBrand(car.getBrand());
        carDto.setModel(car.getModel());
        carDto.setYear(car.getYear());
        carDto.setArea(car.getArea());
        carDto.setCarInsurance(car.getCarInsurance());
        carDto.setCarStatus(car.getCarStatus());
        carDto.setCity(car.getCity());
        carDto.setColor(car.getColor());
        carDto.setDescription(car.getDescription());
        carDto.setFuelType(car.getFuelType());
        carDto.setKmDriven(car.getKmDriven());
        carDto.setOwnerSerial(car.getOwnerSerial());
        carDto.setPrice(car.getPrice());
        carDto.setRegistration(car.getRegistration());
        carDto.setTransmission(car.getTransmission());
        carDto.setAcFeature(car.getAcFeature());
        carDto.setMusicFeature(car.getMusicFeature());
        carDto.setPowerWindowFeature(car.getPowerWindowFeature());
        carDto.setRearParkingCameraFeature(car.getRearParkingCameraFeature());
        carDto.setCarInsuranceDate(car.getCarInsuranceDate());
        carDto.setTitle(car.getTitle());
        carDto.setVariant(car.getVariant());
        carDto.setCarInsuranceType(car.getCarInsuranceType());
        carDto.setSunroof(car.getSunroof());
        carDto.setAirbag(car.getAirbag());
        carDto.setABS(car.getABS());
        carDto.setButtonStart(car.getButtonStart());
        carDto.setChildSafetyLocks(car.getChildSafetyLocks());
        carDto.setDate(car.getDate());
        return carDto;
    }

    public int getCarCountByStatusAndDealer(Status carStatus, int dealerId, String carType) {
        return carRepo.countByCarStatusAndDealerIdAndCarType(carStatus, dealerId,carType);
    }

    @Override
    public long countAllCars() {
        return carRepo.count();
    }

    @Override
    public Page<CarDto> getAllCarsWithCarTypeandPage(int pageNo, int pageSize, String carType) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<Status> statuses = Arrays.asList(Status.PENDING, Status.ACTIVE);

        // Fetch cars by status and carType
        Page<Car> pageOfCars = carRepo.findByCarStatusInAndCarTypeOrderByIdDesc(statuses, carType, pageable);

        if (pageOfCars.isEmpty()) {
            throw new CarNotFoundException("Car not found");
        }

        // Map to CarDto and return a Page
        return pageOfCars.map(CarDto::new);
    }



}