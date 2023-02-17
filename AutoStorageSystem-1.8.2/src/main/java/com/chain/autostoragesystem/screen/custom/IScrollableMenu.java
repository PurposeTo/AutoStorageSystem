package com.chain.autostoragesystem.screen.custom;

public interface IScrollableMenu {
    int getScrollIndex();

    void scrollTo(float scrollPos);

    boolean canScroll();
}
