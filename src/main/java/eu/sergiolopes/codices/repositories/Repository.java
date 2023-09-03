/*
 * Codices-fx, personal library manager (ebooks, audio & paper books).
 * Copyright (C) 2023  Sérgio Lopes, https:www.sergiolopes.eu, knitter.is@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
