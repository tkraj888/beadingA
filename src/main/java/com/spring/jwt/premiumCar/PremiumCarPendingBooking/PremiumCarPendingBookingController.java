package com.spring.jwt.premiumCar.PremiumCarPendingBooking;

import com.spring.jwt.dto.BookingDtos.*;
import com.spring.jwt.dto.PendingBookingDTO;
import com.spring.jwt.dto.ResponseAllPendingBookingDto;
import com.spring.jwt.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("PremiumCarPendingBooking")
public class PremiumCarPendingBookingController {

    @Autowired
    private PremiumCarPendingBookingService premiumCarPendingBookingService;


    @PostMapping("/create")
    public ResponseEntity createBooking(@RequestBody PremiunCarPendingBookingDto dto) {
        try {
            PremiunCarPendingBookingDto savedBooking = premiumCarPendingBookingService.createPremiumCarPendingBookingService(dto);
            return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create booking: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getPendingBookingDetailsById")
    public ResponseEntity<?> getBookingDetailsById(@RequestParam Long bookingId) {
        try {
            PremiunCarPendingBookingDto pendingBookingDTO = premiumCarPendingBookingService.getPendingBookingId(bookingId);
            PendingBookingResponseForSingleDealerDto pendingBookingResponseForSingleDealerDto = new PendingBookingResponseForSingleDealerDto("success");
            pendingBookingResponseForSingleDealerDto.setPremiunCarPendingBookingDto(pendingBookingDTO);
            return ResponseEntity.status(HttpStatus.OK).body(pendingBookingResponseForSingleDealerDto);
        } catch (BookingNotFoundException bookingNotFoundException) {
            PendingBookingResponseForSingleDealerDto pendingBookingResponseForSingleDealerDto = new PendingBookingResponseForSingleDealerDto("unsuccess");
            pendingBookingResponseForSingleDealerDto.setException(String.valueOf(bookingNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pendingBookingResponseForSingleDealerDto);
        }
    }

        @GetMapping("/getAllpendingBookings")
        public ResponseEntity<ResponseAllPendingBookingDto> getAllpendingBookings(@RequestParam int pageNo) {
            try {
                List<PremiunCarPendingBookingDto> listOfPendingBooking = premiumCarPendingBookingService.getAllPendingBookingWithPage(pageNo);
                ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("success");
                responseAllPendingBookingDto.setPremiunCarPendingBookingDtos(listOfPendingBooking);
                return ResponseEntity.status(HttpStatus.OK).body(responseAllPendingBookingDto);

            } catch (CarNotFoundException carNotFoundException) {
                ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
                responseAllPendingBookingDto.setException("Pending Booking not faund");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
            } catch (PageNotFoundException pageNotFoundException) {
                ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
                responseAllPendingBookingDto.setException("page not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
            }

        }

    @GetMapping("/getByUserId")
    public ResponseEntity<?> getByUserId(@RequestParam int pageNo,@RequestParam int userId) {
        try {
            List<PremiunCarPendingBookingDto> listOfPendingBooking = premiumCarPendingBookingService.getAllPendingBookingByUserId(pageNo,userId);

            AllPendingBookingResponseDTO allPendingBookingResponseDTO = new AllPendingBookingResponseDTO("success");
            allPendingBookingResponseDTO.setPremiunCarPendingBookingDtos(listOfPendingBooking);

            return ResponseEntity.status(HttpStatus.OK).body(allPendingBookingResponseDTO);
        } catch (BookingNotFoundException bookingNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(bookingNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(pageNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        }catch (UserNotFoundExceptions userNotFoundExceptions){
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(userNotFoundExceptions));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);

        }
    }

    @GetMapping("/getPendingBookingDetailsByDealerID")
    public ResponseEntity<?> getBookingDetailsByDealerId(@RequestParam int pageNo,@RequestParam int dealerId) {
        try {
            List<PremiunCarPendingBookingDto> listOfPendingBooking = premiumCarPendingBookingService.getPendingBookingsByDealerId(pageNo,dealerId);
            AllPendingBookingResponseDTO allPendingBookingResponseDTO = new AllPendingBookingResponseDTO("success");
            allPendingBookingResponseDTO.setPremiunCarPendingBookingDtos(listOfPendingBooking);

            return ResponseEntity.status(HttpStatus.OK).body(allPendingBookingResponseDTO);
        } catch (BookingNotFoundException bookingNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(bookingNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(pageNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        }catch (DealerNotFoundException dealerNotFoundException){
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(dealerNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);

        }
    }

    @GetMapping("/getPendingBookingDetailsByCarID")
    public ResponseEntity<?> getBookingDetailsByCarId(@RequestParam int pageNo,@RequestParam int CarId) {
        try {

            List<PremiunCarPendingBookingDto> listOfPendingBooking = premiumCarPendingBookingService.getPendingBookingsByCarId(pageNo,CarId);
            AllPendingBookingResponseDTO allPendingBookingResponseDTO = new AllPendingBookingResponseDTO("success");
            allPendingBookingResponseDTO.setPremiunCarPendingBookingDtos(listOfPendingBooking);

            return ResponseEntity.status(HttpStatus.OK).body(allPendingBookingResponseDTO);
        } catch (BookingNotFoundException bookingNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(bookingNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            responseAllPendingBookingDto.setException(String.valueOf(pageNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);
        }catch (CarNotFoundException carNotFoundException){
            ResponseAllPendingBookingDto responseAllPendingBookingDto = new ResponseAllPendingBookingDto("unsuccess");
            System.err.println(carNotFoundException);
            responseAllPendingBookingDto.setException(carNotFoundException+" : car not found ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllPendingBookingDto);

        }
    }

    @PatchMapping("/bookingStatus")
    public ResponseEntity<?> statusUpdate(@RequestBody PremiunCarPendingBookingDto pendingBookingDTO) {
        try {
            premiumCarPendingBookingService.statusUpdate(pendingBookingDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Successful");
        } catch (BookingNotFound | CarNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unsuccessful");

        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBooking(@RequestParam Long id) {
        try {

            premiumCarPendingBookingService.deleteBooking(id);
            DeleteResponseDto dto = new DeleteResponseDto("success");

            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (BookingNotFound e) {
            DeleteResponseDto dto = new DeleteResponseDto("unsuccess");
            dto.setException(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
        }
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestCarBooking(@RequestBody PremiunCarPendingBookingDto pendingBookingDTO) {
        try{

            PremiumCarPendingBookingRequestDto pendingBooking = premiumCarPendingBookingService.savePendingBooking(pendingBookingDTO);
            ResponsePendingBookingRequestDto responsePendingBookingRequestDto = new ResponsePendingBookingRequestDto("success");
            responsePendingBookingRequestDto.setPremiumCarPendingBookingRequestDto(pendingBooking);
            return ResponseEntity.status(HttpStatus.OK).body(pendingBooking);

        }catch (CarNotFoundException carNotFoundException){
            ResponsePendingBookingRequestDto responsePendingBookingRequestDto = new ResponsePendingBookingRequestDto("unsuccess");
            responsePendingBookingRequestDto.setException(String.valueOf(carNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePendingBookingRequestDto);

        }catch (UserNotFoundExceptions userNotFoundExceptions){
            ResponsePendingBookingRequestDto responsePendingBookingRequestDto = new ResponsePendingBookingRequestDto("unsuccess");
            responsePendingBookingRequestDto.setException(String.valueOf(userNotFoundExceptions));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePendingBookingRequestDto);

        }catch (DealerNotFoundException dealerNotFoundException){
            ResponsePendingBookingRequestDto responsePendingBookingRequestDto = new ResponsePendingBookingRequestDto("unsuccess");
            responsePendingBookingRequestDto.setException(String.valueOf(dealerNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePendingBookingRequestDto);

        }
    }

 }





