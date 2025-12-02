//package com.spring.jwt.Bidding.Controller;
//
//
//import com.spring.jwt.Bidding.DTO.SmsDto;
//import com.spring.jwt.Bidding.Interface.SmsService;
//import com.spring.jwt.entity.SmsEntity;
//import com.spring.jwt.utils.OtpUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/sms")
//public class SmsController {
//
//    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);
//
//    @Autowired
//    private SmsService smsService;
//
//    @PostMapping("/send")
//    public String sendMessage(@RequestBody SmsDto smsDto) {
//        logger.info("Program Started....");
//
//        Long mobileNo = smsDto.getMobNumber();
//        if (mobileNo == null) {
//            return "Mobile number is required.";
//        } else if (String.valueOf(mobileNo).length() != 10) {
//            return "Invalid mobile number. Please provide a 10-digit mobile number.";
//        }
//
//        if (!smsService.canResendOtp(mobileNo)) {
//            return "Please wait 3 minutes before requesting a new OTP.";
//        }
//
//        smsService.removePreviousOtp(mobileNo);
//
//        String otp = OtpUtil.generateOtp(6);
//        logger.info("Generated OTP: {}", otp);
//
//        String apiKey = "QYX1V75W9cI3fhegGSonlzrktEBOjKZb4CuvpxPdiN68JUFLDM6EBkZzrWM3FdHTaLo8epOJ7vRQDqnV";
//        String number = String.valueOf(mobileNo);
//
//        smsService.sendSms(otp, number, apiKey);
//
//        SmsEntity smsEntity = new SmsEntity();
//        smsEntity.setMobNumber(mobileNo);
//        smsEntity.setOtp(otp);
//        smsEntity.setStatus("Pending");
//        smsService.saveOtp(smsEntity);
//
//        return "OTP sent successfully";
//    }
//
//
//    @PostMapping("/verify")
//    public String verifyOtp(@RequestBody SmsDto smsDto) {
//        boolean isValid = smsService.verifyOtp(smsDto);
//        return isValid ? "OTP Verified Successfully" : "Invalid OTP. Please enter the valid OTP.";
//    }
//}