package at.ainf.diagnosis.partitioning.scoring;

/**
 * Created by IntelliJ IDEA.
 * User: pfleiss
 * Date: 13.02.12
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
public interface QSS<T> extends Scoring<T> {
    void updateParameters(boolean answerToLastQuery);
}
