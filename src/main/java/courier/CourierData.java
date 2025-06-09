package courier;


import java.util.Random;
//Класс CourierData предоставляет тестовые данные для тестирования функционала, связанного с курьерами.
public class CourierData {
    private final String existingLogin;
    private final String nonExistentLogin;
    private final String existingPassword;
    private final String nonExistentPassword;
    private final String firstName;

    //Конструктор класса, инициализирующий тестовые данные
    public CourierData() {
        this.existingLogin = randomLoginOrPass(8);
        this.nonExistentLogin = randomLoginOrPass(8) + "pon";
        this.existingPassword = "existPass";
        this.nonExistentPassword = randomLoginOrPass(8);
        this.firstName = "firstName";
    }

    //Метод для генерации случайного логина или пароля заданной длины.
    public static String randomLoginOrPass (int length) {
        int leftLimit = 97;   // Код символа 'a' в таблице ASCII
        int rightLimit = 122; // Код символа 'z' в таблице ASCII
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    //Метод для получения существующего логина курьера.
    public String getExistingLogin() {
        return existingLogin;
    }
    //Метод для получения несуществующего логина курьера.
    public String getNonExistLogin() {
        return nonExistentLogin;
    }
    //Метод для получения существующего пароля курьера.
    public String getExistingPassword() {
        return existingPassword;
    }
    //Метод для получения несуществующего пароля курьера.
    public String getNonExistPassword() {
        return nonExistentPassword;
    }
    //Метод для получения имени курьера.
    public String getFirstName() {
        return firstName;
    }
}