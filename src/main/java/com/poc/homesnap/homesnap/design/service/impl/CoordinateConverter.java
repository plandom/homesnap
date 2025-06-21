package com.poc.homesnap.homesnap.design.service.impl;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;

import java.util.Map;

public class CoordinateConverter {

    public static Map<String, String> convertToWGS84(String x, String y) {
        double longitude = Double.parseDouble(x);
        double latitude = Double.parseDouble(y);

        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem srcCrs = factory.createFromName("EPSG:4326"); // WGS84
        CoordinateReferenceSystem destCrs = factory.createFromName("EPSG:2180"); // PUWG 1992 (Poland)


        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(srcCrs, destCrs);

        ProjCoordinate srcCoord = new ProjCoordinate(longitude, latitude);
        ProjCoordinate destCoord = new ProjCoordinate();

        transform.transform(srcCoord, destCoord);

        return Map.of("x", String.valueOf(destCoord.y), "y", String.valueOf(destCoord.x));
    }

    public static Map<String, String> convertTo(String x, String y) {
        double longitude = Double.parseDouble(x);
        double latitude = Double.parseDouble(y);

        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem srcCrs = factory.createFromName("EPSG:2180"); // WGS84
        CoordinateReferenceSystem destCrs = factory.createFromName("EPSG:4326"); // PUWG 1992 (Poland)

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(srcCrs, destCrs);

        ProjCoordinate srcCoord = new ProjCoordinate(longitude, latitude);
        ProjCoordinate destCoord = new ProjCoordinate();

        transform.transform(srcCoord, destCoord);

        return Map.of("x", String.valueOf(destCoord.y), "y", String.valueOf(destCoord.x));
    }

}
