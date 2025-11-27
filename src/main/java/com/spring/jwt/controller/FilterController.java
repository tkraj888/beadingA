package com.spring.jwt.controller;

import com.spring.jwt.Interfaces.FilterService;
import com.spring.jwt.Interfaces.ICarRegister;
import com.spring.jwt.Interfaces.PendingBookingService;
import com.spring.jwt.Interfaces.UserService;
import com.spring.jwt.dto.*;
import com.spring.jwt.exception.CarNotFoundException;
import com.spring.jwt.exception.PageNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class FilterController {
    @Autowired
    private final FilterService filterService;

    @Autowired
    private UserService userService;

    @Autowired
    private ICarRegister iCarRegister;

    private PendingBookingService pendingBookingService;


    @GetMapping("/mainFilter")
    public ResponseEntity<ResponseAllCarDto> searchByFilter(
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String transmission,
            @RequestParam(required = false) String fuelType,
            @RequestParam(defaultValue = "normal") String carType) {

        Integer convertedYear = null;
        try {
            convertedYear = (year != null && !year.isEmpty()) ? Integer.valueOf(year) : null;
        } catch (NumberFormatException e) {
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccess");
            responseAllCarDto.setException("Invalid year format");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseAllCarDto);
        }

        FilterDto filterDto = new FilterDto(minPrice, maxPrice, area, brand, model, transmission, fuelType, convertedYear,carType);

        try {
            List<CarDto> listOfCar = filterService.searchByFilter(filterDto);
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("success");
            responseAllCarDto.setList(listOfCar);
            return ResponseEntity.status(HttpStatus.OK).body(responseAllCarDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccess");
            responseAllCarDto.setException("Page not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        } catch (Exception e) {
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccess");
            responseAllCarDto.setException("An error occurred while filtering cars");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseAllCarDto);
        }
    }

    @GetMapping("/mainFilterPage")
    public ResponseEntity<ResponseAllCarDto> searchByFilter(
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String transmission,
            @RequestParam(required = false) String fuelType,
            @RequestParam(defaultValue = "normal") String carType,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {

        Integer convertedYear = null;
        try {
            convertedYear = (year != null && !year.isEmpty()) ? Integer.valueOf(year) : null;
        } catch (NumberFormatException e) {
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccess");
            responseAllCarDto.setException("Invalid year format");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseAllCarDto);
        }

        FilterDto filterDto = new FilterDto(minPrice, maxPrice, area, brand, model, transmission, fuelType, convertedYear, carType);

        try {
            List<CarDto> listOfCar = filterService.searchByFilterPage(filterDto, pageNo, pageSize);
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("success");
            responseAllCarDto.setList(listOfCar);
            long totalCars = iCarRegister.countAllCars(); // fetch total cars count from service or repo
            responseAllCarDto.setTotalCars(totalCars);
            return ResponseEntity.status(HttpStatus.OK).body(responseAllCarDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccess");
            responseAllCarDto.setException("Page not found");
            long totalCars = iCarRegister.countAllCars(); // fetch total cars count from service or repo
            responseAllCarDto.setTotalCars(totalCars);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        } catch (Exception e) {
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccess");
            responseAllCarDto.setException("An error occurred while filtering cars");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseAllCarDto);
        }
    }

    @GetMapping("/top4Cars")
    public ResponseEntity<ResponseAllCarDto> getTop4Cars() {
        try {
            List<CarDto> topCars = filterService.getTop4Cars();
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("success");
            responseAllCarDto.setList(topCars);
            return ResponseEntity.status(HttpStatus.OK).body(responseAllCarDto);
        } catch (Exception e) {
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccess");
            responseAllCarDto.setException("An error occurred while fetching top 4 cars");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseAllCarDto);
        }
    }




    @GetMapping("/searchBarFilter")
    public ResponseEntity<?> searchBarFilter(@RequestParam String searchBarInput) {
        try {
            List<CarDto> listOfJob = filterService.searchBarFilter(searchBarInput);
            ResponseAllCarDto responseGetAllJobDto = new ResponseAllCarDto("success");
            responseGetAllJobDto.setList(listOfJob);
            return ResponseEntity.status(HttpStatus.OK).body(responseGetAllJobDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllCarDto responseGetAllJobDto = new ResponseAllCarDto("unsuccess");
            responseGetAllJobDto.setException(String.valueOf(pageNotFoundException));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseGetAllJobDto);
        }
    }


    /**
     * Retrieves a single car by its ID.
     *
     * @param carId The ID of the car to retrieve.
     * @return ResponseEntity containing the response for the request.
     *         If the car is found, the response will have a status of OK (200)
     *         and the car details will be included in the body.
     *         If the car is not found, the response will have a status of NOT_FOUND (404)
     *         and an error message will be included in the body.
     */
    @GetMapping("/getCar")
    public ResponseEntity<ResponseSingleCarDto> findByArea(@RequestParam int carId) {
        try {
            ResponseSingleCarDto responseSingleCarDto = new ResponseSingleCarDto("success");

            CarDto car = iCarRegister.findById(carId);

            responseSingleCarDto.setObject(car);
            return ResponseEntity.status(HttpStatus.OK).body(responseSingleCarDto);
        }catch (CarNotFoundException carNotFoundException){
            ResponseSingleCarDto responseSingleCarDto = new ResponseSingleCarDto("unsuccess");
            responseSingleCarDto.setException("car not found by car id");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseSingleCarDto);
        }

//        return ResponseEntity.ok(cars.get());*
    }
    @GetMapping("/getAllCars")
    public ResponseEntity<?> getAllCars(@RequestParam int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            List<CarDto> listOfCar = iCarRegister.getAllCarsWithPages(pageNo, pageSize);

            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("success");
            responseAllCarDto.setList(listOfCar);

            return ResponseEntity.status(HttpStatus.OK).body(responseAllCarDto);
        } catch (CarNotFoundException carNotFoundException) {
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccessful");
            responseAllCarDto.setException("Car not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        } catch (PageNotFoundException pageNotFoundException) {
            ResponseAllCarDto responseAllCarDto = new ResponseAllCarDto("unsuccessful");
            responseAllCarDto.setException("Page not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseAllCarDto);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDto> forgotPass(HttpServletRequest request) throws UserNotFoundExceptions {
        try {
            String email = request.getParameter("email");
            String token = RandomStringUtils.randomAlphabetic(40);
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1);

            userService.updateResetPassword(token, email);

            String resetPasswordLink = "http://localhost:5173/reset-password?token=" + token;

            String htmlContent = generateEmailTemplate(resetPasswordLink);

            userService.sendEmailWithTemplate(email, "Reset Password", htmlContent);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Successful", "Password reset email sent."));
        } catch (UserNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("Unsuccessful", "Invalid email. Please register."));
        }
    }

    private String generateEmailTemplate(String resetPasswordLink) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;'>" +
                "<div style='max-width: 600px; margin: 20px auto; padding: 20px; background-color: #ffffff; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>" +
                "<h2 style='color: #333333;'>Reset Your Password</h2>" +
                "<p>Dear user,</p>" +
                "<p>You have requested to reset your password. Please click the button below to reset your password:</p>" +
                "<a href='" + resetPasswordLink + "' style='display: inline-block; margin: 20px 0; padding: 10px 20px; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px;'>Reset My Password</a>" +
                "<p>If you did not request this, Please ignore this email.</p>" +
                "<p>Thank you,<br>CarTechIndia.com</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }


    @PostMapping("/update-password")
    public ResponseEntity<ResponseDto> resetPassword(@RequestBody ResetPassword resetPassword) throws UserNotFoundExceptions {
        try {
            String token = resetPassword.getToken();
            String newPassword = resetPassword.getPassword();
            String confirmPassword = resetPassword.getConfirmPassword();

            if (!newPassword.equals(confirmPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDto("Unsuccessful", "Passwords do not match"));
            }

            boolean isTokenValid = userService.validateResetToken(token);
            if (!isTokenValid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDto("Unsuccessful", "The reset link is invalid or has expired"));
            }

            boolean isSameAsOldPassword = userService.isSameAsOldPassword(token, newPassword);
            if (isSameAsOldPassword) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDto("Unsuccessful", "The new password cannot be the same as the old password"));
            }

            ResponseDto response = userService.updatePassword(token, newPassword);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Successful", response.getMessage()));

        } catch (UserNotFoundExceptions e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("Unsuccessful", "Something went wrong"));
        }
    }


    //    @GetMapping("/reset-password")
//    public ResponseEntity<String> resetPasswordPage(@RequestParam(name = "token") String token) {
//        try {
//            ClassPathResource resource = new ClassPathResource("templates/reset-password.html");
//            String htmlContent = new String(Files.readAllBytes(Paths.get(resource.getURI())), StandardCharsets.UTF_8);
//            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlContent);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading HTML file");
//        }
//    }
    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocomplete(@RequestParam String query) {
        List<String> suggestions = iCarRegister.getAutocompleteSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }

}