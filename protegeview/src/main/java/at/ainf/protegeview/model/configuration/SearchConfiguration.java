package at.ainf.protegeview.model.configuration;

/**
 * Created with IntelliJ IDEA.
 * User: pfleiss
 * Date: 21.05.12
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class SearchConfiguration {

    public static enum SearchType {UNIFORM_COST, BREATHFIRST};

    public static enum TreeType {REITER, DUAL};

    public static enum QSS {MINSCORE, SPLIT, DYNAMIC};


    public Boolean aBoxInBG = true;
    public Boolean tBoxInBG = false;
    public SearchType searchType = SearchType.UNIFORM_COST;
    public TreeType treeType = TreeType.REITER;
    public Integer numOfLeadingDiags = 9;
    public QSS qss = QSS.MINSCORE;
    public Boolean reduceIncoherency = false;
    public Boolean minimizeQuery = true;
    public Boolean calcAllDiags = false;

    public Boolean inclEntSubClass = true;
    public Boolean incEntClassAssert = true;
    public Boolean incEntEquivClass = false;
    public Boolean incEntDisjClasses = false;
    public Boolean incEntPropAssert = false;
    public Boolean incOntolAxioms = true;
    public Boolean incAxiomsRefThing = false;

    public Double entailmentCalThres = 0.01;

    public String toString() {
        return "SearchType: " +  searchType + ", " +
                "TreeType: " +  treeType + ", " +
                "QSS: " +  qss + ", " +
                "aboxInBg: " + aBoxInBG + ", " +
                "tboxInBg: " + tBoxInBG + ", " +
                "numOfLeadingDiags: " + numOfLeadingDiags + ", " +
                "reduceIncoherency: " + reduceIncoherency + ", " +
                "minimizeQuery: " + minimizeQuery + ", " +
                "calcAllDiags: " + calcAllDiags + ", " +
                "SubClass: " + inclEntSubClass + ", " +
                "ClassAssert: " + incEntClassAssert + ", " +
                "EquivClass: " + incEntEquivClass + ", " +
                "DisjointClass: " + incEntEquivClass + ", " +
                "PropertyAssertions: " + incEntPropAssert + ", " +
                "OntologyAxioms: " + incOntolAxioms + ", " +
                "RefThing: " + incAxiomsRefThing + ", " +
                "double threshold: " + entailmentCalThres;

    }


}
