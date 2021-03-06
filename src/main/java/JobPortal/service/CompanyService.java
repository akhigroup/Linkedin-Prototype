package JobPortal.service;

import JobPortal.Dao.CompanyDao;
import JobPortal.model.JobOpening;
import JobPortal.Dao.InterviewDao;
import JobPortal.model.Interview;

import JobPortal.model.Company;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import javax.transaction.Transactional;

import com.google.gson.Gson;
/**
 * Created by ipshamohanty on 5/1/17.
 */

@Service
@Transactional
public class CompanyService {
    
    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private InterviewDao interviewDao;

    public CompanyService(){

    }

    public CompanyService(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    public Company getCompany(int companyId){

        Company company = null;
        return companyDao.findByCompanyId(companyId);
    }

    public Company getCompany(String email){

        Company company = null;
        return companyDao.findByCompanyEmail(email);
    }


    public Company createCompany(String companyname, String website, String location, 
                     String logoImageUrl, String description, String password, String companyemail) 
    {


        ModelAndView modelAndView;
        ObjectMapper mapper = new ObjectMapper();
        ModelMap model = new ModelMap();

        Company company;

        try {
            company = new Company(companyname, website, location, logoImageUrl, description, 
                        password, companyemail);
            Company newCompany = companyDao.save(company);
            //emailService.sendEmail();
            return newCompany;
        } catch(Exception ex) {
           //TODO : Handle exception  
            return null;
        }
    }

   public Company updateCompany(String companyName, String website, String location, 
                                    String logoImageUrl, String description, String password, int companyId)
   {
        Company company = companyDao.findByCompanyId(companyId);
        if (company == null) {
            //todo : company not found 
            //raise error
        } 
        
       try {
           String email = company.getCompanyemail();
            company = new Company(companyName, website, location, logoImageUrl, description, password, email);
            company.setCompanyId(companyId);
            company = companyDao.save(company);
            return company;
        } catch(Exception ex) {
           //TODO : Handle exception  
            return null;
        }
   }
   
   public String getJobopeningInCompany(Company company, JobOpening jobopening)
   {
        LinkedHashMap<Object, Object> map = new LinkedHashMap<Object, Object> ();
        map.put("company", company);
        map.put("jobopenings", jobopening);
        Gson gson = new Gson();
        String jobOpeningJson = gson.toJson(map, LinkedHashMap.class);
        return jobOpeningJson;
   }

   public String getJobOpenings(Company company, List<JobOpening> jobOpeningList)
   {
        LinkedHashMap<Object, Object> map = new LinkedHashMap<Object, Object> ();
        map.put("company", company);
        map.put("jobopenings", jobOpeningList);
        Gson gson = new Gson();
        String jobOpeningsJson = gson.toJson(map, LinkedHashMap.class);
        return jobOpeningsJson;

   }

    public String getCompany(Company company, int size)
   {
        LinkedHashMap<Object, Object> map = new LinkedHashMap<Object, Object> ();
        map.put("company", company);
        map.put("no_of_openings", size);
        Gson gson = new Gson();
        String jobOpeningsJson = gson.toJson(map, LinkedHashMap.class);
        return jobOpeningsJson;

   }

  public String scheduleInterview(int userId, int jobId, int applicationid, String title, String email, 
                             String location, String startTime, String endTime)
  {
        String status = "InvitationSent";
        Interview interview = new Interview(userId, jobId, applicationid, location, startTime, status);
        try {
            Interview newInterview = interviewDao.save(interview);
            LinkedHashMap<Object, Object> map = new LinkedHashMap<Object, Object>();
            map.put("status", "Schedule Interview");
            Gson gson = new Gson();
            String resultJson = gson.toJson(map, LinkedHashMap.class); 
            System.out.println("scheduling");
            return resultJson; 
        } catch (Exception e) {
            return null;
        }
    }


 public List<Interview> getInterviewByStatus(String statusList)
 {
      List<String> statuslist= Arrays.asList(statusList.split("\\s*,\\s*"));
      List<Interview> result = new ArrayList<>(); 
      try {    
          result = interviewDao.getInterviewByStatus(statuslist);
          return result; 
      } catch(Exception e) {
          return null;
      } 
}   





}
