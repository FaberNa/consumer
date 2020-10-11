package com.example.demo.consumer.service;

import com.example.demo.consumer.contract.NotaExtended;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.generaligroup.auto.srvaut.note.client.api.BaseProviderControllerApi;
import com.generaligroup.auto.srvaut.note.model.Nota;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumerService {

    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    private ModelMapper modelMapper;

    public NotaExtended getNotaExtById(String id) {
        BaseProviderControllerApi apiController = new BaseProviderControllerApi();
        Nota nota = apiController.getNota(id);
        NotaExtended notaExtended = modelMapper.map(nota, NotaExtended.class);
        notaExtended.setExtraInfo("Additional info");
        return notaExtended;
    }


    public List<NotaExtended> getNotaExtByDate(LocalDate data) throws JsonProcessingException {
        BaseProviderControllerApi apiController = new BaseProviderControllerApi();
        List<NotaExtended> result = new ArrayList<>();

        String nota = apiController.getNotaByDate(data);


        Nota[] note = objectMapper.readValue(nota, Nota[].class);
        for (Nota actual : note) {
            NotaExtended notaExtended = modelMapper.map(actual, NotaExtended.class);
            notaExtended.setExtraInfo("Additional info");
            result.add(notaExtended);
        }


        return result;
    }

}
