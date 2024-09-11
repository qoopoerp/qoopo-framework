package net.qoopo.qoopoframework.web.components.kanban;

import org.primefaces.event.DragDropEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnDragDrop {

    private int source;
    private int dest;
    private int item;

    public ColumnDragDrop() {
        //
    }

    public void proccess(DragDropEvent ddEvent) {
        // DragId=[principal:j_idt151:j_idt167:0:j_idt173:2:pnl]|#]
        // DropId=[principal:j_idt151:j_idt167:1:columna]|#]
        // Información: Drag id=principal:j_idt297:0:j_idt301:0:pnl
        // Información: DropId id=principal:j_idt297:1:columna
        System.out.println("DragId=[" + ddEvent.getDragId() + "]");
        System.out.println("DropId=[" + ddEvent.getDropId() + "]");

        String[] partsDrag = ddEvent.getDragId().split(":");
        String[] partsDrop = ddEvent.getDropId().split(":");
        int size = partsDrag.length;
        switch (size) {
            case 6:
                this.source = Integer.valueOf(partsDrag[2]);// el id de donde viene
                this.dest = Integer.valueOf(partsDrop[2]); // el id a donde llega
                this.item = Integer.valueOf(partsDrag[4]); // el id del objeto que se mueve
                break;
            case 7:
                this.source = Integer.valueOf(partsDrag[3]);// el id de donde viene
                this.dest = Integer.valueOf(partsDrop[3]); // el id a donde llega
                this.item = Integer.valueOf(partsDrag[5]); // el id del objeto que se mueve
                break;
        }

    }
}
