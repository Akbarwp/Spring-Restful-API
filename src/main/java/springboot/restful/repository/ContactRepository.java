package springboot.restful.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import springboot.restful.entity.Contact;
import springboot.restful.entity.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String>, JpaSpecificationExecutor<Contact> {

    Optional<Contact> findByUserAndId(User user, String id);
}
