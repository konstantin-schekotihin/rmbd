package at.ainf.asp.interactive.solver;


import at.ainf.diagnosis.model.BaseSearchableObject;
import at.ainf.diagnosis.model.SolverException;
import at.ainf.diagnosis.storage.FormulaSet;

import java.util.Set;

/**
 *  *
 */
public class ASPTheory extends BaseSearchableObject<String> {

    public ASPTheory(ASPSolver solver, ASPKnowledgeBase kb){
        setKnowledgeBase(kb);
        setReasoner(solver);
    }

	@Override
	public ASPSolver getReasoner() {
		return (ASPSolver) super.getReasoner();
	}
	
	@Override
	public boolean verifyConsistency() throws SolverException {
        boolean consistent = getReasoner().isConsistent();
        return consistent && checkTestsConsistency();
    }

    private boolean checkTestsConsistency() {
        //OWLReasoner solver = getSolver();
        for (Set<String> test : getKnowledgeBase().getNegativeTests()) {
            if (!getReasoner().isEntailed(test)) {
                return true;
            }
        }

        for (Set<String> test : getKnowledgeBase().getNonentailedTests()) {
            if (test != null && getReasoner().isEntailed(test)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<String> getEntailments(Set<String> hittingSet) throws SolverException {
        final Set<String> program = getASPKnowledgeBase().generateDiagnosisProgram(hittingSet);
        getReasoner().clearFormulasCache();
        getReasoner().addFormulasToCache(program);
        return getReasoner().getEntailments();
    }

    @Override
    public boolean isEntailed(Set<String> formulas) {
        final Set<String> program = getASPKnowledgeBase().generateDebuggingProgram();
        getReasoner().clearFormulasCache();
        getReasoner().addFormulasToCache(program);
        return getReasoner().getEntailments().containsAll(formulas);
    }

    @Override
    public void reset() {
    }

    @Override
    public boolean diagnosisEntails(FormulaSet<String> hs, Set<String> ent) {
        try {
            return getEntailments(hs).containsAll(ent);
        } catch (SolverException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean diagnosisCandidateConsistent(Set<String> hs) {
        final Set<String> program = getASPKnowledgeBase().generateDiagnosisProgram(hs);
        getReasoner().clearFormulasCache();
        getReasoner().addFormulasToCache(program);
        try {
            return verifyConsistency();
        } catch (SolverException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean diagnosisConsistent(FormulaSet<String> hs, Set<String> ent) {
        final Set<String> program = getASPKnowledgeBase().generateDiagnosisProgram(hs);
        getReasoner().clearFormulasCache();
        getReasoner().addFormulasToCache(program);
        getReasoner().addFormulasToCache(ent);
        try {
            return verifyConsistency();
        } catch (SolverException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean diagnosisConsistent(FormulaSet<String> hs, Set<String> testcase, Set<String> positive) {
        final Set<String> program = getASPKnowledgeBase().generateDiagnosisProgram(hs);
        getReasoner().clearFormulasCache();
        getReasoner().addFormulasToCache(program);
        getReasoner().addFormulasToCache(testcase);
        getReasoner().addFormulasToCache(positive);
        try {
            return verifyConsistency();
        } catch (SolverException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean diagnosisEntails(FormulaSet<String> hs, Set<String> testcase, Set<String> positive) {
        final Set<String> program = getASPKnowledgeBase().generateDiagnosisProgram(hs);
        getReasoner().clearFormulasCache();
        getReasoner().addFormulasToCache(program);
        getReasoner().addFormulasToCache(positive);
        return getReasoner().getEntailments().containsAll(testcase);
    }


    public ASPKnowledgeBase getASPKnowledgeBase() {
        return (ASPKnowledgeBase) getKnowledgeBase();
    }
}
