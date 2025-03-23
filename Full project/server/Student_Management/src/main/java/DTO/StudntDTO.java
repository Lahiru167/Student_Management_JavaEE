package DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudntDTO {

    private String id;
    private  String name;
    private  String address;
    private  String gender;
    private String contact_number;
    private  int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudntDTO studntDTO = (StudntDTO) o;
        return Objects.equals(id, studntDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
