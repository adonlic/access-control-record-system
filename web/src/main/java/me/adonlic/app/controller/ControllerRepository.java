package me.adonlic.app.controller;

import me.adonlic.app.controller.model.Controller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControllerRepository extends JpaRepository<Controller, Long> {
    public boolean existsByControllerSN(String controllerSN);
}
