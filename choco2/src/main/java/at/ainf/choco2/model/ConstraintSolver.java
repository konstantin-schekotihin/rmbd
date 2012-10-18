package at.ainf.choco2.model;

import at.ainf.diagnosis.model.Solver;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.constraints.Constraint;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 18.10.12
 * Time: 10:09
 * To change this template use File | Settings | File Templates.
 */
public class ConstraintSolver implements Solver<Constraint> {

    private Model model;

    public ConstraintSolver() {
        this.model = new CPModel();
    }

    public ConstraintSolver(Model model) {
        this.model = model;
    }

    @Override
    public void setFormulars(Set<Constraint> formulas) {

        for (Iterator<Constraint> iterator = model.getConstraintIterator(); iterator.hasNext(); ) {
            iterator.next();
            iterator.remove();
        }

        for (Constraint cons : formulas)
            model.addConstraint(cons);
    }

    public Set<Constraint> getFormulars() {
        Set<Constraint> formulars = new LinkedHashSet<Constraint>();
        for (Iterator<Constraint> iterator = model.getConstraintIterator(); iterator.hasNext(); )
            formulars.add(iterator.next());
        return Collections.unmodifiableSet(formulars);
    }

    @Override
    public boolean isConsistent() {
        choco.kernel.solver.Solver solver = new CPSolver();
        solver.read(this.model);
        return solver.solve();
    }

}