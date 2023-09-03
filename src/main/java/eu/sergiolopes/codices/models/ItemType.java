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
package eu.sergiolopes.codices.models;

/**
 * Simple enumeration to hide the item type values used in the database.
 */
public enum ItemType {
    EBOOK("ebook"),
    AUDIO_BOOK("audio"),
    PAPER_BOOK("paper");

    private String type;

    private ItemType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public String getType() {
        return type;
    }

    public static ItemType fromString(String type) {
        switch (type) {
            case "ebook":
                return ItemType.EBOOK;
            case "audio":
                return ItemType.AUDIO_BOOK;
            case "paper":
                return ItemType.PAPER_BOOK;
        }

        return null;
    }
}
