import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;

public class Library {

    // Подклюение JDBC-драйвера.
    public static final String url = "jdbc:mysql://localhost:3306/library?useUnicode=true&characterEncoding=utf8";
    public static final String user = "root";
    public static final String pwd = "";

    // Функция.
    public HashSet<String> unashamed_reader(){
        // Множество злостных читателей (используется множество, так как они могут повторяться - один и тот же человек
        // долго не возвращает разные книги).
        HashSet<String> reader = new HashSet<>();
        // Формирование запроса, который выводит имя студента, когда он взял и вернул книгу.
        String query = "select student.full_name, take_book.date_accept, take_book.date_back from take_book join student on take_book.id_stud=student.ID";

        try {
            // Соединение с БД.
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            // Цикл по полученным данным.
            while (rs.next()) {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-dd-MM");

                Date date = new Date();

                // Дата, когда книга была возвращена.
                String back = rs.getString(3);
                // Дата, когда книгу взяли.
                String accept = rs.getString(2);
                // Если книгу так и не вернули, берем текущую дату.
                if (back == null){
                    back = myFormat.format(date);
                }
                Date oldDate = myFormat.parse(accept);
                Date newDate = myFormat.parse(back);

                int diff = (int)( (newDate.getTime() - oldDate.getTime()) / (1000 * 60 * 60 * 24)); // Разница между датами в днях.
                int max_days = 30;

                // Если разница между датами больше 30 дней, то данный студент злостный - добавляем его в список злостных студентов.
                if (diff > max_days) {
                    reader.add(rs.getString(1));
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return reader;
    }
}
