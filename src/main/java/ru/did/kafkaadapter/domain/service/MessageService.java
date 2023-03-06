package ru.did.jpaenumcustomrsort.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.did.jpaenumcustomrsort.domain.controller.CreatureDto;
import ru.did.jpaenumcustomrsort.domain.controller.CreaturePredicate;

public interface CreatureService {

    Page<CreatureDto> findCreatures(CreaturePredicate predicate, Pageable pageable);
}
