package at.ainf.diagnosis.debugger;

import at.ainf.diagnosis.partitioning.CKK;
import at.ainf.diagnosis.partitioning.QueryMinimizer;
import at.ainf.diagnosis.partitioning.ScoringFunction;
import at.ainf.diagnosis.quickxplain.NewQuickXplain;
import at.ainf.diagnosis.tree.BreadthFirstSearch;
import at.ainf.diagnosis.tree.TreeSearch;
import at.ainf.diagnosis.tree.exceptions.NoConflictException;
import at.ainf.theory.model.ITheory;
import at.ainf.theory.model.InconsistentTheoryException;
import at.ainf.theory.model.SolverException;
import at.ainf.theory.storage.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/*import at.ainf.querygen.partitioning.CKK;
  import at.ainf.querygen.partitioning.QueryMinimizer;
  import at.ainf.querygen.partitioning.ScoringFunction;*/

/**
 * Created by IntelliJ IDEA.
 * User: pfleiss
 * Date: 18.11.11
 * Time: 08:37
 * To change this template use File | Settings | File Templates.
 */
public class SimpleQueryDebugger<Id> implements QueryDebugger<Id> {


    protected ITheory<Id> theory;

    protected TreeSearch<? extends AxiomSet<Id>, Id> search;

    protected StorageConflictSetsListener conflictSetsListener;

    protected StorageHittingSetsListener hittingSetsListener;

    private int maxDiags = 9;

    protected SimpleQueryDebugger(ITheory<Id> theory, boolean init) {
        this.theory = theory;
        if (init)
            init();
    }

    public SimpleQueryDebugger(ITheory<Id> theory) {
        this(theory, true);
    }

    public void init() {
        SimpleStorage<Id> storage = new SimpleStorage<Id>();
        search = new BreadthFirstSearch<Id>(storage);
        search.setSearcher(new NewQuickXplain<Id>());
        if (theory != null) getTheory().reset();
        search.setTheory(getTheory());
        conflictSetsListener = new StorageConflictSetsListenerImpl();
        hittingSetsListener = new StorageHittingSetsListenerImpl();
        search.getStorage().addStorageConflictSetsListener(conflictSetsListener);
        search.getStorage().addStorageHittingSetsListener(hittingSetsListener);

    }

    public Set<? extends AxiomSet<Id>> getConflictSets() {
        return search.getStorage().getConflictSets();
    }

    public Set<? extends AxiomSet<Id>> getHittingSets() {
        return search.getStorage().getHittingSets();
    }

    public Set<? extends AxiomSet<Id>> getValidHittingSets() {
        return search.getStorage().getDiagnoses();
    }

    public void set_Theory(ITheory<Id> theory) {
        this.theory = theory;
    }

    public ITheory<Id> getTheory() {
        return theory;
    }

    private void minimizePartitionAx(Partition<Id> query) {
        if (query.partition == null) return;
        QueryMinimizer<Id> mnz = new QueryMinimizer<Id>(query, getTheory());
        NewQuickXplain<Id> q = new NewQuickXplain<Id>();
        try {
            query.partition = q.search(mnz, query.partition, null);
        } catch (NoConflictException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SolverException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InconsistentTheoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        for (AxiomSet<Id> hs : query.dx) {
            if (!hs.getEntailments().containsAll(query.partition) && !search.getTheory().diagnosisEntails(hs, query.partition))
                throw new IllegalStateException("DX diagnosis is not entailing a query");
        }


        for (
                AxiomSet<Id> hs
                : query.dnx)

        {
            if (search.getTheory().diagnosisConsistent(hs, query.partition))
                throw new IllegalStateException("DNX diagnosis might entail a query");
        }

        for (
                AxiomSet<Id> hs
                : query.dz)

        {
            if (search.getTheory().diagnosisEntails(hs, query.partition) || hs.getEntailments().containsAll(query.partition))
                throw new IllegalStateException("DZ diagnosis entails a query");
            if (!search.getTheory().diagnosisConsistent(hs, query.partition))
                throw new IllegalStateException("DZ diagnosis entails a query complement");
        }
    }


    public Partition<Id> getQuery(ScoringFunction<Id> func, boolean minimize, double acceptanceThreshold) {
        CKK<Id> ckk = new CKK<Id>(theory, func);
        ckk.setThreshold(acceptanceThreshold);

        TreeSet<AxiomSet<Id>> set = new TreeSet<AxiomSet<Id>>(getValidHittingSets());

        Partition<Id> best = null;
        try {
            best = ckk.generatePartition(set);
        } catch (SolverException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InconsistentTheoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (minimize)
            minimizePartitionAx(best);

        return best;
    }

    public void updateMaxHittingSets(int number) {
        maxDiags = number;
        search.setMaxHittingSets(maxDiags);

    }

    public boolean debug() {
        try {
            search.run();
        } catch (SolverException e) {
            return false;
        } catch (NoConflictException e) {
            return false;
        } catch (InconsistentTheoryException e) {
            return false;
        }
        return true;

    }

    protected class StorageConflictSetsListenerImpl implements StorageConflictSetsListener {
        public void conflictSetAdded(StorageItemAddedEvent e) {
            for (QueryDebuggerListener<Id> listener : listeners)
                listener.conflictSetAdded(search.getStorage().getConflictSets());
        }
    }

    protected class StorageHittingSetsListenerImpl implements StorageHittingSetsListener {
        public void hittingSetAdded(StorageItemAddedEvent e) {
            for (QueryDebuggerListener<Id> listener : listeners)
                listener.hittingSetAdded(search.getStorage().getDiagnoses());
        }

    }

    private List<QueryDebuggerListener<Id>> listeners = new LinkedList<QueryDebuggerListener<Id>>();

    public void addQueryDebuggerListener(QueryDebuggerListener<Id> listener) {
        listeners.add(listener);
    }

    public void removeQueryDebuggerListener(QueryDebuggerListener<Id> listener) {
        listeners.remove(listener);
    }

    public boolean resume() {
        try {
            search.run(maxDiags);
        } catch (SolverException e) {
            return false;
        } catch (NoConflictException e) {
            return false;
        } catch (InconsistentTheoryException e) {
            return false;
        }
        return true;

    }

    //
    public void reset() {
        maxDiags = 9;
        search.getStorage().removeStorageConflictSetsListener(conflictSetsListener);
        search.getStorage().removeStorageHittingSetsListener(hittingSetsListener);
        init();


    }

}