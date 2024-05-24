package com.dollop.app.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dollop.app.models.EmailReply;
public interface EmailReplyRepo extends JpaRepository<EmailReply, Integer>{

}
