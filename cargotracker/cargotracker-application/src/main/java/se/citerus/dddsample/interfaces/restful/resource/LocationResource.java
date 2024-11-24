package se.citerus.dddsample.interfaces.restful.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import se.citerus.dddsample.domain.model.location.LocationFinder;
import se.citerus.dddsample.interfaces.model.convertor.CargoConvertor;
import se.citerus.dddsample.interfaces.model.dto.LocationDto;

import java.util.List;


@Slf4j
@Tag(name = "location")
@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationResource {

    private final LocationFinder locationFinder;
    private final CargoConvertor cargoConvertor;

    @Operation(summary = "list all locations")
    @GetMapping
    public List<LocationDto> list() {
        return locationFinder.listAll()
            .stream()
            .map(cargoConvertor::toDto)
            .toList();
    }


}
