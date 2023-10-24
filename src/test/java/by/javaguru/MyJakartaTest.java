package by.javaguru;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class MyJakartaTest {
    private static final Path RESOURCES_PATH = Path.of("src", "test", "resources");
    private static final Path CORRUPTED_JSON_PATH = RESOURCES_PATH.resolve("corrupted.json").toAbsolutePath();
    private static Path JSON_WITH_CONTENT_PATH;
    private static String SAVE_FILE_NAME;

    private static MyJakarta createJakarta() {
        String version = "10";
        String description = "Jakarta EE 10";
        Technology maven = new Technology("Maven", "build automation tool");
        Technology jackson = new Technology("Jackson", "library JSON");
        List<Technology> technologies = List.of(maven, jackson);
        return new MyJakarta(version, description, technologies);
    }

    @BeforeAll
    public static void loadProperties() {
        Properties properties;
        try (InputStream input = MyJakartaTest.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            properties = new Properties();
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String jsonName = properties.getProperty("jsonPath");
        JSON_WITH_CONTENT_PATH = RESOURCES_PATH.resolve(jsonName).toAbsolutePath();
        SAVE_FILE_NAME = properties.getProperty("saveFileName");

        System.out.println("json name for tests: " + jsonName); // for debugging
        System.out.println("jakarta objects saves to: " + SAVE_FILE_NAME);
    }

    @Test
    public void writeToJson_invalidPath_throwsRuntimeException() {
        Path path = Path.of(".com/wrongPath");
        MyJakarta myJakarta = new MyJakarta();
        assertThrows(RuntimeException.class, () -> myJakarta.writeToJson(path.toString()));
    }

    @Test
    public void readFromJson_invalidPath_throwsRuntimeException() {
        assertThrows(RuntimeException.class, () -> MyJakarta.readFromJson(".com/wrongPath"));
    }

    @Test
    public void readFromJson_wrongJsonContent_doesNotThrowException() {
        assertThrows(RuntimeException.class, () -> MyJakarta.readFromJson(CORRUPTED_JSON_PATH.toString()));
    }

    @Test
    public void updateTechnology_nullArgument_doesNotThrowException() {
        MyJakarta myJakarta = new MyJakarta();
        Technology technology1 = new Technology(
                "Technology 1", "Description about technology 1.");
        Technology technology2 = new Technology(
                "Technology 2", "Description about technology 2.");
        Technology technology3 = new Technology(
                "Technology 3", "Description about technology 3.");
        List<Technology> tech = List.of(technology1, technology2, technology3);
        myJakarta.setTechnologies(tech);
        assertDoesNotThrow(() -> myJakarta.updateTechnology(null));
        assertEquals(myJakarta.getTechnologies(), tech);
    }

    @Test
    public void updateTechnology_validArgument_replaceDescription() {
        MyJakarta myJakarta = new MyJakarta();
        Technology technology1 = new Technology(
                "Technology 1", "Description about technology 1.");
        Technology technology2 = new Technology(
                "Technology 2", "Description about technology 2.");
        Technology technology3 = new Technology(
                "Technology 3", "Description about technology 3.");
        List<Technology> tech = List.of(technology1, technology2, technology3);
        myJakarta.setTechnologies(tech);
        Technology editedTechnology2 = new Technology(
                "Technology 2",
                "New description about technology 2.");
        myJakarta.updateTechnology(editedTechnology2);

        List<Technology> updatedTech = myJakarta.getTechnologies();
        assertEquals(updatedTech.size(), 3);
        assertEquals(updatedTech, List.of(technology1, editedTechnology2, technology3));
    }
}
