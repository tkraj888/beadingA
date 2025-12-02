package com.spring.jwt.Color.ServieceImpl;

import com.spring.jwt.Color.Dto.ColorDto;
import com.spring.jwt.Color.Dto.OneColorDto;
import com.spring.jwt.Color.DuplicateColorException;
import com.spring.jwt.Color.Entity.Color;
import com.spring.jwt.Color.ColorNotFoundException;
import com.spring.jwt.Color.Repo.ColorRepository;
import com.spring.jwt.Color.Service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColorServiceImpl implements ColorService {

    @Autowired
    private ColorRepository colorRepository;

    // Create a new color
    @Override
    public Color createColor(ColorDto colorDto) {
        // Check if color already exists
        Optional<Color> existingColor = colorRepository.findByName(colorDto.getName());
        if (existingColor.isPresent()) {
            throw new DuplicateColorException("Color '" + colorDto.getName() + "' already exists");
        }
        Color color = new Color(null, colorDto.getName());
        return colorRepository.save(color);
    }

    // Get all colors
    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    // Get a color by id
    @Override
    public Optional<Color> getColorById(Integer colorId) {
        return colorRepository.findById(colorId)
                .or(() -> {
                    throw new ColorNotFoundException("Color with ID " + colorId + " not found");
                });
    }

    // Update a color by id
    @Override
    public Color updateColor(Integer colorId, ColorDto colorDto) {
        Color existingColor = colorRepository.findById(colorId)
                .orElseThrow(() -> new ColorNotFoundException("Color with ID " + colorId + " not found"));

        existingColor.setName(colorDto.getName());
        return colorRepository.save(existingColor);
    }

    // Delete a color by id
    @Override
    public void deleteColor(Integer colorId) {
        Color color = colorRepository.findById(colorId)
                .orElseThrow(() -> new ColorNotFoundException("Color with ID " + colorId + " not found"));
        colorRepository.delete(color);
    }

    // Get all colors (only names)
    @Override
    public List<OneColorDto> gelAllColorOnly() {
        return colorRepository.findAll()
                .stream()
                .map(color -> new OneColorDto(color.getName()))
                .collect(Collectors.toList());
    }
}
