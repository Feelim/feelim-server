package cmc.feelim.utils;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class DistanceService {


    public static double getDistance(double laboratoryX, double laboratoryY, double x, double y) {

        double theta = laboratoryY - y;

        double dist = Math.sin(deg2rad(laboratoryX)) * Math.sin(deg2rad(x)) + Math.cos(deg2rad(laboratoryX)) * Math.cos(deg2rad(x)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);

        // m 단위
        dist *= 60 * 1.1515 * 1609.344;

        return dist;
    }


    //10진수를 radian(라디안)으로 변환
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //radian(라디안)을 10진수로 변환
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
