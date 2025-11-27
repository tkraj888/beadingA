package com.spring.jwt.service;

import com.spring.jwt.Interfaces.DealerService;
import com.spring.jwt.dto.ChangePasswordDto;
import com.spring.jwt.dto.DealerDto;
import com.spring.jwt.dto.RegisterDto;
import com.spring.jwt.entity.Car;
import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.Status;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.CarRepo;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.RoleRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class DealerServiceImpl implements DealerService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private CarRepo carRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public BaseResponseDTO updateDealer(Integer userId, RegisterDto registerDto) {
        BaseResponseDTO response = new BaseResponseDTO();
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getRoles().stream().anyMatch(role -> role.getName().equals("DEALER"))) {
                Dealer dealer = user.getDealer();
                if (dealer != null) {
                    updateDealerDetails(dealer, registerDto);
                    dealerRepository.save(dealer);
                    response.setCode(String.valueOf(HttpStatus.OK.value()));
                    response.setMessage("Dealer details updated successfully");
                    return response;

                } else {
                    throw new DealerDeatilsNotFoundException("Dealer details not found");
                }
            } else {
                throw new UserNotDealerException("User is not a dealer");
            }
        } else {
            throw new UserNotFoundExceptions("User not found");
        }
    }

    private void updateDealerDetails(Dealer dealer, RegisterDto registerDto) {

        if (registerDto.getAddress() != null) {
            dealer.setAddress(registerDto.getAddress());
        }
        if (registerDto.getArea() != null) {
            dealer.setArea(registerDto.getArea());
        }
        if (registerDto.getCity() != null) {
            dealer.setCity(registerDto.getCity());
        }
        if (registerDto.getFirstName() != null) {
            dealer.setFirstname(registerDto.getFirstName());
        }
        if (registerDto.getLastName() != null) {
            dealer.setLastName(registerDto.getLastName());
        }
        if (registerDto.getShopName() != null) {
            dealer.setShopName(registerDto.getShopName());
        }

            User user = dealer.getUser();
            if (user != null) {
                if (registerDto.getMobileNo() != null && !registerDto.getMobileNo().isEmpty()) {
                    if (!registerDto.getMobileNo().equals(user.getMobileNo())) {

                        boolean mobileExists = userRepository.existsByMobileNo(registerDto.getMobileNo());
                        if (mobileExists) {
                            throw new DuplicateRecordException("The Mobile Number you entered is already in use. Please try another one", HttpStatus.CONFLICT);
                        }
                        boolean existsmobile = dealerRepository.existsByMobileNo(registerDto.getMobileNo());
                        if (existsmobile){
                            throw new DuplicateRecordException("The Mobile Number you entered is already in use. Please try another one", HttpStatus.CONFLICT);

                        }
                        dealer.setMobileNo(registerDto.getMobileNo());
                        user.setMobileNo(registerDto.getMobileNo());
                    }
                }

                if (registerDto.getEmail() != null && !registerDto.getEmail().isEmpty()) {
                    if (!registerDto.getEmail().equals(user.getEmail())) {

                        boolean emailExists = userRepository.existsByEmail(registerDto.getEmail());

                        boolean emails = dealerRepository.existsByEmail(registerDto.getEmail());

                        if (emails){
                            throw new DuplicateRecordException("The email address you entered is already in use. Please try another one", HttpStatus.CONFLICT);
                        }
                        if (emailExists) {
                            throw new DuplicateRecordException("The email address you entered is already in use. Please try another one", HttpStatus.CONFLICT);
                        }
                        user.setEmail(registerDto.getEmail());

                        dealer.setEmail(registerDto.getEmail());
                    }

                }
            }
    }

    @Override
    public List<DealerDto> getAllDealers(int pageNo) {
        List<Dealer> dealers = dealerRepository.findAllByOrderByIdDesc();
        if (dealers.isEmpty()) {
            throw new DealerNotFoundException("Dealer not found");
        }
        if ((pageNo * 10) > dealers.size() - 1) {
            throw new PageNotFoundException("Page not found");
        }

        List<DealerDto> listOfDealerDto = new ArrayList<>();

        int pageStart = pageNo * 10;
        int pageEnd = pageStart + 10;
        int diff = (dealers.size()) - pageStart;

        for (int counter = pageStart, i = 1; counter < pageEnd; counter++, i++) {
            if (pageStart > dealers.size()) {
                break;
            }

            // Fetch the car count for the current dealer
            int carCount = carRepo.countByDealerId(dealers.get(counter).getId());

            int premiumCarCount= carRepo.countByDealerIdAndCarType(dealers.get(counter).getId());

            DealerDto dealerDto = new DealerDto(dealers.get(counter), carCount,premiumCarCount);
            listOfDealerDto.add(dealerDto);

            if (diff == i) {
                break;
            }
        }
        return listOfDealerDto;
    }


    @Override
    public DealerDto getDealerById(Integer dealerId) {
        Optional<Dealer> dealerOptional = dealerRepository.findById(dealerId);
        if(dealerOptional.isEmpty())
        {
            throw new DealerNotFoundException("dealer not found by id");
        }
        return dealerOptional.map(this::convertToDto).orElse(null);
    }

    @Override
    public DealerDto getDealerByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Dealer dealer = user.getDealer();

            if (dealer != null) {
                return convertToDto(dealer);
            } else {
                throw new DealerDeatilsNotFoundException("Dealer details not found for user id: " + userId);
            }
        } else {
            throw new UserNotFoundExceptions("User not found for id: " + userId);
        }
    }

    private DealerDto convertToDto(Dealer dealer) {
        DealerDto dealerDto = new DealerDto();
        dealerDto.setDealer_id(dealer.getId());
        dealerDto.setAddress(dealer.getAddress());
        dealerDto.setArea(dealer.getArea());
        dealerDto.setCity(dealer.getCity());
        dealerDto.setFirstName(dealer.getFirstname());
        dealerDto.setStatus(dealer.getStatus());
        dealerDto.setLastName(dealer.getLastName());
        dealerDto.setMobileNo(dealer.getMobileNo());
        dealerDto.setShopName(dealer.getShopName());
        dealerDto.setEmail(dealer.getEmail());
        return dealerDto;
    }

    @Override
    @Transactional
    public BaseResponseDTO deleteDealer(Integer dealerId) {
        BaseResponseDTO response = new BaseResponseDTO();

        Optional<Dealer> dealerOptional = dealerRepository.findById(dealerId);
        if (dealerOptional.isPresent()) {
            Dealer dealer = dealerOptional.get();
            User user = dealer.getUser();

            // Delete user roles associated with the dealer
            user.getRoles().clear();
            userRepository.save(user);

            // Delete the dealer
            dealerRepository.deleteById(dealerId);
            userRepository.delete(user);

            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Dealer deleted successfully");
            return response;
        } else {

            throw new DealerNotFoundException("Dealer not found");
        }

    }

    @Override
    public BaseResponseDTO changePassword(Integer dealerId, ChangePasswordDto changePasswordDto) {
        BaseResponseDTO response = new BaseResponseDTO();

        Dealer dealer = userRepository.findDealerById(dealerId);

        if (dealer != null) {
            User user = dealer.getUser();

            if (user != null && user.getRoles().stream().anyMatch(role -> role.getName().equals("DEALER"))) {

                if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
                    throw new InvalidOldPasswordException("Invalid old password");
                }

                if (changePasswordDto.getOldPassword().equals(changePasswordDto.getNewPassword())) {
                    throw new OldNewPasswordMustBeDifferentException("New password must be different from the old password");
                }

                if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())) {
                    throw new NewAndOldPasswordDoseNotMatchException("New password and confirm password do not match");
                }

                user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                userRepository.save(user);
                response.setCode(String.valueOf(HttpStatus.OK.value()));
                response.setMessage("Password changed successfully");
            } else {
                throw new UserNotDealerException("User is not a dealer");
            }
        } else {
            throw new DealerNotFoundException("Dealer not found");
        }

        return response;
    }

    @Override
    public int getDealerIdByEmail(String email) {
        Optional<Dealer> dealer = dealerRepository.findByEmail(email);
        if(dealer.isEmpty()) {
            throw new EmailNotExistException("Email Not Exist Exception");
        }
        return dealer.get().getId();

    }
    @Override
    public void updateStatus(Integer dealerId, Boolean status) {
        Optional<Dealer> dealer=dealerRepository.findById(dealerId);

        System.err.println(dealer.isPresent());
//        System.out.println(dealer.get().toString());
        if(dealer.isEmpty()){throw new DealerNotFoundException("dealer not found by id");}
        dealer.get().setStatus(status);
//        System.out.println(dealer.get().toString());
        dealerRepository.save(dealer.get());
        Optional<List<Car>> carList = carRepo.getByDealerId(dealerId);
        if(carList.isEmpty()){
            return;
        }
        else if(status == false){
            for (int counter=0;counter<carList.get().size();counter++){
                if(carList.get().get(counter).getCarStatus().equals(Status.ACTIVE) || carList.get().get(counter).getCarStatus().equals(Status.PENDING))
                carList.get().get(counter).setCarStatus(Status.DEACTIVATE);

            }
            carRepo.saveAll(carList.get());
        }else if(status == true){

            for (int counter=0;counter<carList.get().size();counter++){
                if(carList.get().get(counter).getCarStatus().equals(Status.DEACTIVATE) || carList.get().get(counter).getCarStatus().equals(Status.PENDING))
                    carList.get().get(counter).setCarStatus(Status.ACTIVE);

            }
            carRepo.saveAll(carList.get());
        }


    }

    @Override
    public List<DealerDto> getAllDealer() {
        List<Dealer> dealers = dealerRepository.findAll();

        dealers.sort((dealer1, dealer2) -> dealer2.getId().compareTo(dealer1.getId()));

        if (dealers.isEmpty()) {
            throw new DealerNotFoundException("Dealer not found");
        }

        List<DealerDto> listOfDealerDto = new ArrayList<>();

        for (Dealer dealer : dealers) {
            DealerDto dealerDto = new DealerDto(dealer);
            listOfDealerDto.add(dealerDto);
        }

        return listOfDealerDto;
    }

    @Override
    public List<DealerDto> getAllDealersBySalesPersonId(Integer salesPersonID) {
        List<Dealer> bySalesPersonId = dealerRepository.findBySalesPersonIdOrderByIdDesc(salesPersonID);
        List<DealerDto> listOfDealerDto = new ArrayList<>();
        for (Dealer sales : bySalesPersonId) {
            DealerDto bySales = new DealerDto(sales);
            listOfDealerDto.add(bySales);
        }

        return listOfDealerDto;
    }

}