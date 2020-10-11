package com.example.demo.consumer.controller;


import com.example.demo.consumer.contract.NotaExtended;
import com.example.demo.consumer.service.ConsumerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController("api")
public class ConsumerController {


    @Autowired
    ConsumerService consumerService;

    @GetMapping("/notaExt/{id}")
    @Operation(description = "Legge le note ed aggiunge dati")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operazione conclusa correttamente", content = @Content(schema = @Schema(implementation = NotaExtended.class))),
            @ApiResponse(responseCode = "502", description = "Errore nel servizio chiamato", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "Errore nella gestione della richiesta", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "Errore generico", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))})
    @ApiResponse(responseCode = "200", description = "")
    public NotaExtended getNota(@PathVariable(name = "id") String id) throws IOException {
        return consumerService.getNotaExtById(id);
    }

    @GetMapping("/noteExtBefore")
    @Operation(description = "Estrae le note superiore ad una certa data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operazione conclusa correttamente", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "502", description = "Errore nel servizio chiamato", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "Errore nella gestione della richiesta", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "Errore generico", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))})
    @ApiResponse(responseCode = "200", description = "")
    public List<NotaExtended> getNotaByDate(@RequestParam(name = "data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) throws IOException {
        return consumerService.getNotaExtByDate(data);
    }
}
