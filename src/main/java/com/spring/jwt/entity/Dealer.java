package com.spring.jwt.entity;


import com.spring.jwt.dto.DealerDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "dealer")
public class Dealer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Dealer_id")
    private Integer id;

    @NotBlank (message = "Address cannot be blank")
    @Column(name = "address")
    private String address;

    @Column(name = "area", nullable = false, length = 45)
    private String area;

    @Column(name = "city", nullable = false, length = 45)
    private String city;

    @Column(name = "firstname", length = 45)
    private String firstname;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "salesPersonId", length = 10)
    private Integer salesPersonId;

    @NotBlank(message = "Mobile number cannot be blank")
    @Pattern(regexp = "[0-9]{10}", message = "Invalid mobile number format")
    @Column(name = "mobile_no", nullable = false, length = 45)
    private String mobileNo;

    @Column(name = "shop_name", nullable = false, length = 250)
    private String shopName;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    private long dealerDocumentPhoto;

    private Boolean status;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "user_user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

//    @OneToMany(mappedBy = "dealerVendor")
//    private Set<Car> cars = new LinkedHashSet<>();


    public Dealer(DealerDto dealerDto) {
        this.address = dealerDto.address;
        this.status=dealerDto.getStatus();
        this.area = dealerDto.area;
        this.city =dealerDto.city;
        this.firstname = dealerDto.firstName;
        this.lastName = dealerDto.lastName;
        this.mobileNo = dealerDto.mobileNo;
        this.shopName = dealerDto.shopName;
        this.email = dealerDto.email;
        this.salesPersonId = dealerDto.salesPersonId;
    }

}