/**
 * MIT License
 * <p>
 * Copyright (c) 2023 Sérgio Lopes
 * https:www.sergiolopes.eu, knitter.is@gmail.com
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eu.sergiolopes.codices.repositories;

import java.util.List;

/**
 * Defines a simple repository interface that should be implemented by all repository objects that access data storage.
 *
 * @param <T>
 */
public interface Repository<T> {

    T find(int id);

    List<T> findAll();

    List<T> findAllForOwner(int ownerId);

    List<T> list(int page, int size);

    List<T> listForOwner(int ownerId, int page, int size);

    /**
     * Persists an object to storage by either creating a new record or updating one if the object already exists.
     *
     * @param obj Object that will be saved, typed to the generic parameter T.
     * @return True if the object was successfully saved, either created or updated, false otherwise.
     */
    boolean save(T obj);

    /**
     * @param obj Object that will be inserted, typed to the generic parameter T.
     * @return True if a new record was inserted into storage, false otherwise
     */
    boolean insert(T obj);

    /**
     * @param obj Object that will be updated, typed to the generic parameter T.
     * @return True if the data was changed in storage, false otherwise.
     */
    boolean update(T obj);

    /**
     * Deletes the given object from storage.
     *
     * @param obj Object that will be deleted, typed to the generic parameter T.
     * @return True if the object was deleted, false otherwise.
     */
    boolean delete(T obj);

}
