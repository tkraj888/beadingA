package com.spring.jwt.service;

import com.spring.jwt.Interfaces.UserService;
import com.spring.jwt.dto.PasswordChange;
import com.spring.jwt.dto.RegisterDto;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.dto.UserProfileDto;
import com.spring.jwt.entity.*;
import com.spring.jwt.exception.*;
import com.spring.jwt.repository.RoleRepository;
import com.spring.jwt.repository.UserProfileRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.utils.BaseResponseDTO;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Transactional
    public BaseResponseDTO registerAccount(RegisterDto registerDto) {

        BaseResponseDTO response = new BaseResponseDTO();

        validateAccount(registerDto);

        User user = insertUser(registerDto);

        try {
            userRepository.save(user);
            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Account Created !!!!");
        } catch (UserAlreadyExistException e) {
            response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            response.setMessage("User already exist !!");
        } catch (BaseException e) {
            response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            response.setMessage("Invalid role !!");
        }
        return response;
    }


    private User insertUser(RegisterDto registerDto) {
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setMobileNo(registerDto.getMobileNo());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(registerDto.getRoles());
        roles.add(role);
        user.setRoles(roles);

        if (role.getName().equals("USER")) {
            Userprofile profile = new Userprofile();
            profile.setAddress(registerDto.getAddress());
            profile.setCity(registerDto.getCity());
            profile.setFirstName(registerDto.getFirstName());
            profile.setLastName(registerDto.getLastName());

            user.setProfile(profile);
            profile.setUser(user);
        } else if (role.getName().equals("DEALER")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getAuthorities().stream().anyMatch(authority ->
                   authority.getAuthority().equals("ADMIN") ||
                    authority.getAuthority().equals("SALESPERSON"))){
                Dealer dealer = new Dealer();
                dealer.setAddress(registerDto.getAddress());
                dealer.setArea(registerDto.getArea());
                dealer.setCity(registerDto.getCity());
                dealer.setStatus(false);
                dealer.setFirstname(registerDto.getFirstName());
                dealer.setLastName(registerDto.getLastName());
                dealer.setMobileNo(registerDto.getMobileNo());
                dealer.setShopName(registerDto.getShopName());
                dealer.setEmail(registerDto.getEmail());
                dealer.setSalesPersonId(registerDto.getSalesPersonId());
                user.setDealer(dealer);

                dealer.setUser(user);
            }
            else {
                throw new UnauthorizedException("User do not have the authority of ADMIN or SALESPERSON to create this account.");
            }

        } else if (role.getName().equals("INSPECTOR")) {
            InspectorProfile inspectorProfile = new InspectorProfile();
            inspectorProfile.setAddress(registerDto.getAddress());
            inspectorProfile.setCity(registerDto.getCity());
            inspectorProfile.setFirstName(registerDto.getFirstName());
            inspectorProfile.setLastName(registerDto.getLastName());
            inspectorProfile.setStatus(false);
            user.setInspectorProfile(inspectorProfile);
            inspectorProfile.setUser(user);

        } else if (role.getName().equals("SALESPERSON")) {
            SalesPerson salesPerson = new SalesPerson();
            salesPerson.setFirstName(registerDto.getFirstName());
            salesPerson.setLastName(registerDto.getLastName());
            salesPerson.setAddress(registerDto.getAddress());
            salesPerson.setArea(registerDto.getArea());
            salesPerson.setCity(registerDto.getCity());
            salesPerson.setJoiningdate(registerDto.getJoiningdate());
            salesPerson.setDocumentId(registerDto.getDocumentId());
            salesPerson.setStatus(false);
            user.setSalesPerson(salesPerson);
            salesPerson.setUser(user);
        }
        return user;
    }

    private void validateAccount(RegisterDto registerDto) {
        if (ObjectUtils.isEmpty(registerDto)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Data must not be empty");
        }


        User user = userRepository.findByEmail(registerDto.getEmail());
        if (!ObjectUtils.isEmpty(user)) {
            throw new UserAlreadyExistException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Username already exists !!");
        }
        User mobileNo = userRepository.findByMobileNo(registerDto.getMobileNo());
        if (!ObjectUtils.isEmpty(mobileNo)){
            throw new UserAlreadyExistException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Mobile number already exists !!");
        }

        List<String> roles = roleRepository.findAll().stream().map(Role::getName).toList();
        if (!roles.contains(registerDto.getRoles())) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid role");
        }
    }

    @Override
    public BaseResponseDTO changePassword(int id, PasswordChange passwordChange) {
        BaseResponseDTO response = new BaseResponseDTO();

        Optional<Userprofile> userOptional = userProfileRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get().getUser();

            if (passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword())) {

                if (passwordChange.getNewPassword().equals(passwordChange.getConfirmPassword())) {

                    user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
                    userRepository.save(user);

                    response.setCode(String.valueOf(HttpStatus.OK.value()));
                    response.setMessage("Password changed");
                } else {
                    response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                    throw new InvalidPasswordException("New password and confirm password does not match");
                }
            } else {
                response.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
                throw new InvalidPasswordException("Invalid Password");
            }

        } else {

            throw new UserNotFoundExceptions("No user found");
        }
        return response;
    }

    @Override
    public List<UserProfileDto> getAllUsers(int pageNo) {

        //User user= new User();
        List<Userprofile> listOfUsers = userProfileRepository.findAll();

        if ((pageNo * 10) > listOfUsers.size() - 1) {
            throw new PageNotFoundException("Page not found");
        }


        if (listOfUsers.size() <= 0) {
            throw new UserNotFoundExceptions("User not found", HttpStatus.NOT_FOUND);
        }

        // System.out.println("List of users: " + listOfUsers.size());

        List<UserProfileDto> listOfUserDto = new ArrayList<>();

        int pageStart = pageNo * 25;
        int pageEnd = pageStart + 25;

        int diff = (listOfUsers.size()) - pageStart;

        for (int counter = pageStart, i = 1; counter < pageEnd; counter++, i++) {
            if (pageStart > listOfUsers.size()) {
                break;
            }
            Optional<User> users = userRepository.findById(listOfUsers.get(counter).getUser().getId());
            if (users.isEmpty()) {
                throw new UserNotFoundExceptions("User not found ");
            }
            // System.out.println("*");

            UserProfileDto userProfileDto = new UserProfileDto(listOfUsers.get(counter), users.get());

            listOfUserDto.add(userProfileDto);

            if (diff == i) {
                break;
            }
        }
        return listOfUserDto;
    }

    private UserProfileDto convertToDto(Userprofile userprofile, User user) {
        //User user = new User();
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(userprofile.getId());
        userProfileDto.setAddress(userprofile.getAddress());
        userProfileDto.setCity(userprofile.getCity());
        userProfileDto.setFirstName(userprofile.getFirstName());
        userProfileDto.setLastName(userprofile.getLastName());
        userProfileDto.setMobile_no(user.getMobileNo());
        userProfileDto.setEmail(user.getEmail());
        return userProfileDto;
    }

    @Override
    public UserProfileDto getUser(int id) {
        Optional<Userprofile> userOptional = userProfileRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundExceptions("User not found");
        }

        User user = userOptional.get().getUser();

        return convertToDto(userOptional.get(), user);
    }

    @Override
    public BaseResponseDTO editUser(UserProfileDto userProfileDto, int id) {

        BaseResponseDTO response = new BaseResponseDTO();

        Optional<Userprofile> optionalUser = userProfileRepository.findById(id);

        if (optionalUser.isPresent()) {
            Userprofile userProfile = optionalUser.get();

            User user = userProfile.getUser();

            if (userProfileDto.getFirstName() != null) {
                userProfile.setFirstName(userProfileDto.getFirstName());
            }
            if (userProfileDto.getLastName() != null) {
                userProfile.setLastName(userProfileDto.getLastName());
            }
            if (userProfileDto.getAddress() != null) {
                userProfile.setAddress(userProfileDto.getAddress());
            }
            if (userProfileDto.getCity() != null) {
                userProfile.setCity(userProfileDto.getCity());
            }
            if (user != null) {
                if (userProfileDto.getMobile_no() != null && !userProfileDto.getMobile_no().isEmpty()) {
                    if (!userProfileDto.getMobile_no().equals(user.getMobileNo())) {
                        boolean mobileExists = userRepository.existsByMobileNo(userProfileDto.getMobile_no());
                        if (mobileExists) {
                            throw new DuplicateRecordException("The Mobile Number you entered is already in use. Please try another one", HttpStatus.CONFLICT);
                        }
                        user.setMobileNo(userProfileDto.getMobile_no());
                    }
                }
            }

            if (userProfileDto.getEmail() != null && !userProfileDto.getEmail().isEmpty()) {
                if (!userProfileDto.getEmail().equals(user.getEmail())) {
                    boolean emailExists = userRepository.existsByEmail(userProfileDto.getEmail());
                    if (emailExists) {
                        throw new DuplicateRecordException("The email address you entered is already in use. Please try another one", HttpStatus.CONFLICT);
                    }
                    user.setEmail(userProfileDto.getEmail());
                }
            }

            userProfileRepository.save(userProfile);
            userRepository.save(user);

            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("User edited successfully");
        } else {
            response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
            response.setMessage("No user found");
        }

        return response;
    }


    @Override
    @Transactional
    public BaseResponseDTO removeUser(int id) {
        BaseResponseDTO response = new BaseResponseDTO();

        Optional<Userprofile> user = userProfileRepository.findById(id);

        if (user.isPresent()) {

            User users = user.get().getUser();

            userRepository.DeleteById(users.getId());

            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("User deleted");

        } else {
            response.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
            throw new UserNotFoundExceptions("No user found");
        }

        return response;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public ResponseDto forgotPass(String email, String resetPasswordLink, String domain) throws UserNotFoundExceptions {
        ResponseDto response = new ResponseDto();

        User user = userRepository.findByEmail(email);

        if (user != null) {

            String message = "Dear User this is the link to reset your password";

            String resetLink = resetPasswordLink;

            String subject = "Reset Password";

            String from = "b.aniket1414@gmail.com";

            String to = email;

            sendEmail(message, subject, to, from, resetLink, domain);

            response.setStatus(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Email sent");

        } else {
            response.setStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));
            response.setMessage("User not found");
            throw new UserNotFoundExceptions("User not found");
        }

        return response;
    }

    public void sendEmail(String message, String subject, String to, String from, String resetLink, String domain) {


        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        System.out.println(properties);

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("ss", "dd");
            }

        });

        String content = "To reset your password, click here: " + resetLink.replace("169.254.63.118:5173", domain);

        MimeMessage m = new MimeMessage(session);

        try {
            m.setFrom(from);

            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            m.setSubject(subject);

            m.setText(content);

            Transport.send(m);

        } catch (MessagingException e) {
            e.printStackTrace();

        }
    }

    public void updateResetPassword(String token, String email) throws UserNotFoundExceptions {

        User user = userRepository.findByEmail(email);

        if (user != null) {

            user.setResetPasswordToken(token);

            userRepository.save(user);
        } else {

            throw new UserNotFoundExceptions("could not find any user with this email");
        }
    }

    public ResponseDto updatePassword(String token, String newPassword) {
        ResponseDto response = new ResponseDto();

        User user = userRepository.findByResetPasswordToken(token);

        if (user == null) {
            response.setStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            response.setMessage("Invalid or expired token");
            throw new UserNotFoundExceptions("Invalid or expired token");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);

        response.setStatus(String.valueOf(HttpStatus.OK.value()));
        response.setMessage("Password reset successful");

        return response;
    }

    public boolean validateResetToken(String token) {
        return userRepository.findByResetPasswordToken(token) != null;
    }

    public boolean isSameAsOldPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new UserNotFoundExceptions("Invalid or expired token");
        }

        return passwordEncoder.matches(newPassword, user.getPassword());
    }

    public void sendEmailWithTemplate(String toEmail, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }

}