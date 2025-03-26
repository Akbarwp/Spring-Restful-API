package springboot.restful.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import springboot.restful.entity.Address;
import springboot.restful.entity.Contact;

@Repository
public interface AddressRepository extends JpaRepository<Address, String>, JpaSpecificationExecutor<Address> {

    Optional<Address> findByContactAndId(Contact contact, String id);

    List<Address> findAllByContact(Contact contact);
}
