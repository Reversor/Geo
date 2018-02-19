package util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Spliterator.DISTINCT;

public class MyMap<K, V> implements Map<K, V> {
    @SuppressWarnings("unchecked")
    private final Map.Entry<K, V>[] EMPTY_ENTRIES = new Map.Entry[]{};

    private Map.Entry<K, V>[] entries;
    private boolean sortable;
    private boolean sorted;

    private EntrySet entrySet;
    private KeySet keySet;
    private Values values;

    @SuppressWarnings("unchecked")
    public MyMap() {
        this(false);
    }

    @SuppressWarnings("unchecked")
    public MyMap(boolean sortable) {
        this.sortable = sortable;
        this.sorted = false;
        this.entries = new Map.Entry[]{};
    }

    public void sort() {
        Arrays.sort(entries, new KeyHashComparator());
    }

    @Override
    public int size() {
        return entries.length;
    }

    @Override
    public boolean isEmpty() {
        return entries.length == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (isEmpty()) return false;
        for (Map.Entry<K, V> entry : entries) {
            if (entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Map && entrySet().equals(((Map) obj).entrySet());
    }

    @Override
    public String toString() {
        // TODO
        return Arrays.toString(entries);
    }

    @Override
    public V get(Object key) throws ClassCastException, NullPointerException {
        if (isEmpty()) {
            return null;
        }
        for (Map.Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private V replaceValue(Map.Entry<K, V> entry, V value) {
        V oldValue = entry.getValue();
        entry.setValue(value);
        return oldValue;
    }

    @Override
    public V put(K key, V value) throws NullPointerException {
        if (!isEmpty()) {
            for (Map.Entry<K, V> entry : entries) {
                if (entry == null) {
                    return null;
                }
                if (entry.getKey().equals(key)) {
                    return replaceValue(entry, value);
                }
            }
        }
        Map.Entry<K, V> newEntry = new Entry<>(key, value);
        entries = Arrays.copyOf(entries, entries.length + 1);
        entries[entries.length - 1] = newEntry;
        if (sortable) sort();
        return null;
    }

    public int find(Object key) {
        if (!(sorted)) sort();
        int hashKey = key.hashCode();
        int low = 0;
        int high = entries.length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = entries[mid].getKey().hashCode();

            if (midVal < hashKey)
                low = mid + 1;
            else if (midVal > hashKey)
                high = mid - 1;
            else
                return mid;
        }
        return -(low + 1);
    }

    @Override
    public V remove(Object key) {
        if (isEmpty()) return null;
        // O(log n)
        if (sorted) return removeByIndex(find(key));
        // O(n)
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].getKey().equals(key)) {
                return removeByIndex(i);
            }
        }
        return null;
    }

    private V removeByIndex(int index) {
        V oldValue = entries[index].getValue();
        @SuppressWarnings("unchecked")
        Map.Entry<K, V>[] newEntries = new Map.Entry[entries.length - 1];
        System.arraycopy(entries, 0, newEntries, 0, index);
        System.arraycopy(entries, index + 1, newEntries, index, entries.length - index - 1);
        entries = newEntries;
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) throws ClassCastException, NullPointerException {
        if (this != m) m.forEach(this::put);
    }

    @Override
    public void clear() {
        sorted = false;
        entries = EMPTY_ENTRIES;
    }

    @Override
    public Set<K> keySet() {
        return keySet == null
                ? keySet = new KeySet()
                : keySet;
    }

    @Override
    public Collection<V> values() {
        return keySet == null
                ? values = new Values()
                : values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet == null
                ? entrySet = new EntrySet()
                : entrySet;
    }

    private int hashCompare(Object first, Object second) {
        return first.hashCode() - second.hashCode();
    }

    static final class Entry<K, V> implements Map.Entry<K, V> {
        final K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public int hashCode() {
            int keyHash = key.hashCode();
            int valueHash = value.hashCode();
            return 37 * keyHash ^ 17 * valueHash;
        }

        @Override
        public String toString() {
            return "{ " + key + ": " + value + " }";
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof Entry
                    && key.equals(((Map.Entry) o).getKey())
                    && value.equals(((Map.Entry) o).getValue());
        }
    }

    class KeyHashComparator implements Comparator<Map.Entry<K, V>> {
        @Override
        public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
            return hashCompare(o1.getKey(), o2.getKey());
        }
    }

    private final class MySpliterator<T> implements Spliterator<T> {
        private final int DEFAULT_CHARACTERISTIC = SIZED | SUBSIZED;

        private final int fence;
        private final int characteristic;
        private final Function<Map.Entry<K, V>, T> getter;
        private int index;

        MySpliterator(Function<Map.Entry<K, V>, T> getter) {
            this(getter, 0);
        }

        MySpliterator(Function<Map.Entry<K, V>, T> getter, int characteristic) {
            this(0, entries.length, getter, characteristic);
        }

        MySpliterator(int index, int fence, Function<Map.Entry<K, V>, T> getter, int characteristic) {
            this.index = index;
            this.fence = fence;
            this.characteristic = DEFAULT_CHARACTERISTIC | characteristic;
            this.getter = getter;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (action == null)
                throw new NullPointerException();
            if (index >= 0 && index < fence) {
                action.accept(getter.apply(entries[index++]));
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<T> trySplit() {
            int mid = (index + fence) >>> 1;
            return (index >= mid)
                    ? null
                    : new MySpliterator<>(index, index = mid, getter, characteristic);
        }

        @Override
        public long estimateSize() {
            return fence - index;
        }

        @Override
        public int characteristics() {
            return characteristic;
        }

        @Override
        public Comparator<? super T> getComparator() {
            return null;
        }
    }

    private abstract class MyIterator<T> implements Iterator<T> {
        int index;

        MyIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < MyMap.this.size();
        }

        Map.Entry<K, V> nextEntry() {
            if (hasNext()) {
                return MyMap.this.entries[index++];
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            MyMap.this.removeByIndex(index - 1);
        }
    }

    private class EntryIterator extends MyIterator<Map.Entry<K, V>> {
        EntryIterator() {
            super();
        }

        @Override
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    private class KeyIterator extends MyIterator<K> {
        KeyIterator() {
            super();
        }

        public K next() {
            return nextEntry().getKey();
        }
    }

    private class ValueIterator extends MyIterator<V> {
        ValueIterator() {
            super();
        }

        @Override
        public V next() {
            return nextEntry().getValue();
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return MyMap.this.size();
        }

        @Override
        public boolean remove(Object o) {
            return MyMap.this.remove(o) != null;
        }

        @Override
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return new MySpliterator<>((e) -> e, Spliterator.SIZED | Spliterator.SUBSIZED);
        }
    }

    private class KeySet extends AbstractSet<K> {

        @Override
        public boolean removeAll(Collection<?> c) {
            // TODO
            return super.removeAll(c);
        }

        @Override
        public boolean remove(Object o) {
            return MyMap.this.remove(o) != null;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
//            c.forEach();
            // TODO
            return super.retainAll(c);
        }

        @Override
        public void clear() {
            super.clear();
        }

        @Override
        public Spliterator<K> spliterator() {
            return new MySpliterator<>(Map.Entry::getKey, DISTINCT);
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return MyMap.this.size();
        }

    }

    private class Values extends AbstractCollection<V> {
        @Override
        public final int size() {
            return MyMap.this.size();
        }

        @Override
        public final void clear() {
            MyMap.this.clear();
        }

        @Override
        public final Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public final boolean contains(Object o) {
            return containsValue(o);
        }

        @Override
        public final boolean remove(Object o) {
            return super.remove(o);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return super.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return super.retainAll(c);
        }

        @Override
        public final Spliterator<V> spliterator() {
            return new MySpliterator<>(Map.Entry::getValue);
        }
    }

}
