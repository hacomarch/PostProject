package goormton.postappproject.service;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CursorResult<T> {
    private List<T> values;
}
