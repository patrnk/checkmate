package io.github.patrnk.checkmate;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author vergeev
 */
public interface Test extends Serializable {
    public List<TestAnswer> check(List<TestAnswer> studentAnswers);
}
