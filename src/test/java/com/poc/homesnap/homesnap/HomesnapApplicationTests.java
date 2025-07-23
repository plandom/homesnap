package com.poc.homesnap.homesnap;

import com.poc.homesnap.homesnap.design.model.ParcelData;
import com.poc.homesnap.homesnap.design.model.ZoningData;
import com.poc.homesnap.homesnap.design.service.impl.GeoPortalServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class HomesnapApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testGetBBoxFromParcelXY() {
        GeoPortalServiceImpl geoPortal = new GeoPortalServiceImpl();
        String x = "16.869683019671115";
        String y = "52.45125272770617";
        String bbox = geoPortal.getBBoxFromCoordinates(x, y);

        ParcelData featureInfoParcel = geoPortal.getFeatureInfoParcel(bbox);
        String parcelId = featureInfoParcel.parcelIdentifier();
        assertEquals("306401_1.0025.AR_23.109", parcelId);
        ZoningData featureInfoZoning = geoPortal.getFeatureInfoZoning(bbox);

    }

    @Test
    void testCallGetCoordinatesFromXY() {
        GeoPortalServiceImpl geoPortal = new GeoPortalServiceImpl();
        String x = "16.869683019671115";
        String y = "52.45125272770617";
        List<String> coordinates = geoPortal.getPolygonFromXy(x, y);
        System.out.println(coordinates);
        assertNotNull(coordinates);
    }

    @Test
    void testCallGetFeatureInfoZoning() {
        GeoPortalServiceImpl geoPortal = new GeoPortalServiceImpl();
        String bbox = "511600,355262,511650,355312";
        ZoningData response = geoPortal.getFeatureInfoZoning(bbox);
        assertNotNull(response);
    }

    @Test
    void testCallGetFeatureInfoParcel() {
        GeoPortalServiceImpl geoPortal = new GeoPortalServiceImpl();
        String bbox = "511600,355262,511650,355312";
        ParcelData response = geoPortal.getFeatureInfoParcel(bbox);
        assertNotNull(response);
        System.out.println(response);
    }
}
