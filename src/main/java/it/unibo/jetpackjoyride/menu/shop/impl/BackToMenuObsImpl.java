package it.unibo.jetpackjoyride.menu.shop.impl;

import it.unibo.jetpackjoyride.menu.shop.api.BackToMenuObs;
import it.unibo.jetpackjoyride.menu.shop.api.ShopController;

/** The implementation of the {@link BackToMenuObs} interface.
 * @author alessandro.valmori2@studio.unibo.it
*/
public class BackToMenuObsImpl implements BackToMenuObs {

    /** The shop controller. */
    private final ShopController shopController;

    /**
     * The constructor.
     * 
     * @param shopController the {@link ShopController}.
     */
    public BackToMenuObsImpl(final ShopController shopController) {
        this.shopController = shopController;
    }

    /**
     * Invokes the method to navigate back to the main menu of the game.
     */
    @Override
    public void goBack() {
        this.shopController.backToMenu();
    }

}
