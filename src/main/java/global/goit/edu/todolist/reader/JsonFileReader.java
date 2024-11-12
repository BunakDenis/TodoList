package global.goit.edu.todolist.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class JsonFileReader implements FileReader {
    @Override
    public <E> ArrayList<E> read(String path, Type listType) {
        StringBuilder content = new StringBuilder();

        File jsonFile = new File(path);

        try {
            Scanner scanner = new Scanner(jsonFile);
            while (scanner.hasNext()) {
                content.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        ArrayList<E> result = gson.fromJson(content.toString(), listType);
        return result;
    }
}
