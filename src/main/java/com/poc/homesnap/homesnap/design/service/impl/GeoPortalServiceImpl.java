package com.poc.homesnap.homesnap.design.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.homesnap.homesnap.design.model.ParcelData;
import com.poc.homesnap.homesnap.design.model.ZoningData;
import com.poc.homesnap.homesnap.design.service.GeoportalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class GeoPortalServiceImpl implements GeoportalService {

    public static final String URL_FEATURE_INFO_TEMPLATE = "https://integracja.gugik.gov.pl/cgi-bin/KrajowaIntegracjaEwidencjiGruntow?SERVICE=WMS&request=GetFeatureInfo&version=1.3.0&layers=obreby,dzialki&styles=&crs=EPSG:2180&bbox=%s&width=100&height=100&format=image/png&transparent=true&query_layers=dzialki,obreby&i=50&j=50&INFO_FORMAT=application/json";
    public static final String URL_PARCEL_DATA_RESULTS_TEMPLATE = "https://uldk.gugik.gov.pl/?request=GetParcelByXY&xy=%s,%s&result=teryt,id,parcel,voivodeship,county,commune,region,geom_wkt,geom_extent";
    public static final String URL_PARCEL_DATA_ONLY_GEO_TEMPLATE = "https://uldk.gugik.gov.pl/?request=GetParcelByXY&xy=%s,%s&result=geom_wkt&srid=2180";
    public static final String URL_ZONING_DATA_TEMPLATE =
        "https://mapy.geoportal.gov.pl/wss/ext/KrajowaIntegracjaMiejscowychPlanowZagospodarowaniaPrzestrzennego?SERVICE=WMS&request=GetFeatureInfo"
            + "&version=1.3.0&layers=wektor-str&styles=&crs=EPSG:2180&bbox=%s&width=100&height=100&format=image/png&transparent=true&query_layers=wektor"
            + "-str&i=50&j=50&INFO_FORMAT=application/json";

    public String getBBoxFromCoordinates(String x, String y) {
        Map<String, String> coordinates = CoordinateConverter.convertToWGS84(x, y);
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(
            URL_PARCEL_DATA_RESULTS_TEMPLATE,
            coordinates.get("y"), coordinates.get("x")
        );

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String parcelXYResponse = response.getBody();
                String[] lines = parcelXYResponse.split("\\|");
                String result = lines[lines.length - 1].trim();
                return formatToBBox(result);
            } else {
                log.error("Failed to retrieve bbox from coordinates {}, url: {}", response.getBody(), url);
                throw new RuntimeException("Failed to get bbox from coordinates. " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error while calling Geoportal API: {} url : {}", e.getMessage(), url);
            throw new RuntimeException("Error while calling Geoportal API for geometry: " + e.getMessage(), e);
        }
    }

    public List<String> getPolygonFromXy(String x, String y) {
        Map<String, String> coordinates = CoordinateConverter.convertToWGS84(x, y);
        String url = String.format(
            URL_PARCEL_DATA_ONLY_GEO_TEMPLATE,
            coordinates.get("y"), coordinates.get("x")
        );

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                String polygonData = responseBody.split(";")[1]; // Extract the POLYGON data
                String polygonCoordinates = polygonData.substring(
                    polygonData.indexOf("((") + 2, polygonData.lastIndexOf("))")
                );
                return Arrays.stream(polygonCoordinates.split(","))
                    .map(String::trim)
                    .toList();
            } else {
                log.error("Failed to retrieve coordinates. Status code: {}", response.getStatusCode());
                throw new RuntimeException("Nie udało się pobrać danych: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error while calling Geoportal API: {}", e.getMessage(), e);
            throw new RuntimeException("Error while calling Geoportal API " + e.getMessage(), e);
        }
    }

    public ZoningData getFeatureInfoZoning(String bbox) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(
            URL_ZONING_DATA_TEMPLATE,
            bbox
        );
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String responseBody = new String(response.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                log.info("Successfully retrieved zoning data: {}", responseBody);
                return objectMapper.readValue(responseBody, ZoningData.class);
            } else {
                log.error("Failed to retrieve zoning data. Status code: {}, url : {} ", response.getStatusCode(), url);
                throw new RuntimeException("Nie udało się pobrać danych: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error while calling Geoportal API for zoning data: {}, url {}", e.getMessage(), url, e);
            if (e instanceof JsonParseException) {
                log.error("JSON parsing error: {}", e.getMessage());
                if (e.getMessage().contains("Search returned no results")) {
                    log.warn("Geoportal API returned no results for that parcel: {}", url);
                    return null;
                }
            }
            throw new RuntimeException("Error while calling Geoportal API for zoning data" + e.getMessage() + " url: " + url, e);
        }
    }

    /**
     * Converts a string to the required format: minx,miny,maxx,maxy.
     *
     * @param bbox The bounding box string in the format "x1,y1,x2,y2".
     * @return The formatted bounding box string in the required order.
     */
    private static String formatToBBox(String bbox) {
        String[] split = bbox.split(",");
        List<Double> list = Arrays.stream(split).map(Double::parseDouble).sorted().toList().reversed();
        double maxx = list.get(0);
        double minx = list.get(1);
        double maxy = list.get(2);
        double miny = list.get(3);
        bbox = (int) minx + "," + (int) miny + "," + (int) maxx + "," + (int) maxy;
        return bbox;
    }

    public ParcelData getFeatureInfoParcel(String bbox) {
        String url = String.format(
            URL_FEATURE_INFO_TEMPLATE,
            bbox
        );

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = Objects.requireNonNull(response.getBody());
                log.info("Successfully retrieved parcel data: {}", responseBody);
                return parseFromResponse(responseBody);
            } else {
                log.error("Failed to retrieve parcel data. Status code: {}, Response: {}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Failed to retrieve parcel data." + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error while calling Geoportal API for parcel data: {}, url: {}", e.getMessage(), url, e);
            throw new RuntimeException("Error while calling Geoportal API for parcel data: " + e.getMessage(), e);
        }
    }

    private ParcelData parseFromResponse(String xmlResponse) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            xmlResponse = new String(xmlResponse.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        NodeList attributes = doc.getElementsByTagName("Attribute");
        Map<String, String> attributeMap = new HashMap<>();

        for (int i = 0; i < attributes.getLength(); i++) {
            Element attribute = (Element) attributes.item(i);
            String name = attribute.getAttribute("Name");
            String value = attribute.getTextContent().trim();
            attributeMap.put(name, value);
        }

        return new ParcelData(
            attributeMap.getOrDefault("Identyfikator działki", null),
            attributeMap.getOrDefault("Województwo", null),
            attributeMap.getOrDefault("Powiat", null),
            attributeMap.getOrDefault("Gmina", null),
            attributeMap.getOrDefault("Obręb", null),
            attributeMap.getOrDefault("Numer działki", null),
            attributeMap.getOrDefault("Pole pow. w ewidencji gruntów (ha)", null),
            attributeMap.getOrDefault("Grupa rejestrowa", null),
            attributeMap.getOrDefault("Oznaczenie użytku", null),
            attributeMap.getOrDefault("Oznaczenie konturu", null),
            attributeMap.getOrDefault("Data publikacji danych", null),
            attributeMap.getOrDefault("Informacje o pochodzeniu danych", null),
            attributeMap.getOrDefault("Informacje dodatkowe o działce", null),
            attributeMap.getOrDefault("Kod QR", null)
        );
    }
}
