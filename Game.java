import java.util.Scanner;

// Класс игрового раунда
public class Game {
    // Наилучший результат за все игровые раунды против компьютера
    private static int bestResult = -1;
    // Доска игрового раунда
    private Board board;
    // Игровая доска на предыдущем шаге
    private Board previous;

    // Getter наилучшего игрового результата
    public static int getBestResult() {
        return bestResult;
    }

    // Процедура запуска игрового раунда против другого игрока
    public void startPVP() {
        board = new Board();
        previous = null;
        System.out.println("Ваш экран:");
        board.print();
        while (true) {
            var move = getMove();
            if (canPlay()) {
                if (move.getFirst() == -2 && move.getSecond() == -2) {
                    board.skipMove();
                    board.print();
                } else {
                    if (move.getFirst() == -1 && move.getSecond() == -1) {
                        board = previous.copy();
                        previous = null;
                        board.print();
                        continue;
                    }
                    previous = board.copy();
                    board.add(move.getFirst() + 1, move.getSecond() + 1);
                    System.out.println("Ваш экран:");
                    board.print();
                }
            } else {
                if (move.getFirst() == -1 && move.getSecond() == -1) {
                    board = previous.copy();
                    previous = null;
                    System.out.println("Ваш экран:");
                    board.print();
                    continue;
                }
                printWinnerPVP();
                return;
            }
        }
    }

    // Процедура запуска игрового раунда против компьютера
    public void startPVE() {
        board = new Board();
        previous = null;
        System.out.println("Ваш экран:");
        board.print();
        while (true) {
            var move = getMove();
            if (canPlay()) {
                if (move.getFirst() == -2 && move.getSecond() == -2) {
                    board.skipMove();
                    board.print();
                } else {
                    if (move.getFirst() == -1 && move.getSecond() == -1) {
                        board = previous.copy();
                        previous = null;
                        board.print();
                        continue;
                    }
                    previous = board.copy();
                    board.add(move.getFirst() + 1, move.getSecond() + 1);
                    System.out.println("Экран соперника:");
                    board.print();
                    var pair = board.bestPoint();
                    if (pair.getFirst() != -1 && pair.getSecond() != -1) {
                        board.add(pair.getFirst() + 1, pair.getSecond() + 1);
                    }
                    System.out.println("Ваш экран:");
                    board.print();
                }
            } else {
                if (move.getFirst() == -1 && move.getSecond() == -1) {
                    board = previous.copy();
                    previous = null;
                    System.out.println("Ваш экран:");
                    board.print();
                    continue;
                }
                printWinnerPVE();
                return;
            }
        }
    }

    // Проверкаа игрового раунда на возможность продолжения игры
    private boolean canPlay() {
        boolean firstPlayer = board.possibleMoves().size() != 0;
        board.skipMove();
        boolean secondPlayer = board.possibleMoves().size() != 0;
        board.skipMove();
        boolean fullTable = board.countPoints(0) != 0;
        return (firstPlayer || secondPlayer) && fullTable;
    }

    // Процедура вывода победителя для раунда с другим игроком
    private void printWinnerPVP() {
        System.out.println("Игра завершена!");
        System.out.println("Очки первого игрока: " + board.countPoints(1));
        System.out.println("Очки второго игрока " + board.countPoints(2));
        if (board.countPoints(1) > board.countPoints(2)) {
            System.out.println("Победил первый игрок!");
        } else {
            if (board.countPoints(1) == board.countPoints(2)) {
                System.out.println("Ничья!");
                return;
            }
            System.out.println("Победил второй игрок!");
        }
    }

    // Процедура вывода победителя для раунда с компьютером
    private void printWinnerPVE() {
        System.out.println("Игра завершена!");
        System.out.println("Очки компьютера: " + board.countPoints(1));
        System.out.println("Ваши очки: " + board.countPoints(2));
        if (board.countPoints(2) > bestResult) {
            bestResult = board.countPoints(2);
        }
        if (board.countPoints(1) > board.countPoints(2)) {
            System.out.println("Победил компьютер!");
        } else {
            if (board.countPoints(1) == board.countPoints(2)) {
                System.out.println("Ничья!");
                return;
            }
            System.out.println("Вы победили!");
        }
    }

    // Вспомогательная функция для чтения выбора позиции пользователя
    private Pair getMove() {
        var possibleMoves = board.possibleMoves();
        Scanner scan = new Scanner(System.in);
        if (possibleMoves.size() != 0) {
            System.out.println("Возможные ходы:");
            System.out.println("0: отмена хода");
            int i = 1;
            for (Pair move : possibleMoves) {
                System.out.print(i + ": (");
                System.out.println(move.getFirst() + 1 + ", " + (move.getSecond() + 1) + ")");
                i++;
            }
            int action = scan.nextInt();
            while (action < 0 || action > possibleMoves.size()) {
                System.out.println("Неверный формат ввода - введите число от 0 до " + possibleMoves.size());
                action = scan.nextInt();
            }
            if (action != 0) return possibleMoves.get(action - 1);
            if (previous == null) {
                System.out.println("Данный ход отменить нельзя. Выберите другой вариант.");
                return getMove();
            }
            return new Pair(-1, -1);
        }
        System.out.println("Нет доступных ходов.");
        System.out.println("0 - отменить ход");
        System.out.println("1 - пропустить ход");
        int action = scan.nextInt();
        while (action != 0 && action != 1) {
            System.out.println("Неверный формат ввода - введите число 0 или 1");
            action = scan.nextInt();
        }
        if (action != 0) return new Pair(-2, -2);
        if (previous == null) {
            System.out.println("Данный ход отменить нельзя. Выберите другой вариант.");
            return getMove();
        }
        return new Pair(-1, -1);
    }
}
