package com.spring.jwt.service;

import com.spring.jwt.Interfaces.FilterService;
import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.FilterDto;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.DealerRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterServiceImpl implements FilterService {

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private DealerRepository dealerRepo;

    @Override
    public List<CarDto> searchByFilter(FilterDto filterDto) {
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
            if (filterDto.getCarType() != null && !filterDto.getCarType().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("carType"), filterDto.getCarType()));
            }
            Predicate statusPredicate = criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("carStatus"), Status.ACTIVE),
                    criteriaBuilder.equal(root.get("carStatus"), Status.PENDING)
            );
            predicates.add(statusPredicate);

            query.orderBy(criteriaBuilder.desc(root.get("id")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Car> carList = carRepo.findAll(spec);
        if (carList.isEmpty()) {
            throw new PageNotFoundException("Page Not found");
        }

        List<CarDto> listOfCarDto = new ArrayList<>();
        for (Car car : carList) {
            CarDto carDto = new CarDto(car);
            carDto.setCarId(car.getId());
            listOfCarDto.add(carDto);
        }

        return listOfCarDto;
    }

    @Override
    public List<CarDto> searchByFilterPage(FilterDto filterDto, int pageNo, int pageSize) {
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
            if (filterDto.getCarType() != null && !filterDto.getCarType().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("carType"), filterDto.getCarType()));
            }
            Predicate statusPredicate = criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("carStatus"), Status.ACTIVE),
                    criteriaBuilder.equal(root.get("carStatus"), Status.PENDING)
            );
            predicates.add(statusPredicate);

            query.orderBy(criteriaBuilder.desc(root.get("id")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize); // Convert to zero-based index for page number

        Page<Car> carPage = carRepo.findAll(spec, pageable);
        if (carPage.isEmpty()) {
            throw new PageNotFoundException("No cars found for the specified filter and page.");
        }

        List<CarDto> listOfCarDto = carPage.stream()
                .map(CarDto::new)
                .toList();

        return listOfCarDto;
    }


    public List<CarDto> getTop4Cars() {
        // Fetch the top 4 cars based on their ID in descending order
        List<Car> topCars = carRepo.findTop4ByOrderByIdDesc();
        List<CarDto> carDtoList = new ArrayList<>();

        // Convert Car entities to CarDto
        for (Car car : topCars) {
            CarDto carDto = new CarDto(car);
            carDto.setCarId(car.getId());
            carDtoList.add(carDto);
        }

        return carDtoList;
    }




    @Override
    public List<CarDto> getAllCarsWithPages(int PageNo) {
        List<Car> listOfCar = carRepo.getPendingAndActivateCar();
        System.err.println(listOfCar.size());
        CarNotFoundException carNotFoundException;
        if((PageNo*10)>listOfCar.size()-1){
            throw new PageNotFoundException("page not found");

        }
        if(listOfCar.size()<=0){throw new CarNotFoundException("car not found",HttpStatus.NOT_FOUND);}
//        System.out.println("list of de"+listOfCar.size());
        List<CarDto> listOfCarDto = new ArrayList<>();

        int pageStart=PageNo*10;
        int pageEnd=pageStart+10;
        int diff=(listOfCar.size()) - pageStart;
        for(int counter=pageStart,i=1;counter<pageEnd;counter++,i++){

            if(pageStart>listOfCar.size()){break;}

            CarDto carDto = new CarDto(listOfCar.get(counter));
            carDto.setCarId(listOfCar.get(counter).getId());
            listOfCarDto.add(carDto);

//            System.out.println("*");

            if(diff == i){
                break;
            }
        }

        if(listOfCarDto.isEmpty()){
            throw new PageNotFoundException("page not found ..");
        }
        return listOfCarDto;
    }

    @Override
    public List<CarDto> searchBarFilter(String searchBarInput) {
        List<Car> cars = carRepo.searchCarsByKeyword(searchBarInput.toLowerCase());
        return cars.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private CarDto convertToDto(Car car) {
        CarDto carDto = new CarDto();
        carDto.setCarId(car.getId());
        carDto.setBrand(car.getBrand());
        carDto.setModel(car.getModel());
        carDto.setYear(car.getYear());
        carDto.setPrice(car.getPrice());
        carDto.setArea(car.getArea());
        carDto.setFuelType(car.getFuelType());
        carDto.setTransmission(car.getTransmission());

        return carDto;
    }
}