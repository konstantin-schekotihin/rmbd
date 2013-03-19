package at.ainf.owlapi3.module.iterative;

import org.semanticweb.owlapi.model.OWLLogicalAxiom;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 12.03.13
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 */
public class ModuleTargetDiagSearcher extends ModuleMinDiagSearcher {

    private Set<OWLLogicalAxiom> mappingAxioms;

    public ModuleTargetDiagSearcher(String path) {
        GS_MappingsReader reader = new GS_MappingsReader();
        this.mappingAxioms = reader.loadGSmappings(path);
    }

    public Set<OWLLogicalAxiom> getGSMappings() {
        return mappingAxioms;
    }

    @Override
    protected Set<OWLLogicalAxiom> chooseDiagnosis(Collection<? extends Set<OWLLogicalAxiom>> diags) {
        for (Set<OWLLogicalAxiom> diagnosis : diags) {
            boolean isAllFalse = true;
            for (OWLLogicalAxiom axiom : diagnosis) {
                if (getGSMappings().contains(axiom)) {
                    isAllFalse = false;
                    break;
                }
            }
            if (isAllFalse)
                return diagnosis;
        }
        // gs incoherent
        List<Set<OWLLogicalAxiom>> diagnoses = new LinkedList<Set<OWLLogicalAxiom>>(diags);
        Set<OWLLogicalAxiom> min = Collections.min(diagnoses, new Comparator<Set<OWLLogicalAxiom>>() {
            @Override
            public int compare(Set<OWLLogicalAxiom> o1, Set<OWLLogicalAxiom> o2) {
                Set<OWLLogicalAxiom> o1r = new HashSet<OWLLogicalAxiom>(o1);
                o1r.removeAll(getGSMappings());
                Set<OWLLogicalAxiom> o2r = new HashSet<OWLLogicalAxiom>(o1);
                o2r.removeAll(getGSMappings());
                return new Integer(o1r.size()).compareTo(o2r.size());
            }
        });
        return min;
    }

}