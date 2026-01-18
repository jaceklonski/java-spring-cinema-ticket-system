package com.example.Cinema3D.repository;

import com.example.Cinema3D.entity.Booking;
import com.example.Cinema3D.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserOrderByIdDesc(User user);
}
