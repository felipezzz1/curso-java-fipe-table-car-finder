package br.com.fezor.TabelaFipe.main;

import br.com.fezor.TabelaFipe.models.Data;
import br.com.fezor.TabelaFipe.models.Modelos;
import br.com.fezor.TabelaFipe.services.ApiUse;
import br.com.fezor.TabelaFipe.services.ConvertData;

import java.util.Comparator;
import java.util.Scanner;

public class Main {
    private final Scanner read = new Scanner(System.in);
    private final String BASE_URL = "https://parallelum.com.br/fipe/api/v1/";
    private ApiUse apiUse = new ApiUse();

    private ConvertData convertData = new ConvertData();

    public void showMenu(){
        var menu = """
                *** OPTIONS ***
                Carro
                Moto
                Caminhão
                
                Type one of the options to search:
                """;

        System.out.println(menu);

        var option = read.nextLine();
        String address;

        if(option.toLowerCase().contains("carr")) {
            address = BASE_URL + "carros/marcas";
        }else if(option.toLowerCase().contains("mot")){
            address = BASE_URL + "motos/marcas";
        }else{
            address = BASE_URL + "caminhoes/marcas";
        }

        var json = apiUse.getData(address);
        System.out.println(json);

        var brands = convertData.getList(json, Data.class);
        brands.stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

        System.out.println("Please type the code of brand you want to search: ");

        var brandCode = read.nextLine();

        address = address + "/" + brandCode + "/modelos";
        json = apiUse.getData(address);

        var listModel = convertData.getData(json, Modelos.class);

        System.out.println("\nModels from this brand: ");
        listModel.modelos().stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);
    }
}
