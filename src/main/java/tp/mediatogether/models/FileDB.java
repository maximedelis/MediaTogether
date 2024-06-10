package tp.mediatogether.models;

import jakarta.persistence.*;

@Entity
@Table(name = "files")
public class FileDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String uploader;

    @Lob
    private byte[] data;

    public FileDB() {
    }

    public FileDB(String name, String type, String uploader, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.uploader = uploader;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
