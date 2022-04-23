import java.io.*;
import java.util.ArrayList;

public class Main {
    // пример №2 нерелевантен, там 0 совпадений слов. Можно попробовать реализовать через список синонимов,
    // но тогда придется все слова файла сверять с этим списком, это умножит время.

    public static void main(String[] args) {
        File file = new File("C://IDEA_project/input.txt");
        // список свойств
        ArrayList<String> properties = new ArrayList<>();
        // список подходящих значений
        ArrayList<String> values = new ArrayList<>();

        try {
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);

            int n = Integer.parseInt(reader.readLine());
            for (int i = 0; i < n; i++) {
                properties.add(reader.readLine());
            }

            int m = Integer.parseInt(reader.readLine());
            for (int i = 0; i < m; i++) {
                values.add(reader.readLine());
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ArrayList<String> result = new ArrayList<>();
        // идем по списку свойств
        for (String property : properties) {
            // делим свойство на список слов
            String[] splitProp = property.split(" ");

            String temp = "";
            // идем по списку значений
            for (String value : values) {
                // делим значение на список слов
                String[] splitValue = value.split(" ");

                // сравниваем свойство со значений по словам, пропуская предлоги до 3 букв, игнорируя регистр
                for (String sProp: splitProp) {
                    for (String sValue : splitValue) {
                        if (sProp.length() > 3 && sValue.length() > 3 &&
                                (sProp.toLowerCase().contains(sValue.toLowerCase())
                                || sValue.toLowerCase().contains(sProp.toLowerCase())) ) {
                            // если есть совпадение по 1 слову, составляем временную строку свойство:значение
                            temp = property + ":" + value;
                            break;
                        }
                    }
                }
            }
            // если временная строка пустая, составляем строку свойство:?
            if (temp.equals("")) {
                temp = property + ":?";
            }

            // записываем строку в результирующий список
            result.add(temp);
        }


        // создаем список добавочных значений, если число значений было больше числа свойств
        ArrayList<String> addition = new ArrayList<>();
        // проверяем результирующий список
        for (String res : result) {
            String flagStr = "";

            // если в списке есть значение, то мы переходим на следующее значение,
            // иначе записываем его во временную строку
            for (String value: values) {
                if (res.contains(value)) {
                    flagStr = "";
                    break;
                }
                flagStr = value;
            }

            // если временная строка непуста и значение еще не в добавочном списке, добавляем
            if (!flagStr.equals("") && !addition.contains(flagStr)) {
                addition.add(flagStr + ":?");
            }
        }

        // добавляем весь список в результирующий
        result.addAll(addition);

        // записываем результат в файл
        try( FileWriter writer = new FileWriter("C://IDEA_project/output.txt")) {
            for (String res: result) {
                writer.write(res + "\n");
            }
            writer.flush();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

    }
}
