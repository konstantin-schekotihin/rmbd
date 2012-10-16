package at.ainf.diagnosis.model;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 10.10.12
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public class FaultyAxiomsManager<T> {

    private final LinkedHashSet<T> formulaStack = new LinkedHashSet<T>();
    private Boolean result = null;



    public boolean add(Collection<T> formulas) {
        if (formulas == null)
            return false;
        resetResult();
        this.formulaStack.addAll(formulas);
        return true;
    }

    public void remove(Collection<T> formulas) {
        resetResult();
        formulaStack.removeAll(formulas);

    }

    public void clean() {
        formulaStack.clear();
    }


    public Set<T> getFormulaStack() {
        return Collections.unmodifiableSet(this.formulaStack);
    }

    /*public int getTheoryCount() {
        return this.lastStackCount.size();
    }*/

    public void resetResult() {
        this.result = null;
    }

    public Boolean getResult() {
        return this.result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }


}
