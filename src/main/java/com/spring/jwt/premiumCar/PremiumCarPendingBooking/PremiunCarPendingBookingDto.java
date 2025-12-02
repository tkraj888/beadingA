package com.spring.jwt.premiumCar.PremiumCarPendingBooking;
import com.spring.jwt.dto.BookingDtos.DealerDetails;
import com.spring.jwt.entity.PendingBooking;
import com.spring.jwt.entity.Status;
import com.spring.jwt.premiumCar.PremiumCar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class PremiunCarPendingBookingDto {

    private Long premiumCarPendingBookingId;
    private LocalDate date;
    private int price;
    private Integer dealerId;
    private Integer userId;
    private Status status;
    private int askingPrice;
    private Integer premiumCarId;
    private String username;
    private String mobileNumber;


    public PremiunCarPendingBookingDto() {

    }


    public PremiunCarPendingBookingDto(PremiumCarPendingBooking pendingBooking) {
        this.date = pendingBooking.getDate();
        this.price = pendingBooking.getPrice();
        this.askingPrice = pendingBooking.getAskingPrice();
        this.status = pendingBooking.getStatus();
        //  this.carId = pendingBooking.getCarCar().getId();
        this.dealerId = pendingBooking.getDealerId();
        this.userId = pendingBooking.getUserId();


    }


}
