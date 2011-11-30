/*
 * Copyright (c) 2009 Kostyantyn Shchekotykhin
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * http://www.gnu.org/licenses/gpl.txt
 */

package at.ainf.diagnosis.model;


import static org.junit.Assert.*;
import org.junit.Test;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IVecInt;

import java.util.List;
import java.util.ArrayList;

public class PropositionalTheoryTest {

    @Test
    public void testTheory() throws ContradictionException, SolverException, UnsatisfiableFormulasException {
        int[] clause = new int[]{5, 6};
        PropositionalTheory th = new PropositionalTheory(SolverFactory.newDefault());
        th.addBackgroundFormula(new VecInt(clause));
        assertTrue(th.hasBackgroundTheory());

        int count = th.getTheoryCount();
        assertEquals(0, count);

        insertConflicts(th);
        assertTrue(th.isConsistent());

        assertEquals(1, th.getTheoryCount());

        clause = new int[]{-3};
        IVecInt fl = th.addClause(clause);
        th.push(fl);
        assertEquals(2, th.getTheoryCount());
        assertFalse(th.isConsistent());
        th.pop();

        assertEquals(1, th.getTheoryCount());

        addTheories(3, 7, th);
        assertEquals(4, th.getTheoryCount());
        th.pop(4);

        fl = th.addClause(clause);
        th.push(fl);
        assertTrue(th.isConsistent());
    }

    private void addTheories(int numberOfTheories, int from, PropositionalTheory th) throws SolverException {
        if (numberOfTheories == 0)
            return;

        int ncl = (int) Math.round(Math.random() * 10);
        List<IVecInt> list = new ArrayList<IVecInt>(ncl);
        for (int i = 0; i < ncl; i++) {
            int length = (int) Math.round(Math.random() * 10);
            int[] lclause = new int[ncl];
            for (int j = 0; j < length; j++) {
                from += i * j;
                lclause[i] = from;

            }
            IVecInt fl = th.addClause(lclause);
            list.add(fl);
        }
        th.push(list);
        addTheories(--numberOfTheories, from, th);
    }

    private void insertConflicts(PropositionalTheory th) throws ContradictionException {

        // simple conflict c1-c4
        List<IVecInt> list = new ArrayList<IVecInt>(3);

        int[] clause = new int[]{-1, -2, 3};
        list.add(th.addClause(clause));

        clause = new int[]{1};
        list.add(th.addClause(clause));

        clause = new int[]{2};
        list.add(th.addClause(clause));

        th.push(list);
    }
}
