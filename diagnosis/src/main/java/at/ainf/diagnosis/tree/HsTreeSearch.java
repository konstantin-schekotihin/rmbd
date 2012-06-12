package at.ainf.diagnosis.tree;

import at.ainf.diagnosis.tree.searchstrategy.SearchStrategy;
import at.ainf.theory.storage.AxiomSet;
import at.ainf.theory.storage.SimpleStorage;
import at.ainf.theory.storage.Storage;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 12.06.12
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class HsTreeSearch<T extends AxiomSet<Id>,Id> extends AbstractTreeSearch<T,Id> implements TreeSearch<T,Id>{

    public HsTreeSearch(Storage<T, Id> tIdStorage) {
        super(tIdStorage);
        setLogic(new HsTreeLogic<T, Id>());
    }

}
