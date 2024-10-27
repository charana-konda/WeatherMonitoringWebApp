package com.zeotap.weathermonitoring.Repository;




import com.zeotap.weathermonitoring.Models.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByCity(String city);
}
