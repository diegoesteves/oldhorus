package org.aksw.horus.search.cache;

import java.util.List;

/**
 * Created by dnes on 01/06/16.
 */
public interface ICache<T> {

    /**
     *
     * @return
     */
    public boolean contains(String identifier);

    /**
     *
     * @return
     */
    public T getEntry(String identifier);

    /**
     *
     * @return
     */
    public T removeEntryByPrimaryKey(String primaryKey);

    /**
     *
     * @return
     */
    public boolean updateEntry(T object);

    /**
     *
     * @param listToAdd
     * @return
     */
    public List<T> addAll(List<T> listToAdd);

    /**
     * @return
     */
    public T add(T entry);

}
