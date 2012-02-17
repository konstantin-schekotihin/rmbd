package at.ainf.diagnosis.partitioning;

import at.ainf.theory.model.ITheory;
import at.ainf.theory.model.InconsistentTheoryException;
import at.ainf.theory.model.SolverException;
import at.ainf.theory.storage.AxiomSet;
import at.ainf.theory.storage.Partition;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: kostya
 * Date: 02.05.11
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
public class GreedySearch<Id> extends BruteForce<Id> implements Partitioning<Id> {

    private static Logger logger = Logger.getLogger(GreedySearch.class.getName());
    private double sum;
    private int count = 0;
    private boolean swap = false;


    private class Measurable {
        private AxiomSet<Id> hs;
        private double measure;

        public Measurable(AxiomSet<Id> hs, double measure) {
            this.hs = hs;
            this.measure = measure;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Measurable measure = (Measurable) o;

            if (hs != null ? !hs.equals(measure.hs) : measure.hs != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return hs != null ? hs.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Measurable{" +
                    "hs=" + hs.getName() +
                    ", measure=" + measure +
                    '}';
        }

        public Measurable copy(double measure) {
            return new Measurable(this.hs, measure);
        }
    }

    public GreedySearch(ITheory<Id> theory, ScoringFunction<Id> function) {
        super(theory, function);
    }


    public <E extends AxiomSet<Id>> Partition<Id> generatePartition(Set<E> hittingSets)
            throws SolverException, InconsistentTheoryException {
        if (getScoringFunction() == null)
            throw new IllegalStateException("Scoring function is not set!");
        // save the original hitting sets
        setHittingSets(Collections.unmodifiableSet(hittingSets));

        // preprocessing
        Set<E> hs = new LinkedHashSet<E>(hittingSets);
        removeCommonEntailments(hs);
        getScoringFunction().normalize(hs);

        SortedSet<Measurable> shs = new TreeSet<Measurable>(new Comparator<Measurable>() {
            public int compare(Measurable c, Measurable that) {
                int mult = 10000000;
                if (c == null && that == null)
                    return 0;
                if (c == null)
                    return mult;
                if (that == null)
                    return -mult;

                if (c.equals(that)) return 0;

                long div = Math.round(-c.measure * mult + that.measure * mult);
                if (div == 0)
                    return c.hs.getName().compareTo(that.hs.getName());
                return (int) div;
            }
        }
        );
        for (E hset : hs) {
            Measurable m = new Measurable(hset, hset.getMeasure());
            shs.add(m);
        }
        this.sum = sum(shs);
        // find the best partition
        Partition<Id> partition = findPartition(shs, new LinkedHashMap<Measurable, List<Measurable>>());

        if (logger.isDebugEnabled())
            logger.debug("Searched through " + getPartitionsCount() + " partitionsCount");
        if (getPostprocessor() != null)
            partition = getPostprocessor().run(getPartitions());
        return partition;
    }

    private Partition<Id> findPartition(SortedSet<Measurable> hittingSets, Map<Measurable, List<Measurable>> map)
            throws SolverException, InconsistentTheoryException {
        if ((hittingSets == null || hittingSets.isEmpty() || hittingSets.size() <= 1)) {
            if (hittingSets == null || map.keySet().isEmpty())
                return new Partition<Id>();
            else {
                hittingSets.clear();
                incPartitionsCount();
                Partition<Id> partLeft = createPartition(map.keySet());
                Partition<Id> partRight = createPartition(convert(map.values()));
                if (getScoringFunction().getScore(partLeft) < getScoringFunction().getScore(partRight)) {
                    return partLeft;
                }
                return partRight;
            }
        }

        Iterator<Measurable> ti = hittingSets.iterator();
        // get 2 first elements
        Measurable lt = ti.next();
        ti.remove();
        Measurable rt = ti.next();
        ti.remove();

        /*
        if (this.swap) {
            Measurable a = lt;
            lt = rt;
            rt = a;
        }
        this.swap = !this.swap;
        */

        if (map.containsKey(rt)) {
            List<Measurable> val = map.remove(rt);
            for (Measurable m : val)
                put(map, m, rt);
        }
        put(map, lt, rt);

        // add the difference and test the left branch
        double separate = lt.measure - rt.measure;
        Measurable diff = lt.copy(separate);
        hittingSets.add(diff);
        //}
        if (logger.isDebugEnabled())
            logger.debug("Partitions: " + getPartitionsCount() + " head: " + map.keySet().size() + " hsets:" + hittingSets.size());
        Partition<Id> leftPart = findPartition(hittingSets, map);

        Partition<Id> rightPart = null;
        /*
        join(map, lt, rt);
        Partition<Id> rightPart = findPartition(hittingSets, map);
        //remove(map, lt, rt);
        hittingSets.add(lt);
        hittingSets.add(rt);

        /*
        //if (addedRight) {
            // clean up
            right.remove(rt);
            if (diff != null)
                hittingSets.remove(diff);
            // test the right branch
            left.add(rt);
            double together = lt.measure + rt.measure;
            Measurable copy = null;
            if (!hittingSets.isEmpty()) {
                copy = lt.copy(together);
                hittingSets.add(copy);
            }
            if (logger.isDebugEnabled())
                logger.debug("Partitions: " + getPartitionsCount() + " head: " + left.size() + " hsets:" + hittingSets.size());
            rightPart = findPartition(hittingSets, left, right);
            if (copy != null)
                hittingSets.remove(copy);
            left.remove(rt);
            hittingSets.add(rt);
        //}
        if (addedLeft) {
            left.remove(lt);
            hittingSets.add(lt);
        }

         */
        if (getScoringFunction().getScore(leftPart) < getScoringFunction().getScore(rightPart)) {
            return leftPart;
        }
        return rightPart;
    }

    /*private void join(Map<Measurable, List<Measurable>> map, Measurable lt, Measurable rt) {
        map.get(lt).remove(rt);
        double together = lt.measure + rt.measure;

        Measurable t = new Measurable(new AbstrAxiomSet<Id>("NULL:" + this.count++, 0,Collections.<Id>emptySet(), Collections.<Id>emptySet()), together);
        put(map, t, lt);
        put(map, t, rt);} */

    private Collection<Measurable> convert(Collection<List<Measurable>> values) {
        LinkedHashSet<Measurable> m = new LinkedHashSet<Measurable>();
        for (List<Measurable> l : values)
            m.addAll(l);
        return m;
    }

    private boolean put(Map<Measurable, List<Measurable>> map, Measurable m, Measurable rt) {
        if (map.containsKey(m)) {
            return map.get(m).add(rt);
        } else {
            List<Measurable> l = new LinkedList<Measurable>();
            l.add(rt);
            map.put(m, l);
            return true;
        }
    }

    private Partition<Id> createPartition(Collection<Measurable> left) throws InconsistentTheoryException, SolverException {
        Partition<Id> part = new Partition<Id>();
        for (Measurable el : left)
            part.dx.add(getOriginalHittingSet(el.hs));
        if (logger.isDebugEnabled())
            logger.debug("Creating a partition with dx: " + left);
        if (verifyPartition(part)) {
            if (logger.isDebugEnabled())
                logger.debug("Created partition: \n dx:" + part.dx + "\n dnx:" + part.dnx + "\n dz:" + part.dz);
            if (getPartitions() != null)
                getPartitions().add(part);
            return part;
        }
        return null;
    }

    private double sum(Set<Measurable> set) {
        double sum = 0;
        for (Measurable hs : set)
            sum += hs.measure;
        return sum;
    }
}