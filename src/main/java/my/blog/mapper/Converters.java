package my.blog.mapper;

import org.mapstruct.Named;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Converters {

    @Named("listToString")
    public static String listToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return String.join(",", list);
    }

    @Named("stringToList")
    public static List<String> stringToList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(str.split("\\s*,\\s*"));
    }
}