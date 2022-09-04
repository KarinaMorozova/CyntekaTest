import java.io.*;
import java.util.*;

public class Main {
    public static BufferedReader reader;
    public static Map<String, List<String>> synMap;
    public static boolean isChecked = false;

    static
    {
        try {
            FileReader fr = new FileReader(System.getProperty("user.dir")
                    + File.separator + "src"
                    + File.separator + "resources"
                    + File.separator + "synonyms.txt");
            synMap = new HashMap<>();
            List<String> list;
            reader = new BufferedReader(fr);

            String temp;
            while ((temp = reader.readLine()) != null) {
                String[] arr = temp.split(" ");
                String[] dest = new String[arr.length - 1];
                System.arraycopy(arr, 1, dest, 0, dest.length);

                list = Arrays.stream(dest).toList();
                synMap.put(arr[0], list);
            }
            reader.close();

        } catch (IOException e) {
            synMap = null;
        }

    }

    public static void main(String[] args) throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));

        // Ввод пути до файла с консоли
        System.out.println("Введите путь до файла input.txt через Enter");
        String fileName = reader.readLine();
        File file = new File(fileName);
        System.out.println("Искать в файле синонимов? (Y\\N)");
        isChecked = reader.readLine().trim().equals("Y");

        // список свойств
        ArrayList<String> properties = new ArrayList<>();
        // список подходящих значений
        ArrayList<String> values = new ArrayList<>();

        //создаем объект FileReader для объекта File
        FileReader fr = new FileReader(file);
        //BufferedReader с существующего FileReader для построчного считывания
        reader = new BufferedReader(fr);

        int n = Integer.parseInt(reader.readLine());
        for (int i = 0; i < n; i++) {
            properties.add(reader.readLine());
        }

        int m = Integer.parseInt(reader.readLine());
        for (int i = 0; i < m; i++) {
            values.add(reader.readLine());
        }

        // результирующий список
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
                        } else {
                            if (isChecked && (synMap != null) && synMap.containsKey(sProp.toLowerCase())) {
                                List<String> list = synMap.get(sProp.toLowerCase());
                                // если есть совпадение по 1 слову, составляем временную строку свойство:значение
                                if (list.contains(sValue.toLowerCase())) {
                                    temp = property + ":" + value;
                                    break;
                                }
                            }
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
        System.out.println("Файл output.txt находится по пути " + System.getProperty("user.dir"));
        try( FileWriter writer = new FileWriter(System.getProperty("user.dir") + File.separator + "output.txt")) {
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
