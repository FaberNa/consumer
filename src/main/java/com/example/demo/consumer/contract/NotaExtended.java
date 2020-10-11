package com.example.demo.consumer.contract;

import com.generaligroup.auto.srvaut.note.model.Nota;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class NotaExtended extends Nota {

    String extraInfo;
}
