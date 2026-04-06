package log;

import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Список со слабыми ссылками для слушателей, чтобы предотвратить утечку памяти
 */
public class WeakArrayList<T> extends AbstractList<T> {

    private final List<WeakReference<T>> items;

    public WeakArrayList() {
        items = new ArrayList<>();
    }

    @Override
    public T get(int index) {
        WeakReference<T> ref = items.get(index);
        return ref != null ? ref.get() : null;
    }

    @Override
    public int size() {
        // Очищаем ссылки при подсчёте размера
        cleanDeadReferences();
        return items.size();
    }

    @Override
    public boolean add(T element) {
        return items.add(new WeakReference<>(element));
    }

    @Override
    public boolean remove(Object element) {
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i).get();
            if (item == null || item.equals(element)) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public T remove(int index) {
        WeakReference<T> ref = items.remove(index);
        return ref != null ? ref.get() : null;
    }

    /**
     * Удаляет все ссылки, которые уже не указывают на объекты
     */
    private void cleanDeadReferences() {
        items.removeIf(ref -> ref.get() == null);
    }

     //Возвращает массив живых слушателей
    @SuppressWarnings("unchecked")
    public T[] toLiveArray(T[] array) {
        cleanDeadReferences();
        List<T> liveItems = new ArrayList<>();
        for (WeakReference<T> ref : items) {
            T item = ref.get();
            if (item != null) {
                liveItems.add(item);
            }
        }
        return liveItems.toArray(array);
    }
}