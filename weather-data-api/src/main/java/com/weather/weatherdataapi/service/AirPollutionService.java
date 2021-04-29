package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.entity.AirPollution;
import com.weather.weatherdataapi.model.entity.region.Region;
import com.weather.weatherdataapi.repository.AirPollutionRepository;
import com.weather.weatherdataapi.repository.region.RegionRepository;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionItem;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionOpenApi;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationItem;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationOpenApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AirPollutionService {

    private AirPollutionRepository airPollutionRepository;

    private AirKoreaAirPollutionOpenApi airKoreaAirPollutionOpenApi;
    private AirKoreaStationOpenApi airKoreaStationOpenApi;

    private RegionRepository regionRepository;

    public AirPollution fetchAndStoreAirPollutionInfoUsingOpenApi(String stationName, String bigRegion, String smallRegion) {
        Optional<AirKoreaAirPollutionItem> fetchedResponse = airKoreaAirPollutionOpenApi.getResponseByStationName(stationName);

        if (fetchedResponse.isPresent() == false) {
            return null;
        }

        AirKoreaAirPollutionItem response = fetchedResponse.get();

        Region region = regionRepository.findByBigRegionAndSmallRegion(bigRegion, smallRegion).get(0);

        AirPollution airPollution = new AirPollution(response, region);
        airPollutionRepository.save(airPollution);

        return airPollution;
    }

    public String getStationNameUsingCoords(String tmX, String tmY) {
        Optional<AirKoreaStationItem> fetchedRespense = airKoreaStationOpenApi.getResponseItem(tmX, tmY);

        if (fetchedRespense.isPresent() == false)
            return null;

        return fetchedRespense.get().getStationName();
    }
}
