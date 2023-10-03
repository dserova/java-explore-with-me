package ru.practicum.explorewithmeservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithmeservice.compilation.dto.CompilationRequestDto;
import ru.practicum.explorewithmeservice.compilation.dto.CompilationRequestUpdateDto;
import ru.practicum.explorewithmeservice.compilation.dto.CompilationResponseDto;
import ru.practicum.explorewithmeservice.compilation.model.Compilation;
import ru.practicum.explorewithmeservice.compilation.repository.CompilationRepository;
import ru.practicum.explorewithmeservice.error.CompilationBadRequestException;
import ru.practicum.explorewithmeservice.error.CompilationNotFoundException;
import ru.practicum.explorewithmeservice.event.model.Event;
import ru.practicum.explorewithmeservice.event.repository.EventRepository;
import ru.practicum.explorewithmeservice.helpers.Helper;
import ru.practicum.explorewithmeservice.helpers.Paging;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    private final ModelMapper mapper;

    private final Paging paging = new Paging();

    private final Helper helper = new Helper();

    @Override
    public CompilationResponseDto updateCompilation(Long compilationId, CompilationRequestUpdateDto data) {
        Function<Compilation, Compilation> chain = Function.identity();
        return chain.andThen(
                updating -> {
                    Optional.ofNullable(data.getPinned()).ifPresent(updating::setPinned);
                    Optional.ofNullable(data.getTitle()).ifPresent(updating::setTitle);
                    Optional.ofNullable(data.getPinned()).ifPresent(updating::setPinned);

                    Optional.ofNullable(data.getEvents()).ifPresent(
                            events -> {
                                List<Event> list = events.stream().map(
                                        event -> eventRepository.findById(event)
                                                .orElseThrow(CompilationBadRequestException::new)
                                ).collect(Collectors.toList());
                                updating.setEvents(list);
                            }
                    );
                    return updating;
                }
        ).andThen(
                compilationRepository::save
        ).andThen(
                helper.to(CompilationResponseDto.class)
        ).apply(
                compilationRepository.findById(compilationId)
                        .orElseThrow(() -> new CompilationNotFoundException("Check your parameters in the path."))
        );
    }


    @Override
    public CompilationResponseDto createCompilation(CompilationRequestDto data) {
        Function<CompilationRequestDto, CompilationRequestDto> chain = Function.identity();
        return chain.andThen(
                helper.to(Compilation.class)
        ).andThen(
                updating -> {
                    Optional.ofNullable(data.getEvents()).ifPresent(
                            events -> {
                                List<Event> list = events.stream().map(
                                        event -> eventRepository.findById(event)
                                                .orElseThrow(CompilationBadRequestException::new)
                                ).collect(Collectors.toList());
                                updating.setEvents(list);
                            }
                    );
                    return updating;
                }
        ).andThen(
                compilationRepository::save
        ).andThen(
                helper.to(CompilationResponseDto.class)
        ).apply(
                data
        );
    }

    @Override
    public void deleteCompilation(Long categoryId) {
        compilationRepository.deleteById(categoryId);
    }

    @Override
    public CompilationResponseDto getCompilationById(Long categoryId) {
        Function<Compilation, Compilation> chain = Function.identity();
        return chain.andThen(
                helper.to(CompilationResponseDto.class)
        ).apply(
                compilationRepository.findById(categoryId)
                        .orElseThrow(() -> new CompilationNotFoundException("Check your parameters in the path."))
        );
    }

    @Override
    public List<CompilationResponseDto> getAllCompilations(Integer start, Integer size) {
        Function<Page<Compilation>, Page<Compilation>> chain = Function.identity();
        return chain.andThen(
                helper.fromPage(CompilationResponseDto.class)
        ).apply(
                compilationRepository.findAll(paging.getPageable(start, size))
        );
    }

}


