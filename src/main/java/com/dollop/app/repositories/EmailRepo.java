package com.dollop.app.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.dollop.app.models.EmailRequest;
public interface EmailRepo extends JpaRepository<EmailRequest, Integer> {
    List<EmailRequest> findByStatusIsTrue();  
        @Query("SELECT e FROM EmailRequest e WHERE " +
                "(e.emailFrom = :loggedInUserEmail AND " +
                "(:keyword IN elements(e.sendTo)) OR " +
                "(:keyword IN elements(e.cc)) OR " +
                "(:keyword IN elements(e.bcc)) OR " +
                "e.emailSubject LIKE %:keyword% OR " +
                "e.message LIKE %:keyword% OR " +
                "e.fileName LIKE %:keyword% OR " +
                "e.recipientUsername LIKE %:keyword%)")
        List<EmailRequest> findByKeywordAndLoggedInUser(@Param("keyword") String keyword, @Param("loggedInUserEmail") String loggedInUserEmail);
      
        @Query("SELECT e FROM EmailRequest e " +
                "WHERE :email IN elements(e.sendTo) " +
                "OR :email IN elements(e.cc) " +
                "OR :email IN elements(e.bcc)")
        List<EmailRequest> findByEmailFromOrSendToOrCcOrBcc(String email);   
       
        List<EmailRequest> findByEmailFrom(String emailFrom);

        @Query("SELECT CASE WHEN COUNT(e) = 0 THEN false ELSE (CASE WHEN COUNT(e) = SUM(CASE WHEN e.status = true THEN 1 ELSE 0 END) THEN true ELSE false END) END FROM EmailRequest e")
        boolean allStatusesTrueOrFalse();
}