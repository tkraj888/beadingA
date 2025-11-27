package com.spring.jwt.service;

import com.spring.jwt.Interfaces.BiddingTimerService;
import com.spring.jwt.dto.BiddingTimerRequestDTO;
import com.spring.jwt.entity.BeadingCAR;
import com.spring.jwt.entity.BiddingTimerRequest;
import com.spring.jwt.entity.Role;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BeadingCarNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.repository.BeadingCarRepo;
import com.spring.jwt.repository.BiddingTImerRepo;
import com.spring.jwt.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class BiddingTimerServiceImpl implements BiddingTimerService {

    private final BiddingTImerRepo biddingTImerRepo;

    private final ModelMapper modelMapper;

    private final TemplateEngine templateEngine;

    private final JavaMailSender javaMailSender;

    private final UserRepository userRepository;

    private final BeadingCarRepo beadingCarRepo;

    private final Logger logger = LoggerFactory.getLogger(BiddingTimerServiceImpl.class);

    @Override
    public BiddingTimerRequestDTO startTimer(BiddingTimerRequestDTO biddingTimerRequest) {
        User byUserId = userRepository.findByUserId(biddingTimerRequest.getUserId());

        Optional<BeadingCAR> byId = beadingCarRepo.findById(biddingTimerRequest.getBeadingCarId());
        if (byUserId == null) {
            throw new UserNotFoundExceptions("User not found");
        }

        Set<Role> roles = byUserId.getRoles();
        boolean isSalesPerson = roles.stream().anyMatch(role -> "SALESPERSON".equals(role.getName()) || "ADMIN".equals(role.getName()));
        if (!isSalesPerson) {
            throw new RuntimeException("You're not authorized to perform this action");
        }
        if (byId.isEmpty()) {
            throw new RuntimeException("Car Not Found in our Database");
        }

        BeadingCAR beadingCAR = byId.get();
        String carStatus = beadingCAR.getCarStatus();
        if (!"ACTIVE".equals(carStatus)) {
            throw new RuntimeException("Car is not Verified by SalesInspector, it can't be bid on.");
        }

        LocalDateTime utcEndTime = biddingTimerRequest.getEndTime();
        ZonedDateTime zonedEndTime = utcEndTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        LocalDateTime kolkataEndTime = zonedEndTime.toLocalDateTime();

        biddingTimerRequest.setEndTime(kolkataEndTime);

        BiddingTimerRequest biddingTimerRequestEntity = convertToEntity(biddingTimerRequest);
        biddingTimerRequestEntity.setStatus("PENDING");
        BiddingTimerRequest savedRequest = biddingTImerRepo.save(biddingTimerRequestEntity);

        return convertToDto(savedRequest);
    }

    @Override
    public BiddingTimerRequestDTO updateBiddingTime(BiddingTimerRequestDTO updateBiddingTimeRequest) {
        BiddingTimerRequest biddingTimerRequest = biddingTImerRepo.findById(updateBiddingTimeRequest.getBiddingTimerId())
                .orElseThrow(() -> new BeadingCarNotFoundException("BiddingTimerRequest not found"));

        biddingTimerRequest.setEndTime(updateBiddingTimeRequest.getEndTime());

        biddingTImerRepo.save(biddingTimerRequest);

        return modelMapper.map(biddingTimerRequest, BiddingTimerRequestDTO.class);
    }

    @Override
    public BiddingTimerRequestDTO getCarByTimerId(Integer biddingTimerId) {
        BiddingTimerRequest biddingTimer= biddingTImerRepo.findById(biddingTimerId)
                .orElseThrow(() -> new BeadingCarNotFoundException("BiddingTimerRequest not found"));
        return convertToDto(biddingTimer);

    }


//    @Override
//    public void sendNotification(String recipient, String message) {
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(recipient);
//        mailMessage.setSubject("Bidding Timer Notification");
//        mailMessage.setText(message);
//        javaMailSender.send(mailMessage);
//    }


    public void sendBulkEmails(List<String> recipients, String message) {
        try {
            int batchSize = 50;
            MimeMessage[] messages = new MimeMessage[batchSize];
            int messageIndex = 0;

            for (String recipient : recipients) {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setTo(recipient);
                helper.setSubject("Bidding Timer Notification");

                Context context = new Context(Locale.ENGLISH);

                String htmlContent = templateEngine.process("BiddingNotification", context);
                helper.setText(htmlContent, true);

                // Add logo image
//                ClassPathResource logoImage = new ClassPathResource("/static/images/logo.png");
//                helper.addInline("logoImage", logoImage);

                messages[messageIndex++] = mimeMessage;

                if (messageIndex == batchSize) {
                    javaMailSender.send(messages);
                    messageIndex = 0;
                }
            }

            if (messageIndex > 0) {
                MimeMessage[] lastMessages = new MimeMessage[messageIndex];
                System.arraycopy(messages, 0, lastMessages, 0, messageIndex);
                javaMailSender.send(lastMessages);
            }
        } catch (Exception e) {
            logger.error("Failed to send bulk emails.", e);
        }
    }

    public BiddingTimerRequest convertToEntity(BiddingTimerRequestDTO biddingTimerRequestDTO){
        BiddingTimerRequest biddingtime = modelMapper.map(biddingTimerRequestDTO, BiddingTimerRequest.class);
        return biddingtime;
    }

    public BiddingTimerRequestDTO convertToDto (BiddingTimerRequest biddingTimerRequest) {
        BiddingTimerRequestDTO biddingtimeDto = modelMapper.map(biddingTimerRequest, BiddingTimerRequestDTO.class);
        return biddingtimeDto;
    }


}
