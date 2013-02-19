package eu.trentorise.smartcampus.domain.pois.converter;

import it.sayservice.platform.core.domain.actions.DataConverter;
import it.sayservice.platform.core.domain.ext.Tuple;
import it.sayservice.platform.core.message.Core.POI;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.protobuf.ByteString;

import eu.trentorise.smartcampus.domain.discovertrento.GenericEvent;
import eu.trentorise.smartcampus.domain.discovertrento.GenericPOI;
import eu.trentorise.smartcampus.domain.discovertrento.POIData;

public class PoiDataConverter  implements DataConverter {

	public Serializable toMessage(Map<String, Object> parameters) {
		if (parameters == null)
			return null;
		return new HashMap<String, Object>(parameters);
	}
	
	public Object fromMessage(Serializable object) {
		List<ByteString> data = (List<ByteString>) object;
		Tuple res = new Tuple();
		List<GenericPOI> list = new ArrayList<GenericPOI>();
		for (ByteString bs : data) {
			try {
				eu.trentorise.smartcampus.services.pois.data.message.Pois.GenericPOI poi = eu.trentorise.smartcampus.services.pois.data.message.Pois.GenericPOI.parseFrom(bs);
				GenericPOI gp = extractGenericPOI(poi);
				list.add(gp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		res.put("data", list.toArray(new GenericPOI[list.size()]));
		return res;
	}

	private GenericPOI extractGenericPOI(eu.trentorise.smartcampus.services.pois.data.message.Pois.GenericPOI poi) throws ParseException {
		GenericPOI gp = new GenericPOI();
		gp.setTitle(poi.getTitle());
		if (poi.hasDescription()) {
			gp.setDescription(poi.getDescription());
		}
		gp.setSource(poi.getSource());
		gp.setPoiData(createPOIData(poi.getPoiData()));
		gp.setType(poi.getType());
		gp.setId(poi.getId());
		if (poi.hasCustomData()) {
			gp.setCustomData(gp.getCustomData());
		}

		return gp;
	}

	private POIData createPOIData(POI poi) {
		POIData poiData = new POIData();
		poiData.setStreet(poi.getAddress().getStreet());
		poiData.setLatitude(poi.getCoordinate().getLatitude());
		poiData.setLongitude(poi.getCoordinate().getLongitude());
		poiData.setCity(poi.getAddress().getCity());
		poiData.setRegion(poi.getAddress().getRegion());
		poiData.setCountry(poi.getAddress().getCountry());
		poiData.setState(poi.getAddress().getState());
		poiData.setPoiId(poi.getPoiId());
		return poiData;
	}

	private static String encode(String s) {
		return new BigInteger(s.getBytes()).toString(s.length());
	}

}

