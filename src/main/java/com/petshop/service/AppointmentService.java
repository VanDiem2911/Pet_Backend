package com.petshop.service;

import com.petshop.model.Appointment;
import com.petshop.repository.AppointmentRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment book(Appointment appointment) {
        appointment.setStatus("PENDING");
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    /**
     * Returns the set of booked timeSlots for a given date and serviceType (e.g. "grooming").
     * Only active (non-CANCELLED) bookings are counted.
     */
    public Set<String> getBookedSlots(String date, String serviceType) {
        return appointmentRepository.findByDateAndServiceType(date, serviceType)
                .stream()
                .filter(a -> !"CANCELLED".equalsIgnoreCase(a.getStatus()))
                .map(Appointment::getTimeSlot)
                .filter(slot -> slot != null && !slot.isBlank())
                .collect(Collectors.toSet());
    }
}
