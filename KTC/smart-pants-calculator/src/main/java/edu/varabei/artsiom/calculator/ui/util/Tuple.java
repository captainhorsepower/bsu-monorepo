package edu.varabei.artsiom.calculator.ui.util;

import lombok.Value;

@Value(staticConstructor = "of")
public class Tuple<S, T> {
    S t1;
    T t2;
}
