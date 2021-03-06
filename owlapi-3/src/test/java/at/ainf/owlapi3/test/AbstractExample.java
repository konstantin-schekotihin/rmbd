package at.ainf.owlapi3.test;

import at.ainf.owlapi3.model.OWLTheory;
import at.ainf.owlapi3.parser.MyOWLRendererParser;
import at.ainf.diagnosis.model.InconsistentTheoryException;
import at.ainf.diagnosis.model.SolverException;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOWLObjectRendererImpl;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: pfleiss
 * Date: 14.04.11
 * Time: 19:36
 * To change this template use File | Settings | File Templates.
 */
public class AbstractExample {

    static OWLTheory theory;

    static OWLOntology ontology;

    static OWLReasonerFactory reasonerFactory;

    static Set<OWLLogicalAxiom> bax = new LinkedHashSet<OWLLogicalAxiom>();

    protected static MyOWLRendererParser parser;

    public static File file;


    HashMap<ManchesterOWLSyntax, BigDecimal> map = new HashMap<ManchesterOWLSyntax, BigDecimal>();

    public AbstractExample() {
        map.put(ManchesterOWLSyntax.SOME, new BigDecimal("0.05"));
        map.put(ManchesterOWLSyntax.ONLY, new BigDecimal("0.05"));
        map.put(ManchesterOWLSyntax.AND, new BigDecimal("0.001"));
        map.put(ManchesterOWLSyntax.OR, new BigDecimal("0.001"));
        map.put(ManchesterOWLSyntax.NOT, new BigDecimal("0.01"));

        ManchesterOWLSyntax[] keywords = {ManchesterOWLSyntax.MIN,
                ManchesterOWLSyntax.MAX,
                ManchesterOWLSyntax.EXACTLY,

                ManchesterOWLSyntax.VALUE,

                ManchesterOWLSyntax.INVERSE,
                ManchesterOWLSyntax.SUBCLASS_OF,
                ManchesterOWLSyntax.EQUIVALENT_TO,
                ManchesterOWLSyntax.DISJOINT_WITH,
                ManchesterOWLSyntax.INVERSE_OF,
                ManchesterOWLSyntax.SUB_PROPERTY_OF,
                ManchesterOWLSyntax.SAME_AS,
                ManchesterOWLSyntax.DIFFERENT_FROM,
                ManchesterOWLSyntax.RANGE,
                ManchesterOWLSyntax.DOMAIN,
                ManchesterOWLSyntax.TYPE
        };


        for (ManchesterOWLSyntax keyword : keywords) {
            map.put(keyword, BigDecimal.valueOf(0.0001));
        }
    }

    /*public class MyOWLTestTheory extends ProbabilityOWLTheory {

        HashMap<ManchesterOWLSyntax, Double> map = new HashMap<ManchesterOWLSyntax, Double>();

        public MyOWLTestTheory(OWLReasonerFactory reasonerFactory, OWLOntology ontology, Collection<OWLLogicalAxiom> backgroundAxioms) throws InconsistentTheoryException, SolverException {
            super(reasonerFactory, ontology, backgroundAxioms);
            map.put (ManchesterOWLSyntax.SOME, 0.05);
            map.put (ManchesterOWLSyntax.ONLY, 0.05);
            map.put (ManchesterOWLSyntax.AND, 0.001);
            map.put (ManchesterOWLSyntax.OR, 0.001);
            map.put (ManchesterOWLSyntax.NOT, 0.01);
            map.put (ManchesterOWLSyntax.SUBCLASS_OF, 0.01);


            ManchesterOWLSyntax[] keywords = { ManchesterOWLSyntax.MIN,
                                                   ManchesterOWLSyntax.MAX,
                                                   ManchesterOWLSyntax.EXACTLY,

                                                   ManchesterOWLSyntax.VALUE,

                                                   ManchesterOWLSyntax.INVERSE,
                                                   ManchesterOWLSyntax.SUBCLASS_OF,
                                                   ManchesterOWLSyntax.EQUIVALENT_TO,
                                                   ManchesterOWLSyntax.DISJOINT_WITH,
                                                   ManchesterOWLSyntax.INVERSE_OF,
                                                   ManchesterOWLSyntax.SUB_PROPERTY_OF,
                                                   ManchesterOWLSyntax.SAME_AS,
                                                   ManchesterOWLSyntax.DIFFERENT_FROM,
                                                   ManchesterOWLSyntax.RANGE,
                                                   ManchesterOWLSyntax.DOMAIN,
                                                   ManchesterOWLSyntax.TYPE
                   };


                for (ManchesterOWLSyntax keyword : keywords) {
                    map.put (keyword,0.02);
                }

        }

         protected double getProbability (ManchesterOWLSyntax keyword) {
             return map.get(keyword);
         }}*/


    public String getFullDiagString(Set<OWLLogicalAxiom> axioms, int num) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        double p = 0; //theory.getFailureProbabilityDiagnosis(axioms);
        nf.setMinimumFractionDigits(16);

        return "num: " + num + " p = " + nf.format(p);

    }

    public String getAxiomString(OWLLogicalAxiom axiom) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        ManchesterOWLSyntaxOWLObjectRendererImpl renderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();
        double p = 0;//theory.getFailureProbability(axiom);
        nf.setMinimumFractionDigits(16);

        return " \t p = " + nf.format(p) + " " + renderer.render(axiom);

    }

    public void printDiagnoses(List<Set<OWLLogicalAxiom>> diagnos) {
        System.out.println("********************************************************************************");
        for (Set<OWLLogicalAxiom> axioms : diagnos) {
            int num = diagnos.indexOf(axioms) + 1;
            System.out.println(getFullDiagString(axioms, num));
            for (OWLLogicalAxiom axiom : axioms) {
                System.out.println(getAxiomString(axiom));
            }
            System.out.println("");
        }
        System.out.println("********************************************************************************");

    }

    public static OWLOntologyManager man;

    public static void createOntology() throws OWLOntologyCreationException, InconsistentTheoryException, SolverException {

        man = OWLManager.createOWLOntologyManager();

        ontology = man.loadOntologyFromOntologyDocument(file);

        bax = new LinkedHashSet<OWLLogicalAxiom>();

        for (OWLIndividual ind : ontology.getIndividualsInSignature()) {
            bax.addAll(ontology.getClassAssertionAxioms(ind));
            bax.addAll(ontology.getObjectPropertyAssertionAxioms(ind));
        }
        parser = new MyOWLRendererParser(ontology);

        reasonerFactory = new Reasoner.ReasonerFactory();
    }
}
