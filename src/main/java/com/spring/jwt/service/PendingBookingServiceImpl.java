package com.spring.jwt.service;

import com.spring.jwt.Interfaces.PendingBookingService;
import com.spring.jwt.dto.BookingDtos.DealerDetails;
import com.spring.jwt.dto.BookingDtos.PendingBookingRequestDto;
import com.spring.jwt.dto.CarDto;
import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.entity.*;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PendingBookingServiceImpl implements PendingBookingService {

    private final PendingBookingRepository pendingBookingRepository;

    private final CarRepo carRepository;

    private final UserProfileRepository userProfileRepository;

    private  final UserRepository userRepository;

    private final DealerRepository dealerRepository;

    @Override

    public void deleteBooking(int id) {
        Optional<PendingBooking> pendingBooking = pendingBookingRepository.findById(id);
        if (pendingBooking.isPresent()) {
            pendingBookingRepository.deleteById(id);
        } else {
            throw new BookingNotFound("Booking not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void statusUpdate(PendingBookingDTO pendingBookingDTO) {
        Optional<PendingBooking> pendingBookingOptional = pendingBookingRepository.findById(pendingBookingDTO.getId());
        if (pendingBookingOptional.isPresent()) {
            PendingBooking pendingBooking = pendingBookingOptional.get();
            pendingBooking.setStatus(pendingBookingDTO.getStatus());
            Optional<Car> carOptional = carRepository.findById(pendingBookingDTO.getCarId());
            if (carOptional.isPresent()) {
                Car car = carOptional.get();
                car.setCarStatus(pendingBookingDTO.getStatus());
                carRepository.save(car);
            } else {
                throw new CarNotFoundException("No car found with this id");
            }
            pendingBookingRepository.save(pendingBooking);
        } else {
            throw new BookingNotFound("Booking not found", HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public List<PendingBookingDTO> getAllPendingBookingWithPage(int pageNo) {
        int pageSize = 10;
        List<PendingBooking> listOfPendingBooking = pendingBookingRepository.findAll();

        if (listOfPendingBooking.isEmpty()) {
            throw new CarNotFoundException("Pending Booking not found", HttpStatus.NOT_FOUND);
        }

        listOfPendingBooking.sort((b1, b2) -> b2.getId().compareTo(b1.getId()));
        int totalPendingBookings = listOfPendingBooking.size();
        int pageStart = pageNo * pageSize;
        int pageEnd = Math.min(pageStart + pageSize, totalPendingBookings);

        if (pageStart >= totalPendingBookings) {
            throw new PageNotFoundException("Page not found");
        }

        List<PendingBooking> pagedPendingBookings = listOfPendingBooking.subList(pageStart, pageEnd);
        List<PendingBookingDTO> listOfPendingBookingDto = pagedPendingBookings.stream()
                .map(pendingBooking -> {
                    PendingBookingDTO pendingBookingDTO = new PendingBookingDTO(pendingBooking);
                    Optional<Userprofile> userOptional = userProfileRepository.findByUserId(pendingBooking.getUserId());

                    if (userOptional.isPresent()) {
                        Userprofile userProfile = userOptional.get();
                        User user = userProfile.getUser();
                        pendingBookingDTO.setUsername(userProfile.getFirstName());
                        pendingBookingDTO.setMobileNumber(user.getMobileNo());
                        System.out.println("User Profile found: " + userProfile.getFirstName() + ", " + user.getMobileNo());
                    } else {
                        System.out.println("User Profile not found for userId: " + pendingBooking.getUserId());
                    }

                    return pendingBookingDTO;
                })
                .collect(Collectors.toList());

        return listOfPendingBookingDto;
    }


    @Override
    public PendingBookingRequestDto savePendingBooking(PendingBookingDTO pendingBookingDTO) {
        Optional<Car> car = carRepository.findById(pendingBookingDTO.getCarId());
        if (car.isEmpty()){throw new CarNotFoundException("car not found by id");}

        Optional<Dealer> dealer = dealerRepository.findById(car.get().getDealerId());
        if(dealer.isEmpty()){throw new DealerNotFoundException("dealer not found by id");}

        Optional<User> user=userRepository.findById(pendingBookingDTO.getUserId());
        if(user.isEmpty()){throw new UserNotFoundExceptions("user not found by id");}

        PendingBooking pendingBooking = new PendingBooking(pendingBookingDTO);
        pendingBooking.setCarCar(car.get());
        pendingBooking.setDealerId(pendingBookingDTO.getDealerId());
        pendingBookingRepository.save(pendingBooking);

        PendingBookingRequestDto pendingBookingRequestDto = new PendingBookingRequestDto(car.get());
        DealerDetails dealerDetails = new DealerDetails(dealer.get());
        pendingBookingRequestDto.setDealerDetails(dealerDetails);
        return pendingBookingRequestDto;

    }

    private PendingBooking mapToPendingBooking(PendingBookingDTO pendingBookingDTO) {
        Optional<Car> optionalCar = carRepository.findById(pendingBookingDTO.getCarId());
        Car car = optionalCar.orElseThrow(() -> new EntityNotFoundException("Car not found"));


        int dealerId = Objects.requireNonNullElse(pendingBookingDTO.getDealerId(), -1);

        PendingBooking pendingBooking = new PendingBooking(pendingBookingDTO);
        pendingBooking.setDate(pendingBookingDTO.getDate());
        pendingBooking.setPrice(pendingBookingDTO.getPrice());
        pendingBooking.setStatus(pendingBookingDTO.getStatus());
        pendingBooking.setUserId(pendingBookingDTO.getUserId());
        pendingBooking.setDealerId(dealerId);
        pendingBooking.setAskingPrice(pendingBookingDTO.getAskingPrice());
        pendingBooking.setCarCar(car);
        return pendingBooking;
    }


    private Car mapToCar(CarDto carDto) {
        return new Car(carDto);
    }

    private Dealer mapToDealer(DealerDto dealerDto) {
        return new Dealer(dealerDto);
    }



    @Override
    public PendingBookingDTO getPendingBookingId(int bookingId) {
        Optional<PendingBooking> pendingBookingOptional = pendingBookingRepository.findById(bookingId);
        if (pendingBookingOptional.isEmpty()) {
            throw new BookingNotFoundException("pending booking not found", HttpStatus.NOT_FOUND);
        }

        PendingBooking pendingBooking = pendingBookingOptional.get();
        PendingBookingDTO pendingBookingDTO = new PendingBookingDTO(pendingBooking);

        Optional<Userprofile> userOptional = userProfileRepository.findByUserId(pendingBooking.getUserId());
        if (userOptional.isPresent()) {
            Userprofile userProfile = userOptional.get();
            User user = userProfile.getUser();
            pendingBookingDTO.setUsername(userProfile.getFirstName());
            pendingBookingDTO.setMobileNumber(user.getMobileNo());
        } else {
            throw new UserNotFoundExceptions("User not found for pending booking with ID: " + bookingId);
        }

        return pendingBookingDTO;
    }


    @Override
    public List<PendingBookingDTO> getPendingBookingsByDealerId(int pageNo, int dealerId) {
        Optional<Dealer> dealer = dealerRepository.findById(dealerId);
        if (dealer.isEmpty()) {
            throw new DealerNotFoundException("Dealer not found by id: " + dealerId);
        }

        Optional<List<PendingBooking>> listofPendingBookingOptional = pendingBookingRepository.findByDealerId(dealerId);
        if (listofPendingBookingOptional.isEmpty()) {
            throw new BookingNotFoundException("Pending bookings not found for dealer Id: " + dealerId, HttpStatus.NOT_FOUND);
        }

        List<PendingBooking> listofPendingBooking = listofPendingBookingOptional.get();

        listofPendingBooking.sort(Comparator.comparing(PendingBooking::getId).reversed());

        int pageSize = 10;
        int totalPendingBookings = listofPendingBooking.size();
        int pageStart = pageNo * pageSize;
        int pageEnd = Math.min(pageStart + pageSize, totalPendingBookings);

        if (pageStart >= totalPendingBookings) {
            throw new PageNotFoundException("Page not found");
        }

        List<PendingBookingDTO> listOfPendingBookingDto = listofPendingBooking.subList(pageStart, pageEnd).stream()
                .map(pendingBooking -> {
                    PendingBookingDTO pendingBookingDTO = new PendingBookingDTO(pendingBooking);

                    Optional<Userprofile> userOptional = userProfileRepository.findByUserId(pendingBooking.getUserId());
                    if (userOptional.isPresent()) {
                        Userprofile userProfile = userOptional.get();
                        User user = userProfile.getUser();
                        pendingBookingDTO.setUsername(userProfile.getFirstName());
                        pendingBookingDTO.setMobileNumber(user.getMobileNo());
                    } else {
                        throw new UserNotFoundExceptions("User not found for pending booking with ID: " + pendingBooking.getId());
                    }

                    return pendingBookingDTO;
                })
                .collect(Collectors.toList());

        return listOfPendingBookingDto;
    }



    @Override
    public List<PendingBookingDTO> getPendingBookingsByCarId(int pageNo, int carId) {
        Optional<Car> car = carRepository.findById(carId);
        if (car.isEmpty()) {
            throw new CarNotFoundException("car not found by id");
        }

        Optional<List<PendingBooking>> listofPendingBookingOptional = Optional.ofNullable(pendingBookingRepository.findByCarCarId(carId));
        if (listofPendingBookingOptional.isEmpty()) {
            throw new BookingNotFoundException("pending booking not found by car Id", HttpStatus.NOT_FOUND);
        }

        List<PendingBooking> listofPendingBooking = listofPendingBookingOptional.get();

        listofPendingBooking.sort(Comparator.comparing(PendingBooking::getId).reversed());

        if ((pageNo * 10) > listofPendingBooking.size() - 1) {
            throw new PageNotFoundException("page not found");
        }

        List<PendingBookingDTO> listOfPendingBookingdto = new ArrayList<>();
        int pageStart = pageNo * 10;
        int pageEnd = Math.min(pageStart + 10, listofPendingBooking.size());
        int diff = listofPendingBooking.size() - pageStart;

        for (int counter = pageStart, i = 1; counter < pageEnd; counter++, i++) {
            if (pageStart > listofPendingBooking.size()) {
                break;
            }

            PendingBookingDTO pendingBookingDTO = new PendingBookingDTO(listofPendingBooking.get(counter));
            listOfPendingBookingdto.add(pendingBookingDTO);
            if (diff == i) {
                break;
            }
        }

        return listOfPendingBookingdto;
    }


    @Override
    public List<PendingBookingDTO> getAllPendingBookingByUserId(int pageNo, int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundExceptions("User not found by id");
        }
        Optional<List<PendingBooking>> listofPendingBookingOptional = pendingBookingRepository.getAllPendingBookingByUserId(userId);
        if (listofPendingBookingOptional.isEmpty()) {
            throw new BookingNotFoundException("pending booking not found by User id", HttpStatus.NOT_FOUND);
        }

        List<PendingBooking> listofPendingBooking = listofPendingBookingOptional.get();

        // Sort the list in descending order by ID
        listofPendingBooking.sort(Comparator.comparing(PendingBooking::getId).reversed());

        if ((pageNo * 10) > listofPendingBooking.size() - 1) {
            throw new PageNotFoundException("page not found");
        }

        List<PendingBookingDTO> listOfPendingBookingdto = new ArrayList<>();
        int pageStart = pageNo * 10;
        int pageEnd = Math.min(pageStart + 10, listofPendingBooking.size());
        int diff = listofPendingBooking.size() - pageStart;

        for (int counter = pageStart, i = 1; counter < pageEnd; counter++, i++) {
            if (pageStart > listofPendingBooking.size()) {
                break;
            }
            PendingBookingDTO pendingBookingDTO = new PendingBookingDTO(listofPendingBooking.get(counter));
            listOfPendingBookingdto.add(pendingBookingDTO);
            if (diff == i) {
                break;
            }
        }

        return listOfPendingBookingdto;
    }

}
