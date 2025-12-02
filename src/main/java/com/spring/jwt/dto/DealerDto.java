package com.spring.jwt.dto;

import com.spring.jwt.entity.Dealer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealerDto {
    public String address;
    public int document;
    public String area;
    public String city;
    public String firstName;
    public String lastName;
    public String mobileNo;
    public String shopName;
    public String email;
    public String password;
    private Integer dealer_id;
    private Integer userId;
    private Boolean status;
    public Integer salesPersonId;
    private int totalCarCount;
    private int premiumCarCount;
    public DealerDto() {
    }

    public DealerDto(Dealer dealer, int totalCarCount, int premiumCarCount) {
        this.address = dealer.getAddress();
        this.status=dealer.getStatus();
        this.area = dealer.getArea();
        this.city =dealer.getCity();
        this.firstName = dealer.getFirstname();
        this.lastName =dealer.getLastName();
        this.mobileNo = dealer.getMobileNo();
        this.shopName = dealer.getShopName();
        this.email = dealer.getEmail();
        this.salesPersonId= dealer.getSalesPersonId();
        this.dealer_id = dealer.getId();
        this.userId = dealer.getUser().getId();
        this.totalCarCount = totalCarCount;
        this.premiumCarCount=premiumCarCount;

    }

    public DealerDto(int dealerId) {
        this.dealer_id = dealerId;
    }
    public DealerDto(Dealer dealer) {
        this(dealer, 0,0);
    }
}
