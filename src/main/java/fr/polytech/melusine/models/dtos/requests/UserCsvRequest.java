package fr.polytech.melusine.models.dtos.requests;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class UserCsvRequest {

    @CsvBindByPosition(position = 1)
    private String nom;

    @CsvBindByPosition(position = 2)
    private String prenom;

    @CsvBindByPosition(position = 3)
    private String surnom;

    @CsvBindByPosition(position = 4)
    private String annee;

    @CsvBindByPosition(position = 5)
    private String solde;

}
