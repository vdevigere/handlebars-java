package org.apache.tiles.request.handlebars;

import org.apache.tiles.extras.complete.CompleteAutoloadTilesInitializer;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.request.ApplicationContext;

public class HandlebarsTilesInitializer extends CompleteAutoloadTilesInitializer {
    private String prefix, suffix;

    public HandlebarsTilesInitializer() {
        this.prefix = "/hbr";
        this.suffix = ".hbr";
    }

    public HandlebarsTilesInitializer(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    protected AbstractTilesContainerFactory createContainerFactory(ApplicationContext context) {
        return new HandlebarsTilesContainerFactory(getPrefix(), getSuffix());
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
