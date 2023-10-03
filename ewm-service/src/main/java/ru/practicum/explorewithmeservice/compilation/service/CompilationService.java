package ru.practicum.explorewithmeservice.compilation.service;

import ru.practicum.explorewithmeservice.compilation.dto.CompilationRequestDto;
import ru.practicum.explorewithmeservice.compilation.dto.CompilationRequestUpdateDto;
import ru.practicum.explorewithmeservice.compilation.dto.CompilationResponseDto;

import java.util.List;

public interface CompilationService {
    CompilationResponseDto updateCompilation(
            Long compilationId,
            CompilationRequestUpdateDto compilationRequestDto
    );

    void deleteCompilation(
            Long compilationId
    );

    CompilationResponseDto createCompilation(
            CompilationRequestDto compilationRequestDto
    );

    CompilationResponseDto getCompilationById(
            Long compilationId
    );

    List<CompilationResponseDto> getAllCompilations(
            Integer start,
            Integer size
    );
}