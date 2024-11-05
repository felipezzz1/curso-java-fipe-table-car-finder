package br.com.fezor.TabelaFipe.main;

import br.com.fezor.TabelaFipe.models.Data;
import br.com.fezor.TabelaFipe.models.Modelos;
import br.com.fezor.TabelaFipe.models.Vehicle;
import br.com.fezor.TabelaFipe.services.ApiUse;
import br.com.fezor.TabelaFipe.services.ConvertData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private final Scanner read = new Scanner(System.in);
    private final String BASE_URL = "https://parallelum.com.br/fipe/api/v1/";
    private ApiUse apiUse = new ApiUse();

    private ConvertData convertData = new ConvertData();

    public void showMenu() {
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

        if (option.toLowerCase().contains("carr")) {
            address = BASE_URL + "carros/marcas";
        } else if (option.toLowerCase().contains("mot")) {
            address = BASE_URL + "motos/marcas";
        } else {
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

        System.out.println("\n Please type the name of the car you want to search: ");
        var vehicleName = read.nextLine();

        List<Data> filteredModels = listModel.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(vehicleName.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nFiltered models: ");
        filteredModels.forEach(System.out::println);

        System.out.println("\nPlease type the model code: ");
        var modelCode = read.nextLine();

        address = address + "/" + modelCode + "/anos";
        json = apiUse.getData(address);
        List<Data> years = convertData.getList(json, Data.class);

        List<Vehicle> vehicles = new ArrayList<>();

        for (Data year : years) {
            var addressYears = address + "/" + year.codigo();
            json = apiUse.getData(addressYears);
            Vehicle vehicle = convertData.getData(json, Vehicle.class);
            vehicles.add(vehicle);
        }

        System.out.println("\nAll filtered vehicles by year: ");
        vehicles.forEach(System.out::println);

    }
}
