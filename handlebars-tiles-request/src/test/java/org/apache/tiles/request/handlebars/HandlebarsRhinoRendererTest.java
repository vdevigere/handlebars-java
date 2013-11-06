package org.apache.tiles.request.handlebars;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

public class HandlebarsRhinoRendererTest {

    private Request mockRequest = mock(Request.class);
    private StringWriter writer = new StringWriter();

    @Before
    public void mockRequest() throws IOException {
        when(mockRequest.getWriter()).thenReturn(writer);
        List<String> availableScopes = new ArrayList<String>();
        availableScopes.add("page");
        when(mockRequest.getAvailableScopes()).thenReturn(availableScopes);
        Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put("greeting", "Hello");
        contextMap.put("name", "Viddu");
        when(mockRequest.getContext("page")).thenReturn(contextMap);
    }

    @Test
    public void testRender() throws IOException {
        HandlebarsRenderer hbr = new HandlebarsRhinoRenderer("/hbr", ".hbr");
        hbr.render("template", mockRequest);
        assertEquals("Hello, Viddu",  writer.toString());
    }

}
