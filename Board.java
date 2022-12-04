import java.util.ArrayList;

// Класс игровой доски
public class Board {
    // Поле игровой доски
    private final int[][] board = new int[8][8];

    // Текущий ход
    private int turn = 0;

    // Конструктор
    public Board() {
        reset();
    }

    // Приведение поля к начальному состоянию
    public void reset() {
        turn = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 0;
            }
        }
        board[3][3] = 1;
        board[4][4] = 1;
        board[4][3] = 2;
        board[3][4] = 2;
    }

    // Метод, возвращающий копию объекта класса
    public Board copy() {
        Board result = new Board();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                result.board[i][j] = board[i][j];
            }
        }
        result.turn = this.turn;
        return result;
    }

    // Процедура пропуска хода
    public void skipMove() {
        turn = (turn % 2) + 1;
    }

    // Вывод доски на экран
    public void print() {
        System.out.println("  1 2 3 4 5 6 7 8");
        var moves = possibleMoves();
        for (int i = 0; i < 8; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < 8; j++) {
                if (isIn(j, i, moves)) {
                    System.out.print("x ");
                    continue;
                }
                if (board[i][j] == 1) {
                    System.out.print("○ ");
                    continue;
                }
                if (board[i][j] == 2) {
                    System.out.print("● ");
                    continue;
                }
                System.out.print("- ");
            }
            System.out.println();
        }
    }

    // Процедура добавления фишки с координатами на поле
    public void add(int x, int y) {
        x--;
        y--;
        fill(x, y);
    }

    // Подсчёт очков определённого цвета
    public int countPoints(int color) {
        int points = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == color) {
                    points++;
                }
            }
        }
        return points;
    }

    // Функция, возвращающая список возможных ходов
    public ArrayList<Pair> possibleMoves() {
        ArrayList<Pair> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[j][i] == 0) {
                    board[j][i] = 1 + (turn + 1) % 2;
                    if (isPossible(i, j)) {
                        result.add(new Pair(i, j));
                    }
                    board[j][i] = 0;
                }
            }
        }
        return result;
    }

    // Функция, возвращающая наилучший ход для новичка-компьютера
    public Pair bestPoint() {
        var possibleMoves = possibleMoves();
        int max = 0;
        int x = -1, y = -1;
        for (Pair pair : possibleMoves) {
            if (possiblePoints(pair.getFirst(), pair.getSecond()) > max) {
                max = possiblePoints(pair.getFirst(), pair.getSecond());
                x = pair.getFirst();
                y = pair.getSecond();
            }
        }
        return new Pair(x, y);
    }

    // Проверка позиции на то, возможно ли в неё пойти
    private boolean isPossible(int x, int y) {
        var pair = checkHorizontal(x, y);
        if (pair.getFirst() != x || pair.getSecond() != x) return true;
        pair = checkVertical(x, y);
        if (pair.getFirst() != y || pair.getSecond() != y) return true;
        pair = checkFirstDiagonal(x, y);
        if (pair.getFirst() != 0 || pair.getSecond() != 0) return true;
        pair = checkSecondDiagonal(x, y);
        return pair.getFirst() != 0 || pair.getSecond() != 0;
    }

    // Функция, возвращающая оценку позиции по координатам
    private int possiblePoints(int x, int y) {
        int result = 0;
        board[y][x] = 1 + (turn + 1) % 2;
        var pair = checkHorizontal(x, y);
        result += pair.getFirst() - pair.getSecond();
        pair = checkVertical(x, y);
        result += pair.getFirst() - pair.getSecond();
        pair = checkFirstDiagonal(x, y);
        result += pair.getFirst() + pair.getSecond();
        pair = checkSecondDiagonal(x, y);
        result += pair.getFirst() + pair.getSecond();
        if ((x == 7 && y == 7) || (x == 7 && y == 0) || (x == 0 && y == 7) || (x == 0 && y == 0)) {
            result += 2;
        } else {
            if (x == 7 || x == 0 || y == 7 || y == 0) result += 1;
        }
        board[y][x] = 0;
        return result;
    }

    // Функция обновления новых фишек после нового хода
    private void fill(int x, int y) {
        int color = 1 + (++turn) % 2;
        board[y][x] = color;
        var pair = checkHorizontal(x, y);
        for (int i = x; i <= pair.getFirst(); i++) {
            board[y][i] = color;
        }
        for (int i = pair.getSecond(); i <= x; i++) {
            board[y][i] = color;
        }
        pair = checkVertical(x, y);
        for (int i = y; i <= pair.getFirst(); i++) {
            board[i][x] = color;
        }
        for (int i = pair.getSecond(); i <= y; i++) {
            board[i][x] = color;
        }
        pair = checkFirstDiagonal(x, y);
        for (int i = 1; i <= pair.getFirst(); i++) {
            board[y + i][x + i] = color;
        }
        for (int i = 1; i <= pair.getSecond(); i++) {
            board[y - i][x - i] = color;
        }
        pair = checkSecondDiagonal(x, y);
        for (int i = 1; i <= pair.getFirst(); i++) {
            board[y - i][x + i] = color;
        }
        for (int i = 1; i <= pair.getSecond(); i++) {
            board[y + i][x - i] = color;
        }
    }

    // Проверка фишек по горизонтали
    private Pair checkHorizontal(int x, int y) {
        int first = x, second = x, color = board[y][x];
        for (int i = x + 1; i < 8; i++) {
            if (board[y][i] == 0) break;
            if (board[y][i] == color) {
                first = i - 1;
                break;
            }
        }
        for (int i = x - 1; i >= 0; i--) {
            if (board[y][i] == 0) break;
            if (board[y][i] == color) {
                second = i + 1;
                break;
            }
        }
        return new Pair(first, second);
    }

    // Проверка фишек по вертикали
    private Pair checkVertical(int x, int y) {
        int first = y, second = y, color = board[y][x];
        for (int i = y + 1; i < 8; i++) {
            if (board[i][x] == 0) break;
            if (board[i][x] == color) {
                first = i - 1;
                break;
            }
        }
        for (int i = y - 1; i >= 0; i--) {
            if (board[i][x] == 0) break;
            if (board[i][x] == color) {
                second = i + 1;
                break;
            }
        }
        return new Pair(first, second);
    }

    // Проверка фишек по первой диагонали
    private Pair checkFirstDiagonal(int x, int y) {
        int first = 0, second = 0, color = board[y][x];
        int i = 1;
        while ((x + i) < 8 && (y + i) < 8) {
            if (board[y + i][x + i] == 0) break;
            if (board[y + i][x + i] == color) {
                first = i - 1;
                break;
            }
            i++;
        }
        i = 1;
        while ((x - i) >= 0 && (y - i) >= 0) {
            if (board[y - i][x - i] == 0) break;
            if (board[y - i][x - i] == color) {
                second = i - 1;
                break;
            }
            i++;
        }
        return new Pair(first, second);
    }

    // Проверка фишек по второй диагонали
    private Pair checkSecondDiagonal(int x, int y) {
        int first = 0, second = 0, color = board[y][x];
        int i = 1;
        while ((x + i) < 8 && (y - i) >= 0) {
            if (board[y - i][x + i] == 0) break;
            if (board[y - i][x + i] == color) {
                first = i - 1;
                break;
            }
            i++;
        }
        i = 1;
        while ((x - i) >= 0 && (y + i) < 8) {
            if (board[y + i][x - i] == 0) break;
            if (board[y + i][x - i] == color) {
                second = i - 1;
                break;
            }
            i++;
        }
        return new Pair(first, second);
    }

    // Проверка наличия координат в списке пар координат
    private boolean isIn(int x, int y, ArrayList<Pair> m) {
        for (Pair pair : m) {
            if (pair.getFirst() == x && pair.getSecond() == y) {
                return true;
            }
        }
        return false;
    }
}
