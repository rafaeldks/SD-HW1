import java.util.Scanner;
// Класс игровой сессии
public class Session {

    // Экземпляр игрового раунда
    private Game game = new Game();

    // Процедура запуска игровой сессии
    public void start() {
        while (true) {
            System.out.println("Выберите действие:");
            int action = getAction();
            if (action == 0) {
                System.exit(0);
            }
            if (action == 1) {
                game.startPVE();
            }
            if (action == 2) {
                game.startPVP();
            }
            if (action == 3) {
                if (Game.getBestResult() == -1) {
                    System.out.println("Вы ещё не сыграли против компьютера.");
                    continue;
                }
                System.out.println("Лучший результат против компьютера: " + Game.getBestResult());
            }
        }
    }

    // Вспомогательная функция для чтения действия пользователя
    private int getAction() {
        System.out.println("0 - завершить работу приложения");
        System.out.println("1 - играть с новичком-компьютером");
        System.out.println("2 - играть с другим игроком");
        System.out.println("3 - лучший результат против компьютера");
        Scanner scan = new Scanner(System.in);
        int action = scan.nextInt();
        while (action < 0 || action > 3) {
            System.out.println("Неверный формат ввода: введите число от 0 до 3");
            action = scan.nextInt();
        }
        return action;
    }
}
