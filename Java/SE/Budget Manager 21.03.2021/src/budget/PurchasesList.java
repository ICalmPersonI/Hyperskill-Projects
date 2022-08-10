package budget;


public class PurchasesList<T extends Purchase> {
    private T[] list;

    PurchasesList() {
        list = (T[]) new Purchase[1];
    }

    public void add(T object) {
        int index = find();

        if (index == -1) {
            increaseSizeArray();
            index = find();
        }

        list[index] = object;
    }

    public T get(int index) {
        return list[index];
    }

    public int size() {
        return list.length;
    }


    public void sort() {
        if (list.length > 1) {
            for (int i = 0; i < list.length - 1; i++) {
                for (int s = 0; s < list.length - 1; s++) {
                    if (list[s].getPrice() < list[s + 1].getPrice()) {
                        T temp = list[s];
                        list[s] = list[s + 1];
                        list[s + 1] = temp;

                    }
                }
            }
        }
    }

    public boolean isEmpty() {
        boolean empty = false;
        for (T t : list) {
            empty = t == null;
        }
        return empty;
    }

    private void increaseSizeArray() {
        T[] temp = list;
        list = (T[]) new Purchase[list.length + 1];
        System.arraycopy(temp, 0, list, 0, temp.length);
    }

    private int find() {
        for (int i = 0; i < list.length; i++) {
            if (list[i] == null) {
                return i;
            }
        }

        return -1;
    }
}
