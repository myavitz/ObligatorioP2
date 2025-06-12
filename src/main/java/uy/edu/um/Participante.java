package uy.edu.um;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uy.edu.um.tad.linkedlist.MyLinkedListImpl;
import uy.edu.um.tad.linkedlist.MyList;


@Data
@AllArgsConstructor
@Builder

public class Participante {
    private MyList<String> actores = new MyLinkedListImpl<>();
    private String director;
}
