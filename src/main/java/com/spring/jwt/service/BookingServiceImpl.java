package com.spring.jwt.service;

import com.spring.jwt.Interfaces.BookingService;
import com.spring.jwt.Interfaces.ICarRegister;
import com.spring.jwt.dto.BookingDto;
import com.spring.jwt.dto.CarDto;
import com.spring.jwt.entity.*;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ICarRegister iCarRegister;
    private final PendingBookingRepository pendingBookingRepository;
    private final UserProfileRepository userProfileRepository;
    private final CarRepo carRepo;
    private final TempPendingBookingReqRepository tempPendingBookingReqRepository;

    @Override
    public BookingDto saveBooking(BookingDto bookingDto) {
        CarDto car = iCarRegister.findById(bookingDto.getCarId());
        if (car == null) {
            throw new EntityNotFoundException("Car not found with ID: " + bookingDto.getCarId());
        }

        if (car.getCarStatus() == Status.SOLD) {
            throw new BookingException("Car is not available for booking as it's already sold.");
        }
        bookingDto.setMainCarId(car.getMainCarId());
        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDto, booking);
        booking.setStatus("confirm");
        booking = bookingRepository.save(booking);

        List<PendingBooking> pendingBookings = pendingBookingRepository.findByCarCarId(bookingDto.getCarId());
        if (!pendingBookings.isEmpty()) {
            Car carEntity = carRepo.findById(bookingDto.getCarId())
                    .orElseThrow(() -> new EntityNotFoundException("Car not found with ID: " + bookingDto.getCarId()));
            carEntity.setCarStatus(Status.SOLD);
            carRepo.save(carEntity);

            Integer confirmedBookingId = booking.getId();

            List<TempPendingBookingReq> tempBookings = pendingBookings.stream()
                    .filter(pendingBooking -> !pendingBooking.getId().equals(confirmedBookingId))
                    .map(pendingBooking ->
                    {
                        TempPendingBookingReq tempPendingBookingReq = new TempPendingBookingReq();
                        BeanUtils.copyProperties(pendingBooking, tempPendingBookingReq);
                        if (tempPendingBookingReq.getCreatedDate() == null) {
                            tempPendingBookingReq.setCreatedDate(LocalDateTime.now());
                        }
                        return tempPendingBookingReq;
                    })
                    .collect(Collectors.toList());

            tempPendingBookingReqRepository.saveAll(tempBookings);

            pendingBookingRepository.deleteAll(pendingBookings);

        }

        pendingBookingRepository.deleteByCarCarId(bookingDto.getCarId());
        BookingDto savedBookingDto = new BookingDto();
        BeanUtils.copyProperties(booking, savedBookingDto);
        return savedBookingDto;
    }


    @Override
    public List<BookingDto> getAllBooking(int pageNo) {
        int pageSize = 10;
        List<Booking> listOfBooking = bookingRepository.findAll();

        if (listOfBooking.isEmpty()) {
            throw new BookingNotFound("Booking not found", HttpStatus.NOT_FOUND);
        }

        listOfBooking.sort((b1, b2) -> b2.getId().compareTo(b1.getId()));

        int totalBookings = listOfBooking.size();
        int pageStart = pageNo * pageSize;
        int pageEnd = Math.min(pageStart + pageSize, totalBookings);

        if (pageStart >= totalBookings) {
            throw new PageNotFoundException("Page not found");
        }

        List<Booking> pagedBookings = listOfBooking.subList(pageStart, pageEnd);

        List<BookingDto> listOfBookingDto = pagedBookings.stream()
                .map(booking -> {
                    BookingDto bookingDto = new BookingDto(booking);
                    bookingDto.setId(booking.getId());
                    bookingDto.setDate(booking.getDate());
                    bookingDto.setPrice(booking.getPrice());
                    bookingDto.setCarId(booking.getCarId());
                    bookingDto.setUserId(booking.getUserId());
                    bookingDto.setDealerId(booking.getDealerId());
                    bookingDto.setStatus(booking.getStatus());

                    Optional<Userprofile> userOptional = userProfileRepository.findByUserId(booking.getUserId());
                    if (userOptional.isPresent()) {
                        Userprofile userProfile = userOptional.get();
                        User user = userProfile.getUser();
                        bookingDto.setFirstName(userProfile.getFirstName());
                        bookingDto.setMobileNo(user.getMobileNo());
                    } else {
                        throw new UserNotFoundExceptions("User not found for booking with ID: " + booking.getId());
                    }

                    return bookingDto;
                })
                .collect(Collectors.toList());

        return listOfBookingDto;
    }



    @Override
    public BookingDto getAllBookingsByUserId(int userId) {
        Optional<Booking> booking=bookingRepository.findByUserId(userId);
        if (booking.isPresent()){
            return new BookingDto(booking.get());
        }else {
            throw new BookingNotFoundException("Booking not found");
        }
    }

    @Override
    public List<BookingDto> getAllBookingsByDealerId(int dealerId, int pageNo) {
        int pageSize = 10;
        List<Booking> listOfBooking = bookingRepository.findByDealerId(dealerId);

        if (listOfBooking.isEmpty()) {
            throw new BookingNotFoundException("No bookings found for dealer ID " + dealerId, HttpStatus.NOT_FOUND);
        }

        listOfBooking.sort((b1, b2) -> b2.getId().compareTo(b1.getId()));
        int totalBookings = listOfBooking.size();
        int pageStart = pageNo * pageSize;
        int pageEnd = Math.min(pageStart + pageSize, totalBookings);

        if (pageStart >= totalBookings) {
            throw new PageNotFoundException("Page not found");
        }

        List<Booking> pagedBookings = listOfBooking.subList(pageStart, pageEnd);
        List<BookingDto> listOfBookingDto = pagedBookings.stream()
                .map(booking -> {
                    BookingDto bookingDto = new BookingDto(booking);
                    bookingDto.setId(booking.getId());
                    bookingDto.setDate(booking.getDate());
                    bookingDto.setPrice(booking.getPrice());
                    bookingDto.setCarId(booking.getCarId());
                    bookingDto.setUserId(booking.getUserId());
                    bookingDto.setDealerId(booking.getDealerId());
                    bookingDto.setStatus(booking.getStatus());

                    Optional<Userprofile> userOptional = userProfileRepository.findByUserId(booking.getUserId());
                    if (userOptional.isPresent()) {
                        Userprofile userProfile = userOptional.get();
                        User user = userProfile.getUser();
                        bookingDto.setFirstName(userProfile.getFirstName());
                        bookingDto.setMobileNo(user.getMobileNo());
                    } else {
                        throw new UserNotFoundExceptions("User not found for booking with ID: " + booking.getId());
                    }

                    return bookingDto;
                })
                .collect(Collectors.toList());

        return listOfBookingDto;
    }


    @Override
    public BookingDto getBookingById(int id) {
       Optional<Booking> booking= bookingRepository.findById(id);
       if (booking.isPresent()){
           return new BookingDto(booking.get());
       }else {
           throw new BookingNotFoundException("No booking found");
       }
    }


    @Override
    public String editById(int id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            booking.setStatus("cancel"); // Set booking status to "cancel"
            bookingRepository.save(booking);

            Optional<Car> carOptional = carRepo.findById(booking.getCarId());
            if (carOptional.isPresent()) {
                Car car = carOptional.get();
                car.setCarStatus(Status.ACTIVE);
                carRepo.save(car);
            } else {
                throw new CarNotFoundException("No car found with this id");
            }
            return "Booking and Car status cancel successfully.";
        } else {
            throw new BookingNotFoundException("No booking found with this id");
        }
    }

    @Override
    public BookingDto getBookingByMainCarId(String mainCarId) {
        Optional<Booking> booking= bookingRepository.findByMainCarId(mainCarId);
        if (booking.isPresent()){
            return new BookingDto(booking.get());
        }else {
            throw new BookingNotFoundException("No booking found");
        }
    }
}
