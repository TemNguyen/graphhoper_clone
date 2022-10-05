package com.graphhopper.traffic.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Karich
 */
public class RoadDataTest {

    private final ObjectMapper mapper = CustomGuiceModule.createMapper();

    @Test
    public void testReadFromFile() throws IOException {
        RoadData data = mapper.readValue(new StringReader("[{'id':'1', 'points': [[11.1, 42.4]], 'value': 20.5, 'value_type': 'speed', 'mode':'replace'}]".replaceAll("'", "\"")), RoadData.class);
        RoadEntry entry = data.get(0);
        assertEquals(1, entry.getPoints().size());
        assertEquals(42.4, entry.getPoints().get(0).lat, 0.01);
        assertEquals(11.1, entry.getPoints().get(0).lon, 0.01);
        assertEquals(20.5, entry.getValue(), 0.1);
        assertEquals("speed", entry.getValueType());
    }

    @Test
    public void testWriteToFile() throws IOException {
        RoadData data = new RoadData();
        data.add(new RoadEntry("1", Arrays.asList(new RoadPoint(42.4, 11.1)), 2, "speed", "replace", "", new List<RoadEntry.DAYOFWEEK>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<RoadEntry.DAYOFWEEK> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(RoadEntry.DAYOFWEEK dayofweek) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends RoadEntry.DAYOFWEEK> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends RoadEntry.DAYOFWEEK> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public RoadEntry.DAYOFWEEK get(int index) {
                return null;
            }

            @Override
            public RoadEntry.DAYOFWEEK set(int index, RoadEntry.DAYOFWEEK element) {
                return null;
            }

            @Override
            public void add(int index, RoadEntry.DAYOFWEEK element) {

            }

            @Override
            public RoadEntry.DAYOFWEEK remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<RoadEntry.DAYOFWEEK> listIterator() {
                return null;
            }

            @Override
            public ListIterator<RoadEntry.DAYOFWEEK> listIterator(int index) {
                return null;
            }

            @Override
            public List<RoadEntry.DAYOFWEEK> subList(int fromIndex, int toIndex) {
                return null;
            }
        }));
        StringWriter sWriter = new StringWriter();
        mapper.writeValue(sWriter, data);
        assertEquals("[{\"points\":[[11.1,42.4]],\"value\":2.0,\"value_type\":\"speed\",\"mode\":\"replace\",\"id\":\"1\"}]", sWriter.toString());
    }
}
