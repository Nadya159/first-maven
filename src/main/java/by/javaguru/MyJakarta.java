package by.javaguru;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class MyJakarta {
    private String version, description;
    private List<Technology> technologies;

    public MyJakarta() {
    }

    public MyJakarta(String version, String description, List<Technology> technologies) {
        this.version = version;
        this.description = description;
        this.technologies = technologies;
    }

    public void writeToJson(String path) {
        if (path == null) {
            throw new IllegalArgumentException("File path to write cannot be null");
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        try {
            writer.writeValue(new File(path), this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MyJakarta readFromJson(String path) {
        if (path == null) {
            throw new IllegalArgumentException("File path to read cannot be null");
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(path).getAbsoluteFile(), MyJakarta.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTechnology(Technology technology) {
        if (technology != null) {
            String techName = technology.getName();
            technologies.stream()
                    .filter(t -> t.getName().equalsIgnoreCase(techName))
                    .findFirst()
                    .ifPresent(t -> t.setDescription(technology.getDescription()));
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyJakarta myJakarta = (MyJakarta) o;
        return Objects.equals(version, myJakarta.version) && Objects.equals(description, myJakarta.description) && Objects.equals(technologies, myJakarta.technologies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, description, technologies);
    }

    @Override
    public String toString() {
        return "Jakarta{" +
                "version='" + version + '\'' +
                ", description='" + description + '\'' +
                ", technologies=" + technologies +
                '}';
    }
}
