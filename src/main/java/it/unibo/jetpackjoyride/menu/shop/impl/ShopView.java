package it.unibo.jetpackjoyride.menu.shop.impl;

import it.unibo.jetpackjoyride.core.statistical.api.GameStatsController;
import it.unibo.jetpackjoyride.menu.buttoncommand.ButtonFactory;
import it.unibo.jetpackjoyride.menu.menus.GameMenu;
import it.unibo.jetpackjoyride.menu.shop.api.BackToMenuObs;
import it.unibo.jetpackjoyride.menu.shop.api.CharacterObs;
import it.unibo.jetpackjoyride.menu.shop.api.ShieldEquippedObs;
import it.unibo.jetpackjoyride.menu.shop.api.ShopController;
import it.unibo.jetpackjoyride.menu.shop.api.ShopItemPurchaseObs;
import it.unibo.jetpackjoyride.utilities.GameInfo;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.unibo.jetpackjoyride.menu.shop.api.ShopController.Items;

/**
 * The view class for the shop menu.
 */
public final class ShopView extends GameMenu {
    // Constants related to image positioning
    private static final int IMAGE_X_POS = 50;
    private static final int IMAGE_SIZE = 110;
    private static final int IMAGE_DISTANCE = 30;

    // Constants related to button positioning
    private static final int BUY_BUTTON_X_POS = 210;
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 80;
    private static final int BUY_BUTTON_Y_DISPLACEMENT = (IMAGE_SIZE - BUTTON_HEIGHT) / 2;

    // Constants related to text and font
    private static final int FONT_SIZE = 20;
    private static final int SHIELD_COUNTER_X_POS = BUY_BUTTON_X_POS + 2 * BUTTON_WIDTH + 2 * IMAGE_DISTANCE;
    private static final int DESCR_X_POS = SHIELD_COUNTER_X_POS + IMAGE_DISTANCE;
    private static final String BUTTON_STYLE = "-fx-background-color: #000000; -fx-text-fill: white; -fx-font-size: 16;";
    private static final String EQUIP_SHIELD_BUTTON_STYLE = "-fx-background-color: #0000ff; -fx-text-fill: white; -fx-font-size: 16;";

    // Other constants
    private final int cuddleImageYPos = (int) GameInfo.getInstance().getScreenHeight() / 8;

    private final ShopController controller;
    private final Text moneyText;
    private final Text displayEquipped;
    private final Text dukeUnlocked;
    private final Text shieldNum;

    private final Button equipShieldButton = new Button();

    private final Map<Button, Text> descriptionsMap = new HashMap<>();
    private final Map<Button, Items> buttonMap = new HashMap<>();
    private final Map<Button, ImageView> imageMap = new HashMap<>();

    private List<ShieldEquippedObs> equipObsList = new ArrayList<>();
    private List<ShopItemPurchaseObs> buyObsList = new ArrayList<>();
    private List<BackToMenuObs> backList = new ArrayList<>();
    private List<CharacterObs> charObsList = new ArrayList<>();

    private StackPane root = new StackPane();

    /**
     * Constructor for ShopView.
     */
    public ShopView(final ShopController controller, final Stage primaryStage,
                    final GameStatsController gameStatsHandler) {
        super(primaryStage, gameStatsHandler);

        this.controller = controller;

        scene.setOnKeyPressed(ev -> {
            this.charObsList.forEach(obs -> obs.type(ev.getCode()));
            this.update();
        });
        initializeGameMenu();
        getStage().centerOnScreen();
        stageCloseAction();

        for (final var entry : Items.values()) {
            if (!entry.equals(Items.DUKE)) {
                this.buttonMap.put(new Button(), entry);
            }
        }

       
        for (final var entry : imageMap.entrySet()) {
            entry.getValue().setFitWidth(IMAGE_SIZE);
            entry.getValue().setFitHeight(IMAGE_SIZE);
            entry.getValue().setTranslateX(IMAGE_X_POS);
            entry.getValue().setTranslateY(
                    (this.buttonMap.get(entry.getKey()).getOrder().get()) * (IMAGE_SIZE + IMAGE_DISTANCE)
                            + this.cuddleImageYPos);
        }

        for (final var entry : this.descriptionsMap.entrySet()) {
            entry.getValue().setFont(Font.font("Arial", FontWeight.NORMAL, FONT_SIZE));
            entry.getValue().setFill(Color.WHITE);
            entry.getValue().setTranslateX(DESCR_X_POS);
            entry.getValue().setTranslateY(
                    (this.buttonMap.get(entry.getKey()).getOrder().get()) * (IMAGE_SIZE + IMAGE_DISTANCE)
                            + this.cuddleImageYPos);
        }

        for (final var entry : buttonMap.entrySet()) {

            this.descriptionsMap.put(entry.getKey(), new Text(entry.getValue().getDescription().get()));
            this.imageMap.put(entry.getKey(), new ImageView(new Image(
                    getClass().getClassLoader().getResource("shop/shop" + entry.getValue().name() + ".png")
                            .toExternalForm())));
                            
            entry.getKey().setText(String.valueOf(entry.getValue().getItemCost()));
            entry.getKey().setStyle(BUTTON_STYLE);
            entry.getKey().setPrefWidth(BUTTON_WIDTH);
            entry.getKey().setPrefHeight(BUTTON_HEIGHT);
            entry.getKey().setTranslateX(BUY_BUTTON_X_POS);
            entry.getKey().setTranslateY(
                    entry.getValue().getOrder().get() * (IMAGE_SIZE + IMAGE_DISTANCE)
                            + BUY_BUTTON_Y_DISPLACEMENT + this.cuddleImageYPos);
            entry.getKey().setOnAction(e -> {
                this.buyObsList.forEach(obs -> obs.onItemBought(entry.getValue()));
            });
        }

        final Button backButton = ButtonFactory.createButton("menu", e -> this.backList.forEach(obs -> obs.goBack()), BUTTON_WIDTH * 2,
                30 * 2);

        backButton.setTranslateX(20);
        backButton.setTranslateY(20);

        shieldNum = new Text(String.valueOf(this.controller.getNumOfShields()));
        shieldNum.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
        shieldNum.setFill(Color.GREEN);
        shieldNum.setTranslateX(SHIELD_COUNTER_X_POS);
        shieldNum.setTranslateY(Items.SHIELD.getOrder().get() * (IMAGE_SIZE + IMAGE_DISTANCE) + this.cuddleImageYPos);

        equipShieldButton.setText("EQUIP");
        equipShieldButton.setStyle(EQUIP_SHIELD_BUTTON_STYLE);
        equipShieldButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        equipShieldButton.setTranslateX(BUY_BUTTON_X_POS + BUTTON_WIDTH + IMAGE_DISTANCE);
        equipShieldButton.setTranslateY(Items.SHIELD.getOrder().get() * (IMAGE_SIZE + IMAGE_DISTANCE)
                + this.cuddleImageYPos + BUY_BUTTON_Y_DISPLACEMENT);
        equipShieldButton.setOnAction(e -> {
            this.equipObsList.forEach(obs -> obs.toggleShieldEquipped());
        });

        moneyText = new Text();
        moneyText.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        moneyText.setFill(Color.WHITE);
        moneyText.setTranslateY(BUTTON_HEIGHT * 2);
        moneyText.setTextAlignment(TextAlignment.RIGHT);
        moneyText.setWrappingWidth(gameInfo.getScreenWidth() - BUTTON_HEIGHT);

        displayEquipped = new Text();
        displayEquipped.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
        displayEquipped.setFill(Color.WHITE);
        displayEquipped.setTranslateY(this.cuddleImageYPos * 3);
        displayEquipped.setTextAlignment(TextAlignment.RIGHT);
        displayEquipped.setWrappingWidth(gameInfo.getScreenWidth() - BUTTON_HEIGHT);

        dukeUnlocked = new Text();
        dukeUnlocked.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
        dukeUnlocked.setFill(Color.YELLOWGREEN);
        dukeUnlocked.setTranslateY(500);
        dukeUnlocked.setTextAlignment(TextAlignment.RIGHT);
        dukeUnlocked.setWrappingWidth(gameInfo.getScreenWidth() - BUTTON_HEIGHT);

        root.getChildren().addAll(this.imageMap.values());
        root.getChildren().addAll(this.descriptionsMap.values());
        root.getChildren().addAll(this.buttonMap.keySet());

        root.getChildren().addAll(
                backButton,
                equipShieldButton,
                moneyText,
                displayEquipped,
                dukeUnlocked,
                shieldNum);
        this.update();
    }

    /**
     * Sets the scene of the shop menu on Stage.
     */
    public void setSceneOnStage() {
        this.getStage().setScene(this.scene);
    }

    public void addBuyObs(ShopItemPurchaseObs observer) {
        buyObsList.add(observer);
    }

    public void addBackToMenuObs(BackToMenuObs observer) {
        backList.add(observer);
    }

    public void addCharObs(CharacterObs observer) {
        charObsList.add(observer);
    }

    public void removeBackToMenuObs(BackToMenuObs observer) {
        backList.remove(observer);
    }

    public void removeBuyObs(ShopItemPurchaseObs observer) {
        buyObsList.remove(observer);
    }

    public void addEquipObserver(ShieldEquippedObs observer) {
        equipObsList.add(observer);
    }

    public void removeEquipObs(ShieldEquippedObs observer) {
        equipObsList.remove(observer);
    }

    public void removeCharObs(CharacterObs observer) {
        charObsList.remove(observer);
    }

    public void update() {
        if (this.controller.getUnlocked().contains(Items.DUKE)) {
            this.dukeUnlocked.setText("DUKE UNLOCKED ! ! !");
        }
        this.moneyText.setText("Money: $" + this.controller.retrieveBalance());

        this.shieldNum.setText(String.valueOf(this.controller.getNumOfShields()));
        if (this.controller.getNumOfShields() == 0) {
            this.shieldNum.setFill(Color.RED);
        } else {
            this.shieldNum.setFill(Color.GREEN);
        }

        this.displayEquipped.setText(
                this.controller.isShieldEquipped() ? "SHIELD EQUIPPED" : " ");

        for (final var entry : this.buttonMap.entrySet()) {
            if (this.controller.getUnlocked().contains(entry.getValue())) {
                final Image image = new Image(
                        getClass().getClassLoader().getResource("buttons/tick.png").toExternalForm());
                final ImageView imageView = new ImageView(image);
                imageView.setFitWidth(BUTTON_WIDTH);
                imageView.setFitHeight(BUTTON_HEIGHT);
                imageView.setPreserveRatio(false);
                entry.getKey().setStyle(
                        "-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;");
                entry.getKey().setGraphic(imageView);
            }
        }
    }

    @Override
    protected void initializeGameMenu() {
        root.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        final Image menuImage = new Image(getClass().getClassLoader().getResource("shop/shopbg.png").toExternalForm());
        setMenuImage(menuImage);
        addButtons(root);
    }

    @Override
    protected void stageCloseAction() {
        getStage().setOnCloseRequest(event -> {
            this.controller.save();
            defaultCloseAction();
        });
    }


}
