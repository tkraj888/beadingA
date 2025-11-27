package  com.spring.jwt.enquiry;


import java.util.List;

public interface EnquiryService {


    EnquiryDTO createEnquiry(EnquiryDTO enquiryDTO);

    Enquiry getById(Integer id);

    List<Enquiry> getAll();

    Enquiry updateById(Integer id, EnquiryDTO enquiryDTO);

    void deleteById(Integer id);
}
