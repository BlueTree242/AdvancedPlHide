/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2021 BlueTree242
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 *  END
 */

package tk.bluetree242.advancedplhide.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsedMap<K, V> {

    private Map<K, List<V>> map = new HashMap<>();

    public V get(K key) {
        List list = map.get(key);
        if (list == null || list.isEmpty()) return null;
        return (V) list.get(0);
    }

    public void put(K key, V val) {
        List list = map.get(key);
        if (list == null) {
            list = new ArrayList();
            list.add(val);
            map.put(key, list);
        } else {
            list.remove(0);
            list.add(val);
        }
    }

    public void remove(K key) {
        List list = map.get(key);
        if (list == null) {
            //nothing to remove
            return;
        } else {
            list.remove(0);
            if (list.isEmpty()) map.remove(list);
        }
    }
}
