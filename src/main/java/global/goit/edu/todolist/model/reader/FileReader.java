package global.goit.edu.todolist.model.reader;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.List;

public interface FileReader {

    public <E> ArrayList<E> read(String path, Type listType);

}
