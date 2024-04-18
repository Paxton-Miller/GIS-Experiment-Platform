import Draw from 'ol/interaction/Draw'
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import Point from "ol/geom/Point";
import { unByKey } from 'ol/Observable';
import Overlay from 'ol/Overlay';
import { Feature } from "ol";
import { getArea, getLength } from 'ol/sphere';
import LineString from 'ol/geom/LineString';
import Polygon from 'ol/geom/Polygon';
import { Circle as CircleStyle, Fill, Stroke, Style } from 'ol/style';
const measureTool = {
    formatArea(geometry, proj) {
        var area = getArea(geometry, { projection: proj })
        var output;
        if (area > 10000) {
            output = (Math.round(area / 1000000 * 100) / 100) + ' ' + 'km<sup>2</sup>';
            area = Math.round(area * 100) / 100
        } else {
            output = (Math.round(area * 100) / 100) + ' ' + 'm<sup>2</sup>';
            area = Math.round(area * 100) / 100
        }
        return [area, output];
    },
    formatLength(geometry, proj) {
        var length = getLength(geometry, { projection: proj });
        var output;
        if (length > 100) {
            output = (Math.round(length / 1000 * 100) / 100) + ' ' + 'km';
            length = Math.round(length * 100) / 100
        } else {
            output = (Math.round(length * 100) / 100) + ' ' + 'm';
            length = Math.round(length * 100) / 100
        }
        return [length, output];
    }
}


export default measureTool;