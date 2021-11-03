package de.neuefische.backend.controller;

import de.neuefische.backend.dtos.CapstoneDto;
import de.neuefische.backend.services.CapstoneService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/capstones")
public class CapstoneController {

    private final CapstoneService capstoneService;

    public CapstoneController(CapstoneService capstoneService) {
        this.capstoneService = capstoneService;
    }

    @GetMapping
    public List<CapstoneDto> getCapstones() {
        return capstoneService.getCapstones();
    }
}
