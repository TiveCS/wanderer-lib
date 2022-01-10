package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.menu.events.MenuPostRenderEvent;
import io.github.tivecs.wanderer.menu.events.MenuPreRenderEvent;

public interface MenuInteraction {

    void onMenuPreRender(MenuPreRenderEvent event);
    void onMenuPostRender(MenuPostRenderEvent event);

}
