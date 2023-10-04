package ru.practicum.explorewithmeservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmeservice.compilation.dto.CompilationRequestDto;
import ru.practicum.explorewithmeservice.compilation.dto.CompilationRequestUpdateDto;
import ru.practicum.explorewithmeservice.compilation.dto.CompilationResponseDto;
import ru.practicum.explorewithmeservice.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompilationController {

    private static final String fromRequestParam = "from";
    private static final String sizeRequestParam = "size";
    private static final String compilationIdAlias = "compilationId";
    private final CompilationService compilationService;

    @PostMapping(value = "/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDto createCompilation(
            @Valid @RequestBody CompilationRequestDto request
    ) {
        return compilationService.createCompilation(
                request
        );
    }

    @PatchMapping("/admin/compilations/{" + compilationIdAlias + "}")
    public CompilationResponseDto updateCompilation(
            @PathVariable(name = compilationIdAlias) Long compilationId,
            @Valid @RequestBody CompilationRequestUpdateDto request
    ) {
        return compilationService.updateCompilation(
                compilationId,
                request
        );
    }

    @DeleteMapping("/admin/compilations/{" + compilationIdAlias + "}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(
            @PathVariable(name = compilationIdAlias) Long compilationId
    ) {
        compilationService.deleteCompilation(
                compilationId
        );
    }

    @GetMapping("/compilations")
    public List<CompilationResponseDto> getAllCompilations(
            @PositiveOrZero @RequestParam(name = fromRequestParam, defaultValue = "0") int from,
            @Positive @RequestParam(name = sizeRequestParam, defaultValue = "10") int size
    ) {
        return compilationService.getAllCompilations(
                from,
                size
        );
    }

    @GetMapping("/compilations/{" + compilationIdAlias + "}")
    public CompilationResponseDto getCompilationById(
            @PathVariable(name = compilationIdAlias) Long compilationId
    ) {
        return compilationService.getCompilationById(
                compilationId
        );
    }
}
