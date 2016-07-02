package io.github.patrnk.checkmate;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author vergeev
 */
public interface TestWrapper extends Serializable {
    public TestWrapper put(Test t);
    public List<TestAnswer> check (List<TestAnswer> t);    
}
