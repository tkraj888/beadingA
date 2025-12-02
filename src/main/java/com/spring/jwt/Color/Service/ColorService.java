package com.spring.jwt.Color.Service;

import com.spring.jwt.Color.Dto.ColorDto;
import com.spring.jwt.Color.Dto.OneColorDto;
import com.spring.jwt.Color.Entity.Color;

import java.util.List;
import java.util.Optional;

public interface ColorService {
    Color createColor(ColorDto colorDto);
    List<Color> getAllColors();

    List<OneColorDto>gelAllColorOnly();

    Optional<Color> getColorById(Integer colorId);

    Color updateColor(Integer colorId, ColorDto colorDto);

    void deleteColor(Integer colorId);
}
