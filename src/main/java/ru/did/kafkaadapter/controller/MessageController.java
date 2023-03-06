package ru.did.jpaenumcustomrsort.domain.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.did.jpaenumcustomrsort.domain.service.CreatureService;

@RestController("ru.did.jpaenumcustomrsort.domain.controlle.CreatureController")
@RequiredArgsConstructor
public class CreatureController {

    private final CreatureService service;


    @GetMapping("/creatures")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query", dataTypeClass=java.lang.Integer.class, value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", paramType = "query", dataTypeClass=java.lang.Integer.class, value = "Number of records per page.", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", paramType = "query", dataTypeClass=java.lang.String.class, allowMultiple = true, value = "Sorting criteria in the format: property(,asc|desc)"),
            @ApiImplicitParam(name = "name", paramType = "query", dataTypeClass=java.lang.String.class, value = "Название создания"),
            @ApiImplicitParam(name = "kinds", paramType = "query", dataTypeClass=java.lang.String.class, value = "Виды"),
            @ApiImplicitParam(name = "weightFrom", paramType = "query", dataTypeClass=java.lang.Double.class, value = "Вес от"),
            @ApiImplicitParam(name = "weightTo", paramType = "query", dataTypeClass=java.lang.Double.class, value = "Вес до")
    })
    public ResponseEntity<Page<CreatureDto>> CreatureDto(CreaturePredicate predicate,
                                                         Pageable pageable,
                                                         @RequestParam(name = "fields", required = false) String fields) {
        Page<CreatureDto> result = service.findCreatures(predicate, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
