package at.ainf.owlapi3.reasoner.cores;

import at.ainf.owlapi3.reasoner.OWLSatReasoner;
import org.sat4j.specs.IVecInt;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: kostya
 * Date: 14.05.13
 * Time: 20:15
 * To change this template use File | Settings | File Templates.
 */
public class HornCore extends Core {

    public HornCore(OWLSatReasoner reasoner, int symbols) {
        super(reasoner, symbols);
    }

    public HornCore(OWLSatReasoner reasoner) {
        super(reasoner);
    }

    @Override
    protected Collection<CoreSymbol> expand(IVecInt clause, CoreSymbol parent, Collection<CoreSymbol> fringe) {
        if (!isBCHornClause(clause)) {
            return fringe;
        }
        return super.expand(clause, parent, fringe);
    }
}
