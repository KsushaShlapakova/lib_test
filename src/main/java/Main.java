// Основной класс
public class Main {
    public static void main(String[] args){
        Library lib = new Library();
        for (String student_name : lib.unashamed_reader()) {
            System.out.println(student_name);
        }
    }
}

