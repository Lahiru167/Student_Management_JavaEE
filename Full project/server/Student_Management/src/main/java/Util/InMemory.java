package Util;


import DTO.StudntDTO;

import java.util.List;
import java.util.ArrayList;

public class InMemory {

    static List<StudntDTO> studentList = new ArrayList<>();

    static {
      /*  StudntDTO student1 = new StudntDTO("S001", "Alice", 20, "123 Main Street");
        StudntDTO student2 = new StudntDTO("S002", "Bob", 22, "456 Elm Street");
        StudntDTO student3 = new StudntDTO("S003", "Charlie", 21, "789 Pine Avenue");
        StudntDTO student4 = new StudntDTO("S004", "Diana", 23, "321 Oak Lane");
        StudntDTO student5 = new StudntDTO("S005", "Ethan", 20, "654 Maple Road");

        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentList.add(student4);
        studentList.add(student5); */
    }

    public static boolean saveStudent(StudntDTO dto) {
        if (isExist(dto)) {
            return false;
        }
        return studentList.add(dto);
    }

    public static boolean isExist(StudntDTO dto) {
        return studentList.contains(dto);
    }

    public static boolean updateStudent(StudntDTO dto) {
        for ( StudntDTO studentDTO : studentList) {
            if (studentDTO.getId().equals(dto.getId())) {
                studentDTO.setName(dto.getName());
                studentDTO.setAge(dto.getAge());
                studentDTO.setAddress(dto.getAddress());
                return true;
            }
        }
        return false;
    }

    public static boolean deleteStudent(StudntDTO dto) {
        return studentList.remove(dto);
    }

    public static List<StudntDTO> getAllStudent() {
        return studentList;
    }

    public  static  StudntDTO getStudentById(String id){
        for (StudntDTO studntDTO : studentList){
            if (studntDTO.getId().equals(id)){
                return studntDTO;
            }
        }
        return null;
    }
}
