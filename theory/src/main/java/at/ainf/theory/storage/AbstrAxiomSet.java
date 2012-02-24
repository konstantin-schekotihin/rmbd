package at.ainf.theory.storage;

import at.ainf.theory.watchedset.MeasureUpdatedListener;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: kostya
 * Date: 18.04.11
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstrAxiomSet<Id> implements AxiomSet<Id>, Comparable<AxiomSet<Id>> {
    protected Set<Id> axioms;
    boolean valid = true;
    private double measure = 0;
    private final Set<Id> entailments;
    private Set<Id> tempEntailments = null;
    private Object node;

    private final String name;
    protected StorageListener listener;

    public void updateAxioms(Set<Id> axioms) {
        this.axioms = axioms;
    }

    public <T extends AxiomSet<Id>> void setListener(StorageListener<T, Id> listener) {
        this.listener = listener;
    }

    public Object getNode() {
        return node;
    }

    public void setNode(Object node) {
        this.node = node;
    }

    public void setEntailments(Set<Id> entailments) {
        this.tempEntailments = Collections.unmodifiableSet(entailments);
    }

    public void restoreEntailments() {
        this.tempEntailments = Collections.unmodifiableSet(this.entailments);
    }

    public String getName() {
        return name;
    }

    public TypeOfSet getType() {
        return typeOfSet;
    }

    private TypeOfSet typeOfSet;

    protected AbstrAxiomSet(TypeOfSet type, String name, double measure, Set<Id> axioms, Set<Id> entailments) {
        this.typeOfSet = type;
        this.name = name;
        setMeasure(measure);
        this.axioms = Collections.unmodifiableSet(axioms);
        this.entailments = Collections.unmodifiableSet(entailments);
        setEntailments(entailments);
    }


    public boolean isValid() {
        return this.valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }


    @SuppressWarnings("unchecked")
    public void setMeasure(double value) {
        if (value == 0) {
            throw new IllegalArgumentException("Probability of a hitting set is 0!");
        }
        if (((Double) value).isNaN())
            throw new IllegalArgumentException("Trying to set measure to NaN!");
        if (this.measure == value || this.listener == null) {
            this.measure = value;
            for (MeasureUpdatedListener<Double> listener : measureUpdatedListenerList)
                listener.notifiyMeasureUpdated(this, value);
        } else {
            boolean addValid = this.listener.remove(this);
            for (MeasureUpdatedListener<Double> lsn : measureUpdatedListenerList)
                lsn.notifiyMeasureUpdated(this, value);
            this.measure = value;
            this.listener.add(this, addValid);
        }

    }


    public double getMeasure() {
        return this.measure;
    }

    public Set<Id> getEntailments() {
        return this.tempEntailments;
    }

    public int size() {
        return axioms.size();
    }

    public boolean isEmpty() {
        return axioms.isEmpty();
    }

    public boolean contains(Object o) {
        return axioms.contains(o);
    }

    public Iterator<Id> iterator() {
        return this.axioms.iterator();
    }

    public Object[] toArray() {
        return axioms.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return axioms.toArray(a);
    }

    public boolean add(Id id) {
        return axioms.add(id);
    }

    public boolean remove(Object o) {
        return axioms.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return axioms.containsAll(c);
    }

    public boolean addAll(Collection<? extends Id> c) {
        return axioms.addAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return axioms.retainAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return axioms.removeAll(c);
    }

    public void clear() {
        axioms.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if ( Set.class.isInstance(o)) {
            Set that = (Set) o;
            return axioms != null && axioms.equals(that);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return axioms.hashCode();
    }

    @Override
    public String toString() {
        String str = name + "{valid=" + valid +
                ", measure=" + measure;
        if (entailments != null)
            str += ", entailments=" + this.tempEntailments.size() + "/" + entailments.size();
        str += '}';
        return str;
    }

    List<MeasureUpdatedListener<Double>> measureUpdatedListenerList = new LinkedList<MeasureUpdatedListener<Double>>();

    public void addMeasureUpdatedListener(MeasureUpdatedListener<Double> eDoubleMeasureUpdatedListener) {
        measureUpdatedListenerList.add(eDoubleMeasureUpdatedListener);
    }

    public void removeMeasureUpdatedListener(MeasureUpdatedListener<Double> eDoubleMeasureUpdatedListener) {
        measureUpdatedListenerList.remove(eDoubleMeasureUpdatedListener);
    }

    public void setWatchedElementMeasure(Double value) {
        measure = value;
    }

    public int compareTo(AxiomSet<Id> that) {
        if (this.equals(that))
            return 0;
        int res = Double.valueOf(getMeasure()).compareTo(that.getMeasure());
        if (res == 0)
            if (-1 * getName().compareTo(that.getName()) >= 0)
                return 1;
            else
                return -1;
        return res;
    }

}
