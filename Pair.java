// Вспомогательный класс для хранения двух целочисленных элементов
public class Pair {
    // Первый элемент
    private final int first;
    // Второй элемент
    private final int second;

    // Конструктор
    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    // Getter первого элемента
    public int getFirst() {
        return first;
    }

    // Getter второго элемента
    public int getSecond() {
        return second;
    }
}
