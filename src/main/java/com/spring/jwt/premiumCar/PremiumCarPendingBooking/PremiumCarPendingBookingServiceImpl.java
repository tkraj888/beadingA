package com.spring.jwt.premiumCar.PremiumCarPendingBooking;

import com.spring.jwt.dto.BookingDtos.DealerDetails;
import com.spring.jwt.dto.BookingDtos.PendingBookingRequestDto;
import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.entity.*;
import com.spring.jwt.exception.*;
import com.spring.jwt.premiumCar.PremiumCar;
import com.spring.jwt.premiumCar.PremiumCarRepository;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.UserProfileRepository;
import com.spring.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PremiumCarPendingBookingServiceImpl implements PremiumCarPendingBookingService {

    @Autowired
    PremiumCarPendingBookingRepository premiumCarPendingBookingRepository;

    @Autowired
    DealerRepository dealerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    PremiumCarRepository premiumCarRepository;

    private PremiunCarPendingBookingDto mapToDto(PremiumCarPendingBooking premiumCarPendingBooking) {
        PremiunCarPendingBookingDto dto = new PremiunCarPendingBookingDto();
        dto.setDate(premiumCarPendingBooking.getDate());
        dto.setPrice(premiumCarPendingBooking.getPrice());
        dto.setStatus(premiumCarPendingBooking.getStatus());
        dto.setAskingPrice(premiumCarPendingBooking.getAskingPrice());

        dto.setDealerId(premiumCarPendingBooking.getDealerId() != null ?
                premiumCarPendingBooking.getDealerId(): null);

        dto.setUserId(premiumCarPendingBooking.getUserId() != null ?
                premiumCarPendingBooking.getUserId(): null);

        dto.setPremiumCarId(premiumCarPendingBooking.getPremiumCarCar() != null ?
                premiumCarPendingBooking.getPremiumCarCar().getPremiumCarId() : null);

        return dto;
    }

    private PremiumCarPendingBooking mapToEntity(PremiunCarPendingBookingDto dto) {
        PremiumCarPendingBooking entity = new PremiumCarPendingBooking();
        entity.setDate(dto.getDate());
        entity.setPrice(dto.getPrice());
        entity.setStatus(dto.getStatus());
        entity.setAskingPrice(dto.getAskingPrice());

        if (dto.getDealerId() != null) {
            Dealer dealer = dealerRepository.findById(dto.getDealerId())
                    .orElseThrow(() -> new RuntimeException("Dealer not found with ID: " + dto.getDealerId()));
            entity.setDealerId(dto.getDealerId());
        }

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
            entity.setUserId(dto.getUserId());
        }

        if (dto.getPremiumCarId() != null) {
            PremiumCar car = premiumCarRepository.findById(dto.getPremiumCarId())
                    .orElseThrow(() -> new RuntimeException("PremiumCar not found with ID: " + dto.getPremiumCarId()));
            entity.setPremiumCarCar(car);
        }

        return entity;
    }

    @Override
    public PremiunCarPendingBookingDto createPremiumCarPendingBookingService(PremiunCarPendingBookingDto premiunCarPendingBookingDto) {
        // 1.Convert DTO to entity
        PremiumCarPendingBooking entity = mapToEntity(premiunCarPendingBookingDto);

        // 2.Save entity
        PremiumCarPendingBooking savedEntity = premiumCarPendingBookingRepository.save(entity);

        //3. Convert saved entity back to DTO and return
        return mapToDto(savedEntity);
    }


    @Override
    public PremiunCarPendingBookingDto getPendingBookingId(Long bookingId) {
        Optional<PremiumCarPendingBooking> pendingBookingOptional = premiumCarPendingBookingRepository.findById(bookingId);
        if (pendingBookingOptional.isEmpty()) {
            throw new BookingNotFoundException("pending booking not found", HttpStatus.NOT_FOUND);
        }

        PremiumCarPendingBooking pendingBooking = pendingBookingOptional.get();
        PremiunCarPendingBookingDto pendingBookingDTO = new PremiunCarPendingBookingDto(pendingBooking);

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
    public List<PremiunCarPendingBookingDto> getAllPendingBookingWithPage(int pageNo) {
        int pageSize = 10;
        List<PremiumCarPendingBooking> listOfPendingBooking = premiumCarPendingBookingRepository.findAll();

        if (listOfPendingBooking.isEmpty()) {
            throw new CarNotFoundException("Pending Booking not found", HttpStatus.NOT_FOUND);
        }

        listOfPendingBooking.sort((b1, b2) -> b2.getPremiumCarPendingBookingId().compareTo(b1.getPremiumCarPendingBookingId()));
        int totalPendingBookings = listOfPendingBooking.size();
        int pageStart = pageNo * pageSize;
        int pageEnd = Math.min(pageStart + pageSize, totalPendingBookings);

        if (pageStart >= totalPendingBookings) {
            throw new PageNotFoundException("Page not found");
        }

        List<PremiumCarPendingBooking> pagedPendingBookings = listOfPendingBooking.subList(pageStart, pageEnd);
        List<PremiunCarPendingBookingDto> listOfPendingBookingDto = pagedPendingBookings.stream()
                .map(pendingBooking -> {
                    PremiunCarPendingBookingDto pendingBookingDTO = new PremiunCarPendingBookingDto(pendingBooking);
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
    public List<PremiunCarPendingBookingDto> getAllPendingBookingByUserId(int pageNo, int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundExceptions("User not found by id");
        }
        Optional<List<PremiumCarPendingBooking>> listofPendingBookingOptional = premiumCarPendingBookingRepository.getAllPendingBookingByUserId(userId);
        if (listofPendingBookingOptional.isEmpty()) {
            throw new BookingNotFoundException("pending booking not found by User id", HttpStatus.NOT_FOUND);
        }

        List<PremiumCarPendingBooking> listofPendingBooking = listofPendingBookingOptional.get();

        // Sort the list in descending order by ID
        listofPendingBooking.sort(Comparator.comparing(PremiumCarPendingBooking::getPremiumCarPendingBookingId).reversed());

        if ((pageNo * 10) > listofPendingBooking.size() - 1) {
            throw new PageNotFoundException("page not found");
        }

        List<PremiunCarPendingBookingDto> listOfPendingBookingdto = new ArrayList<>();
        int pageStart = pageNo * 10;
        int pageEnd = Math.min(pageStart + 10, listofPendingBooking.size());
        int diff = listofPendingBooking.size() - pageStart;

        for (int counter = pageStart, i = 1; counter < pageEnd; counter++, i++) {
            if (pageStart > listofPendingBooking.size()) {
                break;
            }
            PremiunCarPendingBookingDto pendingBookingDTO = new PremiunCarPendingBookingDto(listofPendingBooking.get(counter));
            listOfPendingBookingdto.add(pendingBookingDTO);
            if (diff == i) {
                break;
            }
        }

        return listOfPendingBookingdto;
    }

    @Override
    public List<PremiunCarPendingBookingDto> getPendingBookingsByDealerId(int pageNo, int dealerId) {
        Optional<Dealer> dealer = dealerRepository.findById(dealerId);
        if (dealer.isEmpty()) {
            throw new DealerNotFoundException("Dealer not found by id: " + dealerId);
        }

        Optional<List<PremiumCarPendingBooking>> listofPendingBookingOptional = premiumCarPendingBookingRepository.findByDealerId(dealerId);
        if (listofPendingBookingOptional.isEmpty()) {
            throw new BookingNotFoundException("Pending bookings not found for dealer Id: " + dealerId, HttpStatus.NOT_FOUND);
        }

        List<PremiumCarPendingBooking> listofPendingBooking = listofPendingBookingOptional.get();

        listofPendingBooking.sort(Comparator.comparing(PremiumCarPendingBooking::getPremiumCarPendingBookingId).reversed());

        int pageSize = 10;
        int totalPendingBookings = listofPendingBooking.size();
        int pageStart = pageNo * pageSize;
        int pageEnd = Math.min(pageStart + pageSize, totalPendingBookings);

        if (pageStart >= totalPendingBookings) {
            throw new PageNotFoundException("Page not found");
        }

        List<PremiunCarPendingBookingDto> listOfPendingBookingDto = listofPendingBooking.subList(pageStart, pageEnd).stream()
                .map(pendingBooking -> {
                    PremiunCarPendingBookingDto pendingBookingDTO = new PremiunCarPendingBookingDto(pendingBooking);

                    Optional<Userprofile> userOptional = userProfileRepository.findByUserId(pendingBooking.getUserId());
                    if (userOptional.isPresent()) {
                        Userprofile userProfile = userOptional.get();
                        User user = userProfile.getUser();
                        pendingBookingDTO.setUsername(userProfile.getFirstName());
                        pendingBookingDTO.setMobileNumber(user.getMobileNo());
                    } else {
                        throw new UserNotFoundExceptions("User not found for pending booking with ID: " + pendingBooking.getPremiumCarPendingBookingId());
                    }

                    return pendingBookingDTO;
                })
                .collect(Collectors.toList());

        return listOfPendingBookingDto;
    }

    @Override
    public List<PremiunCarPendingBookingDto> getPendingBookingsByCarId(int pageNo, int carId) {
        Optional<PremiumCar> car = premiumCarRepository.findById(carId);
        if (car.isEmpty()) {
            throw new CarNotFoundException("car not found by id");
        }

        Optional<List<PremiumCarPendingBooking>> listofPendingBookingOptional = Optional.ofNullable(premiumCarPendingBookingRepository.findByCarCarId(carId));
        if (listofPendingBookingOptional.isEmpty()) {
            throw new BookingNotFoundException("pending booking not found by car Id", HttpStatus.NOT_FOUND);
        }

        List<PremiumCarPendingBooking> listofPendingBooking = listofPendingBookingOptional.get();

        listofPendingBooking.sort(Comparator.comparing(PremiumCarPendingBooking::getPremiumCarPendingBookingId).reversed());

        if ((pageNo * 10) > listofPendingBooking.size() - 1) {
            throw new PageNotFoundException("page not found");
        }

        List<PremiunCarPendingBookingDto> listOfPendingBookingdto = new ArrayList<>();
        int pageStart = pageNo * 10;
        int pageEnd = Math.min(pageStart + 10, listofPendingBooking.size());
        int diff = listofPendingBooking.size() - pageStart;

        for (int counter = pageStart, i = 1; counter < pageEnd; counter++, i++) {
            if (pageStart > listofPendingBooking.size()) {
                break;
            }

            PremiunCarPendingBookingDto pendingBookingDTO = new PremiunCarPendingBookingDto(listofPendingBooking.get(counter));
            listOfPendingBookingdto.add(pendingBookingDTO);
            if (diff == i) {
                break;
            }
        }

        return listOfPendingBookingdto;
    }

    @Override
    public void statusUpdate(PremiunCarPendingBookingDto pendingBookingDTO) {
        Optional<PremiumCarPendingBooking> pendingBookingOptional = premiumCarPendingBookingRepository.findById(pendingBookingDTO.getPremiumCarPendingBookingId());
        if (pendingBookingOptional.isPresent()) {
            PremiumCarPendingBooking pendingBooking = pendingBookingOptional.get();
            pendingBooking.setStatus(pendingBookingDTO.getStatus());
            Optional<PremiumCar> carOptional = premiumCarRepository.findById(pendingBookingDTO.getPremiumCarId());
            if (carOptional.isPresent()) {
                PremiumCar car = carOptional.get();
                car.setCarStatus(pendingBookingDTO.getStatus());
                premiumCarRepository.save(car);
            } else {
                throw new CarNotFoundException("No car found with this id");
            }
            premiumCarPendingBookingRepository.save(pendingBooking);
        } else {
            throw new BookingNotFound("Booking not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteBooking(Long id) {
        Optional<PremiumCarPendingBooking> pendingBooking = premiumCarPendingBookingRepository.findById(id);
        if (pendingBooking.isPresent()) {
            premiumCarPendingBookingRepository.deleteById(id);
        } else {
            throw new BookingNotFound("Booking not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public PremiumCarPendingBookingRequestDto savePendingBooking(PremiunCarPendingBookingDto pendingBookingDTO) {
        Optional<PremiumCar> car = premiumCarRepository.findById(pendingBookingDTO.getPremiumCarId());
        if (car.isEmpty()){throw new CarNotFoundException("car not found by id");}

        Optional<Dealer> dealer = dealerRepository.findById(car.get().getDealerId());
        if(dealer.isEmpty()){throw new DealerNotFoundException("dealer not found by id");}

        Optional<User> user=userRepository.findById(pendingBookingDTO.getUserId());
        if(user.isEmpty()){throw new UserNotFoundExceptions("user not found by id");}

        PremiumCarPendingBooking pendingBooking = new PremiumCarPendingBooking(pendingBookingDTO);
        pendingBooking.setPremiumCarCar(car.get());
        pendingBooking.setDealerId(pendingBookingDTO.getDealerId());
        premiumCarPendingBookingRepository.save(pendingBooking);

        PremiumCarPendingBookingRequestDto premiumCarPendingBookingRequestDto = new PremiumCarPendingBookingRequestDto();
        DealerDetails dealerDetails = new DealerDetails(dealer.get());
        premiumCarPendingBookingRequestDto.setDealerDetails(dealerDetails);
        return premiumCarPendingBookingRequestDto;

    }
}






