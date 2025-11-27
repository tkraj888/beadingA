package com.spring.jwt.premiumCar.PremiumCarFilter;

import com.spring.jwt.dto.FilterDto;
import com.spring.jwt.entity.Status;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.premiumCar.FilterDto1;
import com.spring.jwt.premiumCar.PremiumCar;
import com.spring.jwt.premiumCar.PremiumCarDto;
import com.spring.jwt.premiumCar.PremiumCarRepository;
import com.spring.jwt.repository.DealerRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class PremiumCarFilterServiceImpl implements PremiumCarFilterService {
    @Autowired
    private PremiumCarRepository carRepo;
    @Autowired
    private DealerRepository dealerRepo;

    @Override
    public List<PremiumCarDto> searchByFilter(FilterDto1 filterDto) {

        Specification<PremiumCar> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filterDto.getMinPrice()));
            }
            if (filterDto.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filterDto.getMaxPrice()));
            }
            if (filterDto.getArea() != null && !filterDto.getArea().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("area")),
                        "%" + filterDto.getArea().toLowerCase() + "%"));
            }
            if (filterDto.getBrand() != null && !filterDto.getBrand().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")),
                        "%" + filterDto.getBrand().toLowerCase() + "%"));
            }
            if (filterDto.getModel() != null && !filterDto.getModel().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("model")),
                        "%" + filterDto.getModel().toLowerCase() + "%"));
            }
            if (filterDto.getTransmission() != null && !filterDto.getTransmission().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("transmission"), filterDto.getTransmission()));
            }
            if (filterDto.getFuelType() != null && !filterDto.getFuelType().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("fuelType"), filterDto.getFuelType()));
            }

            // Only cars with PENDING or ACTIVE status
            Predicate statusPredicate = criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("carStatus"), Status.PENDING),
                    criteriaBuilder.equal(root.get("carStatus"), Status.ACTIVE)
            );
            predicates.add(statusPredicate);
            query.orderBy(criteriaBuilder.desc(root.get("premiumCarId")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<PremiumCar> carList = carRepo.findAll(spec, Sort.by(Sort.Direction.DESC, "premiumCarId"));

        if (carList.isEmpty()) {
            throw new PageNotFoundException("car found");
        }

        return carList.stream()
                .map(car -> {
                    PremiumCarDto carDto = new PremiumCarDto(car);
                    carDto.setPremiumCarId(car.getPremiumCarId());
                    return carDto;
                })
                .collect(Collectors.toList());
    }




    @Override
    public List<PremiumCarDto> searchByFilterPage(FilterDto1 filterDto, int pageNo, int pageSize) {
        Specification<PremiumCar> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filterDto.getMinPrice()));
            }
            if (filterDto.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filterDto.getMaxPrice()));
            }
            if (filterDto.getArea() != null && !filterDto.getArea().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("area"), filterDto.getArea()));
            }
//            if (filterDto.getYear() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("year"), filterDto.getYear()));
//            }
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

            // Combine status predicate for ACTIVE or PENDING
            Predicate statusPredicate = criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("carStatus"), Status.ACTIVE),
                    criteriaBuilder.equal(root.get("carStatus"), Status.PENDING)
            );
            predicates.add(statusPredicate);

            // Sort by premiumCarId descending
            query.orderBy(criteriaBuilder.desc(root.get("premiumCarId")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Convert pageNo to zero-based index
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        // Fetch page
        Page<PremiumCar> carPage = carRepo.findAll(spec, pageable);
        if (carPage.isEmpty()) {
            throw new PageNotFoundException("No cars found for the specified filter and page.");
        }
System.out.println(carPage);
        // Map to DTO list
        return carPage.stream()
                .map(PremiumCarDto::new)
                .toList();
    }



    @Override
    public List<PremiumCarDto> getAllCarsWithPages(int PageNo,int pageSize) {
        // Fetch the top 4 cars based on their ID in descending order
        List<PremiumCar> topCars = carRepo.findTop4ByOrderByPremiumCarIdDesc();
        List<PremiumCarDto> carDtoList = new ArrayList<>();

        // Convert Car entities to CarDto
        for (PremiumCar car : topCars) {
            PremiumCarDto carDto = new PremiumCarDto(car);
            carDto.setPremiumCarId(car.getPremiumCarId());
            carDtoList.add(carDto);
        }

        return carDtoList;
    }

    @Override
    public List<PremiumCarDto> searchBarFilter(String searchBarInput , int PageNo ) {
        List<PremiumCar> listOfCar = carRepo.getPendingAndActivateCar();
        System.err.println(listOfCar.size());
        CarNotFoundException carNotFoundException;
        if((PageNo*10)>listOfCar.size()-1){
            throw new PageNotFoundException("page not found");

        }
        if(listOfCar.size()<=0){throw new CarNotFoundException("car not found", HttpStatus.NOT_FOUND);}
//        System.out.println("list of de"+listOfCar.size());
        List<PremiumCarDto> listOfCarDto = new ArrayList<>();

        int pageStart=PageNo*10;
        int pageEnd=pageStart+10;
        int diff=(listOfCar.size()) - pageStart;
        for(int counter=pageStart,i=1;counter<pageEnd;counter++,i++){

            if(pageStart>listOfCar.size()){break;}

            PremiumCarDto carDto = new PremiumCarDto(listOfCar.get(counter));
            carDto.setPremiumCarId(listOfCar.get(counter).getPremiumCarId());
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
    public List<PremiumCarDto> getTop4Cars(String searchBarInput) {

        List<PremiumCar> cars = carRepo.searchPremiumCarByKeyword(searchBarInput.toLowerCase());
        return cars.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private PremiumCarDto convertToDto(PremiumCar car) {
        PremiumCarDto carDto = new PremiumCarDto();
        carDto.setPremiumCarId(car.getPremiumCarId());
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

