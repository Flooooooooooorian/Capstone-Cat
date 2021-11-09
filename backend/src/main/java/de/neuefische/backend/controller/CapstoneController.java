package de.neuefische.backend.controller;

import de.neuefische.backend.model.Capstone;
import de.neuefische.backend.services.CapstoneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/capstones")
public class CapstoneController {

    private final CapstoneService capstoneService;

    public CapstoneController(CapstoneService capstoneService) {
        this.capstoneService = capstoneService;
    }

    @GetMapping
    public List<Capstone> getCapstones() {
        return capstoneService.getCapstones();
    }

    @GetMapping("/refreshed/{id}")
    public Capstone refreshCapstoneById(@PathVariable String id) {
        return capstoneService.refreshCapstone(id);
    }
}
