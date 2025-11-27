package com.spring.jwt.Color.controller;

import com.spring.jwt.Color.Dto.AllColorDto;
import com.spring.jwt.Color.Dto.AllColorDto1;
import com.spring.jwt.Color.Dto.ColorDto;
import com.spring.jwt.Color.Dto.OneColorDto;
import com.spring.jwt.Color.DuplicateColorException;
import com.spring.jwt.Color.Entity.Color;
import com.spring.jwt.Color.Service.ColorService;
import com.spring.jwt.dto.ResponceDto;
import com.spring.jwt.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/colors")
public class ColorController {

    @Autowired
    private ColorService colorService;

    // Create a new color
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> createColor(@RequestBody ColorDto colorDto) {
        try {
            Color createdColor = colorService.createColor(colorDto);
            ResponseDto response = new ResponseDto("Color created successfully", null);
            return ResponseEntity.ok(response);
        } catch (DuplicateColorException ex) {
            // Handle the custom exception for duplicate colors
            ResponseDto response = new ResponseDto("Failed","Color already exists: " + ex.getMessage());
            return ResponseEntity.status(400).body(response);
        } catch (Exception ex) {
            // Handle any other exceptions that might occur
            ResponseDto response = new ResponseDto("Failed","An error occurred: " + ex.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Get all colors
    @GetMapping("/getAll")
    public ResponseEntity<AllColorDto1> getAllColors() {
        List<Color> colors = colorService.getAllColors();
        AllColorDto1 response = new AllColorDto1("List of all colors", colors,"");
        return ResponseEntity.ok(response);
    }

    // Get a color by id
    @GetMapping("/getbyId")
    public ResponseEntity<?> getColorById(@RequestParam Integer id) {
        Optional<Color> color = colorService.getColorById(id);
        if (color.isPresent()) {
            ResponceDto response = new ResponceDto("Color found", color.get());
            return ResponseEntity.ok(response);
        } else {
            ResponseDto response = new ResponseDto("Color not found", "not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    // Update a color by id
    @PutMapping("/edit")
    public ResponseEntity<ResponseDto> updateColor(@RequestParam Integer id, @RequestBody ColorDto colorDto) {
        Color updatedColor = colorService.updateColor(id, colorDto);
        ResponseDto response = new ResponseDto("successful", "Color updated successfully");
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteColor(@RequestParam Integer id) {
        colorService.deleteColor(id);
        ResponseDto response = new ResponseDto("Color deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    // Get only color names
    @GetMapping("/names")
    public ResponseEntity<AllColorDto> getAllColorNames() {
        List<OneColorDto> colorNames = colorService.gelAllColorOnly();
        AllColorDto response = new AllColorDto("List of color names", colorNames,null);
        return ResponseEntity.ok(response);
    }
}
