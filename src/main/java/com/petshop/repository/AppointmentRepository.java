package com.petshop.repository;

import com.petshop.model.Appointment;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByDateAndServiceType(String date, String serviceType);
}
