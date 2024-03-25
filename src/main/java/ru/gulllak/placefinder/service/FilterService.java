package ru.gulllak.placefinder.service;

import ru.gulllak.placefinder.model.Filter;

public interface FilterService {
    void save(Filter filter);

    Filter getById(long userId);
}
