package ru.gulllak.placefinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gulllak.placefinder.model.Filter;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {
    @Override
    public void save(Filter filter) {

    }

    @Override
    public Filter getById(long userId) {
        return null;
    }
}
