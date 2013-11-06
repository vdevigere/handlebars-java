package org.apache.tiles.request.handlebars;

import org.apache.tiles.startup.TilesInitializer;
import org.apache.tiles.web.startup.AbstractTilesListener;

public class HandlebarsAutoloadTilesListener extends AbstractTilesListener {

    @Override
    protected TilesInitializer createTilesInitializer() {
        return new HandlebarsTilesInitializer("/hbr", ".hbr");
    }

}
